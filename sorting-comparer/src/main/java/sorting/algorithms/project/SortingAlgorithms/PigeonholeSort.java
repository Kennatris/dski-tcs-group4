package sorting.algorithms.project.SortingAlgorithms;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Component
public class PigeonholeSort implements SortingAlgorithm {
    private long steps;
    private List<Integer> dataSet;

    @Override
    public String getName() {
        return "PigeonholeSort";
    }

    @Override
    public String getWorstCase() {
        return "O(n + range)";
    }

    @Override
    public String getAverageCase() {
        return "O(n + range)";
    }

    @Override
    public String getBestCase() {
        return "O(n + range)";
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
        pigeonholeSort(copy, step -> {});
        return copy;
    }

    @Override
    public void sortWithCallback(List<Integer> input, Consumer<List<Integer>> stepCallback) {
        dataSet = new ArrayList<>(input);
        steps = 0;
        pigeonholeSort(input, stepCallback);
    }

    // 🔹 Interne PigeonholeSort-Implementierung mit Callback
    private void pigeonholeSort(List<Integer> arr, Consumer<List<Integer>> stepCallback) {
        int n = arr.size();
        if (n == 0) return;

        int min = arr.get(0);
        int max = arr.get(0);

        // Finde min und max
        for (int i = 0; i < n; i++) {
            steps++;
            if (arr.get(i) > max) max = arr.get(i);
            if (arr.get(i) < min) min = arr.get(i);
            stepCallback.accept(new ArrayList<>(arr));
        }

        int range = max - min + 1;
        int[] holes = new int[range];

        // Zähle Elemente in den Löchern
        for (int i = 0; i < n; i++) {
            holes[arr.get(i) - min]++;
            steps++;
            stepCallback.accept(new ArrayList<>(arr));
        }

        // Rekonstruiere sortiertes Array
        int index = 0;
        for (int j = 0; j < range; j++) {
            while (holes[j] > 0) {
                arr.set(index++, j + min);
                holes[j]--;
                steps++;
                stepCallback.accept(new ArrayList<>(arr));
            }
        }
    }
}
