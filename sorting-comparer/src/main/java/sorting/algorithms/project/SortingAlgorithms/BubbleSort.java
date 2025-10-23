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
 * Implements the Bubble Sort algorithm.
 * It repeatedly steps through the list, compares adjacent elements and swaps them if they are in the wrong order.
 * The pass through the list is repeated until the list is sorted.
 */
@Component
public class BubbleSort implements SortingAlgorithm {
    // Counter for the number of steps (comparisons + swaps).
    long steps;
    // Stores the original dataset passed to the sort method.
    List<Integer> dataSet;

    /**
     * Returns the name of the sorting algorithm.
     * @return The string "BubbleSort".
     */
    @Override
    public String getName() {
        return "BubbleSort";
    }

    /**
     * Returns the worst-case time complexity of Bubble Sort.
     * Occurs when the list is in reverse order.
     * @return The string "O(n²)".
     */
    @Override
    public String getWorstCase() { return "O(n²)"; }

    /**
     * Returns the average-case time complexity of Bubble Sort.
     * @return The string "O(n²)".
     */
    @Override
    public String getAverageCase() { return "O(n²)"; }

    /**
     * Returns the best-case time complexity of Bubble Sort.
     * Occurs when the list is already sorted.
     * @return The string "O(n)".
     */
    @Override
    public String getBestCase() { return "O(n)"; }

    /**
     * Returns the total number of steps (comparisons + swaps) performed during the last sort operation.
     * @return The number of steps.
     */
    @Override
    public long getSteps() { return steps; }

    /**
     * Returns the original dataset that was provided to the sorting algorithm.
     * Ensures that a non-null list is returned.
     * @return The original list of integers.
     */
    @Override
    public List<Integer> getData() {
        // Return the stored dataset, or call the default method if it's null.
        return dataSet != null ? dataSet : SortingAlgorithm.super.getData();
    }

    /**
     * Sorts a copy of the input list using Bubble Sort.
     * The original list remains unchanged.
     * @param input The list of integers to sort.
     * @return A new list containing the sorted elements.
     */
    @Override
    public List<Integer> sort(List<Integer> input) {
        // Create a mutable copy.
        List<Integer> copy = new ArrayList<>(input);
        // Store the original data for getData().
        dataSet = new ArrayList<>(copy);
        // Reset step count for this run.
        steps = 0;
        // Call the callback version with a no-op consumer.
        bubbleSort(copy, (SortStep step) -> {});
        // Return the sorted copy.
        return copy;
    }

    /**
     * Sorts the input list in-place using Bubble Sort and reports each step (comparison or swap).
     * @param input The list to be sorted (will be modified).
     * @param stepCallback A consumer function to report each {@link SortStep} for visualization.
     */
    @Override
    public void sortWithCallback(List<Integer> input, Consumer<SortStep> stepCallback) {
        // Store the original data.
        dataSet = new ArrayList<>(input);
        // Reset step count.
        steps = 0;
        // Send the initial state of the array.
        stepCallback.accept(new SortStep(new ArrayList<>(input), Collections.emptySet(), Collections.emptySet()));
        // Perform the actual sorting logic.
        bubbleSort(input, stepCallback);
        // Send the final sorted state.
        stepCallback.accept(new SortStep(new ArrayList<>(input), Collections.emptySet(), Collections.emptySet()));
    }

    /**
     * Performs the Bubble Sort logic in-place, reporting each comparison and swap.
     * Optimizes by reducing the range in each pass, as the largest elements "bubble up" to the end.
     * @param arr The list to be sorted (will be modified).
     * @param stepCallback The consumer function receiving {@link SortStep} updates.
     */
    public void bubbleSort(List<Integer> arr, Consumer<SortStep> stepCallback) {
        // Get the size of the list.
        int n = arr.size();
        // If the list has 0 or 1 elements, it's already sorted.
        if (n <= 1) return;

        boolean swapped;
        do {
            // Assume no swaps will occur in this pass.
            swapped = false;
            // Iterate through the unsorted portion of the list.
            // The range decreases by 1 in each pass as the largest element moves to the end.
            for (int i = 1; i < n; i++) {
                // --- Track accessed and changed indices for visualization ---
                Set<Integer> accessed = new HashSet<>();
                Set<Integer> changed = new HashSet<>();
                accessed.add(i - 1); // Index accessed for arr.get(i-1)
                accessed.add(i);     // Index accessed for arr.get(i)
                steps++;             // Count the comparison.

                // Report the state *before* a potential swap.
                stepCallback.accept(new SortStep(new ArrayList<>(arr), new HashSet<>(accessed), new HashSet<>(changed)));


                // Compare adjacent elements.
                if (arr.get(i - 1) > arr.get(i)) {
                    // Perform the swap.
                    int t = arr.get(i - 1);
                    arr.set(i - 1, arr.get(i));
                    arr.set(i, t);
                    // Mark that a swap occurred in this pass.
                    swapped = true;
                    // Mark the swapped indices as changed.
                    changed.add(i - 1);
                    changed.add(i);
                    steps++; // Optionally count the swap as an additional step.

                    // Report the state *after* the swap.
                    stepCallback.accept(new SortStep(new ArrayList<>(arr), accessed, changed));
                }
                // --- Reporting state after comparison removed as it's covered by before/after swap reports ---
                // stepCallback.accept(new SortStep(new ArrayList<>(arr), accessed, changed));
            }
            // Reduce the size of the unsorted portion for the next pass.
            n--;
            // Repeat the pass if any swaps occurred.
        } while (swapped);
    }
}