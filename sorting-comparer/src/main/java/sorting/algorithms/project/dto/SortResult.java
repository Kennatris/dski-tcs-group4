package sorting.algorithms.project.dto;

import java.util.List;

/**
 * Data Transfer Object (DTO) storing the result of a single sorting algorithm execution.
 * Includes performance metrics (time, steps) and excerpts of the data.
 */
public class SortResult {
    private String algorithm;     // Name of the algorithm used
    private long steps;           // Number of steps (e.g., comparisons, swaps) performed
    private long durationMillis;  // Execution time in milliseconds
    private List<Integer> unsorted; // Excerpt of the original unsorted list
    private List<Integer> sorted;   // Excerpt of the resulting sorted list
    private String worstCase;     // Worst-case time complexity
    private String averageCase;   // Average-case time complexity
    private String bestCase;      // Best-case time complexity

    /**
     * Constructs a new SortResult instance.
     * @param algorithm The name of the algorithm.
     * @param durationMillis The duration of the sort operation in milliseconds.
     * @param steps The number of steps (operations) performed by the algorithm.
     * @param unsorted An excerpt (e.g., first few elements) of the original unsorted list.
     * @param sorted An excerpt (e.g., first few elements) of the final sorted list.
     * @param worstCase The worst-case time complexity string.
     * @param averageCase The average-case time complexity string.
     * @param bestCase The best-case time complexity string.
     */
    public SortResult(String algorithm, long durationMillis, long steps, List<Integer> unsorted, List<Integer> sorted,
                      String worstCase, String averageCase, String bestCase) {
        this.algorithm = algorithm;
        this.durationMillis = durationMillis;
        this.steps = steps;
        this.unsorted = unsorted;
        this.sorted = sorted;
        this.worstCase = worstCase;
        this.averageCase = averageCase;
        this.bestCase = bestCase;
    }

    // --- Standard Getters and Setters ---

    public String getAlgorithm() { return algorithm; }
    public void setAlgorithm(String algorithm) { this.algorithm = algorithm; }

    public long getSteps() { return steps; }
    public void setSteps(long steps) { this.steps = steps; }

    public long getDurationMillis() { return durationMillis; }
    public void setDurationMillis(long durationMillis) { this.durationMillis = durationMillis; }

    public List<Integer> getUnsorted() { return unsorted; }
    public void setUnsorted(List<Integer> unsorted) { this.unsorted = unsorted; }

    public List<Integer> getSorted() { return sorted; }
    public void setSorted(List<Integer> sorted) { this.sorted = sorted; }

    public String getWorstCase() { return worstCase; }
    public void setWorstCase(String worstCase) { this.worstCase = worstCase; }

    public String getAverageCase() { return averageCase; }
    public void setAverageCase(String averageCase) { this.averageCase = averageCase; }

    public String getBestCase() { return bestCase; }
    public void setBestCase(String bestCase) { this.bestCase = bestCase; }
}