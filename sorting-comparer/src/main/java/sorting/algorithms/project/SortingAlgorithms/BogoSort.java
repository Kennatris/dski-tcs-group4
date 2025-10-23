package sorting.algorithms.project.SortingAlgorithms;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Component
public class BogoSort implements SortingAlgorithm {

    private long steps;
    private List<Integer> dataSet;

    @Override
    public String getName() {
        return "BogoSort";
    }

    @Override
    public String getBestCase() {
        return "O(1)";
    }

    @Override
    public String getAverageCase() {
        return "O(∞)";
    }

    @Override
    public String getWorstCase() {
        return "O(∞)";
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
        bogoSort(copy, step -> {}); // Default: keine Callback
        return copy;
    }

    @Override
    public void sortWithCallback(List<Integer> input, Consumer<List<Integer>> stepCallback) {
        steps = 0;
        bogoSort(input, stepCallback);
    }

    // 🔹 Interne BogoSort-Implementierung mit Callback
    private void bogoSort(List<Integer> arr, Consumer<List<Integer>> stepCallback) {
        while (!isSorted(arr, stepCallback)) {
            shuffle(arr);
            steps++; // Zähle jeden Shuffle als Schritt
            stepCallback.accept(new ArrayList<>(arr));
        }
    }

    private void shuffle(List<Integer> arr) {
        for (int i = 0; i < arr.size(); i++) {
            int index1 = (int) (Math.random() * arr.size());
            int index2 = (int) (Math.random() * arr.size());
            int temp = arr.get(index1);
            arr.set(index1, arr.get(index2));
            arr.set(index2, temp);
        }
    }

    private boolean isSorted(List<Integer> arr, Consumer<List<Integer>> stepCallback) {
        for (int i = 1; i < arr.size(); i++) {
            steps++; // Vergleich arr[i-1] > arr[i]
            if (arr.get(i - 1) > arr.get(i)) {
                return false;
            }
        }
        stepCallback.accept(new ArrayList<>(arr));
        return true;
    }
}
