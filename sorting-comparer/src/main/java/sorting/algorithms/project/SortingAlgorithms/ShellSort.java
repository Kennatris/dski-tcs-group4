package sorting.algorithms.project.SortingAlgorithms;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Component
public class ShellSort implements SortingAlgorithm {
    private long steps;
    private List<Integer> dataSet;

    @Override
    public String getName() {
        return "ShellSort";
    }

    @Override
    public String getWorstCase() {
        return "O(n^(3/2))";
    }

    @Override
    public String getAverageCase() {
        return "O(n^(3/2))";
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
        shellSort(copy, step -> {});
        return copy;
    }

    @Override
    public void sortWithCallback(List<Integer> input, Consumer<List<Integer>> stepCallback) {
        dataSet = new ArrayList<>(input);
        steps = 0;
        shellSort(input, stepCallback);
    }

    // 🔹 Interne ShellSort-Implementierung mit Callback
    private void shellSort(List<Integer> arr, Consumer<List<Integer>> stepCallback) {
        int n = arr.size();

        int h = 1;
        while (h < n) {
            h = h * 3 + 1;
        }
        h = h / 3;

        while (h > 0) {
            for (int i = h; i < n; i++) {
                int temp = arr.get(i);
                int j = i;

                while (j >= h) {
                    steps++; // jeder Vergleich zählt
                    if (arr.get(j - h) > temp) {
                        arr.set(j, arr.get(j - h));
                        j -= h;
                        stepCallback.accept(new ArrayList<>(arr));
                    } else {
                        break;
                    }
                }

                arr.set(j, temp);
                stepCallback.accept(new ArrayList<>(arr));
            }
            h = h / 2;
        }
    }
}
