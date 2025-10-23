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
 * Implements the Pigeonhole Sort algorithm.
 * Suitable for sorting lists where the number of elements (n) and the range of possible key values (k)
 * are approximately the same. It finds the min/max values, creates "pigeonholes" (an auxiliary array)
 * for each value in the range, places elements into their corresponding pigeonhole, and then iterates
 * through the pigeonholes to reconstruct the sorted list. Assumes non-negative integers.
 */
@Component
public class PigeonholeSort implements SortingAlgorithm {
    // Counter for steps (comparisons, reads, writes, increments).
    private long steps;
    // Stores the original dataset.
    private List<Integer> dataSet;

    /**
     * Returns the name of the sorting algorithm.
     * @return The string "PigeonholeSort".
     */
    @Override
    public String getName() {
        return "PigeonholeSort";
    }

    /**
     * Returns the worst-case time complexity. O(n + range), where n is the number of elements
     * and range is the difference between the max and min values plus one.
     * @return The string "O(n + range)".
     */
    @Override
    public String getWorstCase() {
        return "O(n + range)";
    }

    /**
     * Returns the average-case time complexity. O(n + range).
     * @return The string "O(n + range)".
     */
    @Override
    public String getAverageCase() {
        return "O(n + range)";
    }

    /**
     * Returns the best-case time complexity. O(n + range).
     * @return The string "O(n + range)".
     */
    @Override
    public String getBestCase() {
        return "O(n + range)";
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
     * Sorts a copy of the input list using Pigeonhole Sort. Assumes non-negative integers.
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
        pigeonholeSort(copy, (SortStep step) -> {});
        // Return sorted copy.
        return copy;
    }

    /**
     * Sorts the input list using Pigeonhole Sort and reports steps during min/max finding and array reconstruction.
     * Assumes non-negative integers. Modifies the list in-place during reconstruction.
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
        pigeonholeSort(input, stepCallback);
        // Note: Final state is sent at the end of pigeonholeSort method.
    }

    /**
     * Performs the Pigeonhole Sort logic. Finds min/max, creates and fills pigeonholes (counts),
     * and reconstructs the sorted array. Assumes non-negative integers. Reports steps during
     * min/max search and reconstruction phases.
     * @param arr The list to sort (modified in-place during reconstruction).
     * @param stepCallback The consumer for reporting steps.
     */
    private void pigeonholeSort(List<Integer> arr, Consumer<SortStep> stepCallback) {
        int n = arr.size();
        // Handle lists with 0 or 1 element.
        if (n <= 1) {
            stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), Collections.emptySet())); // Send final state
            return;
        }


        // 1. Find Minimum and Maximum values, reporting steps for visualization.
        int min = arr.get(0);
        int max = arr.get(0);
        Set<Integer> accessedMinMax = new HashSet<>();
        accessedMinMax.add(0); // Access first element.
        // Report initial access.
        stepCallback.accept(new SortStep(new ArrayList<>(arr), new HashSet<>(accessedMinMax), Collections.emptySet()));

        // Iterate through the rest of the list.
        for (int i = 1; i < n; i++) {
            accessedMinMax.clear();
            accessedMinMax.add(i); // Mark current index as accessed.
            int currentVal = arr.get(i);
            steps++; // Count read access.

            // Report state before comparisons.
            stepCallback.accept(new SortStep(new ArrayList<>(arr), new HashSet<>(accessedMinMax), Collections.emptySet()));

            steps++; // Count comparison with max.
            if (currentVal > max) {
                max = currentVal;
            }
            steps++; // Count comparison with min.
            // Check for negative numbers; this simple version assumes non-negative.
            if (currentVal < 0) {
                System.err.println("PigeonholeSort requires non-negative integers. Found: " + currentVal);
                stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), Collections.emptySet())); // Unchanged state
                return; // Stop processing if negative found
            }
            if (currentVal < min) {
                min = currentVal;
            }
            // Report state after comparisons (implicitly shows new min/max if changed).
            // stepCallback.accept(new SortStep(new ArrayList<>(arr), new HashSet<>(accessedMinMax), Collections.emptySet()));
        }
        // Report state after iterating through the whole list for min/max.
        Set<Integer> allAccessed = IntStream.range(0, n).boxed().collect(Collectors.toSet());
        stepCallback.accept(new SortStep(new ArrayList<>(arr), allAccessed, Collections.emptySet()));


        // 2. Create and fill the "Pigeonholes" (count array).
        int range = max - min + 1;

        // Safety check: If the range is excessively large compared to the number of elements,
        // Pigeonhole Sort becomes very inefficient in terms of memory. Warn or fallback.
        if (range > n * 10 && range > 1000000) { // Example heuristic: range > 10*n and > 1 million
            System.err.println("PigeonholeSort range (" + range + ") is very large compared to size (" + n + "). Consider a different algorithm.");
            // Send unchanged state and return.
            stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), Collections.emptySet()));
            return;
        }
        // Handle potential edge case where min > max (only if input validation failed).
        if (range <= 0) {
            stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), Collections.emptySet())); // Unchanged state
            return;
        }

        // Create the pigeonholes (count array).
        int[] holes = new int[range];

        // Count occurrences of each element. No callback here as 'arr' isn't modified.
        for (int i = 0; i < n; i++) {
            // Calculate the index in the 'holes' array.
            int holeIndex = arr.get(i) - min;
            // Check bounds before accessing the holes array.
            if(holeIndex >= 0 && holeIndex < range) {
                holes[holeIndex]++;
                steps++; // Count read access and increment.
            } else {
                // Should not happen if min/max calculation and non-negative check are correct.
                System.err.println("Value " + arr.get(i) + " resulted in invalid hole index " + holeIndex);
            }
        }

        // 3. Reconstruct the sorted array.
        int index = 0; // Index for placing elements back into 'arr'.
        // Iterate through the pigeonholes (indices represent values relative to min).
        for (int j = 0; j < range; j++) {
            // Place the value (j + min) into 'arr' as many times as it was counted.
            while (holes[j] > 0) {
                Set<Integer> changed = new HashSet<>();
                // Calculate the actual value to write.
                int valueToWrite = j + min;

                // Check if the target position exists and if the value needs changing.
                // This handles both adding to an empty list and overwriting existing elements.
                // It avoids unnecessary steps if the element is already correct (though unlikely here).
                if (index >= arr.size() || !arr.get(index).equals(valueToWrite)) { // Use equals for Integer
                    // If index is out of bounds, add; otherwise, set.
                    if (index >= arr.size()) arr.add(valueToWrite); else arr.set(index, valueToWrite);

                    changed.add(index); // Mark the index as changed.
                    steps++; // Count write/add access and decrement.
                    holes[j]--; // Decrement the count for this value.
                    // Report the state after the modification.
                    stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), changed));
                } else {
                    // Value is already correct, just decrement the count.
                    holes[j]--;
                    steps++; // Count the decrement.
                    // Optionally report accessing the index without changing it.
                    // Set<Integer> accessed = Set.of(index);
                    // stepCallback.accept(new SortStep(new ArrayList<>(arr), accessed, Collections.emptySet()));
                }
                index++; // Move to the next position in 'arr'.
            }
        }
        // Send the final sorted state.
        stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), Collections.emptySet()));
    }
}