package sorting.algorithms.project.SortingAlgorithms;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Component
public class OddEvenSort implements SortingAlgorithm {
    private long steps;
    private List<Integer> dataSet;

    @Override
    public String getName() {
        return "OddEvenSort";
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
        oddEvenSort(copy, step -> {});
        return copy;
    }

    @Override
    public void sortWithCallback(List<Integer> input, Consumer<List<Integer>> stepCallback) {
        dataSet = new ArrayList<>(input);
        steps = 0;
        oddEvenSort(input, stepCallback);
    }

    // 🔹 Interne OddEvenSort-Implementierung mit Callback
    private void oddEvenSort(List<Integer> arr, Consumer<List<Integer>> stepCallback) {
        boolean sorted = false;

        while (!sorted) {
            sorted = true;

            // Odd indexed pass
            for (int i = 1; i < arr.size() - 1; i += 2) {
                steps++; // Vergleich
                if (arr.get(i) > arr.get(i + 1)) {
                    int temp = arr.get(i);
                    arr.set(i, arr.get(i + 1));
                    arr.set(i + 1, temp);
                    sorted = false;
                    stepCallback.accept(new ArrayList<>(arr));
                }
            }

            // Even indexed pass
            for (int i = 0; i < arr.size() - 1; i += 2) {
                steps++; // Vergleich
                if (arr.get(i) > arr.get(i + 1)) {
                    int temp = arr.get(i);
                    arr.set(i, arr.get(i + 1));
                    arr.set(i + 1, temp);
                    sorted = false;
                    stepCallback.accept(new ArrayList<>(arr));
                }
            }
        }
    }
}
