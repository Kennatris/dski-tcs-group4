package sorting.algorithms.project.SortingAlgorithms;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Component
public class CycleSort implements SortingAlgorithm {

    private long steps;
    private List<Integer> dataSet;

    @Override
    public String getName() {
        return "CycleSort";
    }

    @Override
    public String getBestCase() {
        return "O(n²)";
    }

    @Override
    public String getAverageCase() {
        return "O(n²)";
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
        cycleSort(copy, step -> {}); // Default: keine Callback
        return copy;
    }

    @Override
    public void sortWithCallback(List<Integer> input, Consumer<List<Integer>> stepCallback) {
        steps = 0;
        cycleSort(input, stepCallback);
    }

    // 🔹 Interne CycleSort-Implementierung mit Callback
    private void cycleSort(List<Integer> arr, Consumer<List<Integer>> stepCallback) {
        int n = arr.size();

        for (int start = 0; start <= n - 2; start++) {
            int item = arr.get(start);
            int pos = start;

            // Find position where we put the item
            for (int i = start + 1; i < n; i++) {
                steps++; // Vergleich
                if (arr.get(i) < item) pos++;
            }

            if (pos == start) continue;

            while (pos < n && item == arr.get(pos)) pos++;

            if (pos != start) {
                int temp = item;
                item = arr.get(pos);
                arr.set(pos, temp);
                steps++; // Swap
                stepCallback.accept(new ArrayList<>(arr));
            }

            while (pos != start) {
                pos = start;

                for (int i = start + 1; i < n; i++) {
                    steps++; // Vergleich
                    if (arr.get(i) < item) pos++;
                }

                while (pos < n && item == arr.get(pos)) pos++;

                if (item != arr.get(pos)) {
                    int temp = item;
                    item = arr.get(pos);
                    arr.set(pos, temp);
                    steps++; // Swap
                    stepCallback.accept(new ArrayList<>(arr));
                }
            }
        }
    }
}
