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
 * Implements the Cycle Sort algorithm.
 * An in-place, unstable sorting algorithm that is theoretically optimal in terms of the total
 * number of writes to the original array. It minimizes writes by identifying cycles of elements
 * that need to be rotated to reach their correct positions.
 */
@Component
public class CycleSort implements SortingAlgorithm {

    // Counter for steps (comparisons and writes/swaps).
    private long steps;
    // Stores the original dataset.
    private List<Integer> dataSet;

    /**
     * Returns the name of the sorting algorithm.
     * @return The string "CycleSort".
     */
    @Override
    public String getName() {
        return "CycleSort";
    }

    /**
     * Returns the best-case time complexity. O(n²).
     * @return The string "O(n²)".
     */
    @Override
    public String getBestCase() {
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
     * Returns the worst-case time complexity. O(n²).
     * @return The string "O(n²)".
     */
    @Override
    public String getWorstCase() {
        return "O(n²)";
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
     * Sorts a copy of the input list using Cycle Sort.
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
        cycleSort(copy, (SortStep step) -> {});
        // Return sorted copy.
        return copy;
    }

    /**
     * Sorts the input list in-place using Cycle Sort and reports each comparison and write.
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
        cycleSort(input, stepCallback);
        // Note: Final state is sent at the end of cycleSort method.
    }

    /**
     * Performs the Cycle Sort logic in-place. Iterates through the array, and for each element,
     * finds its correct position and rotates the cycle involving that element.
     * Reports each comparison and write operation.
     * @param arr The list to sort in-place.
     * @param stepCallback The consumer for reporting steps.
     */
    private void cycleSort(List<Integer> arr, Consumer<SortStep> stepCallback) {
        int n = arr.size();
        // Handle lists with 0 or 1 element.
        if (n <= 1) {
            stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), Collections.emptySet())); // Send final state
            return;
        }


        // Iterate through the array to find cycles starting at each index.
        for (int cycle_start = 0; cycle_start <= n - 2; cycle_start++) {
            // Sets to track accessed and changed indices within the current cycle processing.
            Set<Integer> accessedInCycle = new HashSet<>();
            Set<Integer> changedInCycle = new HashSet<>();

            // The element to place correctly.
            int item = arr.get(cycle_start);
            accessedInCycle.add(cycle_start); // Mark the start index as accessed.

            // Find the correct position 'pos' for 'item' by counting smaller elements to its right.
            int pos = cycle_start;
            for (int i = cycle_start + 1; i < n; i++) {
                accessedInCycle.add(i); // Mark index 'i' as accessed for comparison.
                steps++; // Count the comparison.
                // Report state before potential increment of 'pos'.
                stepCallback.accept(new SortStep(new ArrayList<>(arr), new HashSet<>(accessedInCycle), new HashSet<>(changedInCycle)));
                if (arr.get(i) < item) {
                    pos++; // Increment position if a smaller element is found.
                }
            }


            // If the item is already in its correct position, skip to the next cycle start.
            if (pos == cycle_start) {
                // Report the state even if nothing happened, showing the indices checked.
                stepCallback.accept(new SortStep(new ArrayList<>(arr), new HashSet<>(accessedInCycle), new HashSet<>(changedInCycle)));
                continue;
            }

            // Skip duplicate elements. Find the first position after 'pos' that doesn't hold 'item'.
            while (pos < n && item == arr.get(pos)) {
                accessedInCycle.add(pos); // Mark 'pos' as accessed during duplicate check.
                steps++; // Count the comparison.
                // Report state during duplicate skipping.
                stepCallback.accept(new SortStep(new ArrayList<>(arr), new HashSet<>(accessedInCycle), new HashSet<>(changedInCycle)));
                pos++;
            }


            // Place the 'item' at its correct position 'pos'.
            if (pos != cycle_start && pos < n) { // Ensure 'pos' is valid and different from start.
                accessedInCycle.add(pos); // Mark 'pos' for get/set access.
                int temp = item; // Store item temporarily.
                item = arr.get(pos); // The element currently at 'pos' becomes the new 'item' to place.
                arr.set(pos, temp); // Place the original 'item' at 'pos'.
                changedInCycle.add(pos); // Mark 'pos' as changed.
                steps++; // Count the write/swap operation.
                // Report state after placing the item.
                stepCallback.accept(new SortStep(new ArrayList<>(arr), new HashSet<>(accessedInCycle), new HashSet<>(changedInCycle)));
            } else if (pos >=n ) {
                // Handle cases where position is out of bounds (shouldn't happen if duplicates handled correctly)
                // or all remaining elements were duplicates. Report state and continue.
                stepCallback.accept(new SortStep(new ArrayList<>(arr), new HashSet<>(accessedInCycle), new HashSet<>(changedInCycle)));
                continue; // Move to the next cycle_start.
            }


            // Rotate the rest of the cycle until the element originally at cycle_start is placed.
            while (pos != cycle_start) {
                // Reset accessed/changed sets for finding the position of the *new* item.
                accessedInCycle.clear();
                changedInCycle.clear();

                // Find the correct position for the *new* 'item' (which was displaced).
                pos = cycle_start; // Start searching from cycle_start again.
                for (int i = cycle_start + 1; i < n; i++) {
                    accessedInCycle.add(i); // Mark index 'i' as accessed.
                    steps++; // Count comparison.
                    // Report state before potential increment of 'pos'.
                    stepCallback.accept(new SortStep(new ArrayList<>(arr), new HashSet<>(accessedInCycle), new HashSet<>(changedInCycle)));
                    if (arr.get(i) < item) {
                        pos++;
                    }
                }

                // Skip duplicates for the new 'item'.
                while (pos < n && item == arr.get(pos)) {
                    accessedInCycle.add(pos); // Mark 'pos' as accessed.
                    steps++; // Count comparison.
                    // Report state during duplicate skipping.
                    stepCallback.accept(new SortStep(new ArrayList<>(arr), new HashSet<>(accessedInCycle), new HashSet<>(changedInCycle)));
                    pos++;
                }

                // Place the new 'item' at its correct position 'pos', if valid and different.
                if (pos < n && !arr.get(pos).equals(item)) { // Use equals for Integer comparison
                    accessedInCycle.add(pos); // Mark 'pos' for get/set access.
                    int temp = item;
                    item = arr.get(pos); // The next element in the cycle becomes the new 'item'.
                    arr.set(pos, temp);
                    changedInCycle.add(pos); // Mark 'pos' as changed.
                    steps++; // Count write/swap.
                    // Report state after placing the item.
                    stepCallback.accept(new SortStep(new ArrayList<>(arr), new HashSet<>(accessedInCycle), new HashSet<>(changedInCycle)));
                } else if (pos >= n || arr.get(pos).equals(item)) { // Position invalid or duplicate reached start?
                    // Report state and break the inner while loop (cycle should be complete or error).
                    stepCallback.accept(new SortStep(new ArrayList<>(arr), new HashSet<>(accessedInCycle), new HashSet<>(changedInCycle)));
                    break;
                }
            }
        }
        // Send the final sorted state.
        stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), Collections.emptySet()));
    }
}