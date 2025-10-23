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
 * Implements the Odd-Even Sort algorithm (also known as Odd-Even Transposition Sort).
 * It's a variation of Bubble Sort that operates in phases. In each phase, it first compares
 * and swaps adjacent elements at odd indices (1&2, 3&4, ...), and then compares and swaps
 * adjacent elements at even indices (0&1, 2&3, ...). Phases repeat until the list is sorted.
 * Suitable for parallel processing.
 */
@Component
public class OddEvenSort implements SortingAlgorithm {
    // Counter for steps (comparisons and swaps).
    private long steps;
    // Stores the original dataset.
    private List<Integer> dataSet;

    /**
     * Returns the name of the sorting algorithm.
     * @return The string "OddEvenSort".
     */
    @Override
    public String getName() {
        return "OddEvenSort";
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
     * Returns the best-case time complexity. O(n) occurs when the list is already sorted.
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
     * Sorts a copy of the input list using Odd-Even Sort.
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
        oddEvenSort(copy, (SortStep step) -> {});
        // Return sorted copy.
        return copy;
    }

    /**
     * Sorts the input list in-place using Odd-Even Sort and reports each comparison and swap.
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
        // Perform the actual sorting.
        oddEvenSort(input, stepCallback);
        // Note: Final state is sent at the end of oddEvenSort method.
    }

    /**
     * Performs the Odd-Even Sort logic in-place. Alternates between odd and even phases,
     * comparing and swapping adjacent elements, until a full pass completes with no swaps.
     * Reports each comparison and swap operation.
     * @param arr The list to sort in-place.
     * @param stepCallback The consumer for reporting steps.
     */
    private void oddEvenSort(List<Integer> arr, Consumer<SortStep> stepCallback) {
        int n = arr.size();
        // Handle lists with 0 or 1 element.
        if (n <= 1) {
            stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), Collections.emptySet())); // Send final state
            return;
        }

        // Flag indicating whether the list is sorted (true if no swaps in a full odd+even phase).
        boolean sorted = false;
        while (!sorted) {
            // Assume the list is sorted at the beginning of the phase.
            sorted = true;

            // --- Odd Phase ---
            // Compare elements at odd indices with their right neighbor (1&2, 3&4, ...).
            for (int i = 1; i < n - 1; i += 2) {
                Set<Integer> accessed = new HashSet<>();
                Set<Integer> changed = new HashSet<>();
                accessed.add(i);
                accessed.add(i + 1);
                steps++; // Count comparison.

                // If elements are out of order, swap them.
                if (arr.get(i) > arr.get(i + 1)) {
                    int temp = arr.get(i);
                    arr.set(i, arr.get(i + 1));
                    arr.set(i + 1, temp);
                    sorted = false; // A swap occurred, so list might not be sorted yet.
                    changed.add(i);
                    changed.add(i + 1);
                    steps++; // Optionally count swap.
                }
                // Report the state after the comparison/swap.
                stepCallback.accept(new SortStep(new ArrayList<>(arr), accessed, changed));
            }

            // --- Even Phase ---
            // Compare elements at even indices with their right neighbor (0&1, 2&3, ...).
            for (int i = 0; i < n - 1; i += 2) {
                Set<Integer> accessed = new HashSet<>();
                Set<Integer> changed = new HashSet<>();
                accessed.add(i);
                accessed.add(i + 1);
                steps++; // Count comparison.

                // If elements are out of order, swap them.
                if (arr.get(i) > arr.get(i + 1)) {
                    int temp = arr.get(i);
                    arr.set(i, arr.get(i + 1));
                    arr.set(i + 1, temp);
                    sorted = false; // A swap occurred, so list might not be sorted yet.
                    changed.add(i);
                    changed.add(i + 1);
                    steps++; // Optionally count swap.
                }
                // Report the state after the comparison/swap.
                stepCallback.accept(new SortStep(new ArrayList<>(arr), accessed, changed));
            }
            // Repeat the odd and even phases if any swap occurred in the last full phase.
        }
        // Send the final sorted state.
        stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), Collections.emptySet()));
    }
}