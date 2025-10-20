package sorting.algorithms.project.SortingAlgorithms;// code based on code by diptangsu from https://github.com/diptangsu/Sorting-Algorithms/tree/master

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class IntroSort {

	/**
	 * Performs IntroSort on the input array and returns all intermediate array states.
	 *
	 * @param input The array to be sorted
	 * @return A list of array snapshots after each swap
	 */
	public static List<int[]> introSort(int[] input) {
		List<int[]> steps = new ArrayList<>();
		int[] arr = Arrays.copyOf(input, input.length);
		int depth = calcDepth(arr);

		introSortRecursive(arr, 0, arr.length - 1, depth, steps);

		return steps;
	}

	private static void introSortRecursive(int[] arr, int low, int high, int depth, List<int[]> steps) {
		if ((high - low) < 2) return;

		int p = partition(arr, low, high, steps);

		if (depth <= 0) {
			// Call HeapSort; assuming HeapSort class already rewritten with step tracking
			List<int[]> heapSteps = HeapSort.heapSort(Arrays.copyOfRange(arr, low, high + 1));
			// Integrate steps into main array
			for (int[] step : heapSteps) {
				System.arraycopy(step, 0, arr, low, step.length);
				// Visualizer.update(arr);
				steps.add(Arrays.copyOf(arr, arr.length));
			}
		} else {
			introSortRecursive(arr, low, p - 1, depth - 1, steps);
			introSortRecursive(arr, p + 1, high, depth - 1, steps);
		}
	}

	private static int partition(int[] arr, int low, int high, List<int[]> steps) {
		int pivot = getPivot(arr, low, high);
		int j = low;

		for (int i = low; i <= high; i++) {
			if (arr[i] < pivot) {
				swap(arr, i, j);
				// Visualizer.update(arr);
				steps.add(Arrays.copyOf(arr, arr.length));
				j++;
			}
		}
		swap(arr, high, j);
		// Visualizer.update(arr);
		steps.add(Arrays.copyOf(arr, arr.length));

		return j;
	}

	private static int getPivot(int[] arr, int low, int high) {
		Random rd = new Random();
		return arr[rd.nextInt((high - low) + 1) + low];
	}

	private static void swap(int[] arr, int x, int y) {
		int aux = arr[x];
		arr[x] = arr[y];
		arr[y] = aux;
	}

	private static int calcDepth(int[] arr) {
		return (int) (2 * Math.log(arr.length));
	}
}
