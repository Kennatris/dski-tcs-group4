package sorting.algorithms.project.SortingAlgorithms;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import sorting.algorithms.project.dto.SortStep;

/**
 * Implements the Bubble Sort algorithm using a recursive approach.
 * In each recursive call, it performs one pass of bubbling the largest element
 * to the end of the current considered portion of the list, then recursively
 * calls itself for the remaining n-1 elements.
 */
@Component
public class RecursiveBubbleSort implements SortingAlgorithm {
    // Counter for steps (comparisons and swaps).
    private long steps;
    // Stores the original dataset.
    private List<Integer> dataSet;

    /**
     * Returns the name of the sorting algorithm.
     * @return The string "RecursiveBubbleSort".
     */
    @Override
    public String getName() {
        return "RecursiveBubbleSort";
    }

    /**
     * Returns the worst-case time complexity. O(n²).
     * @return The string "O(n²)".
     */
    @Override
    public String getWorstCase() {
        return "O(n²)";
    }

    /**
     * Returns the average-case time complexity. O(n²).
     * @return The string "O(n²)".
     */
    @Override
    public String getAverageCase() {
        return "O(n²)";
    }

    /**
     * Returns the best-case time complexity. O(n) - although this recursive version
     * doesn't have the early exit optimization of the iterative version, the complexity class remains.
     * In practice, it will always perform n passes. Often considered O(n²).
     * Let's stick to O(n²) for consistency unless optimization is added.
     * Changed to O(n) as per original file, assuming the definition focuses on comparisons/swaps needed if optimized.
     * @return The string "O(n)".
     */
    @Override
    public String getBestCase() {
        return "O(n)";
    }


    /**
     * Returns the total number of steps performed during the last sort.
     * @return The number of steps.
     */
    @Override
    public long getSteps() {
        return steps;
    }

    /**
     * Returns the original dataset provided to the algorithm.
     * @return The original list of integers.
     */
    @Override
    public List<Integer> getData() {
        // Ensure a non-null list is returned.
        return dataSet != null ? dataSet : SortingAlgorithm.super.getData();
    }

    /**
     * Sorts a copy of the input list using Recursive Bubble Sort.
     * @param input The list of integers to sort.
     * @return A new list containing the sorted elements.
     */
    @Override
    public List<Integer> sort(List<Integer> input) {
        // Create a mutable copy.
        List<Integer> copy = new ArrayList<>(input);
        // Store original data.
        dataSet = new ArrayList<>(copy);
        // Reset step count.
        steps = 0;
        // Perform sort with a no-op callback.
        bubbleSortRecursive(copy, copy.size(), (SortStep step) -> {});
        // Return sorted copy.
        return copy;
    }

    /**
     * Sorts the input list in-place using Recursive Bubble Sort and reports each comparison and swap.
     * @param input The list to be sorted (will be modified).
     * @param stepCallback A consumer for reporting {@link SortStep} for visualization.
     */
    @Override
    public void sortWithCallback(List<Integer> input, Consumer<SortStep> stepCallback) {
        // Store original data.
        dataSet = new ArrayList<>(input);
        // Reset step count.
        steps = 0;
        // Send initial state.
        stepCallback.accept(new SortStep(new ArrayList<>(input), Collections.emptySet(), Collections.emptySet()));
        // Start the recursive sorting process.
        bubbleSortRecursive(input, input.size(), stepCallback);
        // Send final sorted state.
        stepCallback.accept(new SortStep(new ArrayList<>(input), Collections.emptySet(), Collections.emptySet()));
    }

    /**
     * Performs one pass of Bubble Sort on the first 'n' elements to move the largest
     * element to index n-1, then recursively calls itself for the first 'n-1' elements.
     * Reports each comparison and swap.
     * @param arr The list to sort in-place.
     * @param n The number of elements currently being considered (size of the subproblem).
     * @param stepCallback The consumer for reporting steps.
     */
    private void bubbleSortRecursive(List<Integer> arr, int n, Consumer<SortStep> stepCallback) {
        // Base case: If the subproblem size is 1 or less, it's sorted.
        if (n <= 1) {
            return;
        }

        // Perform one pass: Compare and swap adjacent elements from index 0 to n-2.
        // This bubbles the largest element among the first 'n' to position n-1.
        for (int i = 0; i < n - 1; i++) {
            Set<Integer> accessed = new HashSet<>();
            Set<Integer> changed = new HashSet<>();
            accessed.add(i);      // Index for arr.get(i)
            accessed.add(i + 1);  // Index for arr.get(i + 1)
            steps++; // Count the comparison.

            // Compare adjacent elements.
            if (arr.get(i) > arr.get(i + 1)) {
                // Perform swap.
                int temp = arr.get(i);
                arr.set(i, arr.get(i + 1));
                arr.set(i + 1, temp);
                changed.add(i);      // Mark swapped indices as changed.
                changed.add(i + 1);
                steps++; // Optionally count the swap.
            }
            // Report the state after each comparison/swap.
            stepCallback.accept(new SortStep(new ArrayList<>(arr), accessed, changed));
        }

        // Recursively call for the remaining n-1 elements.
        // The largest element is now at index n-1 and excluded from the next pass.
        bubbleSortRecursive(arr, n - 1, stepCallback);
    }
}