package sorting.algorithms.project.SortingAlgorithms;// code by diptangsu from https://github.com/diptangsu/Sorting-Algorithms/tree/master
import java.util.Random;

public class ThreeWayQuickSort {

    private static void threeWayQuickSort(int[] arr, int low, int high)
    {
        List<int[]> steps = new ArrayList<>();
        if (low >= high) return;

        int lt = low, gt = high;
        int pivot = arr[low];
        int i = low + 1;

        while (i <= gt) {
            if (arr[i] < pivot) {
                int temp = arr[lt]; arr[lt] = arr[i]; arr[i] = temp;
                lt++; i++;
            } else if (arr[i] > pivot) {
                int temp = arr[i]; arr[i] = arr[gt]; arr[gt] = temp;
                gt--;
            } else {
                i++;
            }
            // Convert to array and record step
            int[] snapshot = sortedList.stream().mapToInt(Integer::intValue).toArray();
            // Visualizer.update(snapshot);
            steps.add(snapshot);
        }

        threeWayQuickSort(arr, low, lt - 1);
        threeWayQuickSort(arr, gt + 1, high);
    }

    private static void printSequence(int[] sequence)
    {
        for (int num : sequence)
            System.out.print(num + " ");
    }
}
