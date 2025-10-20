package sorting.algorithms.project.SortingAlgorithms;// code based on code by diptangsu from https://github.com/diptangsu/Sorting-Algorithms/tree/master

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecursiveBubbleSort {

    /**
     * Performs recursive bubble sort on the input array and returns all intermediate steps.
     *
     * @param input The array to sort
     * @return A list of array snapshots after each recursive step
     */
    public static List<int[]> bubbleSort(int[] input) {
        List<int[]> steps = new ArrayList<>();
        int[] arr = Arrays.copyOf(input, input.length);
        bubbleSortRecursive(arr, arr.length, steps);
        return steps;
    }

    private static void bubbleSortRecursive(int[] arr, int n, List<int[]> steps) {
        if (n == 1)
            return;

        for (int i = 0; i < n - 1; i++) {
            if (arr[i] > arr[i + 1]) {
                int temp = arr[i];
                arr[i] = arr[i + 1];
                arr[i + 1] = temp;

                // Visualizer.update(arr);
                steps.add(Arrays.copyOf(arr, arr.length));
            }
        }

        bubbleSortRecursive(arr, n - 1, steps);
    }
}
