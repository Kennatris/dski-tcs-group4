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
 * Implements the Comb Sort algorithm.
 * It improves on Bubble Sort by using a gap between compared elements that starts large
 * and shrinks over iterations, helping to eliminate "turtles" (small values near the end).
 */
@Component
public class CombSort implements SortingAlgorithm {

    // Counter for the number of steps (comparisons + swaps).
    private long steps;
    // Stores the original dataset passed to the sort method.
    private List<Integer> dataSet;

    /**
     * Returns the name of the sorting algorithm.
     * @return The string "CombSort".
     */
    @Override
    public String getName() {
        return "CombSort";
    }

    /**
     * Returns the best-case time complexity of Comb Sort.
     * While often better than O(n²), some sequences can approach O(n log n).
     * @return The string "O(n log n)".
     */
    @Override
    public String getBestCase() {
        return "O(n log n)";
    }

    /**
     * Returns the average-case time complexity of Comb Sort.
     * Significantly better than O(n²) in practice, depends on the shrink factor.
     * O(n²/2^p) where p is the number of increments. Often approximates O(n log n).
     * @return The string "O(n² / 2^p)".
     */
    @Override
    public String getAverageCase() {
        return "O(n² / 2^p)";
    }

    /**
     * Returns the worst-case time complexity of Comb Sort.
     * Can degrade to O(n²) in some cases, although rare with the standard shrink factor.
     * @return The string "O(n²)".
     */
    @Override
    public String getWorstCase() {
        return "O(n²)";
    }

    /**
     * Returns the total number of steps (comparisons + swaps) performed during the last sort operation.
     * @return The number of steps.
     */
    @Override
    public long getSteps() {
        return steps;
    }

    /**
     * Returns the original dataset provided to the sorting algorithm.
     * Ensures a non-null list is returned.
     * @return The original list of integers.
     */
    @Override
    public List<Integer> getData() {
        // Return the stored dataset, or call the default method if it's null.
        return dataSet != null ? dataSet : SortingAlgorithm.super.getData();
    }

    /**
     * Sorts a copy of the input list using Comb Sort.
     * The original list remains unchanged.
     * @param input The list of integers to sort.
     * @return A new list containing the sorted elements.
     */
    @Override
    public List<Integer> sort(List<Integer> input) {
        // Create a mutable copy.
        List<Integer> copy = new ArrayList<>(input);
        // Store the original data.
        dataSet = new ArrayList<>(copy);
        // Reset step count.
        steps = 0;
        // Perform sort with a no-op callback.
        combSort(copy, (SortStep step) -> {});
        // Return the sorted copy.
        return copy;
    }

    /**
     * Sorts the input list in-place using Comb Sort and reports each step.
     * @param input The list to be sorted (will be modified).
     * @param stepCallback A consumer function to report each {@link SortStep} for visualization.
     */
    @Override
    public void sortWithCallback(List<Integer> input, Consumer<SortStep> stepCallback) {
        // Store the original data.
        dataSet = new ArrayList<>(input);
        // Reset step count.
        steps = 0;
        // Send the initial state.
        stepCallback.accept(new SortStep(new ArrayList<>(input), Collections.emptySet(), Collections.emptySet()));
        // Perform the actual sorting.
        combSort(input, stepCallback);
        // Note: Final state is sent at the end of combSort method.
    }

    /**
     * Performs the Comb Sort logic in-place, reporting each comparison and swap.
     * Uses a gap that shrinks by a factor of approximately 1.3 in each iteration.
     * Continues until the gap is 1 and no swaps are made in a pass.
     * @param arr The list to be sorted (will be modified).
     * @param stepCallback The consumer function receiving {@link SortStep} updates.
     */
    private void combSort(List<Integer> arr, Consumer<SortStep> stepCallback) {
        int n = arr.size();
        // Initialize gap to the size of the list.
        int gap = n;
        // Flag to track if swaps occurred in a pass. Loop continues until gap is 1 AND no swaps occurred.
        boolean swapped = true;

        // The loop continues as long as the gap is greater than 1 or swaps were made in the last pass with gap 1.
        while (gap != 1 || swapped) {
            // Calculate the next gap size using the shrink factor.
            gap = getNextGap(gap);
            // Assume no swaps will occur in this pass.
            swapped = false;

            // Iterate through the list, comparing elements with the current gap.
            for (int i = 0; i < n - gap; i++) {
                Set<Integer> accessed = new HashSet<>();
                Set<Integer> changed = new HashSet<>();
                // Mark the indices being compared.
                accessed.add(i);
                accessed.add(i + gap);
                steps++; // Count the comparison.

                // Compare elements separated by the gap.
                if (arr.get(i) > arr.get(i + gap)) {
                    // Perform the swap.
                    int temp = arr.get(i);
                    arr.set(i, arr.get(i + gap));
                    arr.set(i + gap, temp);
                    // Mark that a swap occurred.
                    swapped = true;
                    // Mark the changed indices.
                    changed.add(i);
                    changed.add(i + gap);
                    steps++; // Optionally count the swap.
                }
                // Report the state after each comparison/swap.
                stepCallback.accept(new SortStep(new ArrayList<>(arr), accessed, changed));
            }
        }
        // Send the final sorted state.
        stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), Collections.emptySet()));
    }

    /**
     * Calculates the next gap size for Comb Sort using a shrink factor of 1.3.
     * Ensures the gap is at least 1.
     * @param gap The current gap size.
     * @return The new, smaller gap size (minimum 1).
     */
    private int getNextGap(int gap) {
        // Shrink the gap by a factor of 1.3. Integer division achieves this.
        gap = (gap * 10) / 13;
        // Ensure the gap does not become less than 1.
        if (gap < 1) {
            return 1;
        }
        return gap;
    }
}