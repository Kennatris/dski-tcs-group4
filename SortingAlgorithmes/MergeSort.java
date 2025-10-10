// code based on code by diptangsu from https://github.com/diptangsu/Sorting-Algorithms/tree/master

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MergeSort {

    /**
     * Performs Merge Sort on the input array and returns all intermediate array states.
     *
     * @param input The array to be sorted
     * @return A list of array snapshots after each merge
     */
    public static List<int[]> mergeSort(int[] input) {
        List<int[]> steps = new ArrayList<>();
        int[] arr = Arrays.copyOf(input, input.length);

        mergeSortRecursive(arr, 0, arr.length - 1, steps);

        return steps;
    }

    private static void mergeSortRecursive(int[] arr, int lower, int upper, List<int[]> steps) {
        if (lower >= upper) return;
        int mid = (lower + upper) / 2;

        mergeSortRecursive(arr, lower, mid, steps);
        mergeSortRecursive(arr, mid + 1, upper, steps);
        merge(arr, lower, upper, steps);
    }

    private static void merge(int[] arr, int lower, int upper, List<int[]> steps) {
        int mid = (lower + upper) / 2;
        int[] left = new int[mid - lower + 1];
        int[] right = new int[upper - mid];

        for (int i = 0; i < left.length; i++)
            left[i] = arr[lower + i];
        for (int i = 0; i < right.length; i++)
            right[i] = arr[mid + 1 + i];

        int i = 0, j = 0, k = lower;
        while (i < left.length && j < right.length) {
            if (left[i] < right[j]) {
                arr[k++] = left[i++];
            } else {
                arr[k++] = right[j++];
            }

            // Visualizer.update(arr);
            steps.add(Arrays.copyOf(arr, arr.length));
        }

        while (i < left.length) {
            arr[k++] = left[i++];

            // Visualizer.update(arr);
            steps.add(Arrays.copyOf(arr, arr.length));
        }

        while (j < right.length) {
            arr[k++] = right[j++];

            // Visualizer.update(arr);
            steps.add(Arrays.copyOf(arr, arr.length));
        }
    }
}
