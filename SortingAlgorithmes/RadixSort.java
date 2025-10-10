// code based on code by diptangsu from https://github.com/diptangsu/Sorting-Algorithms/tree/master

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Radix {

    /**
     * Performs Radix Sort on the input array and returns all intermediate array states.
     *
     * @param input The array to be sorted
     * @return A list of array snapshots after each digit-level sorting
     */
    public static List<int[]> radixSort(int[] input) {
        List<int[]> steps = new ArrayList<>();
        int[] arr = Arrays.copyOf(input, input.length);
        int n = arr.length;

        int m = getMax(arr); // Maximum number to know digits

        for (int exp = 1; m / exp > 0; exp *= 10) {
            countSort(arr, n, exp, steps);
        }

        return steps;
    }

    private static int getMax(int[] arr) {
        int mx = arr[0];
        for (int i = 1; i < arr.length; i++)
            if (arr[i] > mx)
                mx = arr[i];
        return mx;
    }

    private static void countSort(int[] arr, int n, int exp, List<int[]> steps) {
        int[] output = new int[n];
        int[] count = new int[10];
        Arrays.fill(count, 0);

        for (int i = 0; i < n; i++)
            count[(arr[i] / exp) % 10]++;

        for (int i = 1; i < 10; i++)
            count[i] += count[i - 1];

        for (int i = n - 1; i >= 0; i--) {
            output[count[(arr[i] / exp) % 10] - 1] = arr[i];
            count[(arr[i] / exp) % 10]--;
        }

        for (int i = 0; i < n; i++) {
            arr[i] = output[i];
        }

        // Visualizer.update(arr);
        steps.add(Arrays.copyOf(arr, arr.length));
    }
}
