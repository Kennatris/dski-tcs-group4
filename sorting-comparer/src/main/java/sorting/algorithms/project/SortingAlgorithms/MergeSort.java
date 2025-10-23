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
 * Implements the Merge Sort algorithm.
 * A divide-and-conquer algorithm that divides the input list into two halves,
 * recursively sorts each half, and then merges the two sorted halves.
 */
@Component
public class MergeSort implements SortingAlgorithm {
    // Counter for steps (primarily comparisons and writes during merge).
    private long steps;
    // Stores the original dataset.
    private List<Integer> dataSet;

    /**
     * Returns the name of the sorting algorithm.
     * @return The string "MergeSort".
     */
    @Override
    public String getName() {
        return "MergeSort";
    }

    /**
     * Returns the worst-case time complexity. O(n log n).
     * @return The string "O(n log n)".
     */
    @Override
    public String getWorstCase() {
        return "O(n log n)";
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
     * Returns the best-case time complexity. O(n log n).
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
     * Sorts a copy of the input list using Merge Sort.
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
        mergeSort(copy, 0, copy.size() - 1, (SortStep step) -> {});
        // Return sorted copy.
        return copy;
    }

    /**
     * Sorts the input list in-place using Merge Sort and reports steps during the merge phase.
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
        mergeSort(input, 0, input.size() - 1, stepCallback);
        // Send final sorted state.
        stepCallback.accept(new SortStep(new ArrayList<>(input), Collections.emptySet(), Collections.emptySet()));
    }

    /**
     * The recursive core of Merge Sort. Divides the list into two halves,
     * recursively sorts them, and then merges the sorted halves.
     * @param arr The list (or sublist) to sort.
     * @param lower The starting index of the current sub-array.
     * @param upper The ending index of the current sub-array.
     * @param stepCallback The consumer for reporting steps during the merge phase.
     */
    private void mergeSort(List<Integer> arr, int lower, int upper, Consumer<SortStep> stepCallback) {
        // Optionally report the current recursion range being processed.
        // Set<Integer> accessedRange = IntStream.rangeClosed(lower, upper).filter(i >= 0 && i < arr.size()).boxed().collect(Collectors.toSet());
        // stepCallback.accept(new SortStep(new ArrayList<>(arr), accessedRange, Collections.emptySet()));

        // Base case: If the sub-array has 0 or 1 element, it's already sorted.
        if (lower >= upper) {
            return;
        }

        // Calculate the middle index to divide the array.
        // Using this formula avoids potential integer overflow for very large arrays.
        int mid = lower + (upper - lower) / 2;

        // Recursively sort the left half.
        mergeSort(arr, lower, mid, stepCallback);
        // Recursively sort the right half.
        mergeSort(arr, mid + 1, upper, stepCallback);

        // Merge the two sorted halves back into the original array segment.
        merge(arr, lower, mid, upper, stepCallback);
    }

    /**
     * Merges two sorted sub-arrays (arr[lower..mid] and arr[mid+1..upper]) back into
     * the single sorted sub-array arr[lower..upper]. Reports comparisons and writes
     * during the merge process.
     * @param arr The main list containing the sub-arrays.
     * @param lower The starting index of the first sub-array.
     * @param mid The ending index of the first sub-array.
     * @param upper The ending index of the second sub-array.
     * @param stepCallback The consumer for reporting steps.
     */
    private void merge(List<Integer> arr, int lower, int mid, int upper, Consumer<SortStep> stepCallback) {
        // Create temporary copies of the left and right sub-arrays.
        // Ensure indices are valid before creating sublist views.
        if (lower < 0 || mid < lower || mid >= arr.size() || upper < mid + 1 || upper >= arr.size()) {
            // Handle invalid indices - maybe log an error or return.
            System.err.println("Invalid indices for merge: " + lower + ", " + mid + ", " + upper);
            return;
        }
        List<Integer> left = new ArrayList<>(arr.subList(lower, mid + 1));
        List<Integer> right = new ArrayList<>(arr.subList(mid + 1, upper + 1));

        int i = 0; // Pointer for the temporary left sub-array.
        int j = 0; // Pointer for the temporary right sub-array.
        int k = lower; // Pointer for the main array 'arr' where merged elements are placed.

        // Merge elements from 'left' and 'right' back into 'arr' in sorted order.
        while (i < left.size() && j < right.size()) {
            Set<Integer> accessed = new HashSet<>();
            Set<Integer> changed = new HashSet<>();

            // Identify corresponding indices in the main array for visualization.
            accessed.add(lower + i);   // Element in 'arr' corresponding to left[i].
            accessed.add(mid + 1 + j); // Element in 'arr' corresponding to right[j].

            steps++; // Count the comparison.

            // Report state *before* placing the chosen element. Shows the comparison.
            stepCallback.accept(new SortStep(new ArrayList<>(arr), new HashSet<>(accessed), new HashSet<>(changed)));

            // Compare elements from the left and right sub-arrays.
            if (left.get(i) <= right.get(j)) {
                // Take element from the left sub-array.
                arr.set(k, left.get(i));
                changed.add(k); // Mark index 'k' in the main array as changed.
                i++; // Move left pointer.
            } else {
                // Take element from the right sub-array.
                arr.set(k, right.get(j));
                changed.add(k); // Mark index 'k' in the main array as changed.
                j++; // Move right pointer.
            }
            // Report state *after* placing the element. Shows the write operation.
            stepCallback.accept(new SortStep(new ArrayList<>(arr), accessed, changed));
            k++; // Move main array pointer.
        }

        // Copy any remaining elements from the left sub-array (if right sub-array exhausted first).
        while (i < left.size()) {
            Set<Integer> changed = new HashSet<>();
            Set<Integer> accessed = new HashSet<>();
            accessed.add(lower+i); // Indicate read access from the conceptual left part.
            arr.set(k, left.get(i));
            changed.add(k); // Mark index 'k' as changed.
            steps++; // Count the write operation as a step.
            // Report the state after copying the element.
            stepCallback.accept(new SortStep(new ArrayList<>(arr), accessed, changed));
            i++;
            k++;
        }

        // Copy any remaining elements from the right sub-array (if left sub-array exhausted first).
        while (j < right.size()) {
            Set<Integer> changed = new HashSet<>();
            Set<Integer> accessed = new HashSet<>();
            accessed.add(mid + 1 + j); // Indicate read access from the conceptual right part.
            arr.set(k, right.get(j));
            changed.add(k); // Mark index 'k' as changed.
            steps++; // Count the write operation as a step.
            // Report the state after copying the element.
            stepCallback.accept(new SortStep(new ArrayList<>(arr), accessed, changed));
            j++;
            k++;
        }
    }
}