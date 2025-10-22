package sorting.algorithms.project.SortingAlgorithms;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Component
public class BubbleSort implements SortingAlgorithm {
    long steps;
    List<Integer> dataSet;

    @Override
    public String getName() {
        return "BubbleSort";
    }

    @Override
    public String getWorstCase() { return "O(n²)"; }

    @Override
    public String getAverageCase() { return "O(n²)"; }

    @Override
    public String getBestCase() { return "O(n)"; }

    @Override
    public long getSteps() { return steps; }

    @Override
    public List<Integer> getData() { return dataSet; }

    @Override
    public List<Integer> sort(List<Integer> input) {
        // Normale Sortierung ohne Callback
        List<Integer> copy = new ArrayList<>(input);
        dataSet = new ArrayList<>(copy);
        bubbleSort(copy, step -> {}); // Default: keine Callback
        return copy;
    }

    // 🔹 Neue Methode für Live-Visualisierung
    @Override
    public void sortWithCallback(List<Integer> input, Consumer<List<Integer>> stepCallback) {
        bubbleSort(input, stepCallback);
    }

    // Interne BubbleSort-Implementierung mit Callback
    public void bubbleSort(List<Integer> arr, Consumer<List<Integer>> stepCallback) {
        steps = 0;
        int n = arr.size();
        boolean swapped;
        do {
            swapped = false;
            for (int i = 1; i < n; i++) {
                if (arr.get(i - 1) > arr.get(i)) {
                    int t = arr.get(i - 1);
                    arr.set(i - 1, arr.get(i));
                    arr.set(i, t);
                    swapped = true;
                }
                steps++;
                // Zwischenschritt senden
                stepCallback.accept(new ArrayList<>(arr));
            }
            n--;
        } while (swapped);
    }
}
