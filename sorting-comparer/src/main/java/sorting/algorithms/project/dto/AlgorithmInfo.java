package sorting.algorithms.project.dto;

/**
 * Data Transfer Object (DTO) containing metadata about a sorting algorithm.
 * Used to provide clients with information like name and time complexities.
 */
public class AlgorithmInfo {
    private String name;
    private String worstCase;
    private String averageCase;
    private String bestCase;

    /**
     * Constructs a new AlgorithmInfo instance.
     * @param name The display name of the algorithm (e.g., "BubbleSort").
     * @param worstCase The time complexity in the worst-case scenario (e.g., "O(n²)").
     * @param averageCase The time complexity in the average-case scenario (e.g., "O(n log n)").
     * @param bestCase The time complexity in the best-case scenario (e.g., "O(n)").
     */
    public AlgorithmInfo(String name, String worstCase, String averageCase, String bestCase) {
        this.name = name;
        this.worstCase = worstCase;
        this.averageCase = averageCase;
        this.bestCase = bestCase;
    }

    // --- Getters ---

    /**
     * Gets the display name of the algorithm.
     * @return The algorithm name.
     */
    public String getName() { return name; }

    /**
     * Gets the worst-case time complexity.
     * @return The worst-case complexity string.
     */
    public String getWorstCase() { return worstCase; }

    /**
     * Gets the average-case time complexity.
     * @return The average-case complexity string.
     */
    public String getAverageCase() { return averageCase; }

    /**
     * Gets the best-case time complexity.
     * @return The best-case complexity string.
     */
    public String getBestCase() { return bestCase; }

    // No setters needed as this DTO is typically created by the service and sent to the client.
}