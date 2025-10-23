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
 * Implements the Stalin Sort "algorithm".
 * This algorithm iterates through the list and removes any element
 * that is smaller than its preceding element. It is not a true sorting
 * algorithm as it modifies the list content by removing elements.
 */
@Component
public class StalinSort implements SortingAlgorithm {
    // Stores the number of steps (comparisons + removals) performed during the last sort.
    private long steps;
    // Stores a copy of the original dataset *before* elements were removed.
    private List<Integer> dataSet;

    /**
     * Returns the name of the sorting algorithm.
     * @return The string "StalinSort".
     */
    @Override
    public String getName() {
        return "StalinSort";
    }

    /**
     * Returns the time complexity for the worst-case scenario.
     * The algorithm always iterates through the list once.
     * @return The string "O(n)".
     */
    @Override
    public String getWorstCase() {
        return "O(n)";
    }

    /**
     * Returns the time complexity for the average-case scenario.
     * @return The string "O(n)".
     */
    @Override
    public String getAverageCase() {
        return "O(n)";
    }

    /**
     * Returns the time complexity for the best-case scenario (already sorted list).
     * @return The string "O(n)".
     */
    @Override
    public String getBestCase() {
        return "O(n)";
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
     * Returns the original dataset that was provided *before* sorting/element removal.
     * Returns a default dataset if no sort has been performed yet.
     * @return The list of integers representing the original dataset.
     */
    @Override
    public List<Integer> getData() {
        // Ensure dataSet is not null; return default if it is.
        return dataSet != null ? dataSet : SortingAlgorithm.super.getData();
    }

    /**
     * "Sorts" a copy of the input list using the Stalin Sort algorithm.
     * Important: This method modifies the list size and content by removing elements.
     * It always works on a copy to avoid modifying the original input list.
     * @param input The list of integers to "sort".
     * @return A new list containing the remaining elements after applying Stalin Sort.
     */
    @Override
    public List<Integer> sort(List<Integer> input) {
        // Important: StalinSort modifies the list by removing elements.
        // Always create a copy for the 'sort' method to ensure the original isn't changed.
        List<Integer> copy = new ArrayList<>(input);
        dataSet = new ArrayList<>(input); // Store the original list before modification.
        steps = 0;
        stalinSort(copy, (SortStep step) -> {}); // Operate on the copy.
        return copy; // Return the modified copy.
    }

    /**
     * "Sorts" the input list in-place using the Stalin Sort algorithm by removing elements,
     * and provides step-by-step updates via a callback function.
     * Warning: This method modifies the input list directly, including its size.
     * @param input The list of integers to "sort" (will be modified).
     * @param stepCallback A consumer function that accepts SortStep objects for visualization.
     */
    @Override
    public void sortWithCallback(List<Integer> input, Consumer<SortStep> stepCallback) {
        // This method modifies the input list directly. Ensure this is the intended behavior.
        // If not, a copy should be created here as well.
        dataSet = new ArrayList<>(input); // Store a copy of the original list before modification.
        steps = 0;
        // Send the initial state.
        stepCallback.accept(new SortStep(new ArrayList<>(input), Collections.emptySet(), Collections.emptySet()));
        stalinSort(input, stepCallback); // Modifies 'input' directly by removing elements.
        // Send the final state after removals.
        stepCallback.accept(new SortStep(new ArrayList<>(input), Collections.emptySet(), Collections.emptySet()));
    }

    /**
     * Performs the Stalin Sort algorithm in-place. It iterates through the list
     * and removes any element found to be smaller than the element preceding it.
     * @param arr The list to be "sorted" (will be modified, potentially shortened).
     * @param stepCallback The consumer for reporting each step (comparison or removal).
     */
    private void stalinSort(List<Integer> arr, Consumer<SortStep> stepCallback) {
        // If the list is empty, there's nothing to do.
        if (arr.isEmpty()) {
            stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), Collections.emptySet())); // Send final state
            return;
        }

        int i = 1; // Start checking from the second element (index 1).
        // Loop while 'i' is within the current bounds of the list.
        // Important: Use arr.size() inside the loop condition as it changes upon removal.
        while (i < arr.size()) {
            Set<Integer> accessed = new HashSet<>();
            // 'changed' is typically used for swaps, less direct for removal, but could mark removed index area.
            Set<Integer> changed = new HashSet<>();

            accessed.add(i);     // Current element being checked.
            accessed.add(i - 1); // Previous element for comparison.
            steps++; // Count the comparison.

            // Send the state *before* potential removal. Shows the comparison.
            stepCallback.accept(new SortStep(new ArrayList<>(arr), new HashSet<>(accessed), new HashSet<>(changed)));

            // If the current element is smaller than the previous one...
            if (arr.get(i) < arr.get(i - 1)) {
                arr.remove(i); // ...remove the current element.
                // Important: Do NOT increment 'i'. The element originally at i+1
                // is now at index 'i' and needs to be compared with arr[i-1] in the next iteration.
                steps++; // Optionally count the removal as a step.

                // Indices from 'i' onwards have effectively shifted. While we don't mark them
                // 'changed' in the traditional swap sense, the array state reflects the removal.
                // We'll leave 'changed' empty here as the primary change is the array structure.

                // Send the state *after* the removal.
                stepCallback.accept(new SortStep(new ArrayList<>(arr), accessed, changed));

            } else {
                // Element is in order relative to the previous one, move to the next element.
                i++;
                // Optionally send state after moving forward if every check should be a step.
                // stepCallback.accept(new SortStep(new ArrayList<>(arr), accessed, changed));
            }
            // Removed redundant step send here, as state is sent after removal or optionally after increment.
        }
    }
}