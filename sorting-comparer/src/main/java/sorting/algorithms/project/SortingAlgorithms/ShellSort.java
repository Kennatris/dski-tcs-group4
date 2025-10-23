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
 * Implements the Shell Sort algorithm.
 * This algorithm is an optimization over Insertion Sort, allowing comparison
 * and exchange of elements that are far apart. It uses a diminishing gap sequence.
 */
@Component
public class ShellSort implements SortingAlgorithm {
    // Stores the number of steps (comparisons + swaps/shifts) performed during the last sort.
    private long steps;
    // Stores a copy of the original dataset that was last sorted.
    private List<Integer> dataSet;

    /**
     * Returns the name of the sorting algorithm.
     * @return The string "ShellSort".
     */
    @Override
    public String getName() {
        return "ShellSort";
    }

    /**
     * Returns the time complexity for the worst-case scenario.
     * This depends heavily on the chosen gap sequence. Knuth's sequence is often cited.
     * @return A typical worst-case complexity like "O(n^(3/2))" or "O(n log^2 n)".
     */
    @Override
    public String getWorstCase() {
        // Dependent on the gap sequence, this is a common assumption for Knuth's sequence.
        return "O(n^(3/2))"; // or O(n log^2 n) for better sequences
    }

    /**
     * Returns the time complexity for the average-case scenario.
     * Also depends on the gap sequence.
     * @return A typical average-case complexity like "O(n^(3/2))" or better.
     */
    @Override
    public String getAverageCase() {
        // Also dependent on the gap sequence.
        return "O(n^(3/2))"; // or better
    }

    /**
     * Returns the time complexity for the best-case scenario.
     * @return The string "O(n log n)" for certain gap sequences.
     */
    @Override
    public String getBestCase() {
        return "O(n log n)"; // For some gap sequences
    }

    /**
     * Returns the total number of steps performed during the last sort operation.
     * @return The number of steps.
     */
    @Override
    public long getSteps() {
        return steps;
    }

    /**
     * Returns the original dataset that was provided for the last sort operation.
     * Returns a default dataset if no sort has been performed yet.
     * @return The list of integers representing the original dataset.
     */
    @Override
    public List<Integer> getData() {
        // Ensure dataSet is not null; return default if it is.
        return dataSet != null ? dataSet : SortingAlgorithm.super.getData();
    }

    /**
     * Sorts a copy of the input list using the Shell Sort algorithm.
     * This method does not modify the original input list.
     * @param input The list of integers to sort.
     * @return A new list containing the sorted elements.
     */
    @Override
    public List<Integer> sort(List<Integer> input) {
        // Create a mutable copy.
        List<Integer> copy = new ArrayList<>(input);
        // Store the original list.
        dataSet = new ArrayList<>(copy);
        // Reset steps.
        steps = 0;
        // Perform sort on the copy with a no-op callback.
        shellSort(copy, (SortStep step) -> {});
        // Return sorted copy.
        return copy;
    }

    /**
     * Sorts the input list in-place using the Shell Sort algorithm
     * and provides step-by-step updates via a callback function.
     * @param input The list of integers to sort (will be modified).
     * @param stepCallback A consumer function that accepts SortStep objects for visualization.
     */
    @Override
    public void sortWithCallback(List<Integer> input, Consumer<SortStep> stepCallback) {
        // Store a copy of the original list.
        dataSet = new ArrayList<>(input);
        // Reset steps.
        steps = 0;
        // Send the initial state.
        stepCallback.accept(new SortStep(new ArrayList<>(input), Collections.emptySet(), Collections.emptySet()));
        // Perform the in-place sort.
        shellSort(input, stepCallback);
        // Send the final state.
        stepCallback.accept(new SortStep(new ArrayList<>(input), Collections.emptySet(), Collections.emptySet()));
    }

