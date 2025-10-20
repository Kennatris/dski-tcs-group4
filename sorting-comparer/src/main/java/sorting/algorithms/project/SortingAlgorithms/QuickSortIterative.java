package sorting.algorithms.project.SortingAlgorithms;// code based on code by diptangsu from https://github.com/diptangsu/Sorting-Algorithms/tree/master

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuickSortIterative {

	/**
	 * Performs Quick Sort (recursive) on the input array and returns all intermediate array states.
	 *
	 * @param input The array to be sorted
	 * @return A list of array snapshots after each swap
	 */
	public static List<int[]> quickSort(int[] input) {
		List<int[]> steps = new ArrayList<>();
		int[] arr = Arrays.copyOf(input, input.length);

		quickSortRecursive(arr, 0, arr.length - 1, steps);

		return steps;
	}

	private static void quickSortRecursive(int[] arr, int low, int high, List<int[]> steps) {
		if (low < high) {
			int pi = partition(arr, low, high, steps);
			quickSortRecursive(arr, low, pi - 1, steps);
			quickSortRecursive(arr, pi + 1, high, steps);
		}
	}

	private static int partition(int[] arr, int low, int high, List<int[]> steps) {
		int pivot = arr[high];
		int i = low - 1;

		for (int j = low; j <= high - 1; j++) {
			if (arr[j] <= pivot) {
				i++;
				swap(arr, i, j, steps);
			}
		}
		swap(arr, i + 1, high, steps);
		return i + 1;
	}

	private static void swap(int[] arr, int i, int j, List<int[]> steps) {
		int temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;

		// Visualizer.update(arr);
		steps.add(Arrays.copyOf(arr, arr.length));
	}
}
