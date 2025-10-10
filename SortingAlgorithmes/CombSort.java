// code based on code by diptangsu from https://github.com/diptangsu/Sorting-Algorithms/tree/master

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CombSort {

    /**
     * Performs Comb Sort on the input array and returns the intermediate steps.
     *
     * @param input The array to be sorted
     * @return A list of array states after each swap
     */
    public static List<int[]> combSort(int[] input) {
        List<int[]> steps = new ArrayList<>();
        int[] arr = Arrays.copyOf(input, input.length);
        int n = arr.length;

        int gap = n;
        boolean swapped = true;

        while (gap != 1 || swapped) {
            gap = getNextGap(gap);
            swapped = false;

            for (int i = 0; i < n - gap; i++) {
                if (arr[i] > arr[i + gap]) {
                    int temp = arr[i];
                    arr[i] = arr[i + gap];
                    arr[i + gap] = temp;
                    swapped = true;

                    // Visualizer.update(arr);
                    steps.add(Arrays.copyOf(arr, arr.length));
                }
            }
        }

        return steps;
    }

    /**
     * Calculates the next gap size for comb sort.
     *
     * @param gap Current gap size
     * @return Next gap size
     */
    private static int getNextGap(int gap) {
        gap = (gap * 10) / 13;
        if (gap < 1)
            return 1;
        return gap;
    }
}
