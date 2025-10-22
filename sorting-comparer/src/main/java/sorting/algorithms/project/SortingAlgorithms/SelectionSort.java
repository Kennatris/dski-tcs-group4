package sorting.algorithms.project.SortingAlgorithms;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Component
public class SelectionSort implements SortingAlgorithm {
    private long steps;
    private List<Integer> dataSet;

    @Override
    public String getName() {
        return "SelectionSort";
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
        return "O(n²)";
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
        selectionSort(copy, step -> {});
        return copy;
    }

    @Override
    public void sortWithCallback(List<Integer> input, Consumer<List<Integer>> stepCallback) {
        dataSet = new ArrayList<>(input);
        steps = 0;
        selectionSort(input, stepCallback);
    }

    // 🔹 Interne SelectionSort-Implementierung mit Callback
    private void selectionSort(List<Integer> arr, Consumer<List<Integer>> stepCallback) {
        int n = arr.size();

        for (int i = 0; i < n; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                steps++; // jeder Vergleich zählt
                if (arr.get(j) < arr.get(minIndex)) {
                    minIndex = j;
                }
                stepCallback.accept(new ArrayList<>(arr));
            }

            if (minIndex != i) {
                int temp = arr.get(i);
                arr.set(i, arr.get(minIndex));
                arr.set(minIndex, temp);
                stepCallback.accept(new ArrayList<>(arr));
            }
        }
    }
}
