package sorting.algorithms.project.SortingAlgorithms;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Component
public class HeapSort implements SortingAlgorithm {
    private long steps;
    private List<Integer> dataSet;

    @Override
    public String getName() {
        return "HeapSort";
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
        heapSort(copy, step -> {}); // Default: keine Callback
        return copy;
    }

    @Override
    public void sortWithCallback(List<Integer> input, Consumer<List<Integer>> stepCallback) {
        steps = 0;
        heapSort(input, stepCallback);
    }

    // 🔹 Interne HeapSort-Implementierung mit Callback
    private void heapSort(List<Integer> arr, Consumer<List<Integer>> stepCallback) {
        int n = arr.size();
        makeMaxHeap(arr, n, stepCallback);

        for (int i = n - 1; i > 0; i--) {
            // Swap
            int temp = arr.get(0);
            arr.set(0, arr.get(i));
            arr.set(i, temp);
            steps++;
            stepCallback.accept(new ArrayList<>(arr));

            heapAdjust(arr, 0, i, stepCallback);
        }
    }

    private void makeMaxHeap(List<Integer> arr, int n, Consumer<List<Integer>> stepCallback) {
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapAdjust(arr, i, n, stepCallback);
        }
    }

    private void heapAdjust(List<Integer> arr, int i, int n, Consumer<List<Integer>> stepCallback) {
        int tmp = arr.get(i);
        int j = 2 * i + 1;

        while (j < n) {
            steps++; // Vergleich zwischen arr[j] und arr[j+1] falls j<n-1
            if (j < n - 1 && arr.get(j) < arr.get(j + 1)) {
                j++;
            }

            steps++; // Vergleich zwischen tmp und arr[j]
            if (tmp >= arr.get(j)) {
                break;
            }

            arr.set((j - 1) / 2, arr.get(j));
            j = 2 * j + 1;
            stepCallback.accept(new ArrayList<>(arr));
        }

        arr.set((j - 1) / 2, tmp);
        stepCallback.accept(new ArrayList<>(arr));
    }
}
