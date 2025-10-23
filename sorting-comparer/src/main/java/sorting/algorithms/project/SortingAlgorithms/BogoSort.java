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
 * Implements the BogoSort (also known as Permutation Sort, Stupid Sort, or Slowsort) algorithm.
 * This is a highly inefficient sorting algorithm based on generating permutations of the input
 * until a sorted one is found. Primarily used for educational purposes to illustrate a worst-case scenario.
 */
@Component
public class BogoSort implements SortingAlgorithm {

    // Counter for the number of steps (shuffles and comparisons) performed during the sort.
    private long steps;
    // Stores the original dataset passed to the sort method.
    private List<Integer> dataSet;

    /**
     * Returns the name of the sorting algorithm.
     * @return The string "BogoSort".
     */
    @Override
    public String getName() {
        return "BogoSort";
    }

    /**
     * Returns the best-case time complexity of BogoSort.
     * O(1) occurs if the list is already sorted upon the first check.
     * @return The string "O(1)".
     */
    @Override
    public String getBestCase() {
        return "O(1)";
    }

    /**
     * Returns the average-case time complexity of BogoSort.
     * There is no upper bound on the number of shuffles required.
     * @return The string "O(∞)".
     */
    @Override
    public String getAverageCase() {
        return "O(∞)";
    }

    /**
     * Returns the worst-case time complexity of BogoSort.
     * There is no upper bound on the runtime.
     * @return The string "O(∞)".
     */
    @Override
    public String getWorstCase() {
        return "O(∞)";
    }

    /**
     * Returns the total number of steps (shuffles + comparisons) performed during the last sort operation.
     * @return The number of steps.
     */
    @Override
    public long getSteps() {
        return steps;
    }

    /**
     * Returns the original dataset that was provided to the sorting algorithm.
     * Ensures that a non-null list is returned, defaulting to the superclass method if necessary.
     * @return The original list of integers.
     */
    @Override
    public List<Integer> getData() {
        // Return the stored dataset, or call the default method if it's null.
        return dataSet != null ? dataSet : SortingAlgorithm.super.getData();
    }

    /**
     * Sorts a copy of the input list using BogoSort.
     * The original list remains unchanged.
     * @param input The list of integers to sort.
     * @return A new list containing the sorted elements.
     */
    @Override
    public List<Integer> sort(List<Integer> input) {
        // Create a mutable copy to sort.
        List<Integer> copy = new ArrayList<>(input);
        // Store the original data.
        dataSet = new ArrayList<>(copy);
        // Reset step count for this sort operation.
        steps = 0;
        // Perform the sort using the callback version with a no-op consumer.
        bogoSort(copy, (SortStep step) -> {});
        // Return the sorted copy.
        return copy;
    }

    /**
     * Sorts the input list in-place using BogoSort and reports each step (shuffle or check).
     * @param input The list to be sorted (will be modified).
     * @param stepCallback A consumer function to report each {@link SortStep} for visualization.
     */
    @Override
    public void sortWithCallback(List<Integer> input, Consumer<SortStep> stepCallback) {
        // Store the original data.
        dataSet = new ArrayList<>(input);
        // Reset step count.
        steps = 0;
        // Send the initial state of the array.
        stepCallback.accept(new SortStep(new ArrayList<>(input), Collections.emptySet(), Collections.emptySet()));
        // Perform the actual sorting.
        bogoSort(input, stepCallback);
    }

    /**
     * The main BogoSort logic. Repeatedly shuffles the list until it is sorted.
     * Reports each shuffle and each comparison check via the stepCallback.
     * @param arr The list to sort in-place.
     * @param stepCallback The consumer for reporting steps.
     */
    private void bogoSort(List<Integer> arr, Consumer<SortStep> stepCallback) {
        // Continue shuffling until the list is sorted.
        while (!isSorted(arr, stepCallback)) {
            // Shuffle the list and get the indices that were potentially changed.
            Set<Integer> changedIndices = shuffle(arr);
            // Count the shuffle operation as one step.
            steps++;
            // Report the state of the array after shuffling.
            stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), changedIndices));
        }
        // Report the final sorted state (also sent by isSorted when it returns true).
        stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), Collections.emptySet()));
    }

    /**
     * Shuffles the list in-place using the Fisher-Yates (inside-out) algorithm variant.
     * Iterates through the list, swapping each element with a randomly chosen element
     * from the beginning of the list up to the current element's index.
     * @param arr The list to be shuffled.
     * @return A set containing the indices of elements that were potentially modified during the shuffle.
     */
    private Set<Integer> shuffle(List<Integer> arr) {
        Set<Integer> changed = new HashSet<>();
        // Iterate through the list.
        for (int i = 0; i < arr.size(); i++) {
            // Choose a random index from 0 to i (inclusive).
            int index1 = i;
            int index2 = (int) (Math.random() * arr.size()); // Simple random swap approach used here

            // Swap elements only if indices are different to avoid redundant steps.
            if (index1 != index2) {
                // Perform the swap.
                int temp = arr.get(index1); // Read access
                arr.set(index1, arr.get(index2)); // Write access + Read access
                arr.set(index2, temp); // Write access

                // Record the indices that were involved in the swap.
                changed.add(index1);
                changed.add(index2);
            }
        }
        // Return the set of indices that might have changed.
        return changed;
    }


    /**
     * Checks if the given list is sorted in ascending order.
     * Reports each comparison made during the check via the stepCallback.
     * @param arr The list to check.
     * @param stepCallback The consumer for reporting comparison steps.
     * @return true if the list is sorted, false otherwise.
     */
    private boolean isSorted(List<Integer> arr, Consumer<SortStep> stepCallback) {
        // Iterate through the list, comparing adjacent elements.
        for (int i = 1; i < arr.size(); i++) {
            Set<Integer> accessed = new HashSet<>();
            // Record the indices being compared.
            accessed.add(i - 1);
            accessed.add(i);
            // Count the comparison as one step.
            steps++;
            // Report the state *before* potentially returning false.
            stepCallback.accept(new SortStep(new ArrayList<>(arr), accessed, Collections.emptySet()));
            // If an element is smaller than its predecessor, the list is not sorted.
            if (arr.get(i - 1) > arr.get(i)) {
                return false;
            }
        }
        // If the loop completes without finding unsorted elements, the list is sorted.
        // Report the final state, confirming it's sorted.
        stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), Collections.emptySet()));
        return true;
    }
}