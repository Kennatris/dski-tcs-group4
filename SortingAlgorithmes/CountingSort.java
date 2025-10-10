// code based on code by diptangsu from https://github.com/diptangsu/Sorting-Algorithms/tree/master

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CountingSort {

    /**
     * Performs Counting Sort on the given character array.
     * Returns a list of array states after each placement into the output array.
     *
     * @param input The character array to be sorted
     * @return A list of array states after each change
     */
    public static List<char[]> countingSort(char[] input) {
        List<char[]> steps = new ArrayList<>();
        char[] arr = Arrays.copyOf(input, input.length);
        int n = arr.length;

        char[] output = new char[n];
        int[] count = new int[256];

        // Initialize count array
        for (int i = 0; i < 256; i++)
            count[i] = 0;

        // Store count of each character
        for (int i = 0; i < n; i++)
            count[arr[i]]++;

        // Update count[i] so that it contains position information
        for (int i = 1; i <= 255; i++)
            count[i] += count[i - 1];

        // Build the output character array (in reverse for stability)
        for (int i = n - 1; i >= 0; i--) {
            output[count[arr[i]] - 1] = arr[i];
            count[arr[i]]--;

            // Visualizer.update(output);
            steps.add(Arrays.copyOf(output, output.length));
        }

        // Copy output back into input
        for (int i = 0; i < n; i++) {
            arr[i] = output[i];
        }

        steps.add(Arrays.copyOf(arr, arr.length)); // final sorted state
        return steps;
    }
}
