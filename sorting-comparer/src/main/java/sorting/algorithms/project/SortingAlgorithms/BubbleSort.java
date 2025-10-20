package sorting.algorithms.project.SortingAlgorithms;// code based on code by diptangsu from https://github.com/diptangsu/Sorting-Algorithms/tree/master

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BubbleSort {

    /**
     * Performs Bubble Sort and returns all intermediate array states.
     * @param input The array to be sorted
     * @return A list of array snapshots after each swap
     */
    public static List<int[]> bubbleSort(int[] input) {
        List<int[]> steps = new ArrayList<>();
        int[] array = Arrays.copyOf(input, input.length);
        int len = array.length;
        boolean swapped;

        for (int i = 0; i < len - 1; i++) {
            swapped = false;
            for (int j = 0; j < len - i - 1; j++) {
                if (array[j] > array[j + 1]) {
                    int temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;

                    // Visualizer.update(array);
                    steps.add(Arrays.copyOf(array, array.length));
                    swapped = true;
                }
            }
            if (!swapped) break;
        }

        return steps;
    }
}
