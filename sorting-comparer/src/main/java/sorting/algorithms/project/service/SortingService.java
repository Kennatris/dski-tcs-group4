package sorting.algorithms.project.service;

import org.springframework.stereotype.Service;
import sorting.algorithms.project.SortingAlgorithms.SortingAlgorithm;
import sorting.algorithms.project.dto.AlgorithmInfo;
import sorting.algorithms.project.dto.CompareRequest;
import sorting.algorithms.project.dto.SortResult;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class responsible for managing and executing different sorting algorithms.
 * It provides methods to compare algorithms, retrieve available algorithms,
 * and get specific algorithm implementations or their default datasets.
 */
@Service
public class SortingService {

    // A map storing available sorting algorithm implementations, keyed by their lowercase names.
    private final Map<String, SortingAlgorithm> algorithms = new HashMap<>();

    /**
     * Constructs the SortingService and populates the algorithms map.
     * Spring automatically injects all beans that implement the SortingAlgorithm interface.
     * @param algorithmImplementations A list of beans implementing SortingAlgorithm.
     */
    public SortingService(List<SortingAlgorithm> algorithmImplementations) {
        // Populate the map with discovered algorithm implementations.
        for (SortingAlgorithm algo : algorithmImplementations) {
            // Use lowercase name as the key for case-insensitive lookup.
            algorithms.put(algo.getName().toLowerCase(), algo);
        }
    }

    /**
     * Compares the performance of the requested sorting algorithms using the provided input data.
     * @param request The CompareRequest containing the list of algorithm names and the input list.
     * @return A list of SortResult objects, each containing the performance metrics and results for one algorithm.
     * Returns an empty list if the request or its contents are invalid.
     */
    public List<SortResult> compare(CompareRequest request) {
        List<SortResult> results = new ArrayList<>();
        // Basic validation of the request object.
        if (request == null || request.getAlgorithms() == null || request.getInput() == null) {
            return results; // Return empty list if request is invalid
        }

        // Iterate through the requested algorithm names.
        for (String algorithmName : request.getAlgorithms()) {
            if (algorithmName == null) continue; // Skip null names

            // Retrieve the algorithm implementation by its lowercase name.
            SortingAlgorithm algo = algorithms.get(algorithmName.toLowerCase());

            // If the algorithm implementation exists:
            if (algo != null) {
                // Create a mutable copy of the input data for sorting.
                List<Integer> listToSort = new ArrayList<>(request.getInput());
                // Keep a reference to the original input for the result DTO.
                List<Integer> originalUnsorted = request.getInput();

                // Record start time, execute sort, record end time.
                long startTime = System.currentTimeMillis();
                List<Integer> sortedList = algo.sort(listToSort); // Assumes sort works on the copy
                long endTime = System.currentTimeMillis();
                long durationMillis = endTime - startTime;

                // Get the number of steps performed by the algorithm.
                long steps = algo.getSteps();

                // Create a SortResult DTO with the collected information.
                // Include excerpts of unsorted and sorted lists (e.g., first 5 elements).
                SortResult result = new SortResult(
                        algo.getName(), // Use the algorithm's canonical name
                        durationMillis,
                        steps,
                        // Get first 5 elements or fewer if list is smaller
                        originalUnsorted.stream().limit(5).collect(Collectors.toList()),
                        sortedList.stream().limit(5).collect(Collectors.toList()),
                        algo.getWorstCase(),
                        algo.getAverageCase(),
                        algo.getBestCase()
                );
                // Add the result to the list.
                results.add(result);
            }
            // If algo is null (not found), it's simply skipped.
        }
        // Return the list of results.
        return results;
    }

    /**
     * Retrieves metadata (name, complexities) for all registered sorting algorithms.
     * @return A sorted list of AlgorithmInfo objects.
     */
    public List<AlgorithmInfo> getAvailableAlgorithms() {
        return algorithms.values().stream() // Stream over the algorithm implementations
                .map(algo -> new AlgorithmInfo( // Map each implementation to an AlgorithmInfo DTO
                        algo.getName(),
                        algo.getWorstCase(),
                        algo.getAverageCase(),
                        algo.getBestCase()
                ))
                // Sort the results alphabetically by algorithm name.
                .sorted(Comparator.comparing(AlgorithmInfo::getName))
                .collect(Collectors.toList()); // Collect into a list
    }

    /**
     * Retrieves a specific sorting algorithm implementation by its name.
     * The lookup is case-insensitive.
     * @param name The name of the desired algorithm.
     * @return The SortingAlgorithm implementation, or null if not found.
     */
    public SortingAlgorithm getAlgorithmByName(String name) {
        if (name == null) return null;
        // Use lowercase name for lookup in the map.
        return algorithms.get(name.toLowerCase());
    }

    /**
     * Retrieves the default sample dataset associated with a specific algorithm.
     * The lookup is case-insensitive.
     * @param name The name of the algorithm.
     * @return The default list of integers for the algorithm, or null if the algorithm is not found.
     */
    public List<Integer> getDatasetByName(String name) {
        if (name == null) return null;
        // Find the algorithm implementation.
        SortingAlgorithm algo = algorithms.get(name.toLowerCase());
        if (algo == null) return null;
        // Return the default data provided by the algorithm.
        return algo.getData();
    }
}