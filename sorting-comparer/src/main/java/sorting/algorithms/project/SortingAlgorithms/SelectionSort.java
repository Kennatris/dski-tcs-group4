package sorting.algorithms.project.SortingAlgorithms;// code based on code by diptangsu from https://github.com/diptangsu/Sorting-Algorithms/tree/master

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SelectionSort {

    /**
     * Performs selection sort on the input array and returns all intermediate steps.
     *
     * @param input The array to sort
     * @return A list of array snapshots after each swap
     */
    public static List<int[]> selectionSort(int[] input) {
        List<int[]> steps = new ArrayList<>();
        int[] arr = Arrays.copyOf(input, input.length);
        int len = arr.length;

        for (int i = 0; i < len; i++) {
            int pos = i;
            for (int j = i + 1; j < len; j++) {
                if (arr[j] < arr[pos])
                    pos = j;
            }

            if (pos != i) {
                int tmp = arr[i];
                arr[i] = arr[pos];
                arr[pos] = tmp;

                // Visualizer.update(arr);
                steps.add(Arrays.copyOf(arr, arr.length));
            }
        }

        return steps;
    }
}
