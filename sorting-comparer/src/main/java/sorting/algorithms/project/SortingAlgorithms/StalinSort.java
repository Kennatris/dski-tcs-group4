package sorting.algorithms.project.SortingAlgorithms;// code based on code by diptangsu from https://github.com/diptangsu/Sorting-Algorithms/tree/master
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StalinSort {

    /**
     * Performs Stalin Sort on the input array and returns all intermediate steps.
     *
     * @param input The array to sort
     * @return A list of array snapshots after each removal
     */
    public static List<int[]> stalinSort(int[] input) {
        List<int[]> steps = new ArrayList<>();
        if (input.length == 0) return steps;

        List<Integer> sortedList = new ArrayList<>();
        sortedList.add(input[0]);

        // Iterate and remove elements that break the increasing order
        for (int i = 1; i < input.length; i++) {
            if (input[i] >= sortedList.get(sortedList.size() - 1)) {
                sortedList.add(input[i]);
            }
            // Convert to array and record step
            int[] snapshot = sortedList.stream().mapToInt(Integer::intValue).toArray();
            // Visualizer.update(snapshot);
            steps.add(snapshot);
        }

        return steps;
    }
}
