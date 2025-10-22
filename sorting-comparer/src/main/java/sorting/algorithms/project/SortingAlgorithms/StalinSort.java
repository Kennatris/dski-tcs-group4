package sorting.algorithms.project.SortingAlgorithms;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Component
public class StalinSort implements SortingAlgorithm {
    private long steps;
    private List<Integer> dataSet;

    @Override
    public String getName() {
        return "StalinSort";
    }

    @Override
    public String getWorstCase() {
        return "O(n)";
    }

    @Override
    public String getAverageCase() {
        return "O(n)";
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
        stalinSort(copy, step -> {});
        return copy;
    }

    @Override
    public void sortWithCallback(List<Integer> input, Consumer<List<Integer>> stepCallback) {
        dataSet = new ArrayList<>(input);
        steps = 0;
        stalinSort(input, stepCallback);
    }

    // 🔹 Interne StalinSort-Implementierung mit Callback
    private void stalinSort(List<Integer> arr, Consumer<List<Integer>> stepCallback) {
        if (arr.isEmpty()) return;

        int last = arr.get(0);
        List<Integer> result = new ArrayList<>();
        stepCallback.accept(new ArrayList<>(result));
        result.add(last);

        for (int i = 1; i < arr.size(); i++) {
            steps++; // jeder Vergleich zählt
            stepCallback.accept(new ArrayList<>(result));
            if (arr.get(i) >= last) {
                result.add(arr.get(i));
                last = arr.get(i);
            }
        }

        // Ergebnis zurück in arr
        arr.clear();
        arr.addAll(result);
    }
}
