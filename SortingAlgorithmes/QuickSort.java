// code based on code by diptangsu from https://github.com/diptangsu/Sorting-Algorithms/tree/master

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuickSort {

    /**
     * Performs Quick Sort on the input array and returns all intermediate array states.
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

    private static void quickSortRecursive(int[] arr, int lower, int upper, List<int[]> steps) {
        if (lower >= upper) return;

        int p = partition(arr, lower, upper, steps);
        quickSortRecursive(arr, lower, p - 1, steps);
        quickSortRecursive(arr, p + 1, upper, steps);
    }

    private static int partition(int[] arr, int lower, int upper, List<int[]> steps) {
        int pivot = arr[upper];
        int j = lower;

        for (int i = lower; i <= upper; i++) {
            if (arr[i] < pivot) {
                swap(arr, i, j, steps);
                j++;
            }
        }

        swap(arr, upper, j, steps);
        return j;
    }

    private static void swap(int[] arr, int i, int j, List<int[]> steps) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;

        // Visualizer.update(arr);
        steps.add(Arrays.copyOf(arr, arr.length));
    }
}
