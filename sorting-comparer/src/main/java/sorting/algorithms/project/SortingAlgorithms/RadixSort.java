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
 * Implements the Radix Sort algorithm using the Least Significant Digit (LSD) approach.
 * It sorts data with integer keys by grouping keys by the individual digits which share the
 * same significant position and value. A positional notation is required, but it does not
 * need to be decimal. Counting Sort is commonly used as a subroutine for sorting by digit.
 * This implementation assumes non-negative integer inputs.
 */
@Component
public class RadixSort implements SortingAlgorithm {
    // Counter for steps (reads, writes, calculations, increments).
    private long steps;
    // Stores the original dataset.
    private List<Integer> dataSet;

    /**
     * Returns the name of the sorting algorithm.
     * @return The string "RadixSort".
     */
    @Override
    public String getName() {
        return "RadixSort";
    }

    /**
     * Returns the worst-case time complexity. O(nk) or O(d(n+b)), where n is the number of keys,
     * k (or d) is the number of digits (or passes), and b is the base used (e.g., 10 for decimal).
     * @return The string "O(nk)".
     */
    @Override
    public String getWorstCase() {
        return "O(nk)";
    }

    /**
     * Returns the average-case time complexity. O(nk).
     * @return The string "O(nk)".
     */
    @Override
    public String getAverageCase() {
        return "O(nk)";
    }

    /**
     * Returns the best-case time complexity. O(nk).
     * @return The string "O(nk)".
     */
    @Override
    public String getBestCase() {
        return "O(nk)";
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
     * Sorts a copy of the input list using LSD Radix Sort. Assumes non-negative integers.
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
        radixSort(copy, (SortStep step) -> {});
        // Return sorted copy.
        return copy;
    }

    /**
     * Sorts the input list in-place using LSD Radix Sort and reports steps during the copy-back phase of Counting Sort.
     * Assumes non-negative integers.
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
        radixSort(input, stepCallback);
        // Send final sorted state.
        stepCallback.accept(new SortStep(new ArrayList<>(input), Collections.emptySet(), Collections.emptySet()));
    }

    /**
     * Performs the LSD Radix Sort logic. Finds the maximum element to determine the number of digits (passes).
     * Then, repeatedly calls a stable sorting algorithm (Counting Sort here) for each digit,
     * starting from the least significant digit to the most significant digit.
     * Assumes non-negative integers.
     * @param arr The list to sort in-place.
     * @param stepCallback The consumer for reporting steps (primarily during Counting Sort's copy-back).
     */
    private void radixSort(List<Integer> arr, Consumer<SortStep> stepCallback) {
        int n = arr.size();
        // Handle lists with 0 or 1 element.
        if (n <= 1) {
            stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), Collections.emptySet())); // Send final state
            return;
        }


        // Find the maximum element to determine the number of digits/passes required.
        // No visualization during this phase. Also checks for negative numbers.
        int max = 0; // Initialize max for non-negative numbers.
        boolean hasNonNegative = false; // Track if any valid numbers are present.
        for (int num : arr) {
            // Check for negative numbers; this LSD implementation requires non-negative.
            if (num < 0) {
                System.err.println("RadixSort (LSD) requires non-negative integers.");
                stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), Collections.emptySet())); // Send unchanged state
                return; // Stop processing.
            }
            hasNonNegative = true; // Found at least one non-negative number.
            if (num > max) {
                max = num; // Update maximum value.
            }
            // Count finding max as steps? Maybe not, focus on sorting steps.
        }
        // Handle case where list might contain only invalid (e.g., negative) numbers, though checked above.
        if (!hasNonNegative && n > 0) {
            stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), Collections.emptySet())); // Send unchanged state
            return;
        }


        // Perform Counting Sort for every digit. 'exp' represents the current digit's place value (1, 10, 100,...).
        // The loop continues as long as there are digits left in the maximum number (max / exp > 0).
        for (int exp = 1; max / exp > 0; exp *= 10) {
            // Call Counting Sort to sort the list based on the digit at the 'exp' place value.
            countSortByDigit(arr, n, exp, stepCallback);
        }
        // Final state is sent by sortWithCallback after this loop finishes.
    }


    /**
     * Performs a stable Counting Sort on the list 'arr' based on the digit specified by 'exp'.
     * Modifies 'arr' in-place during the final copy-back phase. Reports steps during copy-back.
     * @param arr The list to be sorted based on the current digit.
     * @param n The size of the list.
     * @param exp The current digit's place value (1 for units, 10 for tens, etc.).
     * @param stepCallback The consumer for reporting steps during the copy-back phase.
     */
    private void countSortByDigit(List<Integer> arr, int n, int exp, Consumer<SortStep> stepCallback) {
        // Output array to store the sorted elements for this digit pass.
        int[] output = new int[n];
        // Count array for digits 0 through 9.
        int[] count = new int[10];
        // Initialize count array implicitly to zeros.

        // 1. Count occurrences of each digit (0-9) at the current place value 'exp'.
        for (int i = 0; i < n; i++) {
            int num = arr.get(i);
            // Calculate the digit at the 'exp' place.
            int digit = (num / exp) % 10;
            // Ensure digit is valid (0-9).
            if (digit >= 0 && digit < 10) {
                count[digit]++;
                steps++; // Count read, calculation, and increment.
            }
            // Error handling for unexpected digits isn't strictly needed if non-negative check passed.
        }

        // 2. Modify the count array so that count[i] contains the actual position
        // (ending index) of digit 'i' in the output array. (Cumulative counts).
        for (int i = 1; i < 10; i++) {
            count[i] += count[i - 1];
            steps++; // Count addition.
        }

        // 3. Build the output array. Iterate backwards to maintain stability.
        for (int i = n - 1; i >= 0; i--) {
            int num = arr.get(i);
            // Calculate the digit.
            int digit = (num / exp) % 10;
            // Check digit validity.
            if (digit >= 0 && digit < 10) {
                // Determine the correct index in the output array using the cumulative count.
                int outputIndex = count[digit] - 1;
                // Place the number in the output array. Check bounds.
                if (outputIndex >= 0 && outputIndex < n) {
                    output[outputIndex] = num;
                    // Decrement the count for this digit, moving the position for the next element with the same digit.
                    count[digit]--;
                    steps++; // Count reads, decrement, and write.
                }
                // Handle index out of bounds if necessary (shouldn't happen with correct logic).
            }
            // Handle invalid digits if necessary.
        }

        // 4. Copy the sorted elements from the output array back into the original array 'arr'.
        // Report each copy operation via the callback if the value changes.
        for (int i = 0; i < n; i++) {
            // Safety check for array bounds, though should not be needed if n is correct.
            if (i >= output.length) break;
            // Only report if the value at index 'i' actually changes.
            if (!arr.get(i).equals(output[i])) { // Use equals for Integer comparison.
                Set<Integer> changed = new HashSet<>();
                arr.set(i, output[i]);
                changed.add(i);
                steps++; // Count write access.
                // Send the state after the change.
                stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), changed));
            } else {
                // Optionally, report even if no change occurred to show progress during copy-back.
                // Set<Integer> accessed = Set.of(i);
                // stepCallback.accept(new SortStep(new ArrayList<>(arr), accessed, Collections.emptySet()));
            }
        }
        // A final state report after each digit pass isn't strictly necessary,
        // as the main loop in radixSort will continue, and the final final state
        // is sent by sortWithCallback.
    }
}