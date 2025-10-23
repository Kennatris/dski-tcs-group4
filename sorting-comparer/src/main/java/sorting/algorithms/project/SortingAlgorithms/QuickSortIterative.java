package sorting.algorithms.project.SortingAlgorithms;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack; // Import Stack
import java.util.function.Consumer;
// Unused imports - can be removed
// import java.util.stream.Collectors;
// import java.util.stream.IntStream;
import sorting.algorithms.project.dto.SortStep;

/**
 * Implements the QuickSort algorithm using an iterative approach with an explicit stack.
 * This avoids deep recursion calls that can lead to stack overflow errors for large datasets.
 * The partitioning logic remains the same as the recursive version.
 */
@Component
public class QuickSortIterative implements SortingAlgorithm {
	// Counter for steps (comparisons and swaps).
	private long steps;
	// Stores the original dataset.
	private List<Integer> dataSet;

	/**
	 * Returns the name of the sorting algorithm.
	 * @return The string "QuickSortIterative".
	 */
	@Override
	public String getName() {
		return "QuickSortIterative";
	}

	/**
	 * Returns the worst-case time complexity. O(n²) occurs with poor pivot choices.
	 * @return The string "O(n²)".
	 */
	@Override
	public String getWorstCase() {
		return "O(n²)";
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
	 * Returns the best-case time complexity. O(n log n) occurs with balanced partitions.
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
	 * Sorts a copy of the input list using iterative QuickSort.
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
		quickSort(copy, 0, copy.size() - 1, (SortStep step) -> {});
		// Return sorted copy.
		return copy;
	}

	/**
	 * Sorts the input list in-place using iterative QuickSort and reports steps during partitioning.
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
		// Start the iterative sorting process.
		quickSort(input, 0, input.size() - 1, stepCallback);
		// Send final sorted state.
		stepCallback.accept(new SortStep(new ArrayList<>(input), Collections.emptySet(), Collections.emptySet()));
	}

	/**
	 * Performs QuickSort iteratively using an explicit stack to manage sub-array ranges.
	 * Reports steps (comparisons, swaps) during the partitioning phase.
	 * @param arr The list to sort in-place.
	 * @param low The starting index of the initial range.
	 * @param high The ending index of the initial range.
	 * @param stepCallback The consumer for reporting steps.
	 */
	private void quickSort(List<Integer> arr, int low, int high, Consumer<SortStep> stepCallback) {
		// Handle trivial cases: null array, empty or single element, or invalid initial range.
		if (arr == null || arr.size() <= 1 || low >= high) {
			stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), Collections.emptySet())); // Send final state
			return;
		}


		// Create a stack to store the start and end indices of sub-arrays that need sorting.
		Stack<Integer> stack = new Stack<>();

		// Push the initial range (low, high) onto the stack.
		// Convention: Push 'high' first, then 'low', so 'low' is popped first.
		stack.push(low);
		stack.push(high);

		// Main loop: Continue as long as there are sub-arrays on the stack to process.
		while (!stack.isEmpty()) {
			// Pop the end index 'high' and start index 'low' for the current sub-array.
			high = stack.pop();
			low = stack.pop();

			// Optional: Report the current sub-array range being processed.
			// Set<Integer> accessedRange = IntStream.rangeClosed(low, high).filter(i -> i >= 0 && i < arr.size()).boxed().collect(Collectors.toSet());
			// stepCallback.accept(new SortStep(new ArrayList<>(arr), accessedRange, Collections.emptySet()));


			// Skip if the range is invalid or already sorted (low >= high).
			if (low >= high) continue;

			// Partition the current sub-array arr[low..high].
			// 'pi' is the final index of the pivot element.
			int pi = partition(arr, low, high, stepCallback);

			// Push the left sub-array range (low to pi-1) onto the stack if it has elements.
			if (pi - 1 > low) {
				stack.push(low);
				stack.push(pi - 1);
			}

			// Push the right sub-array range (pi+1 to high) onto the stack if it has elements.
			if (pi + 1 < high) {
				stack.push(pi + 1);
				stack.push(high);
			}
		}
		// Final state is sent by sortWithCallback after this method returns.
	}

	/**
	 * Partitions the sub-array arr[low..high] using the Lomuto partition scheme.
	 * Selects the last element (arr[high]) as the pivot. Places the pivot element
	 * at its correct sorted position and moves smaller elements to its left and
	 * larger elements to its right. Reports comparisons and swaps.
	 * (Identical to the one in the recursive QuickSort implementation).
	 * @param arr The list containing the sub-array to partition.
	 * @param low The starting index of the sub-array.
	 * @param high The ending index of the sub-array (inclusive, pivot index).
	 * @param stepCallback The consumer for reporting steps.
	 * @return The final index of the pivot element after partitioning.
	 */
	private int partition(List<Integer> arr, int low, int high, Consumer<SortStep> stepCallback) {
		// This method implementation is identical to the partition method in QuickSort.java.
		// See QuickSort.java for detailed comments on the partitioning logic.

		// Ensure indices are valid.
		if (low < 0 || high >= arr.size() || low > high) return low;

		Set<Integer> accessed = new HashSet<>();
		Set<Integer> changed = new HashSet<>();

		// Choose pivot (last element)
		accessed.add(high);
		int pivotValue = arr.get(high);
		stepCallback.accept(new SortStep(new ArrayList<>(arr), new HashSet<>(accessed), new HashSet<>(changed)));

		int i = low - 1; // Index of smaller element boundary

		// Iterate through elements before the pivot
		for (int j = low; j < high; j++) {
			accessed.clear();
			changed.clear();
			accessed.add(j);      // Current element being compared
			accessed.add(high);   // Pivot index for comparison value
			steps++;              // Count comparison

			stepCallback.accept(new SortStep(new ArrayList<>(arr), new HashSet<>(accessed), new HashSet<>(changed))); // Report before potential swap

			// If current element is smaller than pivot
			if (arr.get(j) < pivotValue) {
				i++; // Increment boundary index
				accessed.add(i); // Mark 'i' for swap access
				// Swap arr[i] and arr[j]
				int temp = arr.get(i);
				arr.set(i, arr.get(j));
				arr.set(j, temp);
				changed.add(i); // Mark swapped indices
				changed.add(j);
				steps++; // Optionally count swap
				stepCallback.accept(new SortStep(new ArrayList<>(arr), new HashSet<>(accessed), new HashSet<>(changed))); // Report after swap
			}
		}

		// Place pivot at its final position
		accessed.clear();
		changed.clear();
		int pivotFinalIndex = i + 1;

		// Swap pivot (at high) with element at pivotFinalIndex
		if (pivotFinalIndex < arr.size() && high < arr.size()) {
			accessed.add(pivotFinalIndex);
			accessed.add(high);
			int temp = arr.get(pivotFinalIndex);
			arr.set(pivotFinalIndex, arr.get(high));
			arr.set(high, temp);
			changed.add(pivotFinalIndex);
			changed.add(high);
			steps++; // Count swap
			stepCallback.accept(new SortStep(new ArrayList<>(arr), accessed, changed)); // Report after final swap
		} else {
			// Report even if no swap (e.g., pivot already largest or indices invalid)
			Set<Integer> finalAccessed = new HashSet<>();
			if(pivotFinalIndex < arr.size()) finalAccessed.add(pivotFinalIndex);
			if(high < arr.size()) finalAccessed.add(high);
			stepCallback.accept(new SortStep(new ArrayList<>(arr), finalAccessed, changed)); // changed is empty
		}

		return pivotFinalIndex; // Return pivot's final index
	}
}