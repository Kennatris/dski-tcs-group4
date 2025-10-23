package sorting.algorithms.project.SortingAlgorithms;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
// Unused imports - can be removed
// import java.util.stream.Collectors;
// import java.util.stream.IntStream;
import sorting.algorithms.project.dto.SortStep;

/**
 * Implements the QuickSort algorithm using a recursive approach.
 * It's a divide-and-conquer algorithm that picks an element as a pivot and partitions
 * the given array around the picked pivot.
 */
@Component
public class QuickSort implements SortingAlgorithm {
    // Counter for steps (comparisons and swaps).
    private long steps;
    // Stores the original dataset.
    private List<Integer> dataSet;

    /**
     * Returns the name of the sorting algorithm.
     * @return The string "QuickSort".
     */
    @Override
    public String getName() {
        return "QuickSort";
    }

    /**
     * Returns the worst-case time complexity. O(n²) occurs with poor pivot choices (e.g., already sorted array).
     * @return The string "O(n²)".
     */
    @Override
    public String getWorstCase() {
        return "O(n²)";
    }

    /**
     * Returns the average-case time complexity. O(n log n).
     * @return The string "O(n log n)".
     */
    @Override
    public String getAverageCase() {
        return "O(n log n)";
    }

    /**
     * Returns the best-case time complexity. O(n log n) occurs with balanced partitions.
     * @return The string "O(n log n)".
     */
    @Override
    public String getBestCase() {
        return "O(n log n)";
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
     * Sorts a copy of the input list using recursive QuickSort.
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
        quickSortRecursive(copy, 0, copy.size() - 1, (SortStep step) -> {});
        // Return sorted copy.
        return copy;
    }

    /**
     * Sorts the input list in-place using recursive QuickSort and reports steps during partitioning.
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
        quickSortRecursive(input, 0, input.size() - 1, stepCallback);
        // Send final sorted state.
        stepCallback.accept(new SortStep(new ArrayList<>(input), Collections.emptySet(), Collections.emptySet()));
    }

    /**
     * The recursive core of the QuickSort algorithm.
     * Partitions the array and recursively sorts the sub-arrays before and after the pivot.
     * @param arr The list (or sublist) to sort.
     * @param lower The starting index of the current sub-array.
     * @param upper The ending index of the current sub-array.
     * @param stepCallback The consumer for reporting steps during partitioning.
     */
    private void quickSortRecursive(List<Integer> arr, int lower, int upper, Consumer<SortStep> stepCallback) {
        // Optional: Report the current range being processed.
        // Set<Integer> accessedRange = IntStream.rangeClosed(lower, upper).filter(i -> i >= 0 && i < arr.size()).boxed().collect(Collectors.toSet());
        // stepCallback.accept(new SortStep(new ArrayList<>(arr), accessedRange, Collections.emptySet()));

        // Base case: If the sub-array has less than 2 elements or indices are invalid, return.
        if (lower >= upper || lower < 0 || upper >= arr.size()) {
            return;
        }

        // Partition the sub-array arr[lower..upper] around a pivot.
        // 'p' is the final index of the pivot element.
        int p = partition(arr, lower, upper, stepCallback);

        // Recursively sort the sub-array before the pivot (arr[lower..p-1]).
        quickSortRecursive(arr, lower, p - 1, stepCallback);
        // Recursively sort the sub-array after the pivot (arr[p+1..upper]).
        quickSortRecursive(arr, p + 1, upper, stepCallback);
    }

    /**
     * Partitions the sub-array arr[lower..upper] using the Lomuto partition scheme.
     * Selects the last element (arr[upper]) as the pivot. Places the pivot element
     * at its correct sorted position and moves smaller elements to its left and
     * larger elements to its right. Reports comparisons and swaps.
     * @param arr The list containing the sub-array to partition.
     * @param lower The starting index of the sub-array.
     * @param upper The ending index of the sub-array (inclusive, pivot index).
     * @param stepCallback The consumer for reporting steps.
     * @return The final index of the pivot element after partitioning.
     */
    private int partition(List<Integer> arr, int lower, int upper, Consumer<SortStep> stepCallback) {
        Set<Integer> accessed = new HashSet<>();
        Set<Integer> changed = new HashSet<>();

        // Choose the last element as the pivot.
        accessed.add(upper); // Mark pivot index accessed.
        int pivotValue = arr.get(upper);
        // Report state: Pivot selected.
        stepCallback.accept(new SortStep(new ArrayList<>(arr), new HashSet<>(accessed), new HashSet<>(changed)));

        // 'i' tracks the index of the last element known to be smaller than the pivot.
        // It starts one position before the beginning of the sub-array.
        int i = lower - 1;

        // Iterate through the sub-array from 'lower' up to (but not including) the pivot 'upper'.
        for (int j = lower; j < upper; j++) {
            accessed.clear(); // Reset for this comparison step.
            changed.clear();
            accessed.add(j);      // Element 'j' is being compared.
            accessed.add(upper);  // Pivot index used for comparison value.
            steps++; // Count the comparison.

            // Report state *before* potential swap, showing the comparison pair.
            stepCallback.accept(new SortStep(new ArrayList<>(arr), new HashSet<>(accessed), new HashSet<>(changed)));

            // If the current element arr[j] is smaller than the pivot...
            if (arr.get(j) < pivotValue) {
                i++; // Increment 'i' to point to the next position for a smaller element.
                // Swap arr[i] with arr[j].
                accessed.add(i); // Mark 'i' as accessed for the swap.
                int temp = arr.get(i);
                arr.set(i, arr.get(j));
                arr.set(j, temp);
                changed.add(i); // Mark swapped indices as changed.
                changed.add(j);
                steps++; // Optionally count the swap.
                // Report state *after* the swap.
                stepCallback.accept(new SortStep(new ArrayList<>(arr), new HashSet<>(accessed), new HashSet<>(changed)));
            }
        }

        // Place the pivot element (arr[upper]) at its final correct position (i + 1).
        // Swap it with the element currently at arr[i + 1].
        accessed.clear();
        changed.clear();
        int pivotFinalIndex = i + 1;

        // Ensure indices are within bounds before swapping.
        if (pivotFinalIndex < arr.size() && upper < arr.size()) {
            accessed.add(pivotFinalIndex); // Mark indices involved in the final pivot swap.
            accessed.add(upper);
            int temp = arr.get(pivotFinalIndex);
            arr.set(pivotFinalIndex, arr.get(upper));
            arr.set(upper, temp);
            changed.add(pivotFinalIndex); // Mark indices as changed.
            changed.add(upper);
            steps++; // Count the swap.
            // Report state after the final pivot swap.
            stepCallback.accept(new SortStep(new ArrayList<>(arr), accessed, changed));
        } else {
            // Report state even if no swap occurred (e.g., pivot was already largest).
            // Show potentially accessed indices if they are valid.
            Set<Integer> finalAccessed = new HashSet<>();
            if(pivotFinalIndex < arr.size()) finalAccessed.add(pivotFinalIndex);
            if(upper < arr.size()) finalAccessed.add(upper);
            stepCallback.accept(new SortStep(new ArrayList<>(arr), finalAccessed, changed)); // changed is empty here
        }

        // Return the final index of the pivot.
        return pivotFinalIndex;
    }
}