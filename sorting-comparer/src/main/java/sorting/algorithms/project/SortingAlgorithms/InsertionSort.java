package sorting.algorithms.project.SortingAlgorithms;// code based on code by diptangsu from https://github.com/diptangsu/Sorting-Algorithms/tree/master

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InsertionSort {

    /**
     * Performs Insertion Sort on the input array and returns all intermediate array states.
     *
     * @param input The array to be sorted
     * @return A list of array snapshots after each insertion
     */
    public static List<int[]> insertionSort(int[] input) {
        List<int[]> steps = new ArrayList<>();
        int[] arr = Arrays.copyOf(input, input.length);
        int len = arr.length;

        for (int i = 1; i < len; i++) {
            int tmp = arr[i];
            int j = i;

            while (j > 0 && arr[j - 1] > tmp) {
                arr[j] = arr[j - 1];
                j++;

                // Visualizer.update(arr);
                steps.add(Arrays.copyOf(arr, arr.length));
                j--; // adjust back after adding snapshot
                j--;
            }

            arr[j] = tmp;

            // Visualizer.update(arr);
            steps.add(Arrays.copyOf(arr, arr.length));
        }

        return steps;
    }
}
