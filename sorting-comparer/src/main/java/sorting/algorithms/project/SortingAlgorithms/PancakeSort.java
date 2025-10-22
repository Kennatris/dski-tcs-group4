package sorting.algorithms.project.SortingAlgorithms;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Component
public class PancakeSort implements SortingAlgorithm {
    private long steps;
    private List<Integer> dataSet;

    @Override
    public String getName() {
        return "PancakeSort";
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
        pancakeSort(copy, step -> {});
        return copy;
    }

    @Override
    public void sortWithCallback(List<Integer> input, Consumer<List<Integer>> stepCallback) {
        dataSet = new ArrayList<>(input);
        steps = 0;
        pancakeSort(input, stepCallback);
    }

    // 🔹 Interne PancakeSort-Implementierung mit Callback
    private void pancakeSort(List<Integer> arr, Consumer<List<Integer>> stepCallback) {
        int n = arr.size();

        for (int curr_size = n; curr_size > 1; --curr_size) {
            int mi = findMax(arr, curr_size, stepCallback);

            if (mi != curr_size - 1) {
                flip(arr, mi, stepCallback);            // Max an den Anfang
                flip(arr, curr_size - 1, stepCallback); // Max an das Ende
            }
        }
    }

    private void flip(List<Integer> arr, int i, Consumer<List<Integer>> stepCallback) {
        int start = 0;
        while (start < i) {
            int temp = arr.get(start);
            arr.set(start, arr.get(i));
            arr.set(i, temp);
            steps++; // Swap zählt als Schritt
            stepCallback.accept(new ArrayList<>(arr));
            start++;
            i--;
        }
    }

    private int findMax(List<Integer> arr, int n, Consumer<List<Integer>> stepCallback) {
        int mi = 0;
        for (int i = 0; i < n; i++) {
            steps++; // jeder Vergleich zählt
            if (arr.get(i) > arr.get(mi)) {
                mi = i;
            }
            stepCallback.accept(new ArrayList<>(arr));
        }
        return mi;
    }
}
