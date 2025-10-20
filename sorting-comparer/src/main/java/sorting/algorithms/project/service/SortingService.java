package sorting.algorithms.project.service;

import org.springframework.stereotype.Service;
import sorting.algorithms.project.SortingAlgorithms.SortingAlgorithm;
import sorting.algorithms.project.dto.CompareRequest;
import sorting.algorithms.project.dto.SortResult;
import sorting.algorithms.project.SortingAlgorithms.SortingAlgorithm;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SortingService {

    // Feste Zahlenfolge (ändere hier falls du willst)
    private static final List<Integer> FIXED_INPUT = Arrays.asList(5, 2, 8, 4, 1);

    private final Map<String, SortingAlgorithm> algorithms = new HashMap<>();

    // Spring injiziert alle implementierungen von SortingAlgorithm
    public SortingService(List<SortingAlgorithm> algorithmImpls) {
        for (SortingAlgorithm algo : algorithmImpls) {
            algorithms.put(algo.getName().toLowerCase(), algo);
        }
    }

    public List<SortResult> compare(CompareRequest request) {
        List<SortResult> results = new ArrayList<>();
        if (request == null || request.getAlgorithms() == null) return results;

        for (String algoName : request.getAlgorithms()) {
            if (algoName == null) continue;
            SortingAlgorithm algo = algorithms.get(algoName.toLowerCase());
            if (algo != null) {
                List<Integer> toSort = new ArrayList<>(FIXED_INPUT);
                long start = System.nanoTime();
                List<Integer> sorted = algo.sort(toSort);
                long end = System.nanoTime();
                results.add(new SortResult(algo.getName(), (end - start) / 1_000_000, sorted));
            }
        }
        return results;
    }

    public List<String> getAvailableAlgorithms() {
        return algorithms.values().stream()
                .map(SortingAlgorithm::getName)
                .sorted()
                .collect(Collectors.toList());
    }
}