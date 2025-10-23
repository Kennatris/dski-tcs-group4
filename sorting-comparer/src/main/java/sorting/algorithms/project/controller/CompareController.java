package sorting.algorithms.project.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import sorting.algorithms.project.dto.CompareRequest;
import sorting.algorithms.project.dto.SortResult;
import sorting.algorithms.project.service.SortingService;
import sorting.algorithms.project.SortingAlgorithms.SortingAlgorithm;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
@RequestMapping("/api/compare")
@CrossOrigin
public class CompareController {

    private final SortingService sortingService;

    @Autowired
    public CompareController(SortingService sortingService) {
        this.sortingService = sortingService;
    }

    // 🔹 Vergleich von zwei Algorithmen
    @PostMapping
    public List<SortResult> compareAlgorithms(@RequestBody CompareRequest request) {
        return sortingService.compare(request);
    }

    // 🔹 Verfügbare Algorithmen abrufen
    @GetMapping("/algorithms")
    public List<String> availableAlgorithms() {
        return sortingService.getAvailableAlgorithms();
    }

    // 🔹 Datensätze abrufen
    @GetMapping("/datasets")
    public Map<String, List<Integer>> getDatasets() {
        Map<String, List<Integer>> sets = new LinkedHashMap<>();

        // Anzahl und Maximum als Variablen
        final int ELEMENT_COUNT = 1000;
        final int MAX_VALUE = ELEMENT_COUNT - 1;

        // Liste 1: Aufsteigend (0 -> 9999)
        List<Integer> ascending = new ArrayList<>();
        for (int i = 0; i < ELEMENT_COUNT; i++) {
            ascending.add(i);
        }

        // Liste 2: Absteigend (9999 -> 0)
        List<Integer> descending = new ArrayList<>();
        for (int i = MAX_VALUE; i >= 0; i--) {
            descending.add(i);
        }

        // Liste 3: Halbsortiert (erste Hälfte sortiert, zweite Hälfte zufällig)
        List<Integer> halfSorted = new ArrayList<>(ascending);
        List<Integer> secondHalf = new ArrayList<>(halfSorted.subList(ELEMENT_COUNT / 2, ELEMENT_COUNT));
        Collections.shuffle(secondHalf);
        for (int i = 0; i < secondHalf.size(); i++) {
            halfSorted.set(ELEMENT_COUNT / 2 + i, secondHalf.get(i));
        }

        // Liste 4: Vollständig unsortiert (komplett zufällig)
        List<Integer> unsorted = new ArrayList<>(ascending);
        Collections.shuffle(unsorted);

        sets.put("unsorted", unsorted);
        sets.put("halfSorted", halfSorted);
        sets.put("sorted", ascending);
        sets.put("reverse", descending);
        return sets;
    }

    /**
     * SSE-Endpoint für Visualisierung.
     * - dataset: einer von "unsorted","halfSorted","sorted","reverse" (optional)
     * - input: optional JSON-Array (zB "%5B5%2C2%2C3%5D" = "[5,2,3]"), hat Vorrang vor dataset
     *
     * Robustness:
     * - Verwende AtomicBoolean active, um nach onCompletion/onTimeout/onError keine weiteren sends zu versuchen.
     * - Fang IllegalStateException/IOException beim emitter.send() ab und beende sauber.
     * - Setze Emitter-Timeout auf 0 (kein automatischer Timeout) - passe falls nötig.
     */
    @GetMapping("/visualizer/{algorithm}")
    public SseEmitter visualize(@PathVariable String algorithm,
                                @RequestParam(required = false) String dataset,
                                @RequestParam(required = false) String input) {
        // kein Timeout (0) — setze falls du einen Timeout brauchst
        SseEmitter emitter = new SseEmitter(0L);
        SortingAlgorithm algo = sortingService.getAlgorithmByName(algorithm);

        // Default input fallback
        List<Integer> inputData = sortingService.getDatasetByName(algorithm);

        // 1) Wenn "input" (JSON) übergeben wurde -> decode + parse
        if (input != null && !input.isEmpty()) {
            try {
                String decoded = URLDecoder.decode(input, StandardCharsets.UTF_8.name());
                ObjectMapper mapper = new ObjectMapper();
                List<Integer> parsed = mapper.readValue(decoded, new TypeReference<List<Integer>>() {});
                inputData = new ArrayList<>(parsed);
            } catch (Exception e) {
                // Parsing fehlgeschlagen -> fallback auf dataset oder default
                e.printStackTrace();
            }
        } else if (dataset != null && !dataset.isEmpty()) {
            // 2) else: dataset-Name verwenden (verwende die Controller-eigene Map)
            Map<String, List<Integer>> sets = this.getDatasets();
            List<Integer> ds = sets.get(dataset);
            if (ds != null) inputData = new ArrayList<>(ds);
        }

        // Flag, das anzeigt, ob der Emitter noch aktiv ist
        AtomicBoolean active = new AtomicBoolean(true);

        // onCompletion/onTimeout/onError setzen, damit wir wissen, wenn Client schließt / Timeout / Fehler
        emitter.onCompletion(() -> {
            active.set(false);
            // optional logging
            System.out.println("SSE completed (onCompletion).");
        });
        emitter.onTimeout(() -> {
            active.set(false);
            try { emitter.complete(); } catch (Exception ignore) {}
            System.out.println("SSE timed out (onTimeout).");
        });
        emitter.onError(err -> {
            active.set(false);
            System.err.println("SSE error: " + err);
        });

        if (algo != null) {
            List<Integer> mutableInput = new ArrayList<>(inputData);
            new Thread(() -> {
                try {
                    // Callback wird bei jedem Schritt vom Algorithmus aufgerufen
                    algo.sortWithCallback(mutableInput, step -> {
                        // Wenn nicht mehr aktiv -> nicht senden
                        if (!active.get()) return;

                        try {
                            // Versuche zu senden; wenn der Emitter bereits geschlossen ist, wird IllegalStateException geworfen
                            emitter.send(step);
                            // kurze Pause für Visualisierung; falls InterruptedException -> beenden
                            try { Thread.sleep(1); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
                        } catch (IllegalStateException ise) {
                            // Emitter bereits abgeschlossen / client disconnected -> markiere inaktiv und hör auf zu senden
                            active.set(false);
                            System.out.println("Emitter already completed, stopping sends.");
                        } catch (Exception e) {
                            // Andere Fehler: markiere inaktiv und versuche completeWithError
                            active.set(false);
                            try { emitter.completeWithError(e); } catch (Exception ignore) {}
                            System.err.println("Error while sending SSE event: " + e);
                        }
                    });

                    // nachdem sortWithCallback fertig ist: complete nur wenn noch aktiv
                    if (active.get()) {
                        try { emitter.complete(); } catch (Exception ignore) {}
                    }
                } catch (Exception e) {
                    active.set(false);
                    try { emitter.completeWithError(e); } catch (Exception ignore) {}
                }
            }).start();
        } else {
            // kein Algorithmus gefunden
            try { emitter.complete(); } catch (Exception ignore) {}
        }

        return emitter;
    }
}