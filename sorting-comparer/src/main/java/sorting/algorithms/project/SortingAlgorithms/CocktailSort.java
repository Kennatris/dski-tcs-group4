package sorting.algorithms.project.SortingAlgorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import org.springframework.stereotype.Component;
import sorting.algorithms.project.dto.SortStep;

/**
 * Implements the Cocktail Sort algorithm (also known as Bidirectional Bubble Sort,
 * Cocktail Shaker Sort, Shaker Sort, Ripple Sort, Shuffle Sort, or Shuttle Sort).
 * It's a variation of Bubble Sort that sorts in both directions on each pass through the list.
 */
@Component
public class CocktailSort implements SortingAlgorithm {

    // Counter for the number of steps (comparisons + swaps).
    private long steps;
    // Stores the original dataset passed to the sort method.
    private List<Integer> dataSet;

    /**
     * Returns the name of the sorting algorithm.
     * @return The string "CocktailSort".
     */
    @Override
    public String getName() {
        return "CocktailSort";
    }

    /**
     * Returns the best-case time complexity of Cocktail Sort.
     * O(n) occurs when the list is already sorted or nearly sorted.
     * @return The string "O(n)".
     */
    @Override
    public String getBestCase() {
        return "O(n)";
    }

    /**
     * Returns the average-case time complexity of Cocktail Sort.
     * @return The string "O(n²)".
     */
    @Override
    public String getAverageCase() {
        return "O(n²)";
    }

    /**
     * Returns the worst-case time complexity of Cocktail Sort.
     * @return The string "O(n²)".
     */
    @Override
    public String getWorstCase() {
        return "O(n²)";
    }

    /**
     * Returns the total number of steps (comparisons + swaps) performed during the last sort operation.
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
     * Sorts a copy of the input list using Cocktail Sort.
     * The original list remains unchanged.
     * @param input The list of integers to sort.
     * @return A new list containing the sorted elements.
     */
    @Override
    public List<Integer> sort(List<Integer> input) {
        // Create a mutable copy.
        List<Integer> arr = new ArrayList<>(input);
        // Store the original data.
        dataSet = new ArrayList<>(arr);
        // Reset step count.
        steps = 0;
        // Perform sort with a no-op callback.
        sortWithCallback(arr, (SortStep step) -> {});
        // Return the sorted copy.
        return arr;
    }

    /**
     * Sorts the input list in-place using Cocktail Sort and reports each step.
     * @param arr The list to be sorted (will be modified).
     * @param stepCallback A consumer function to report each {@link SortStep} for visualization.
     */
    @Override
    public void sortWithCallback(List<Integer> arr, Consumer<SortStep> stepCallback) {
        // Store a copy of the original data.
        dataSet = new ArrayList<>(arr);
        // Reset step count for this run.
        steps = 0;
        // Flag to track if any swaps occurred in a pass.
        boolean swapped = true;
        // The start index of the unsorted section.
        int start = 0;
        // The end index (exclusive) of the unsorted section.
        int end = arr.size();

        // Send the initial state of the array.
        stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), Collections.emptySet()));

        // Continue looping as long as swaps are being made.
        while (swapped) {
            // Assume no swaps will happen in this forward pass.
            swapped = false;

            // Forward pass (like Bubble Sort): Move the largest element to the end.
            for (int i = start; i < end - 1; i++) {
                Set<Integer> accessed = new HashSet<>();
                Set<Integer> changed = new HashSet<>();
                accessed.add(i);
                accessed.add(i + 1);
                steps++; // Count comparison.

                // Compare adjacent elements.
                if (arr.get(i) > arr.get(i + 1)) {
                    // Perform swap.
                    int temp = arr.get(i);
                    arr.set(i, arr.get(i + 1));
                    arr.set(i + 1, temp);
                    swapped = true; // Mark that a swap occurred.
                    changed.add(i);
                    changed.add(i + 1);
                    steps++; // Optionally count swap.
                }
                // Report the state after comparison/swap.
                stepCallback.accept(new SortStep(new ArrayList<>(arr), accessed, changed));
            }

            // If no swaps occurred in the forward pass, the list is sorted.
            if (!swapped) break;

            // Reset swapped flag for the backward pass.
            swapped = false;
            // Decrease the end boundary, as the last element is now in its correct place.
            end--;

            // Backward pass: Move the smallest element to the start.
            // Iterate from the second-to-last element down to the start.
            for (int i = end - 1; i >= start; i--) {
                Set<Integer> accessed = new HashSet<>();
                Set<Integer> changed = new HashSet<>();
                // Compare element at 'i' with the element at 'i + 1'.
                // Ensure 'i + 1' is within bounds.
                if (i < arr.size() - 1) {
                    accessed.add(i);
                    accessed.add(i + 1);
                    steps++; // Count comparison.

                    // Compare adjacent elements.
                    if (arr.get(i) > arr.get(i + 1)) {
                        // Perform swap.
                        int temp = arr.get(i);
                        arr.set(i, arr.get(i + 1));
                        arr.set(i + 1, temp);
                        swapped = true; // Mark that a swap occurred.
                        changed.add(i);
                        changed.add(i + 1);
                        steps++; // Optionally count swap.
                    }
                } else {
                    // If 'i' is the last element, mark it as accessed for visualization consistency.
                    accessed.add(i);
                }
                // Report the state after comparison/swap.
                stepCallback.accept(new SortStep(new ArrayList<>(arr), accessed, changed));
            }
            // Increase the start boundary, as the first element is now in its correct place.
            start++;
        }
        // Send the final sorted state.
        stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), Collections.emptySet()));
    }
}