// code based on code by diptangsu from https://github.com/diptangsu/Sorting-Algorithms/tree/master

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BucketSort {

    /**
     * Performs Bucket Sort and returns all intermediate array states.
     * @param input The array to be sorted
     * @return A list of array snapshots after each modification step
     */
    public static List<int[]> bucketSort(int[] input) {
        List<int[]> steps = new ArrayList<>();
        int[] array = Arrays.copyOf(input, input.length);

        int maxValue = findMax(array);
        int[] bucket = new int[maxValue + 1];
        int[] sorted = new int[array.length];

        // Count occurrences
        for (int value : array) {
            bucket[value]++;
            // Visualizer.update(array);
            steps.add(Arrays.copyOf(array, array.length));
        }

        // Build sorted array
        int outPos = 0;
        for (int i = 0; i < bucket.length; i++) {
            for (int j = 0; j < bucket[i]; j++) {
                sorted[outPos++] = i;
                // Visualizer.update(sorted);
                steps.add(Arrays.copyOf(sorted, sorted.length));
            }
        }

        return steps;
    }

    // Helper: find maximum value
    private static int findMax(int[] array) {
        int max = array[0];
        for (int value : array) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }
}
