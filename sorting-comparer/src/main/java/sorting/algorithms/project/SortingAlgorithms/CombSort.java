package sorting.algorithms.project.SortingAlgorithms;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Component
public class CombSort implements SortingAlgorithm {

    private long steps;
    private List<Integer> dataSet;

    @Override
    public String getName() {
        return "CombSort";
    }

    @Override
    public String getBestCase() {
        return "O(n log n)";
    }

    @Override
    public String getAverageCase() {
        return "O(n² / 2^p)";
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
        combSort(copy, step -> {}); // Default: keine Callback
        return copy;
    }

    @Override
    public void sortWithCallback(List<Integer> input, Consumer<List<Integer>> stepCallback) {
        steps = 0;
        combSort(input, stepCallback);
    }

    // 🔹 Interne CombSort-Implementierung mit Callback
    private void combSort(List<Integer> arr, Consumer<List<Integer>> stepCallback) {
        int n = arr.size();
        int gap = n;
        boolean swapped = true;

        while (gap != 1 || swapped) {
            gap = getNextGap(gap);
            swapped = false;

            for (int i = 0; i < n - gap; i++) {
                steps++; // Vergleich arr[i] > arr[i + gap]
                if (arr.get(i) > arr.get(i + gap)) {
                    int temp = arr.get(i);
                    arr.set(i, arr.get(i + gap));
                    arr.set(i + gap, temp);
                    swapped = true;
                    stepCallback.accept(new ArrayList<>(arr));
                }
            }
        }
    }

    private int getNextGap(int gap) {
        gap = (gap * 10) / 13;
        if (gap < 1) return 1;
        return gap;
    }
}
