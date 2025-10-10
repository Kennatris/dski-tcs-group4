// code based on code by diptangsu from https://github.com/diptangsu/Sorting-Algorithms/tree/master

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OddEvenSort {

    /**
     * Performs Odd-Even Sort on the input array and returns all intermediate array states.
     *
     * @param input The array to be sorted
     * @return A list of array snapshots after each swap
     */
    public static List<int[]> oddEvenSort(int[] input) {
        List<int[]> steps = new ArrayList<>();
        int[] arr = Arrays.copyOf(input, input.length);
        boolean sorted = false;

        while (!sorted) {
            sorted = true;

            // Odd indexed pass
            for (int i = 1; i < arr.length - 1; i += 2) {
                if (arr[i] > arr[i + 1]) {
                    swap(arr, i, i + 1);
                    sorted = false;

                    // Visualizer.update(arr);
                    steps.add(Arrays.copyOf(arr, arr.length));
                }
            }

            // Even indexed pass
            for (int i = 0; i < arr.length - 1; i += 2) {
                if (arr[i] > arr[i + 1]) {
                    swap(arr, i, i + 1);
                    sorted = false;

                    // Visualizer.update(arr);
                    steps.add(Arrays.copyOf(arr, arr.length));
                }
            }
        }

        return steps;
    }

    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
