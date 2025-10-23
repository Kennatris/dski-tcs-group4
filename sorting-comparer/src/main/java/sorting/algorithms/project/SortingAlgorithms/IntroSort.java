package sorting.algorithms.project.SortingAlgorithms;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Consumer;
import sorting.algorithms.project.dto.SortStep;

/**
 * Implements the IntroSort (Introspective Sort) algorithm.
 * It's a hybrid sorting algorithm that starts with QuickSort but switches to HeapSort
 * when the recursion depth exceeds a certain limit based on the logarithm of the number of elements.
 * This approach combines the speed of QuickSort on average with the O(n log n) worst-case guarantee of HeapSort.
 * Small partitions might be sorted using Insertion Sort (though not implemented here for simplicity).
 */
@Component
public class IntroSort implements SortingAlgorithm {
	// Counter for steps (comparisons, swaps).
	private long steps;
	// Stores the original dataset.
	private List<Integer> dataSet;

	/**
	 * Returns the name of the sorting algorithm.
	 * @return The string "IntroSort".
	 */
	@Override
	public String getName() {
		return "IntroSort";
	}

	/**
	 * Returns the worst-case time complexity. O(n log n) due to the HeapSort fallback.
	 * @return The string "O(n log n)".
	 */
	@Override
	public String getWorstCase() {
		return "O(n log n)";
	}

	/**
	 * Returns the average-case time complexity. O(n log n), typically faster than HeapSort due to QuickSort.
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
	 * Sorts a copy of the input list using IntroSort.
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
		// Calculate the maximum recursion depth allowed for QuickSort.
		int depth = calcDepth(copy);
		// Start the recursive sort with a no-op callback.
		introSortRecursive(copy, 0, copy.size() - 1, depth, (SortStep step) -> {});
		// Return the sorted copy.
		return copy;
	}

	/**
	 * Sorts the input list in-place using IntroSort and reports steps via callback.
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
		// Calculate the maximum recursion depth.
		int depth = calcDepth(input);
		// Start the recursive sorting process.
		introSortRecursive(input, 0, input.size() - 1, depth, stepCallback);
		// Send final state (might be redundant if last step already covered it).
		// Consider sending only if the array wasn't empty/trivial.
		if (!input.isEmpty()) {
			stepCallback.accept(new SortStep(new ArrayList<>(input), Collections.emptySet(), Collections.emptySet()));
		}
	}

	/**
	 * The recursive core of the IntroSort algorithm.
	 * It partitions the array using QuickSort's partition scheme. If the recursion depth limit
	 * is reached, it switches to HeapSort for the current sub-array. Otherwise, it continues
	 * recursively with QuickSort on the left and right partitions, decrementing the depth limit.
	 *
	 * @param arr The list (or sublist) to sort.
	 * @param low The starting index of the current sub-array.
	 * @param high The ending index of the current sub-array.
	 * @param depth The remaining allowed recursion depth before switching to HeapSort.
	 * @param stepCallback The consumer for reporting steps.
	 */
	private void introSortRecursive(List<Integer> arr, int low, int high, int depth, Consumer<SortStep> stepCallback) {
		// Calculate the size of the current partition.
		int size = high - low + 1;

		// Base case: If the partition size is 1 or less (or indices are invalid), it's sorted.
		// For larger arrays, partitions smaller than a threshold (e.g., 16) could switch to Insertion Sort,
		// but here we simply return for sizes <= 1.
		if (size <= 1) {
			// Optionally report accessing this small partition.
			// Set<Integer> accessed = IntStream.rangeClosed(low, high).filter(i -> i >= 0 && i < arr.size()).boxed().collect(Collectors.toSet());
			// stepCallback.accept(new SortStep(new ArrayList<>(arr), accessed, Collections.emptySet()));
			return;
		}

		// If recursion depth limit is reached, switch to HeapSort for this partition.
		if (depth <= 0) {
			heapSortSublist(arr, low, high, stepCallback);
			return;
		}

		// Perform QuickSort partitioning on the current sub-array [low..high].
		// The partition method returns the final index of the pivot element.
		int p = partition(arr, low, high, stepCallback);

		// Recursively call IntroSort for the left partition (elements before the pivot).
		// Decrement the depth limit.
		introSortRecursive(arr, low, p - 1, depth - 1, stepCallback);
		// Recursively call IntroSort for the right partition (elements after the pivot).
		// Decrement the depth limit.
		introSortRecursive(arr, p + 1, high, depth - 1, stepCallback);
	}

