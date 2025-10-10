// code based on code by diptangsu from https://github.com/diptangsu/Sorting-Algorithms/tree/master

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Binary Insertion Sort using binary search to find insertion points.
 */
public class BinaryInsertion {

    /**
     * This class should not be instantiated.
     */
    private BinaryInsertion() { }

    /**
     * Performs Binary Insertion Sort and returns all intermediate array states.
     * @param input the array to be sorted
     * @return a list of array snapshots after each modification step
     */
    public static List<Comparable[]> binaryInsertionSort(Comparable[] input) {
        List<Comparable[]> steps = new ArrayList<>();
        Comparable[] array = Arrays.copyOf(input, input.length);
        int n = array.length;

        for (int i = 1; i < n; i++) {
            Comparable v = array[i];
            int lo = 0, hi = i;

            // Binary search to find insertion position
            while (lo < hi) {
                int mid = lo + (hi - lo) / 2;
                if (less(v, array[mid])) hi = mid;
                else lo = mid + 1;
            }

            // Shift elements and insert the current element
            for (int j = i; j > lo; j--) {
                array[j] = array[j - 1];
                // Visualizer.update(array);
                steps.add(Arrays.copyOf(array, array.length));
            }
            array[lo] = v;
            // Visualizer.update(array);
            steps.add(Arrays.copyOf(array, array.length));
        }

        return steps;
    }

    // Compare helper
    private static boolean less(final Comparable v, final Comparable w) {
        return v.compareTo(w) < 0;
    }
}
