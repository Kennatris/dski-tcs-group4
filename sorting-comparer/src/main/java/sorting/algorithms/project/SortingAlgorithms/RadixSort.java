package sorting.algorithms.project.SortingAlgorithms;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Component
public class RadixSort implements SortingAlgorithm {
    private long steps;
    private List<Integer> dataSet;

    @Override
    public String getName() {
        return "RadixSort";
    }

    @Override
    public String getWorstCase() {
        return "O(nk)";
    }

    @Override
    public String getAverageCase() {
        return "O(nk)";
    }

    @Override
    public String getBestCase() {
        return "O(nk)";
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
        radixSort(copy, step -> {});
        return copy;
    }

    @Override
    public void sortWithCallback(List<Integer> input, Consumer<List<Integer>> stepCallback) {
        dataSet = new ArrayList<>(input);
        steps = 0;
        radixSort(input, stepCallback);
    }

    // 🔹 Interne RadixSort-Implementierung mit Callback
    private void radixSort(List<Integer> arr, Consumer<List<Integer>> stepCallback) {
        int n = arr.size();
        if (n == 0) return;

        int max = getMax(arr);

        for (int exp = 1; max / exp > 0; exp *= 10) {
            countSort(arr, n, exp, stepCallback);
        }
    }

    private int getMax(List<Integer> arr) {
        int max = arr.get(0);
        for (int i = 1; i < arr.size(); i++) {
            if (arr.get(i) > max) max = arr.get(i);
        }
        return max;
    }

    private void countSort(List<Integer> arr, int n, int exp, Consumer<List<Integer>> stepCallback) {
        int[] output = new int[n];
        int[] count = new int[10];

        // Zähle Vorkommen der Ziffern
        for (int i = 0; i < n; i++) {
            int index = (arr.get(i) / exp) % 10;
            count[index]++;
            steps++;
            // KORREKTUR: Callback entfernt (arr ändert sich hier nicht)
            // stepCallback.accept(new ArrayList<>(arr));
        }

        for (int i = 1; i < 10; i++) {
            count[i] += count[i - 1];
        }

        for (int i = n - 1; i >= 0; i--) {
            int index = (arr.get(i) / exp) % 10;
            output[count[index] - 1] = arr.get(i);
            count[index]--;
        }

        for (int i = 0; i < n; i++) {
            arr.set(i, output[i]);
            steps++;
            stepCallback.accept(new ArrayList<>(arr)); // Diese Visualisierung ist korrekt
        }
    }
}