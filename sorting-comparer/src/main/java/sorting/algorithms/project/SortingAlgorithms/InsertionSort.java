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
 * Implements the Insertion Sort algorithm.
 * It builds the final sorted array one item at a time. It iterates through the input list and
 * for each element, it finds the correct position within the already sorted portion of the list
 * and inserts it there, shifting larger elements one position up.
 */
@Component
public class InsertionSort implements SortingAlgorithm {
    // Counter for steps (comparisons, shifts, insertions).
    private long steps;
    // Stores the original dataset.
    private List<Integer> dataSet;

    /**
     * Returns the name of the sorting algorithm.
     * @return The string "InsertionSort".
     */
    @Override
    public String getName() {
        return "InsertionSort";
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
     * Sorts a copy of the input list using Insertion Sort.
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
        insertionSort(copy, (SortStep step) -> {});
        // Return sorted copy.
        return copy;
    }

    /**
     * Sorts the input list in-place using Insertion Sort and reports each comparison, shift, and insertion.
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
        insertionSort(input, stepCallback);
        // Note: Final state is sent at the end of insertionSort method.
    }

    /**
     * Performs the Insertion Sort logic in-place. Iterates through the list, taking one element ('key')
     * at a time and inserting it into its correct position within the sorted sublist to its left.
     * Reports steps for picking the key, comparisons, shifts, and the final insertion.
     * @param arr The list to sort in-place.
     * @param stepCallback The consumer for reporting steps.
     */
    private void insertionSort(List<Integer> arr, Consumer<SortStep> stepCallback) {
        int n = arr.size();
        // Handle lists with 0 or 1 element.
        if (n <= 1) {
            stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), Collections.emptySet())); // Send final state
            return;
        }


        // Iterate from the second element (index 1) to the end.
        for (int i = 1; i < n; i++) {
            // Track indices accessed/changed in the outer loop (picking the key).
            Set<Integer> accessedOuter = new HashSet<>();
            Set<Integer> changedOuter = new HashSet<>();
            accessedOuter.add(i); // Access 'i' to get the key.
            int key = arr.get(i); // The element to be inserted into the sorted portion.
            int j = i - 1; // Start comparing with the element just before the key.

            // Report the state when the 'key' is selected.
            stepCallback.accept(new SortStep(new ArrayList<>(arr), new HashSet<>(accessedOuter), new HashSet<>(changedOuter)));

            // Shift elements in the sorted portion (arr[0...i-1]) that are greater than 'key'
            // one position to the right to make space for 'key'.
            while (j >= 0) {
                // Track indices accessed/changed in the inner loop (comparison and potential shift).
                Set<Integer> accessedInner = new HashSet<>(accessedOuter); // Inherit outer accesses.
                Set<Integer> changedInner = new HashSet<>(changedOuter);
                accessedInner.add(j); // Access 'j' for comparison.
                steps++; // Count the comparison.

                // Report the state *before* a potential shift.
                stepCallback.accept(new SortStep(new ArrayList<>(arr), new HashSet<>(accessedInner), new HashSet<>(changedInner)));

                // If the element at 'j' is greater than the key...
                if (arr.get(j) > key) {
                    // Shift the element at 'j' one position to the right (to j+1).
                    // accessedInner.add(j); // 'j' is already marked accessed.
                    arr.set(j + 1, arr.get(j));
                    changedInner.add(j + 1); // Mark the destination index as changed.
                    steps++; // Optionally count the shift operation.
                    // Report the state *after* the shift.
                    stepCallback.accept(new SortStep(new ArrayList<>(arr), accessedInner, changedInner));
                    j--; // Move to the next element on the left.
                } else {
                    // Element at 'j' is less than or equal to 'key', so 'key' belongs after 'j'.
                    break; // Exit the while loop.
                }
            }

            // Insert 'key' into its correct position (j + 1).
            // Report the insertion only if the position actually changed (i.e., if j != i-1).
            if (j + 1 != i) {
                Set<Integer> changedInsert = new HashSet<>();
                arr.set(j + 1, key); // Place key.
                changedInsert.add(j + 1); // Mark the insertion index as changed.
                steps++; // Count the insertion (write operation).
                // Report the state after insertion, highlighting only the changed index.
                stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), changedInsert));
            } else {
                // Key was already in its correct sorted position relative to the left sublist.
                // Report the state to show the position was checked.
                Set<Integer> accessedInsert = new HashSet<>();
                accessedInsert.add(j+1); // Mark the potential insertion index as accessed.
                stepCallback.accept(new SortStep(new ArrayList<>(arr), accessedInsert, Collections.emptySet()));
            }
        }
        // Send the final sorted state.
        stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), Collections.emptySet()));
    }
}