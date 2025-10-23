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

    // 🔹 Interne StalinSort-Implementierung mit Callback (KORRIGIERT)
    // Diese Version arbeitet "in-place", indem sie Elemente entfernt.
    private void stalinSort(List<Integer> arr, Consumer<List<Integer>> stepCallback) {
        if (arr.isEmpty()) return;

        // Sende den Startzustand
        stepCallback.accept(new ArrayList<>(arr));

        int i = 1;
        while (i < arr.size()) {
            steps++; // Jeder Vergleich zählt

            // Wenn das aktuelle Element kleiner ist als das vorherige, "eliminiere" es
            if (arr.get(i) < arr.get(i - 1)) {
                arr.remove(i);
                // Sende Snapshot nach der Entfernung
                stepCallback.accept(new ArrayList<>(arr));
                // 'i' wird nicht erhöht, da das nächste Element an die Position 'i' gerückt ist
            } else {
                // Korrekte Reihenfolge, gehe zum nächsten Element
                i++;
            }
        }
    }
}