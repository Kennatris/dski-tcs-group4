package sorting.algorithms.project.SortingAlgorithms;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Component
public class MergeSort implements SortingAlgorithm {
    private long steps;
    private List<Integer> dataSet;

    @Override
    public String getName() {
        return "MergeSort";
    }

    @Override
    public String getWorstCase() {
        return "O(n log n)";
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
        mergeSort(copy, 0, copy.size() - 1, step -> {});
        return copy;
    }

    @Override
    public void sortWithCallback(List<Integer> input, Consumer<List<Integer>> stepCallback) {
        dataSet = new ArrayList<>(input);
        steps = 0;
        mergeSort(input, 0, input.size() - 1, stepCallback);
    }

    // 🔹 Interne MergeSort-Implementierung mit Callback
    private void mergeSort(List<Integer> arr, int lower, int upper, Consumer<List<Integer>> stepCallback) {
        if (lower >= upper) return;

        int mid = (lower + upper) / 2;
        mergeSort(arr, lower, mid, stepCallback);
        mergeSort(arr, mid + 1, upper, stepCallback);
        merge(arr, lower, mid, upper, stepCallback);
    }

    private void merge(List<Integer> arr, int lower, int mid, int upper, Consumer<List<Integer>> stepCallback) {
        List<Integer> left = new ArrayList<>(arr.subList(lower, mid + 1));
        List<Integer> right = new ArrayList<>(arr.subList(mid + 1, upper + 1));

        int i = 0, j = 0, k = lower;

        while (i < left.size() && j < right.size()) {
            steps++; // Vergleich
            if (left.get(i) <= right.get(j)) {
                arr.set(k++, left.get(i++));
            } else {
                arr.set(k++, right.get(j++));
            }
            stepCallback.accept(new ArrayList<>(arr));
        }

        while (i < left.size()) {
            arr.set(k++, left.get(i++));
            steps++;
            stepCallback.accept(new ArrayList<>(arr));
        }

        while (j < right.size()) {
            arr.set(k++, right.get(j++));
            steps++;
            stepCallback.accept(new ArrayList<>(arr));
        }
    }
}
