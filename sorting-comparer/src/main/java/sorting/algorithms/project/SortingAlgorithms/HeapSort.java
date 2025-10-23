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
 * Implements the Heap Sort algorithm.
 * It works by first building a max-heap from the input data, and then
 * repeatedly extracting the maximum element from the heap (the root) and
 * placing it at the end of the sorted portion of the array.
 */
@Component
public class HeapSort implements SortingAlgorithm {
    // Counter for steps (comparisons and swaps).
    private long steps;
    // Stores the original dataset.
    private List<Integer> dataSet;

    /**
     * Returns the name of the sorting algorithm.
     * @return The string "HeapSort".
     */
    @Override
    public String getName() {
        return "HeapSort";
    }

    /**
     * Returns the worst-case time complexity. O(n log n).
     * @return The string "O(n log n)".
     */
    @Override
    public String getWorstCase() {
        return "O(n log n)";
    }

    /**
     * Returns the average-case time complexity. O(n log n).
     * @return The string "O(n log n)".
     */
    @Override
    public String getAverageCase() {
        return "O(n log n)";
    }

    /**
     * Returns the best-case time complexity. O(n log n).
     * @return The string "O(n log n)".
     */
    @Override
    public String getBestCase() {
        return "O(n log n)";
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
     * Sorts a copy of the input list using Heap Sort.
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
        heapSort(copy, (SortStep step) -> {});
        // Return sorted copy.
        return copy;
    }

    /**
     * Sorts the input list in-place using Heap Sort and reports each comparison and swap.
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
        heapSort(input, stepCallback);
        // Note: Final state is sent at the end of heapSort method.
    }

    /**
     * Performs the Heap Sort logic in-place. First builds a max-heap, then repeatedly
     * extracts the maximum element and rebuilds the heap property.
     * Reports steps during heap adjustments and swaps.
     * @param arr The list to sort in-place.
     * @param stepCallback The consumer for reporting steps.
     */
    private void heapSort(List<Integer> arr, Consumer<SortStep> stepCallback) {
        int n = arr.size();
        // Handle lists with 0 or 1 element.
        if (n <= 1) {
            stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), Collections.emptySet())); // Send final state
            return;
        }


        // 1. Build a max-heap from the input array.
        // This involves calling heapAdjust on all non-leaf nodes.
        makeMaxHeap(arr, n, stepCallback);

        // 2. Repeatedly extract the maximum element (root) and place it at the end.
        // Iterate from the last element down to the second element.
        for (int i = n - 1; i > 0; i--) {
            Set<Integer> accessed = new HashSet<>();
            Set<Integer> changed = new HashSet<>();

            // Swap the root (maximum element) with the last element of the current heap (at index i).
            accessed.add(0); // Access root.
            accessed.add(i); // Access last element of current heap.
            int temp = arr.get(0);
            arr.set(0, arr.get(i));
            arr.set(i, temp);
            changed.add(0); // Mark indices as changed.
            changed.add(i);
            steps++; // Count the swap.

            // Report the state after swapping the max element to the end.
            stepCallback.accept(new SortStep(new ArrayList<>(arr), accessed, changed));

            // Restore the max-heap property on the reduced heap (size i).
            // Call heapAdjust on the root (index 0).
            heapAdjust(arr, 0, i, stepCallback);
        }
        // Send the final sorted state.
        stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), Collections.emptySet()));
    }

    /**
     * Builds a max-heap from the given list. It iterates backwards from the last
     * non-leaf node and applies the heapAdjust operation to each node.
     * @param arr The list to heapify.
     * @param n The size of the heap (typically arr.size()).
     * @param stepCallback The consumer for reporting steps within heapAdjust.
     */
    private void makeMaxHeap(List<Integer> arr, int n, Consumer<SortStep> stepCallback) {
        // Start from the last non-leaf node (parent of the last element).
        // The index is (n / 2) - 1.
        for (int i = n / 2 - 1; i >= 0; i--) {
            // Restore heap property for the subtree rooted at index i.
            heapAdjust(arr, i, n, stepCallback);
        }
    }

    /**
     * Restores the max-heap property for the subtree rooted at index 'i'.
     * Assumes that the subtrees rooted at the children of 'i' are already max-heaps.
     * This is also known as max-heapify.
     * @param arr The list representing the heap.
     * @param i The index of the root of the subtree to adjust.
     * @param n The size of the heap (used for boundary checks).
     * @param stepCallback The consumer for reporting steps (comparisons and swaps).
     */
    private void heapAdjust(List<Integer> arr, int i, int n, Consumer<SortStep> stepCallback) {
        Set<Integer> accessed = new HashSet<>();
        Set<Integer> changed = new HashSet<>();

        int largest = i; // Assume the root 'i' is the largest initially.
        int left = 2 * i + 1; // Index of the left child.
        int right = 2 * i + 2; // Index of the right child.

        accessed.add(i); // Root is always accessed.

        // Check if the left child exists and is larger than the current largest.
        if (left < n) {
            accessed.add(left); // Left child is accessed for comparison.
            steps++; // Count comparison.
            if (arr.get(left) > arr.get(largest)) {
                largest = left; // Update largest if left child is larger.
            }
        }

        // Check if the right child exists and is larger than the current largest.
        if (right < n) {
            accessed.add(right); // Right child is accessed for comparison.
            // Access to 'largest' index was already potentially added.
            steps++; // Count comparison.
            if (arr.get(right) > arr.get(largest)) {
                largest = right; // Update largest if right child is larger.
            }
        }

        // Report the state after comparisons, showing accessed nodes.
        stepCallback.accept(new SortStep(new ArrayList<>(arr), new HashSet<>(accessed), new HashSet<>(changed)));


        // If the largest element is not the root 'i', swap them.
        if (largest != i) {
            accessed.add(largest); // 'largest' index needed for get/set.
            // 'i' is already in 'accessed'.
            // Perform the swap.
            int swap = arr.get(i);
            arr.set(i, arr.get(largest));
            arr.set(largest, swap);
            changed.add(i); // Mark indices as changed.
            changed.add(largest);
            steps++; // Count the swap.

            // Report the state after the swap.
            stepCallback.accept(new SortStep(new ArrayList<>(arr), accessed, changed));

            // Recursively call heapAdjust on the affected subtree (rooted at the original 'largest' index)
            // to ensure the max-heap property is maintained downwards.
            heapAdjust(arr, largest, n, stepCallback);
        }
        // If no swap occurred, report the state to show the adjustment check finished.
        else if (changed.isEmpty()) { // Only send if no swap happened in this call
            stepCallback.accept(new SortStep(new ArrayList<>(arr), accessed, changed));
        }
    }
}