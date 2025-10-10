// code based on code by diptangsu from https://github.com/diptangsu/Sorting-Algorithms/tree/master

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HeapSort {

    /**
     * Performs Heap Sort on the input array and returns all intermediate array states.
     *
     * @param input The array to be sorted
     * @return A list of array snapshots after each modification
     */
    public static List<int[]> heapSort(int[] input) {
        List<int[]> steps = new ArrayList<>();
        int[] arr = Arrays.copyOf(input, input.length);
        int len = arr.length;

        makeMaxHeap(arr, len, steps);

        for (int i = len - 1; i > 0; i--) {
            int tmp = arr[0];
            arr[0] = arr[i];
            arr[i] = tmp;

            // Visualizer.update(arr);
            steps.add(Arrays.copyOf(arr, arr.length));

            heapAdjust(arr, 0, i, steps);
        }

        return steps;
    }

    private static void makeMaxHeap(int[] arr, int len, List<int[]> steps) {
        for (int i = len / 2 - 1; i >= 0; i--) {
            heapAdjust(arr, i, len, steps);
        }
    }

    private static void heapAdjust(int[] arr, int i, int n, List<int[]> steps) {
        int j = 2 * i + 1;
        int tmp = arr[i];
        while (j < n) {
            if (j < n - 1 && arr[j] < arr[j + 1])
                j++;
            if (tmp > arr[j])
                break;
            arr[(j - 1) / 2] = arr[j];
            j = 2 * j + 1;

            // Visualizer.update(arr);
            steps.add(Arrays.copyOf(arr, arr.length));
        }
        arr[(j - 1) / 2] = tmp;

        // Visualizer.update(arr);
        steps.add(Arrays.copyOf(arr, arr.length));
    }
}
