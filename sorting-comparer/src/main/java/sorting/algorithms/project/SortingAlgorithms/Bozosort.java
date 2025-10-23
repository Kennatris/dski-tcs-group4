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
 * Implements the BozoSort algorithm.
 * This highly inefficient algorithm works by randomly swapping two elements
 * and checking if the list is sorted after each swap.
 * It's primarily used for educational or humorous purposes.
 */
@Component
public class Bozosort implements SortingAlgorithm {

	// Counter for the number of steps (swaps and comparisons) performed.
	private long steps;
	// Stores the original dataset passed to the sort method.
	private List<Integer> dataSet;

	/**
	 * Returns the name of the sorting algorithm.
	 * @return The string "BozoSort".
	 */
	@Override
	public String getName() {
		return "BozoSort";
	}

	/**
	 * Returns the best-case time complexity of BozoSort.
	 * O(1) occurs if the list is already sorted upon the first check.
	 * @return The string "O(1)".
	 */
	@Override
	public String getBestCase() {
		return "O(1)";
	}

	/**
	 * Returns the average-case time complexity of BozoSort.
	 * There is no upper bound on the number of swaps required.
	 * @return The string "O(∞)".
	 */
	@Override
	public String getAverageCase() {
		return "O(∞)";
	}

	/**
	 * Returns the worst-case time complexity of BozoSort.
	 * There is no upper bound on the runtime.
	 * @return The string "O(∞)".
	 */
	@Override
	public String getWorstCase() {
		return "O(∞)";
	}

	/**
	 * Returns the total number of steps (swaps + comparisons) performed during the last sort operation.
	 * @return The number of steps.
	 */
	@Override
	public long getSteps() {
		return steps;
	}

	/**
	 * Returns the original dataset that was provided to the sorting algorithm.
	 * Ensures that a non-null list is returned.
	 * @return The original list of integers.
	 */
	@Override
	public List<Integer> getData() {
		// Return the stored dataset, or call the default method if it's null.
		return dataSet != null ? dataSet : SortingAlgorithm.super.getData();
	}

	/**
	 * Sorts a copy of the input list using BozoSort.
	 * The original list remains unchanged.
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
		bozoSort(copy, (SortStep step) -> {});
		// Return the sorted copy.
		return copy;
	}

	/**
	 * Sorts the input list in-place using BozoSort and reports each step (swap or check).
	 * @param input The list to be sorted (will be modified).
	 * @param stepCallback A consumer function to report each {@link SortStep} for visualization.
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
		bozoSort(input, stepCallback);
	}

	/**
	 * The main BozoSort logic. Repeatedly swaps two random elements and checks if the list is sorted.
	 * Reports each swap and each comparison check via the stepCallback.
	 * @param arr The list to sort in-place.
	 * @param stepCallback The consumer for reporting steps.
	 */
	private void bozoSort(List<Integer> arr, Consumer<SortStep> stepCallback) {
		boolean sorted = false;

		// Continue until the list is sorted.
		while (!sorted) {
			Set<Integer> accessedSwap = new HashSet<>();
			Set<Integer> changedSwap = new HashSet<>();

			// Select two random indices.
			int index1 = randomIndex(arr.size());
			int index2 = randomIndex(arr.size());

			// Perform swap only if indices are different.
			if (index1 != index2) {
				accessedSwap.add(index1); // Mark for get
				accessedSwap.add(index2); // Mark for get
				// Perform the swap.
				int temp = arr.get(index1);
				arr.set(index1, arr.get(index2));
				arr.set(index2, temp);
				changedSwap.add(index1); // Mark for set
				changedSwap.add(index2); // Mark for set
				// Count the swap as a step.
				steps++;
				// Report the state after the swap.
				stepCallback.accept(new SortStep(new ArrayList<>(arr), accessedSwap, changedSwap));
			} else {
				// Report the state even if no swap occurred, showing the considered indices.
				accessedSwap.add(index1); // Show which index was potentially accessed
				stepCallback.accept(new SortStep(new ArrayList<>(arr), accessedSwap, changedSwap));
			}


			// Check if the list is now sorted.
			sorted = true;
			for (int i = 1; i < arr.size(); i++) {
				Set<Integer> accessedCheck = new HashSet<>();
				accessedCheck.add(i - 1);
				accessedCheck.add(i);
				// Count the comparison as a step.
				steps++;
				// Report the state *before* potentially breaking the check loop.
				stepCallback.accept(new SortStep(new ArrayList<>(arr), accessedCheck, Collections.emptySet()));
				// If elements are out of order, it's not sorted.
				if (arr.get(i - 1) > arr.get(i)) {
					sorted = false;
					break; // Stop checking for this iteration.
				}
			}
			// If the loop finished and 'sorted' remained true, report the final state.
			if (sorted) {
				stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), Collections.emptySet()));
			}
		}
	}

	/**
	 * Generates a random index within the valid range [0, range-1].
	 * @param range The size of the list (exclusive upper bound).
	 * @return A random integer index.
	 */
	private int randomIndex(int range) {
		// Handle empty or single-element list case.
		if (range <= 0) return 0;
		// Generate a random double [0.0, 1.0), scale it, and cast to int.
		return (int) (Math.random() * range);
	}
}