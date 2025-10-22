package sorting.algorithms.project.SortingAlgorithms;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Component
public class CountingSort implements SortingAlgorithm {

    private long steps;
    private List<Integer> dataSet;

    @Override
    public String getName() {
        return "CountingSort";
    }

    @Override
    public String getBestCase() {
        return "O(n + k)";
    }

    @Override
    public String getAverageCase() {
        return "O(n + k)";
    }

    @Override
    public String getWorstCase() {
        return "O(n + k)";
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
        countingSort(copy, step -> {}); // Default: keine Callback
        return copy;
    }

    @Override
    public void sortWithCallback(List<Integer> input, Consumer<List<Integer>> stepCallback) {
        steps = 0;
        countingSort(input, stepCallback);
    }

    // 🔹 Interne CountingSort-Implementierung mit Callback
    private void countingSort(List<Integer> arr, Consumer<List<Integer>> stepCallback) {
        int n = arr.size();
        if (n == 0) return;

        int max = arr.get(0);
        for (int num : arr) {
            if (num > max) max = num;
        }

        int[] output = new int[n];
        int[] count = new int[max + 1];

        // Count occurrences
        for (int num : arr) {
            count[num]++;
            steps++;
            stepCallback.accept(new ArrayList<>(arr));
        }

        // Position info
        for (int i = 1; i <= max; i++) {
            count[i] += count[i - 1];
            steps++;
        }

        // Build output
        for (int i = n - 1; i >= 0; i--) {
            int num = arr.get(i);
            output[count[num] - 1] = num;
            count[num]--;
            steps++;
            stepCallback.accept(new ArrayList<>(arr));
        }

        // Copy back
        for (int i = 0; i < n; i++) {
            arr.set(i, output[i]);
            steps++;
            stepCallback.accept(new ArrayList<>(arr));
        }
    }
}
