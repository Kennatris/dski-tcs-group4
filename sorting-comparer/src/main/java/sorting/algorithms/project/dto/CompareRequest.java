package sorting.algorithms.project.dto;

import java.util.List;

/**
 * Data Transfer Object (DTO) representing a request to compare sorting algorithms.
 * Contains the names of the algorithms to compare and the input data list.
 */
public class CompareRequest {
    // List of algorithm names to be compared (e.g., ["BubbleSort", "QuickSort"]).
    private List<String> algorithms;
    // The list of integers that each algorithm should sort.
    private List<Integer> input;

    /**
     * Default constructor (required for frameworks like Jackson).
     */
    public CompareRequest() {}

    /**
     * Constructs a new comparison request.
     * @param algorithms The names of the algorithms to compare.
     * @param input The input list of integers to be sorted.
     */
    public CompareRequest(List<String> algorithms, List<Integer> input) {
        this.algorithms = algorithms;
        this.input = input; // Input is set via constructor
    }

    // --- Getters and Setters ---

    /**
     * Gets the list of algorithm names.
     * @return The list of algorithm names.
     */
    public List<String> getAlgorithms() {
        return algorithms;
    }

    /**
     * Sets the list of algorithm names.
     * @param algorithms The list of algorithm names.
     */
    public void setAlgorithms(List<String> algorithms) {
        this.algorithms = algorithms;
    }

    /**
     * Gets the input list of integers.
     * @return The input list.
     */
    public List<Integer> getInput() {
        return input;
    }

    /**
     * Sets the input list of integers.
     * @param input The input list.
     */
    public void setInput(List<Integer> input) {
        this.input = input;
    }
}