package sorting.algorithms.project.SortingAlgorithms;// code based on code by diptangsu from https://github.com/diptangsu/Sorting-Algorithms/tree/master

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BitonicSort {

	/**
	 * Performs Bitonic Sort and returns all intermediate array states.
	 * @param input      The array to be sorted
	 * @return           A list of array snapshots after each modification step
	 */
	public static List<int[]> bitonicSort(int[] input) {
		List<int[]> steps = new ArrayList<>();
		int[] array = Arrays.copyOf(input, input.length);
		bitonicSortRecursive(array, 0, array.length, 1, steps);
		return steps;
	}

	/**
	 * Recursive Bitonic Sort function.
	 * @param arr        The array/sub-array to be sorted
	 * @param start      Starting index to consider for sorting
	 * @param length     Length of array segment to sort
	 * @param direction  Sorting direction: 1 for ascending, 0 for descending
	 * @param steps      List to store intermediate states
	 */
	private static void bitonicSortRecursive(int[] arr, int start, int length, int direction, List<int[]> steps) {
		if (length > 1) {
			int mid = length / 2;

			bitonicSortRecursive(arr, start, mid, 1, steps);
			bitonicSortRecursive(arr, start + mid, mid, 0, steps);
			bitonicMerge(arr, start, length, direction, steps);
		}
	}

	/**
	 * Merges two halves of the array in bitonic order.
	 * @param arr        The array/sub-array to be merged
	 * @param start      Starting index to consider for merging
	 * @param length     Length of array segment to merge
	 * @param direction  Sorting direction: 1 for ascending, 0 for descending
	 * @param steps      List to store intermediate states
	 */
	private static void bitonicMerge(int[] arr, int start, int length, int direction, List<int[]> steps) {
		if (length > 1) {
			int mid = length / 2;

			for (int i = start; i < start + mid; i++) {
				if ((arr[i] > arr[i + mid] && direction == 1) ||
						(arr[i] < arr[i + mid] && direction == 0)) {

					int temp = arr[i];
					arr[i] = arr[i + mid];
					arr[i + mid] = temp;

					// Visualizer.update(arr);
					steps.add(Arrays.copyOf(arr, arr.length));
				}
			}

			bitonicMerge(arr, start, mid, direction, steps);
			bitonicMerge(arr, start + mid, mid, direction, steps);
		}
	}
}
