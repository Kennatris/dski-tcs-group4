package sorting.algorithms.project.service;

import org.springframework.stereotype.Service;
import sorting.algorithms.project.SortingAlgorithms.SortingAlgorithm;
import sorting.algorithms.project.dto.AlgorithmInfo; // NEUER IMPORT
import sorting.algorithms.project.dto.CompareRequest;
import sorting.algorithms.project.dto.SortResult;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SortingService {

    private final Map<String, SortingAlgorithm> algorithms = new HashMap<>();

    public SortingService(List<SortingAlgorithm> algorithmImpls) {
        for (SortingAlgorithm algo : algorithmImpls) {
            algorithms.put(algo.getName().toLowerCase(), algo);
        }
    }

    public List<SortResult> compare(CompareRequest request) {
        List<SortResult> results = new ArrayList<>();
        if (request == null || request.getAlgorithms() == null || request.getInput() == null) {
            return results;
        }

        for (String algoName : request.getAlgorithms()) {
            if (algoName == null) continue;
            SortingAlgorithm algo = algorithms.get(algoName.toLowerCase());
            if (algo != null) {
                List<Integer> toSort = new ArrayList<>(request.getInput());
                List<Integer> unsorted = request.getInput();
                long startTime = System.currentTimeMillis();
                List<Integer> sorted = algo.sort(toSort);
                long endTime = System.currentTimeMillis();
                long durationMillis = endTime - startTime;

                long steps = algo.getSteps();
                SortResult result = new SortResult(
                        algo.getName(),
                        durationMillis,
                        steps, // (Name korrigiert basierend auf vorheriger Anfrage)
                        unsorted.stream().limit(5).collect(Collectors.toList()),
                        sorted.stream().limit(5).collect(Collectors.toList()),
                        algo.getWorstCase(),
                        algo.getAverageCase(),
                        algo.getBestCase()
                );
                results.add(result);
            }
        }
        return results;
    }

    // --- START DER ÄNDERUNG ---

    // Wir ändern den Rückgabetyp von List<String> zu List<AlgorithmInfo>
    public List<AlgorithmInfo> getAvailableAlgorithms() {
        return algorithms.values().stream()
                .map(algo -> new AlgorithmInfo(
                        algo.getName(),
                        algo.getWorstCase(),
                        algo.getAverageCase(),
                        algo.getBestCase()
                ))
                .sorted(Comparator.comparing(AlgorithmInfo::getName)) // Sortiere nach Name
                .collect(Collectors.toList());
    }

    // --- ENDE DER ÄNDERUNG ---

    // 🔹 Neue Methode für Controller: Algorithmus nach Name holen
    public SortingAlgorithm getAlgorithmByName(String name) {
        if (name == null) return null;
        return algorithms.get(name.toLowerCase());
    }

    public List<Integer> getDatasetByName(String name) {
        if (name == null) return null;
        return algorithms.get(name.toLowerCase()).getData();
    }
}