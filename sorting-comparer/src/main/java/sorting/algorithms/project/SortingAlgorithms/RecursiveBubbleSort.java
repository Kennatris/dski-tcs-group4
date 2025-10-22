package sorting.algorithms.project.SortingAlgorithms;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Component
public class RecursiveBubbleSort implements SortingAlgorithm {
    private long steps;
    private List<Integer> dataSet;

    @Override
    public String getName() {
        return "RecursiveBubbleSort";
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
        bubbleSortRecursive(copy, copy.size(), step -> {});
        return copy;
    }

    @Override
    public void sortWithCallback(List<Integer> input, Consumer<List<Integer>> stepCallback) {
        dataSet = new ArrayList<>(input);
        steps = 0;
        bubbleSortRecursive(input, input.size(), stepCallback);
    }

    // 🔹 Interne rekursive BubbleSort-Implementierung mit Callback
    private void bubbleSortRecursive(List<Integer> arr, int n, Consumer<List<Integer>> stepCallback) {
        if (n == 1) return;

        for (int i = 0; i < n - 1; i++) {
            steps++; // jeder Vergleich zählt
            if (arr.get(i) > arr.get(i + 1)) {
                int temp = arr.get(i);
                arr.set(i, arr.get(i + 1));
                arr.set(i + 1, temp);
            }
            stepCallback.accept(new ArrayList<>(arr));
        }

        bubbleSortRecursive(arr, n - 1, stepCallback);
    }
}
