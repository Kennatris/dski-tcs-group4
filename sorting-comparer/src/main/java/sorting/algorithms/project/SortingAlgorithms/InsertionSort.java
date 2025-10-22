package sorting.algorithms.project.SortingAlgorithms;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Component
public class InsertionSort implements SortingAlgorithm {
    private long steps;
    private List<Integer> dataSet;

    @Override
    public String getName() {
        return "InsertionSort";
    }

    @Override
    public String getWorstCase() {
        return "O(n²)";
    }

    @Override
    public String getAverageCase() {
        return "O(n²)";
    }

    @Override
    public String getBestCase() {
        return "O(n)";
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
        insertionSort(copy, step -> {}); // Default: keine Callback
        return copy;
    }

    @Override
    public void sortWithCallback(List<Integer> input, Consumer<List<Integer>> stepCallback) {
        steps = 0;
        insertionSort(input, stepCallback);
    }

    // 🔹 Interne InsertionSort-Implementierung mit Callback
    private void insertionSort(List<Integer> arr, Consumer<List<Integer>> stepCallback) {
        int n = arr.size();

        for (int i = 1; i < n; i++) {
            int key = arr.get(i);
            int j = i - 1;

            while (j >= 0) {
                steps++; // jeder Vergleich zählt
                if (arr.get(j) > key) {
                    arr.set(j + 1, arr.get(j));
                    j--;
                    stepCallback.accept(new ArrayList<>(arr));
                } else {
                    break;
                }
            }

            arr.set(j + 1, key);
            stepCallback.accept(new ArrayList<>(arr));
        }
    }
}
