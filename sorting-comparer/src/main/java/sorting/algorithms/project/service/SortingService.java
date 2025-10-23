package sorting.algorithms.project.service;

import org.springframework.stereotype.Service;
import sorting.algorithms.project.SortingAlgorithms.SortingAlgorithm;
import sorting.algorithms.project.dto.AlgorithmInfo;
import sorting.algorithms.project.dto.CompareRequest;
import sorting.algorithms.project.dto.SortResult;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Verwaltet und führt Sortieralgorithmen aus.
 */
@Service
public class SortingService {

    private final Map<String, SortingAlgorithm> algorithms = new HashMap<>();

    /**
     * Initialisiert den Service mit allen verfügbaren Implementierungen von SortingAlgorithm.
     * @param algorithmImpls Eine Liste von Spring-Beans, die SortingAlgorithm implementieren.
     */
    public SortingService(List<SortingAlgorithm> algorithmImpls) {
        for (SortingAlgorithm algo : algorithmImpls) {
            algorithms.put(algo.getName().toLowerCase(), algo);
        }
    }

    /**
     * Führt einen Vergleich für die angeforderten Algorithmen mit den gegebenen Daten durch.
     * @param request Die Vergleichsanfrage.
     * @return Eine Liste von SortResult-Objekten.
     */
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
                        steps,
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

    /**
     * Ruft Metadaten aller verfügbaren Algorithmen ab.
     * @return Eine sortierte Liste von AlgorithmInfo-Objekten.
     */
    public List<AlgorithmInfo> getAvailableAlgorithms() {
        return algorithms.values().stream()
                .map(algo -> new AlgorithmInfo(
                        algo.getName(),
                        algo.getWorstCase(),
                        algo.getAverageCase(),
                        algo.getBestCase()
                ))
                .sorted(Comparator.comparing(AlgorithmInfo::getName))
                .collect(Collectors.toList());
    }

    /**
     * Ruft eine Algorithmus-Implementierung anhand ihres Namens ab.
     * @param name Der Name des Algorithmus (Groß-/Kleinschreibung wird ignoriert).
     * @return Die SortingAlgorithm-Instanz oder null.
     */
    public SortingAlgorithm getAlgorithmByName(String name) {
        if (name == null) return null;
        return algorithms.get(name.toLowerCase());
    }

    /**
     * Ruft den Standard-Datensatz für einen Algorithmus anhand seines Namens ab.
     * @param name Der Name des Algorithmus (Groß-/Kleinschreibung wird ignoriert).
     * @return Eine Liste von Integern oder null.
     */
    public List<Integer> getDatasetByName(String name) {
        if (name == null) return null;
        SortingAlgorithm algo = algorithms.get(name.toLowerCase());
        if (algo == null) return null;
        return algo.getData();
    }
}