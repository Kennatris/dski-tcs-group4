// code based on code by diptangsu from https://github.com/diptangsu/Sorting-Algorithms/tree/master

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GnomeSort {

    /**
     * Performs Gnome Sort on the input array and returns all intermediate array states.
     *
     * @param input The array to be sorted
     * @return A list of array snapshots after each swap
     */
    public static List<int[]> gnomeSort(int[] input) {
        List<int[]> steps = new ArrayList<>();
        int[] arr = Arrays.copyOf(input, input.length);
        int index = 1;

        while (index < arr.length) {
            if (arr[index - 1] <= arr[index]) {
                index++;
            } else {
                int tmp = arr[index - 1];
                arr[index - 1] = arr[index];
                arr[index] = tmp;

                // Visualizer.update(arr);
                steps.add(Arrays.copyOf(arr, arr.length));

                if (--index == 0) {
                    index = 1;
                }
            }
        }

        return steps;
    }
}
