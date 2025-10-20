package sorting.algorithms.project.SortingAlgorithms;// code based on code by diptangsu from https://github.com/diptangsu/Sorting-Algorithms/tree/master

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PancakeSort {

    /**
     * Performs Pancake Sort on the input array and returns all intermediate array states.
     *
     * @param input The array to be sorted
     * @return A list of array snapshots after each flip
     */
    public static List<int[]> pancakeSort(int[] input) {
        List<int[]> steps = new ArrayList<>();
        int[] arr = Arrays.copyOf(input, input.length);
        int n = arr.length;

        for (int curr_size = n; curr_size > 1; --curr_size) {
            int mi = findMax(arr, curr_size);

            if (mi != curr_size - 1) {
                flip(arr, mi, steps);            // Move max to front
                flip(arr, curr_size - 1, steps); // Move max to end
            }
        }

        return steps;
    }

    private static void flip(int[] arr, int i, List<int[]> steps) {
        int start = 0;
        while (start < i) {
            int temp = arr[start];
            arr[start] = arr[i];
            arr[i] = temp;
            start++;
            i--;
        }

        // Visualizer.update(arr);
        steps.add(Arrays.copyOf(arr, arr.length));
    }

    private static int findMax(int[] arr, int n) {
        int mi = 0;
        for (int i = 0; i < n; i++) {
            if (arr[i] > arr[mi]) mi = i;
        }
        return mi;
    }
}
