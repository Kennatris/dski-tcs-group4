package sorting.algorithms.project.SortingAlgorithms;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import sorting.algorithms.project.dto.SortStep;

/**
 * Implements the Selection Sort algorithm.
 * This component finds the minimum element in the unsorted portion of the list
 * and swaps it with the element at the beginning of the unsorted portion.
 */
@Component
public class SelectionSort implements SortingAlgorithm {
    // Stores the number of steps (comparisons + swaps) performed during the last sort operation.
    private long steps;
    // Stores a copy of the original dataset that was last sorted.
    private List<Integer> dataSet;

    /**
     * Returns the name of the sorting algorithm.
     * @return The string "SelectionSort".
     */
    @Override
    public String getName() {
        return "SelectionSort";
    }

    /**
     * Returns the time complexity for the worst-case scenario.
     * @return The string "O(n²)".
     */
    @Override
    public String getWorstCase() {
        return "O(n²)";
    }

    /**
     * Returns the time complexity for the average-case scenario.
     * @return The string "O(n²)".
     */
    @Override
    public String getAverageCase() {
        return "O(n²)";
    }

    /**
     * Returns the time complexity for the best-case scenario.
     * @return The string "O(n²)".
     */
    @Override
    public String getBestCase() {
        return "O(n²)";
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
     * Sorts a copy of the input list using the Selection Sort algorithm.
     * This method does not modify the original input list.
     * @param input The list of integers to sort.
     * @return A new list containing the sorted elements.
     */
    @Override
    public List<Integer> sort(List<Integer> input) {
        // Create a mutable copy of the input list to sort.
        List<Integer> copy = new ArrayList<>(input);
        // Store a copy of the original list for the getData method.
        dataSet = new ArrayList<>(copy);
        // Reset step count for the new sort operation.
        steps = 0;
        // Perform the sort on the copy, using a no-op callback.
        selectionSort(copy, (SortStep step) -> {});
        // Return the sorted copy.
        return copy;
    }

    /**
     * Sorts the input list in-place using the Selection Sort algorithm
     * and provides step-by-step updates via a callback function.
     * @param input The list of integers to sort (will be modified).
     * @param stepCallback A consumer function that accepts SortStep objects for visualization.
     */
    @Override
    public void sortWithCallback(List<Integer> input, Consumer<SortStep> stepCallback) {
        // Store a copy of the original list.
        dataSet = new ArrayList<>(input);
        // Reset step count.
        steps = 0;
        // Send the initial state of the array before sorting begins.
        stepCallback.accept(new SortStep(new ArrayList<>(input), Collections.emptySet(), Collections.emptySet()));
        // Perform the in-place sort.
        selectionSort(input, stepCallback);
        // Send the final state of the array after sorting is complete.
        stepCallback.accept(new SortStep(new ArrayList<>(input), Collections.emptySet(), Collections.emptySet()));
    }

    /**
     * Performs the Selection Sort algorithm in-place on the given list.
     * In each iteration, it finds the minimum element in the unsorted subarray
     * and swaps it with the first element of that subarray.
     * @param arr The list to be sorted (will be modified).
     * @param stepCallback The consumer for reporting each step (comparison or swap).
     */
    private void selectionSort(List<Integer> arr, Consumer<SortStep> stepCallback) {
        int n = arr.size();
        // If the list has 1 or 0 elements, it's already sorted.
        if (n <= 1) {
            stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), Collections.emptySet())); // Send final state
            return;
        }


        // Outer loop iterates through the array positions. 'i' marks the start of the unsorted subarray.
        for (int i = 0; i < n - 1; i++) {
            // Assume the current element is the minimum.
            int minIndex = i;
            // Set to track indices accessed during the search for the minimum.
            Set<Integer> accessedInSearch = new HashSet<>();
            accessedInSearch.add(i); // Initial access for minIndex assumption.

            // Send the state at the beginning of the search for the minimum at position 'i'.
            stepCallback.accept(new SortStep(new ArrayList<>(arr), new HashSet<>(accessedInSearch), Collections.emptySet()));


            // Inner loop finds the index of the actual minimum element in the unsorted subarray arr[i+1...n-1].
            for (int j = i + 1; j < n; j++) {
                // Set to track indices accessed in the current comparison.
                Set<Integer> accessedCurrent = new HashSet<>();
                accessedCurrent.add(j);        // Index j is being read.
                accessedCurrent.add(minIndex); // Current minimum index value is being read for comparison.
                accessedInSearch.add(j);       // Add j to the set of all indices accessed in this search pass.
                steps++; // Count the comparison.

                // Send the state *before* potentially updating minIndex. Shows the comparison happening.
                stepCallback.accept(new SortStep(new ArrayList<>(arr), new HashSet<>(accessedCurrent), Collections.emptySet()));


                // If a smaller element is found, update minIndex.
                if (arr.get(j) < arr.get(minIndex)) {
                    minIndex = j; // Found a new minimum index.
                    // Optionally send state after minIndex update to highlight the new minimum candidate.
                    stepCallback.accept(new SortStep(new ArrayList<>(arr), new HashSet<>(accessedCurrent), Collections.emptySet()));
                }
            }

            // Swap the found minimum element (arr[minIndex]) with the first element
            // of the unsorted subarray (arr[i]), but only if they are different elements.
            if (minIndex != i) {
                // Sets to track accessed and changed indices during the swap.
                Set<Integer> accessedSwap = new HashSet<>();
                Set<Integer> changedSwap = new HashSet<>();
                accessedSwap.add(i);        // Index i is accessed for swap.
                accessedSwap.add(minIndex); // minIndex is accessed for swap.

                // Perform the swap.
                int temp = arr.get(i);
                arr.set(i, arr.get(minIndex));
                arr.set(minIndex, temp);
                changedSwap.add(i);        // Index i is modified.
                changedSwap.add(minIndex); // minIndex is modified.
                steps++; // Count the swap.

                // Send the state after the swap.
                stepCallback.accept(new SortStep(new ArrayList<>(arr), accessedSwap, changedSwap));
            } else {
                // If no swap was needed (minimum was already at index i), send a step
                // indicating the completion of the search for this iteration. Highlight all accessed indices.
                Set<Integer> finalAccessed = IntStream.range(i, n).boxed().collect(Collectors.toSet());
                stepCallback.accept(new SortStep(new ArrayList<>(arr), finalAccessed, Collections.emptySet()));
            }
        }
    }
}