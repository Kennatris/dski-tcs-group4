// code based on code by diptangsu from https://github.com/diptangsu/Sorting-Algorithms/tree/master

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BogoSort {

    // The only callable function
    public static List<int[]> bogoSort(int[] input) {
        List<int[]> steps = new ArrayList<>();
        int[] array = Arrays.copyOf(input, input.length);

        int counter = 0;
        while (!isSorted(array)) {
            shuffle(array);
            counter++;
            // Visualizer.update(array);
            steps.add(Arrays.copyOf(array, array.length));
        }

        // Optionally, could log total shuffles if needed:
        // System.out.println("Shuffled " + counter + " times");

        return steps;
    }

    // Shuffle helper
    private static void shuffle(int[] array) {
        for (int i = 0; i < array.length; i++) {
            int index1 = (int) (Math.random() * array.length);
            int index2 = (int) (Math.random() * array.length);

            int temp = array[index1];
            array[index1] = array[index2];
            array[index2] = temp;
        }
    }

    // Check if array is sorted
    private static boolean isSorted(int[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            if (array[i] > array[i + 1]) {
                return false;
            }
        }
        return true;
    }
}