    /**
     * Performs the Shell Sort algorithm in-place using Knuth's gap sequence (h = 3*h + 1).
     * It performs an h-sort (insertion sort with gaps) for decreasing values of h.
     * @param arr The list to be sorted (will be modified).
     * @param stepCallback The consumer for reporting each step (comparison, shift, insertion).
     */
    private void shellSort(List<Integer> arr, Consumer<SortStep> stepCallback) {
        int n = arr.size();
        // If the list has 1 or 0 elements, it's already sorted.
        if (n <= 1) {
            stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), Collections.emptySet())); // Send final state
            return;
        }


        // Calculate the initial gap 'h' using Knuth's sequence (1, 4, 13, 40, ...).
        int h = 1;
        while (h < n / 3) {
            h = 3 * h + 1;
        }

        // Reduce the gap 'h' until it becomes 1. The last pass (h=1) is a standard Insertion Sort.
        while (h >= 1) {
            // Perform h-sort for the current gap 'h'.
            // This is like Insertion Sort, but instead of comparing adjacent elements,
            // elements 'h' positions apart are compared and shifted.
            for (int i = h; i < n; i++) {
                // Sets to track accessed/changed indices for the outer loop step (selecting temp).
                Set<Integer> accessedOuter = new HashSet<>();
                Set<Integer> changedOuter = new HashSet<>();
                accessedOuter.add(i); // Access for arr.get(i).
                int temp = arr.get(i); // The element to be inserted into its correct h-sorted position.
                int j = i; // Start comparing 'temp' with elements to its left, with gap 'h'.

                // Send the state when 'temp' is selected.
                stepCallback.accept(new SortStep(new ArrayList<>(arr), new HashSet<>(accessedOuter), new HashSet<>(changedOuter)));

                // Shift earlier h-sorted elements up until the correct location for 'temp' is found.
                // This inner loop performs the insertion part for the current gap 'h'.
                while (j >= h) {
                    // Sets to track accessed/changed indices for this inner comparison/shift step.
                    Set<Integer> accessedInner = new HashSet<>(accessedOuter); // Inherit outer access.
                    Set<Integer> changedInner = new HashSet<>(changedOuter);
                    accessedInner.add(j - h); // Element to the 'left' (with gap h) is accessed for comparison.
                    steps++; // Count the comparison.

                    // Send the state *before* the potential shift.
                    stepCallback.accept(new SortStep(new ArrayList<>(arr), new HashSet<>(accessedInner), new HashSet<>(changedInner)));

                    // If the element 'h' positions behind is greater than 'temp', shift it forward.
                    if (arr.get(j - h) > temp) {
                        accessedInner.add(j - h); // Accessed again for the shift operation.
                        arr.set(j, arr.get(j - h)); // Shift the element arr[j-h] to position j.
                        changedInner.add(j); // Index j was modified by the shift.
                        steps++; // Optionally count the shift as a step.

                        // Send the state *after* the shift.
                        stepCallback.accept(new SortStep(new ArrayList<>(arr), new HashSet<>(accessedInner), new HashSet<>(changedInner)));

                        j -= h; // Move 'j' back by the gap 'h' to check the next element to the left.
                    } else {
                        // Correct position found (arr[j-h] <= temp). Stop shifting for this 'temp'.
                        break; // Exit the inner while loop.
                    }
                }

                // Insert 'temp' at its correct position 'j'.
                // Only send a step if the position actually changed (j != i).
                if (j != i) {
                    Set<Integer> changedInsert = new HashSet<>();
                    arr.set(j, temp); // Insert temp.
                    changedInsert.add(j); // Index j was modified by the insertion.
                    steps++; // Count the insertion.
                    // Send the state after insertion. Only highlight the change.
                    stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), changedInsert));
                } else {
                    // 'temp' was already in its correct h-sorted position, no shift occurred.
                    // Send a step indicating the position was checked.
                    Set<Integer> accessedInsert = new HashSet<>();
                    accessedInsert.add(j); // Index j (which equals i) was accessed.
                    stepCallback.accept(new SortStep(new ArrayList<>(arr), accessedInsert, Collections.emptySet()));
                }
            }
            // Move to the next smaller gap in the sequence.
            h = h / 3; // Knuth's sequence uses integer division by 3.
        }
    }
}