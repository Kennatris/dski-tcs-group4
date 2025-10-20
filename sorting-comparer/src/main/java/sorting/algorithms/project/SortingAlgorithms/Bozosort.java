package sorting.algorithms.project.SortingAlgorithms;// code based on code by diptangsu from https://github.com/diptangsu/Sorting-Algorithms/tree/master

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BozoSortAlgorithm {

	/**
	 * Performs Bozo Sort and returns all intermediate array states.
	 * @param input The array to be sorted
	 * @return A list of array snapshots after each modification step
	 */
	public static List<int[]> bozoSort(int[] input) {
		List<int[]> steps = new ArrayList<>();
		int[] array = Arrays.copyOf(input, input.length);
		boolean sorted = false;

		while (!sorted) {
			int index1 = randomIndex(array.length);
			int index2 = randomIndex(array.length);

			// Swap two random elements
			int temp = array[index2];
			array[index2] = array[index1];
			array[index1] = temp;

			// Visualizer.update(array);
			steps.add(Arrays.copyOf(array, array.length));

			// Check if sorted
			sorted = true;
			for (int i = 1; i < array.length; i++) {
				if (array[i - 1] > array[i]) {
					sorted = false;
					break;
				}
			}
		}
		return steps;
	}

	// Generate a random index within range
	private static int randomIndex(int range) {
		return (int) (Math.random() * range);
	}
}
