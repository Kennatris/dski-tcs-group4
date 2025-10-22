package sorting.algorithms.project.SortingAlgorithms;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Component
public class BucketSort implements SortingAlgorithm {

    private long steps;
    private List<Integer> dataSet;

    @Override
    public String getName() {
        return "BucketSort";
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
        bucketSort(copy, step -> {}); // Default: keine Callback
        return copy;
    }

    @Override
    public void sortWithCallback(List<Integer> input, Consumer<List<Integer>> stepCallback) {
        steps = 0;
        bucketSort(input, stepCallback);
    }

    // 🔹 Interne BucketSort-Implementierung mit Callback
    private void bucketSort(List<Integer> arr, Consumer<List<Integer>> stepCallback) {
        int n = arr.size();
        if (n == 0) return;

        int maxValue = findMax(arr);
        int[] bucket = new int[maxValue + 1];

        // Count occurrences
        for (int num : arr) {
            bucket[num]++;
            steps++; // Jede Platzierung in den Bucket zählt als Schritt
            stepCallback.accept(new ArrayList<>(arr));
        }

        // Build sorted array
        arr.clear();
        for (int i = 0; i < bucket.length; i++) {
            for (int j = 0; j < bucket[i]; j++) {
                arr.add(i);
                steps++; // Jede Übertragung in die sortierte Liste zählt als Schritt
                stepCallback.accept(new ArrayList<>(arr));
            }
        }
    }

    private int findMax(List<Integer> arr) {
        int max = arr.get(0);
        for (int num : arr) {
            if (num > max) max = num;
        }
        return max;
    }
}
