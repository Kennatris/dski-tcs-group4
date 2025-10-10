// code based on code by diptangsu from https://github.com/diptangsu/Sorting-Algorithms/tree/master

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CycleSort {

    /**
     * Performs Cycle Sort on the input array and returns all intermediate array states.
     *
     * @param input The array to be sorted
     * @return A list of array snapshots after each write
     */
    public static List<int[]> cycleSort(int[] input) {
        List<int[]> steps = new ArrayList<>();
        int[] arr = Arrays.copyOf(input, input.length);
        int n = arr.length;

        for (int start = 0; start <= n - 2; start++) {
            int item = arr[start];
            int pos = start;

            // Find position where we put the item
            for (int i = start + 1; i < n; i++)
                if (arr[i] < item)
                    pos++;

            if (pos == start) continue;

            while (item == arr[pos]) pos++;

            if (pos != start) {
                int temp = item;
                item = arr[pos];
                arr[pos] = temp;

                // Visualizer.update(arr);
                steps.add(Arrays.copyOf(arr, arr.length));
            }

            while (pos != start) {
                pos = start;

                for (int i = start + 1; i < n; i++)
                    if (arr[i] < item)
                        pos++;

                while (item == arr[pos]) pos++;

                if (item != arr[pos]) {
                    int temp = item;
                    item = arr[pos];
                    arr[pos] = temp;

                    // Visualizer.update(arr);
                    steps.add(Arrays.copyOf(arr, arr.length));
                }
            }
        }

        return steps;
    }
}
