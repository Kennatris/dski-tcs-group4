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
 * Implements a simple version of Bucket Sort, suitable for non-negative integers
 * with a reasonable range. It functions similarly to Counting Sort in this implementation.
 * It distributes elements into buckets based on their value and then concatenates the buckets.
 */
@Component
public class BucketSort implements SortingAlgorithm {

    // Counter for the number of steps (operations like comparisons, increments, additions).
    private long steps;
    // Stores the original dataset passed to the sort method.
    private List<Integer> dataSet;

    /**
     * Returns the name of the sorting algorithm.
     * @return The string "BucketSort".
     */
    @Override
    public String getName() {
        return "BucketSort";
    }

    /**
     * Returns the best-case time complexity of Bucket Sort.
     * O(n + k) where n is the number of elements and k is the range of input values,
     * assuming elements are uniformly distributed.
     * @return The string "O(n + k)".
     */
    @Override
    public String getBestCase() {
        return "O(n + k)";
    }

    /**
     * Returns the average-case time complexity of Bucket Sort.
     * O(n + k) under the assumption of uniform distribution.
     * @return The string "O(n + k)".
     */
    @Override
    public String getAverageCase() {
        return "O(n + k)";
    }

    /**
     * Returns the worst-case time complexity of Bucket Sort.
     * O(n²) if all elements fall into the same bucket and an inefficient sort is used within buckets
     * (though this simple version avoids that by using value as index). Can also be O(n+k) if k is large.
     * @return The string "O(n²)" (Reflecting potential bad distribution or large range issues).
     */
    @Override
    public String getWorstCase() {
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
     * Sorts a copy of the input list using this simplified Bucket Sort.
     * The original list remains unchanged. Assumes non-negative integers.
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
        bucketSort(copy, (SortStep step) -> {});
        // Return the sorted copy.
        return copy;
    }

    /**
     * Sorts the input list in-place using Bucket Sort and reports visualization steps.
     * Assumes non-negative integers. Modifies the input list significantly (clears and rebuilds).
     * @param input The list to be sorted (will be modified).
     * @param stepCallback A consumer function to report each {@link SortStep}.
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
        bucketSort(input, stepCallback);
    }

    /**
     * Performs the simplified Bucket Sort logic. Finds the maximum value, creates buckets (counts),
     * and rebuilds the input array from the buckets. Assumes non-negative integers.
     * @param arr The list to sort (will be cleared and rebuilt).
     * @param stepCallback The consumer for reporting steps.
     */
    private void bucketSort(List<Integer> arr, Consumer<SortStep> stepCallback) {
        int n = arr.size();
        // Handle empty list case.
        if (n == 0) {
            stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), Collections.emptySet())); // Send final state for empty list
            return;
        }

        // Find the maximum value in the array, reporting steps during the search.
        int maxValue = findMaxAndVisualize(arr, stepCallback);
        // This simple version doesn't handle negative numbers well. Check if max is valid.
        if (maxValue < 0) {
            System.err.println("BucketSort (simple version) doesn't handle negative numbers well.");
            // Send the unchanged state and return if only negative numbers or error.
            stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), Collections.emptySet()));
            return;
        }

        // Create buckets (count array). Size is maxValue + 1 to include 0 up to maxValue.
        int[] bucket = new int[maxValue + 1];

        // Count occurrences of each number. No array modification, so no callback here.
        for (int i = 0; i < n; i++) {
            int num = arr.get(i);
            // Ensure the number is within the expected non-negative range.
            if (num >= 0 && num <= maxValue) {
                bucket[num]++;
                steps++; // Count array access and increment.
            } else {
                // Log an error if a value is out of the expected range (e.g., negative).
                System.err.println("Value " + num + " out of expected range [0..." + maxValue + "]");
            }
        }


        // Rebuild the sorted array from the buckets.
        arr.clear(); // Clear the original list. This modifies the state.
        stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), Collections.emptySet())); // Report state after clear().

        int currentArrIndex = 0; // Keep track of the current index in the rebuilt array 'arr'.
        // Iterate through the buckets (indices represent values).
        for (int i = 0; i < bucket.length; i++) {
            // Add the value 'i' to the array 'bucket[i]' times.
            for (int j = 0; j < bucket[i]; j++) {
                Set<Integer> changed = new HashSet<>();
                arr.add(i); // Add the value 'i' to the end of the list.
                changed.add(currentArrIndex); // Mark the newly added index as changed.
                steps++; // Count the add operation.
                // Report the state after adding the element.
                stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), changed));
                currentArrIndex++; // Move to the next index in the rebuilt array.
            }
        }
        // Final state might already be sent by the last add, but send for consistency if needed.
        // stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), Collections.emptySet()));
    }

    /**
     * Finds the maximum non-negative value in the list while reporting access steps for visualization.
     * @param arr The list to search.
     * @param stepCallback The consumer for reporting steps.
     * @return The maximum non-negative value found, or -1 if the list is empty or contains only negative numbers.
     */
    private int findMaxAndVisualize(List<Integer> arr, Consumer<SortStep> stepCallback) {
        if (arr.isEmpty()) return -1;

        int max = -1; // Initialize max to -1 to handle lists with only negative numbers.
        // Find the first non-negative value to initialize max correctly.
        for(int val : arr) {
            if (val >= 0) {
                max = val;
                break; // Found the first non-negative, stop searching for initial max.
            }
        }
        // If no non-negative value was found, use the first element as a fallback (though it might be negative).
        if (max == -1 && !arr.isEmpty()) max = arr.get(0);

        Set<Integer> accessed = new HashSet<>();
        // Report access to the first element (index 0) during initialization/fallback.
        if (!arr.isEmpty()) accessed.add(0);
        stepCallback.accept(new SortStep(new ArrayList<>(arr), accessed, Collections.emptySet()));


        // Iterate through the rest of the list to find the true maximum.
        for (int i = 1; i < arr.size(); i++) {
            accessed.clear();
            accessed.add(i); // Mark the current index as accessed.
            int num = arr.get(i);
            // Update max if the current number is greater.
            if (num > max) {
                max = num;
            }
            steps++; // Count the comparison.
            // Report the access step.
            stepCallback.accept(new SortStep(new ArrayList<>(arr), accessed, Collections.emptySet()));
        }
        // Return the maximum value found.
        return max;
    }
}