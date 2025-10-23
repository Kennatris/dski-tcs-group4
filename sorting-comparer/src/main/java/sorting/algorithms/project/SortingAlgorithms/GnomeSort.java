package sorting.algorithms.project.SortingAlgorithms;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Component
public class GnomeSort implements SortingAlgorithm {

    private long steps;
    private List<Integer> dataSet;

    @Override
    public String getName() {
        return "GnomeSort";
    }

    @Override
    public String getBestCase() {
        return "O(n)";
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
        gnomeSort(copy, step -> {}); // Default: keine Callback
        return copy;
    }

    @Override
    public void sortWithCallback(List<Integer> input, Consumer<List<Integer>> stepCallback) {
        steps = 0;
        gnomeSort(input, stepCallback);
    }

    // 🔹 Interne GnomeSort-Implementierung mit Callback
    private void gnomeSort(List<Integer> arr, Consumer<List<Integer>> stepCallback) {
        int index = 1;
        while (index < arr.size()) {
            steps++; // Vergleich zwischen arr[index-1] und arr[index]

            if (arr.get(index - 1) <= arr.get(index)) {
                index++;
            } else {
                // Swap
                int tmp = arr.get(index - 1);
                arr.set(index - 1, arr.get(index));
                arr.set(index, tmp);
                steps++; // Swap zählt auch als Schritt
                // KORREKTUR: Callback hier entfernt

                index--;
                if (index == 0) {
                    index = 1;
                }
            }
            // KORREKTUR: Callback nach außen verschoben, sendet bei jeder Iteration
            stepCallback.accept(new ArrayList<>(arr));
        }
    }
}