	/**
	 * Sorts a specific sublist (from low to high index) of the main list 'arr' using HeapSort.
	 * This is used as the fallback when IntroSort's recursion depth limit is exceeded.
	 * It creates a temporary sublist, sorts it using a separate HeapSort instance,
	 * and merges the steps and results back into the main list and step count.
	 *
	 * @param arr The main list containing the sublist to be sorted.
	 * @param low The starting index of the sublist (inclusive).
	 * @param high The ending index of the sublist (inclusive).
	 * @param stepCallback The main callback consumer to report steps on the *entire* array 'arr'.
	 */
	private void heapSortSublist(List<Integer> arr, int low, int high, Consumer<SortStep> stepCallback) {
		// Ensure valid range before creating sublist view.
		if (low < 0 || high >= arr.size() || low > high) return;

		// Create a mutable copy of the sublist to be sorted by HeapSort.
		// Using subList directly can cause issues if the underlying list structure changes.
		List<Integer> sublist = new ArrayList<>(arr.subList(low, high + 1));

		// Create a dedicated HeapSort instance to manage its internal state (like step count) separately.
		HeapSort localHeapSort = new HeapSort();

		// Intermediate variable to track steps from the localHeapSort before processing a step.
		// Necessary because localHeapSort.getSteps() changes during sublistCallback execution.
		final long[] stepsBeforeSublistSort = {localHeapSort.getSteps()};


		// Define a callback wrapper. This intercepts steps reported by the localHeapSort
		// operating on the 'sublist' and translates them into steps on the main 'arr'.
		Consumer<SortStep> sublistCallback = (SortStep step) -> {
			// Get the state of the sublist after a step.
			List<Integer> stepArray = step.getCurrentArray();
			Set<Integer> stepAccessed = new HashSet<>();
			Set<Integer> stepChanged = new HashSet<>();

			// Apply changes from the sorted sublist back to the corresponding section of the main list 'arr'.
			for (int i = 0; i < stepArray.size(); i++) {
				int mainIndex = low + i;
				// Only update if the value actually differs to avoid unnecessary writes.
				// Check bounds just in case.
				if (mainIndex < arr.size() && !arr.get(mainIndex).equals(stepArray.get(i))) {
					arr.set(mainIndex, stepArray.get(i));
				}
			}

			// Translate accessed and changed indices from sublist coordinates (0 to sublist.size()-1)
			// to main list coordinates (low to high).
			step.getAccessedIndices().forEach(idx -> stepAccessed.add(low + idx));
			step.getChangedIndices().forEach(idx -> stepChanged.add(low + idx));

			// Calculate the *new* steps performed by localHeapSort *during this single step*.
			long currentLocalSteps = localHeapSort.getSteps();
			long deltaSteps = currentLocalSteps - stepsBeforeSublistSort[0];
			// Add these new steps to the main IntroSort step counter.
			this.steps += deltaSteps;
			// Update the baseline for the next step.
			stepsBeforeSublistSort[0] = currentLocalSteps;


			// Report the updated state of the *entire* main array 'arr' to the original callback.
			stepCallback.accept(new SortStep(new ArrayList<>(arr), stepAccessed, stepChanged));
		};

		// Execute HeapSort on the sublist, using the wrapper callback.
		localHeapSort.sortWithCallback(sublist, sublistCallback);

		// --- Post-Sort Synchronization ---
		// Although the callback should update 'arr' step-by-step, this loop ensures
		// the final sorted state of the sublist is correctly reflected in the main array,
		// especially if the last step from localHeapSort didn't perfectly align.
		for (int i = 0; i < sublist.size(); i++) {
			int mainIndex = low + i;
			if (mainIndex < arr.size() && !arr.get(mainIndex).equals(sublist.get(i))) {
				arr.set(mainIndex, sublist.get(i));
			}
		}

		// Add any remaining steps from localHeapSort accumulated after the last callback step.
		// This ensures the total step count is accurate.
		// NOTE: This might double-count if the very last operation within localHeapSort triggered the callback.
		// A potentially cleaner way is to sum localHeapSort.getSteps() *once* after it finishes,
		// but the current approach tries to attribute steps more granularly.
		// Let's refine: The callback already added delta steps. We just need the final count.
		// The total steps for this HeapSort sub-operation are simply localHeapSort.getSteps().
		// However, the callback already incrementally added these. If we add again here, we double count.
		// Let's remove the final addition, assuming the callback handled all steps.
		// this.steps += localHeapSort.getSteps(); // Removed to avoid double counting.

		// Report the final state of the sorted sub-range within the main array.
		Set<Integer> finalChanged = new HashSet<>();
		for(int i=low; i<=high && i<arr.size(); i++) finalChanged.add(i); // Mark the entire range as potentially changed.
		stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), finalChanged));
	}


	/**
	 * Partitions the sub-array arr[low..high] using the Lomuto partition scheme.
	 * It selects the last element as the pivot, places the pivot element at its correct
	 * position in the sorted array, and places all smaller elements to the left
	 * and all greater elements to the right. Reports comparisons and swaps.
	 *
	 * @param arr The list containing the sub-array to partition.
	 * @param low The starting index of the sub-array.
	 * @param high The ending index of the sub-array (inclusive, pivot index).
	 * @param stepCallback The consumer for reporting steps.
	 * @return The final index of the pivot element after partitioning.
	 */
	private int partition(List<Integer> arr, int low, int high, Consumer<SortStep> stepCallback) {
		// Ensure indices are valid.
		if (low < 0 || high >= arr.size() || low > high) {
			// Handle invalid range - potentially return 'low' or throw exception.
			// Returning low might lead to infinite loops if not handled carefully in caller.
			// For simplicity here, assume valid ranges are passed. If issues arise, add error handling.
			return low; // Or consider a safer return/exception.
		}

		Set<Integer> accessed = new HashSet<>();
		Set<Integer> changed = new HashSet<>();

		// Choose the last element as the pivot.
		accessed.add(high); // Mark pivot index as accessed.
		int pivotValue = arr.get(high);
		// Report the state showing the pivot being read.
		stepCallback.accept(new SortStep(new ArrayList<>(arr), new HashSet<>(accessed), new HashSet<>(changed)));


		// Index of the smaller element. Tracks the boundary between elements smaller than the pivot
		// and elements not yet processed or larger than the pivot.
		int i = low - 1;

		// Iterate through the sub-array from low up to (but not including) the pivot index 'high'.
		for (int j = low; j < high; j++) {
			accessed.clear(); // Reset for this comparison step.
			changed.clear();
			accessed.add(j); // Element 'j' is being compared.
			accessed.add(high); // Pivot value (at index high) is implicitly used in comparison.
			steps++; // Count the comparison arr[j] vs pivotValue.

			// Report the state just before the comparison result is acted upon.
			stepCallback.accept(new SortStep(new ArrayList<>(arr), new HashSet<>(accessed), new HashSet<>(changed)));

			// If the current element arr[j] is smaller than the pivot...
			if (arr.get(j) < pivotValue) {
				i++; // Increment the index 'i' (boundary of smaller elements).
				// Swap arr[i] and arr[j] to move the smaller element arr[j] to the left partition.
				accessed.add(i); // Mark 'i' as accessed for the swap.
				// accessed.add(j); // 'j' is already in 'accessed'.
				int temp = arr.get(i);
				arr.set(i, arr.get(j));
				arr.set(j, temp);
				changed.add(i); // Mark indices 'i' and 'j' as changed due to the swap.
				changed.add(j);
				steps++; // Optionally count the swap.
				// Report the state after the swap.
				stepCallback.accept(new SortStep(new ArrayList<>(arr), new HashSet<>(accessed), new HashSet<>(changed)));
			}
		}

		// After the loop, all elements from low to 'i' are smaller than the pivot.
		// Place the pivot (arr[high]) at its correct final position (i + 1)
		// by swapping it with the element currently at arr[i + 1].
		accessed.clear();
		changed.clear();
		int pivotFinalIndex = i + 1;

		// Check bounds before performing the final pivot swap.
		if (pivotFinalIndex < arr.size() && high < arr.size()) {
			accessed.add(pivotFinalIndex); // Mark indices involved in the swap.
			accessed.add(high);
			int temp = arr.get(pivotFinalIndex);
			arr.set(pivotFinalIndex, arr.get(high));
			arr.set(high, temp);
			changed.add(pivotFinalIndex); // Mark indices as changed.
			changed.add(high);
			steps++; // Count the swap.
			// Report the state after the final pivot swap.
			stepCallback.accept(new SortStep(new ArrayList<>(arr), accessed, changed));
		} else {
			// Report the state even if no swap was needed (pivot already in correct place relative to processed part)
			// or if indices were somehow invalid (though should be caught earlier).
			// Send empty sets if no operation occurred, or potentially accessed indices if appropriate.
			Set<Integer> finalAccessed = new HashSet<>();
			if(pivotFinalIndex < arr.size()) finalAccessed.add(pivotFinalIndex);
			if(high < arr.size()) finalAccessed.add(high);
			stepCallback.accept(new SortStep(new ArrayList<>(arr), finalAccessed, Collections.emptySet()));
		}


		// Return the final index where the pivot element ended up.
		return pivotFinalIndex;
	}


	/**
	 * Selects a pivot element. This implementation uses a random pivot selection strategy.
	 * Note: This method is currently unused as the partition method uses the last element.
	 * It's kept here as an example of alternative pivot selection.
	 *
	 * @param arr The list.
	 * @param low The starting index of the sub-array.
	 * @param high The ending index of the sub-array.
	 * @return The value of the randomly selected pivot element.
	 */
	private int getPivot(List<Integer> arr, int low, int high) {
		// Handle invalid range.
		if (low > high || low < 0 || high >= arr.size()) {
			// Fallback: return the first element if possible, or a default value.
			return low < arr.size() && low >= 0 ? arr.get(low) : (arr.isEmpty() ? 0 : arr.get(0));
		}
		Random rd = new Random();
		// Generate a random index between low and high (inclusive).
		int pivotIndex = rd.nextInt((high - low) + 1) + low;
		// Optional: Report step showing pivot access.
		// stepCallback.accept(new SortStep(new ArrayList<>(arr), Set.of(pivotIndex), Collections.emptySet()));
		// Return the value at the random index.
		return arr.get(pivotIndex);
	}

	/**
	 * Calculates the maximum recursion depth allowed for the QuickSort part of IntroSort.
	 * A common value is 2 * floor(log2(n)), where n is the number of elements.
	 *
	 * @param arr The list for which to calculate the depth limit.
	 * @return The calculated maximum recursion depth.
	 */
	private int calcDepth(List<Integer> arr) {
		if (arr == null || arr.isEmpty()) return 0;
		// Calculate 2 * log base 2 of the list size.
		return (int) (2 * Math.floor(Math.log(arr.size()) / Math.log(2)));
	}
}