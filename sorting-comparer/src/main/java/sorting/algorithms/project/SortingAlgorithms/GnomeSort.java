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
 * Implements the Gnome Sort algorithm (also known as Stupid Sort).
 * It works by looking at the current and previous element. If they are in the correct order,
 * it moves one step forward. If they are in the wrong order, it swaps them and moves one step backward.
 * It continues until it reaches the end of the list.
 */
@Component
public class GnomeSort implements SortingAlgorithm {

    // Counter for steps (comparisons and swaps).
    private long steps;
    // Stores the original dataset.
    private List<Integer> dataSet;

    /**
     * Returns the name of the sorting algorithm.
     * @return The string "GnomeSort".
     */
    @Override
    public String getName() {
        return "GnomeSort";
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
     * Returns the average-case time complexity. O(n²).
     * @return The string "O(n²)".
     */
    @Override
    public String getAverageCase() {
        return "O(n²)";
    }

    /**
     * Returns the worst-case time complexity. O(n²) occurs for reverse-sorted lists.
     * @return The string "O(n²)".
     */
    @Override
    public String getWorstCase() {
        return "O(n²)";
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
     * Sorts a copy of the input list using Gnome Sort.
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
        gnomeSort(copy, (SortStep step) -> {});
        // Return sorted copy.
        return copy;
    }

    /**
     * Sorts the input list in-place using Gnome Sort and reports each comparison and swap.
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
        gnomeSort(input, stepCallback);
        // Note: Final state is sent at the end of gnomeSort method.
    }

    /**
     * Performs the Gnome Sort logic in-place. Moves through the list, swapping elements
     * that are out of order and stepping back, or stepping forward if elements are in order.
     * Reports each comparison and swap operation.
     * @param arr The list to sort in-place.
     * @param stepCallback The consumer for reporting steps.
     */
    private void gnomeSort(List<Integer> arr, Consumer<SortStep> stepCallback) {
        // The current position in the list.
        int index = 0;
        int n = arr.size();

        // Continue until the index reaches the end of the list.
        while (index < n) {
            Set<Integer> accessed = new HashSet<>();
            Set<Integer> changed = new HashSet<>();

            // If at the beginning of the list, move forward.
            if (index == 0) {
                accessed.add(index); // Mark current index as accessed/considered.
                index++;
            } else {
                // Access indices for comparison.
                accessed.add(index - 1);
                accessed.add(index);
                steps++; // Count the comparison.

                // Compare the current element with the previous one.
                if (arr.get(index - 1) <= arr.get(index)) {
                    // If they are in order, move forward.
                    index++;
                } else {
                    // If they are out of order, swap them.
                    int temp = arr.get(index - 1);
                    arr.set(index - 1, arr.get(index));
                    arr.set(index, temp);
                    // Mark swapped indices as changed.
                    changed.add(index - 1);
                    changed.add(index);
                    steps++; // Optionally count the swap.
                    // Move one step back to check the swapped element with its new predecessor.
                    index--;
                }
            }
            // Report the state after the action (move forward or swap and move back).
            stepCallback.accept(new SortStep(new ArrayList<>(arr), accessed, changed));
        }
        // Send the final sorted state.
        stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), Collections.emptySet()));
    }
}