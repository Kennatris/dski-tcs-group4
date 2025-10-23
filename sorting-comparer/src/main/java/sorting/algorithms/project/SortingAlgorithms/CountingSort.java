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
 * Implements the Counting Sort algorithm.
 * Suitable for sorting collections of objects according to keys that are small non-negative integers.
 * It operates by counting the number of objects that have each distinct key value,
 * and using arithmetic on those counts to determine the positions of each key value in the output sequence.
 * Assumes input contains only non-negative integers.
 */
@Component
public class CountingSort implements SortingAlgorithm {

    // Counter for steps (reads, increments, additions, writes).
    private long steps;
    // Stores the original dataset.
    private List<Integer> dataSet;

    /**
     * Returns the name of the sorting algorithm.
     * @return The string "CountingSort".
     */
    @Override
    public String getName() {
        return "CountingSort";
    }

    /**
     * Returns the best-case time complexity. O(n + k), where n is the number of elements
     * and k is the range of input values (max value).
     * @return The string "O(n + k)".
     */
    @Override
    public String getBestCase() {
        return "O(n + k)";
    }

    /**
     * Returns the average-case time complexity. O(n + k).
     * @return The string "O(n + k)".
     */
    @Override
    public String getAverageCase() {
        return "O(n + k)";
    }

    /**
     * Returns the worst-case time complexity. O(n + k). Performance depends heavily on the range k.
     * @return The string "O(n + k)".
     */
    @Override
    public String getWorstCase() {
        return "O(n + k)";
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
     * Sorts a copy of the input list using Counting Sort. Assumes non-negative integers.
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
        countingSort(copy, (SortStep step) -> {});
        // Return sorted copy.
        return copy;
    }

    /**
     * Sorts the input list using Counting Sort and reports steps during the final copy phase.
     * Assumes non-negative integers. Modifies the list in-place during the final copy phase.
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
        countingSort(input, stepCallback);
        // Note: Final state is sent at the end of countingSort method.
    }

    /**
     * Performs the Counting Sort logic. It counts occurrences, calculates positions, builds an output array,
     * and copies the output back to the input array, reporting steps during the copy-back phase.
     * Assumes non-negative integers.
     * @param arr The list to sort (modified in the final step).
     * @param stepCallback The consumer for reporting steps.
     */
    private void countingSort(List<Integer> arr, Consumer<SortStep> stepCallback) {
        int n = arr.size();
        // Handle empty list case.
        if (n == 0) {
            stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), Collections.emptySet()));
            return;
        }

        // Find the maximum value to determine the size of the count array.
        // Assumes non-negative numbers. No visualization during this phase.
        int max = 0;
        for (int num : arr) {
            // Check for negative numbers, as this implementation doesn't support them.
            if (num < 0) {
                System.err.println("CountingSort requires non-negative integers.");
                // Send unchanged state and return on error.
                stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), Collections.emptySet()));
                return;
            }
            if (num > max) {
                max = num;
            }
        }

        // Create the output array and the count array.
        int[] output = new int[n];
        // Size is max + 1 to accommodate values from 0 to max.
        int[] count = new int[max + 1];

        // 1. Count occurrences of each element. No callback as 'arr' isn't changing yet.
        for (int i = 0; i < n; i++) {
            int num = arr.get(i);
            count[num]++;
            steps++; // Count read access and increment.
        }


        // 2. Modify count array to store actual positions (cumulative counts). No callback.
        for (int i = 1; i <= max; i++) {
            count[i] += count[i - 1];
            steps++; // Count addition.
        }

        // 3. Build the output array. Iterate backwards for stability. No callback.
        for (int i = n - 1; i >= 0; i--) {
            int num = arr.get(i);
            // Place the element at its calculated position in the output array.
            output[count[num] - 1] = num;
            // Decrement count for the next occurrence of this number.
            count[num]--;
            steps++; // Count read accesses, decrement, and write access.
        }

        // 4. Copy the sorted elements from the output array back into the original array.
        // Report each copy operation via the callback.
        for (int i = 0; i < n; i++) {
            // Only report if the value actually changes.
            if (!arr.get(i).equals(output[i])) { // Use equals for Integer comparison
                Set<Integer> changed = new HashSet<>();
                arr.set(i, output[i]);
                changed.add(i);
                steps++; // Count write access.
                // Send the state after the change.
                stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), changed));
            } else {
                // Optionally, send a step even if no change occurs to show progress.
                // steps++; // Count comparison
                // Set<Integer> accessed = Set.of(i);
                // stepCallback.accept(new SortStep(new ArrayList<>(arr), accessed, Collections.emptySet()));
            }
        }
        // Send the final sorted state.
        stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), Collections.emptySet()));
    }
}