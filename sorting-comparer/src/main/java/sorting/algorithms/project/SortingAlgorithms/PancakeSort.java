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
 * Implements the Pancake Sort algorithm.
 * The only allowed operation is to reverse the elements of some prefix of the sequence.
 * The goal is to sort the sequence using the minimum number of flips. This implementation
 * repeatedly finds the largest unsorted element, flips it to the top, and then flips it
 * to its correct final position.
 */
@Component
public class PancakeSort implements SortingAlgorithm {
    // Counter for steps (comparisons during findMax and swaps during flip).
    private long steps;
    // Stores the original dataset.
    private List<Integer> dataSet;

    /**
     * Returns the name of the sorting algorithm.
     * @return The string "PancakeSort".
     */
    @Override
    public String getName() {
        return "PancakeSort";
    }

    /**
     * Returns the worst-case time complexity. O(n²).
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
     * Returns the best-case time complexity. While sometimes O(n) flips might suffice
     * if already sorted, the search for max still takes O(n²) comparisons in this implementation.
     * @return The string "O(n²)".
     */
    @Override
    public String getBestCase() {
        return "O(n²)"; // Search still dominates
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
     * Sorts a copy of the input list using Pancake Sort.
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
        pancakeSort(copy, (SortStep step) -> {});
        // Return sorted copy.
        return copy;
    }

    /**
     * Sorts the input list in-place using Pancake Sort and reports steps during findMax and flip operations.
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
        pancakeSort(input, stepCallback);
        // Note: Final state is sent at the end of pancakeSort method.
    }

    /**
     * Performs the Pancake Sort logic in-place. In each iteration, it finds the maximum
     * element in the remaining unsorted prefix, flips it to the beginning, and then flips
     * it to its correct final position at the end of the unsorted prefix.
     * Reports comparisons during the search and swaps during the flips.
     * @param arr The list to sort in-place.
     * @param stepCallback The consumer for reporting steps.
     */
    private void pancakeSort(List<Integer> arr, Consumer<SortStep> stepCallback) {
        int n = arr.size();
        // Handle lists with 0 or 1 element.
        if (n <= 1) {
            stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), Collections.emptySet())); // Send final state
            return;
        }


        // Iterate from the full list size down to 2 elements.
        // 'curr_size' represents the size of the unsorted prefix currently being considered.
        for (int curr_size = n; curr_size > 1; --curr_size) {
            // Find the index 'mi' of the maximum element within the prefix arr[0...curr_size-1].
            int mi = findMax(arr, curr_size, stepCallback);

            // If the maximum element is not already at the end of the current prefix...
            if (mi != curr_size - 1) {
                // 1. Flip the prefix arr[0...mi] to bring the maximum element to the beginning (index 0).
                // Only flip if the max element is not already at the beginning.
                if (mi != 0) {
                    flip(arr, mi, stepCallback);
                }
                // 2. Flip the prefix arr[0...curr_size-1] to move the maximum element (now at index 0)
                // to its correct final position (index curr_size - 1).
                flip(arr, curr_size - 1, stepCallback);
            }
            // Optional: Report state after placing one maximum element correctly.
            // stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), Collections.emptySet()));
        }
        // Send the final sorted state.
        stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), Collections.emptySet()));
    }

    /**
     * Reverses the order of elements in the prefix of the list from index 0 to index 'i' (inclusive).
     * Reports each individual swap performed during the reversal.
     * @param arr The list containing the prefix to flip.
     * @param i The last index of the prefix to be flipped.
     * @param stepCallback The consumer for reporting swap steps.
     */
    private void flip(List<Integer> arr, int i, Consumer<SortStep> stepCallback) {
        // Check for valid index before proceeding.
        if (i < 0 || i >= arr.size()) return;

        int start = 0; // Start pointer for the flip.
        // Continue swapping elements from the ends towards the middle.
        while (start < i) {
            Set<Integer> accessed = new HashSet<>();
            Set<Integer> changed = new HashSet<>();
            accessed.add(start); // Mark accessed indices for the swap.
            accessed.add(i);

            // Perform the swap.
            int temp = arr.get(start);
            arr.set(start, arr.get(i));
            arr.set(i, temp);
            changed.add(start); // Mark changed indices.
            changed.add(i);
            steps++; // Count the swap.

            // Report the state after each swap within the flip operation.
            stepCallback.accept(new SortStep(new ArrayList<>(arr), accessed, changed));

            // Move pointers towards the middle.
            start++;
            i--;
        }
    }

    /**
     * Finds the index of the maximum element within the prefix of the list of size 'n' (i.e., arr[0...n-1]).
     * Reports each comparison made during the search.
     * @param arr The list to search within.
     * @param n The size of the prefix to search (exclusive upper bound for index).
     * @param stepCallback The consumer for reporting comparison steps.
     * @return The index of the maximum element found in the prefix arr[0...n-1]. Returns -1 if n <= 0.
     */
    private int findMax(List<Integer> arr, int n, Consumer<SortStep> stepCallback) {
        // Handle invalid prefix size.
        if (n <= 0) return -1;

        // Assume the first element is the maximum initially.
        int maxIndex = 0;
        Set<Integer> accessedOverall = new HashSet<>(); // Track all indices accessed during this search.
        accessedOverall.add(0); // Add the initial access.

        // Report the state at the beginning of the search.
        stepCallback.accept(new SortStep(new ArrayList<>(arr), new HashSet<>(accessedOverall), Collections.emptySet()));

        // Iterate through the prefix starting from the second element.
        for (int i = 1; i < n; i++) {
            Set<Integer> accessedCurrent = new HashSet<>();
            accessedCurrent.add(i);        // Current element being compared.
            accessedCurrent.add(maxIndex); // Current maximum being compared against.
            accessedOverall.add(i);        // Add current index to overall accessed set.
            steps++; // Count the comparison.

            // Report the state *before* potentially updating maxIndex, showing the comparison.
            stepCallback.accept(new SortStep(new ArrayList<>(arr), new HashSet<>(accessedCurrent), Collections.emptySet()));

            // If the current element is greater than the current maximum...
            if (arr.get(i) > arr.get(maxIndex)) {
                maxIndex = i; // Update the index of the maximum element.
                // Optionally report state *after* updating maxIndex to highlight the new candidate.
                stepCallback.accept(new SortStep(new ArrayList<>(arr), new HashSet<>(accessedCurrent), Collections.emptySet()));
            }
        }
        // Optional: Report state at the end showing all accessed elements in the search range.
        // Set<Integer> finalAccessed = IntStream.range(0, n).boxed().collect(Collectors.toSet());
        // stepCallback.accept(new SortStep(new ArrayList<>(arr), finalAccessed, Collections.emptySet()));

        // Return the index of the maximum element found.
        return maxIndex;
    }
}