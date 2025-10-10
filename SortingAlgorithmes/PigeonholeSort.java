// code based on code by diptangsu from https://github.com/diptangsu/Sorting-Algorithms/tree/master

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PigeonholeSort {

    /**
     * Performs Pigeonhole Sort on the input array and returns all intermediate array states.
     *
     * @param input The array to be sorted
     * @return A list of array snapshots after placing elements into holes
     */
    public static List<int[]> pigeonholeSort(int[] input) {
        List<int[]> steps = new ArrayList<>();
        int[] arr = Arrays.copyOf(input, input.length);
        int n = arr.length;

        int min = arr[0];
        int max = arr[0];

        // Find min and max
        for (int i = 0; i < n; i++) {
            if (arr[i] > max) max = arr[i];
            if (arr[i] < min) min = arr[i];
        }

        int range = max - min + 1;
        int[] phole = new int[range];
        Arrays.fill(phole, 0);

        // Count elements in holes
        for (int i = 0; i < n; i++) {
            phole[arr[i] - min]++;
            // Visualizer.update(arr);
            steps.add(Arrays.copyOf(arr, arr.length));
        }

        // Reconstruct the sorted array
        int index = 0;
        for (int j = 0; j < range; j++) {
            while (phole[j]-- > 0) {
                arr[index++] = j + min;
                // Visualizer.update(arr);
                steps.add(Arrays.copyOf(arr, arr.length));
            }
        }

        return steps;
    }
}
