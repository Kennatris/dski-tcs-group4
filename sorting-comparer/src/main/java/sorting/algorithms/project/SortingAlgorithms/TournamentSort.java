package sorting.algorithms.project.SortingAlgorithms;

// code by diptangsu from https://github.com/diptangsu/Sorting-Algorithms/tree/master
public class TournamentSort {

    public static void sort(int[] arr) {
        List<int[]> steps = new ArrayList<>();
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (arr[j] < arr[minIndex]) {
                    minIndex = j;
                }
            }
            int temp = arr[i];
            arr[i] = arr[minIndex];
            arr[minIndex] = temp;

            // Convert to array and record step
            int[] snapshot = sortedList.stream().mapToInt(Integer::intValue).toArray();
            // Visualizer.update(snapshot);
            steps.add(snapshot);
        }
    }
}
