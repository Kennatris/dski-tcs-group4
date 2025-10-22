package sorting.algorithms.project.SortingAlgorithms;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Component
public class QuickSort implements SortingAlgorithm {
    private long steps;
    private List<Integer> dataSet;

    @Override
    public String getName() {
        return "QuickSort";
    }

    @Override
    public String getWorstCase() {
        return "O(n²)";
    }

    @Override
    public String getAverageCase() {
        return "O(n log n)";
    }

    @Override
    public String getBestCase() {
        return "O(n log n)";
    }

    @Override
    public long getSteps() {
        return steps;
    }

    @Override
    public List<Integer> getData() {
        return dataSet;
    }

    @Override
    public List<Integer> sort(List<Integer> input) {
        List<Integer> copy = new ArrayList<>(input);
        dataSet = new ArrayList<>(copy);
        steps = 0;
        quickSortRecursive(copy, 0, copy.size() - 1, step -> {});
        return copy;
    }

    @Override
    public void sortWithCallback(List<Integer> input, Consumer<List<Integer>> stepCallback) {
        dataSet = new ArrayList<>(input);
        steps = 0;
        quickSortRecursive(input, 0, input.size() - 1, stepCallback);
    }

    // 🔹 Interne QuickSort-Implementierung mit Callback
    private void quickSortRecursive(List<Integer> arr, int lower, int upper, Consumer<List<Integer>> stepCallback) {
        if (lower >= upper) return;

        int p = partition(arr, lower, upper, stepCallback);
        quickSortRecursive(arr, lower, p - 1, stepCallback);
        quickSortRecursive(arr, p + 1, upper, stepCallback);
    }

    private int partition(List<Integer> arr, int lower, int upper, Consumer<List<Integer>> stepCallback) {
        int pivot = arr.get(upper);
        int j = lower;

        for (int i = lower; i <= upper; i++) {
            steps++; // jeder Vergleich zählt
            if (arr.get(i) < pivot) {
                int temp = arr.get(i);
                arr.set(i, arr.get(j));
                arr.set(j, temp);
                j++;
                stepCallback.accept(new ArrayList<>(arr));
            }
        }

        int temp = arr.get(upper);
        arr.set(upper, arr.get(j));
        arr.set(j, temp);
        stepCallback.accept(new ArrayList<>(arr));

        return j;
    }
}
