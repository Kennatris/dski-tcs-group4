// code based on code by diptangsu from https://github.com/diptangsu/Sorting-Algorithms/tree/master

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CocktailSort {

    /**
     * Performs Cocktail Sort and returns all intermediate array states.
     * @param input The array to be sorted
     * @return A list of array snapshots after each swap
     */
    public static List<int[]> cocktailSort(int[] input) {
        List<int[]> steps = new ArrayList<>();
        int[] array = Arrays.copyOf(input, input.length);

        boolean swapped = true;
        int start = 0;
        int end = array.length;

        while (swapped) {
            swapped = false;

            // Forward pass
            for (int i = start; i < end - 1; ++i) {
                if (array[i] > array[i + 1]) {
                    int temp = array[i];
                    array[i] = array[i + 1];
                    array[i + 1] = temp;
                    swapped = true;
                    // Visualizer.update(array);
                    steps.add(Arrays.copyOf(array, array.length));
                }
            }

            if (!swapped) break;

            swapped = false;
            end--;

            // Backward pass
            for (int i = end - 1; i >= start; i--) {
                if (array[i] > array[i + 1]) {
                    int temp = array[i];
                    array[i] = array[i + 1];
                    array[i + 1] = temp;
                    swapped = true;
                    // Visualizer.update(array);
                    steps.add(Arrays.copyOf(array, array.length));
                }
            }

            start++;
        }

        return steps;
    }
}
