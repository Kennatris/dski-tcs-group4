package sorting.algorithms.project.SortingAlgorithms;

import java.util.List;
import java.util.function.Consumer;
import sorting.algorithms.project.dto.SortStep; // Import SortStep for the callback

/**
 * Defines the common interface for all sorting algorithm implementations.
 */
public interface SortingAlgorithm {

    /**
     * Gets the display name of the sorting algorithm.
     * @return The name of the algorithm (e.g., "BubbleSort", "QuickSort").
     */
    String getName();

    /**
     * Sorts a list of integers and returns a new sorted list.
     * The input list must not be modified by this method. Implementations
     * should work on a copy if necessary.
     * @param input The list of integers to be sorted.
     * @return A new list containing the elements from the input list in sorted order.
     */
    List<Integer> sort(List<Integer> input);

    /**
     * Gets the time complexity of the algorithm in the worst-case scenario.
     * @return A string representation of the worst-case complexity (e.g., "O(n²)", "O(n log n)").
     * Defaults to "unknown".
     */
    default String getWorstCase() { return "unknown"; }

    /**
     * Gets the time complexity of the algorithm in the average-case scenario.
     * @return A string representation of the average-case complexity. Defaults to "unknown".
     */
    default String getAverageCase() { return "unknown"; }

    /**
     * Gets the time complexity of the algorithm in the best-case scenario.
     * @return A string representation of the best-case complexity. Defaults to "unknown".
     */
    default String getBestCase() { return "unknown"; }

    /**
     * Gets the number of steps (e.g., comparisons, swaps) performed during the
     * most recent execution of the sort method on this instance.
     * @return The count of steps. Defaults to 0.
     */
    default long getSteps() { return 0; }

    /**
     * Gets a default dataset that can be used with this algorithm, typically for demonstration.
     * @return A list of integers representing a sample dataset.
     */
    default List<Integer> getData() { return List.of(5, 2, 8, 0, 4, 1, 7, 3, 9, 6);}


    /**
     * Sorts a list of integers "in-place" (modifying the input list) and sends
     * each step of the sorting process to a callback consumer for visualization.
     * @param input The list to be sorted (will be modified).
     * @param stepCallback The consumer function that receives SortStep objects
     * representing the state of the list at each step.
     */
    default void sortWithCallback(List<Integer> input, Consumer<SortStep> stepCallback) {
        // Default implementation for algorithms that do not support visualization.
        // It simply calls the standard sort method, which might not be in-place
        // depending on the implementation. This should ideally be overridden.
        sort(input);
    }
}