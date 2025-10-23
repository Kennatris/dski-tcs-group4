package sorting.algorithms.project.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import sorting.algorithms.project.dto.AlgorithmInfo;
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

/**
 * REST-Controller für die API-Endpunkte zum Vergleichen und Visualisieren.
 */
@RestController
@RequestMapping("/api/compare")
@CrossOrigin
public class CompareController {

    private final SortingService sortingService;

    /**
     * Erstellt den Controller mit dem injizierten SortingService.
     * @param sortingService Der Service zur Verwaltung der Algorithmen.
     */
    @Autowired
    public CompareController(SortingService sortingService) {
        this.sortingService = sortingService;
    }

    /**
     * POST-Endpunkt zum Vergleichen von Algorithmen basierend auf einer Anfrage.
     * @param request Die CompareRequest mit Algorithmen und Eingabedaten.
     * @return Eine Liste von SortResult-Objekten.
     */
    @PostMapping
    public List<SortResult> compareAlgorithms(@RequestBody CompareRequest request) {
        return sortingService.compare(request);
    }

    /**
     * GET-Endpunkt zum Abrufen aller verfügbaren Algorithmen.
     * @return Eine Liste von AlgorithmInfo-Objekten.
     */
    @GetMapping("/algorithms")
    public List<AlgorithmInfo> availableAlgorithms() {
        return sortingService.getAvailableAlgorithms();
    }


    /**
     * GET-Endpunkt zum Generieren von Standard-Datensätzen.
     * @param count Die gewünschte Anzahl von Elementen in den Datensätzen.
     * @return Eine Map, die Datensatznamen (z.B. "unsorted") auf Listen abbildet.
     */
    @GetMapping("/datasets")
    public Map<String, List<Integer>> getDatasets(
            @RequestParam(required = false, defaultValue = "5") int count) {

        Map<String, List<Integer>> sets = new LinkedHashMap<>();

        final int ELEMENT_COUNT = Math.max(5, Math.min(count, 1000000));
        final int MAX_VALUE = ELEMENT_COUNT - 1;

        List<Integer> ascending = new ArrayList<>();
        for (int i = 0; i < ELEMENT_COUNT; i++) {
            ascending.add(i);
        }

        List<Integer> descending = new ArrayList<>();
        for (int i = MAX_VALUE; i >= 0; i--) {
            descending.add(i);
        }

        List<Integer> halfSorted = new ArrayList<>(ascending);
        if (ELEMENT_COUNT >= 2) {
            List<Integer> secondHalf = new ArrayList<>(halfSorted.subList(ELEMENT_COUNT / 2, ELEMENT_COUNT));
            Collections.shuffle(secondHalf);
            for (int i = 0; i < secondHalf.size(); i++) {
                halfSorted.set(ELEMENT_COUNT / 2 + i, secondHalf.get(i));
            }
        }

        List<Integer> unsorted = new ArrayList<>(ascending);
        Collections.shuffle(unsorted);

        sets.put("unsorted", unsorted);
        sets.put("halfSorted", halfSorted);
        sets.put("sorted", ascending);
        sets.put("reverse", descending);
        return sets;
    }

    /**
     * GET-Endpunkt zur Initiierung einer Server-Sent Events (SSE) Verbindung
     * für die Live-Visualisierung eines Sortieralgorithmus.
     * @param algorithm Der Name des Algorithmus.
     * @param dataset Der Name eines Standard-Datensatzes (optional).
     * @param input Ein JSON-Array als String (optional, hat Vorrang vor 'dataset').
     * @param speed Die Verzögerung in Millisekunden zwischen den Schritten.
     * @param count Die Elementanzahl, falls 'dataset' oder der Standard verwendet wird.
     * @return Ein SseEmitter, der SortStep-Objekte streamt.
     */
    @GetMapping("/visualizer/{algorithm}")
    public SseEmitter visualize(@PathVariable String algorithm,
                                @RequestParam(required = false) String dataset,
                                @RequestParam(required = false) String input,
                                @RequestParam(required = false, defaultValue = "1") int speed,
                                @RequestParam(required = false, defaultValue = "5") int count
    ) {

        SseEmitter emitter = new SseEmitter(0L);
        SortingAlgorithm algo = sortingService.getAlgorithmByName(algorithm);

        List<Integer> inputData = this.getDatasets(count).getOrDefault("unsorted", new ArrayList<>());


        if (input != null && !input.isEmpty()) {
            try {
                String decoded = URLDecoder.decode(input, StandardCharsets.UTF_8.name());
                ObjectMapper mapper = new ObjectMapper();
                List<Integer> parsed = mapper.readValue(decoded, new TypeReference<List<Integer>>() {});
                inputData = new ArrayList<>(parsed);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (dataset != null && !dataset.isEmpty()) {
            Map<String, List<Integer>> sets = this.getDatasets(count);
            List<Integer> ds = sets.get(dataset);
            if (ds != null) inputData = new ArrayList<>(ds);
        } else {
            List<Integer> algoData = sortingService.getDatasetByName(algorithm);
            if (algoData != null && !algoData.isEmpty() && inputData.isEmpty()) {
                inputData = algoData;
            }
        }


        AtomicBoolean active = new AtomicBoolean(true);

        emitter.onCompletion(() -> {
            active.set(false);
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
                    algo.sortWithCallback(mutableInput, step -> {
                        if (!active.get()) return;

                        try {
                            emitter.send(step);

                            if (speed > 0) {
                                try {
                                    Thread.sleep(speed);
                                } catch (InterruptedException ie) {
                                    Thread.currentThread().interrupt();
                                }
                            }

                        } catch (IllegalStateException ise) {
                            active.set(false);
                            System.out.println("Emitter already completed, stopping sends.");
                        } catch (Exception e) {
                            active.set(false);
                            try { emitter.completeWithError(e); } catch (Exception ignore) {}
                            System.err.println("Error while sending SSE event: " + e);
                        }
                    });

                    if (active.get()) {
                        try { emitter.complete(); } catch (Exception ignore) {}
                    }
                } catch (Exception e) {
                    active.set(false);
                    try { emitter.completeWithError(e); } catch (Exception ignore) {}
                }
            }).start();
        } else {
            try { emitter.complete(); } catch (Exception ignore) {}
        }

        return emitter;
    }
}