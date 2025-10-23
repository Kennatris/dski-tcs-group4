package sorting.algorithms.project.SortingAlgorithms;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import sorting.algorithms.project.dto.SortStep;

@Component
public class OddEvenSort implements SortingAlgorithm {
    private long steps;
    private List<Integer> dataSet;

    @Override
    public String getName() {
        return "OddEvenSort";
    }

    @Override
    public String getWorstCase() {
        return "O(n²)";
    }

    @Override
    public String getAverageCase() {
        return "O(n²)";
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
        // Sicherstellen, dass dataSet nicht null ist
        return dataSet != null ? dataSet : SortingAlgorithm.super.getData();
    }

    @Override
    public List<Integer> sort(List<Integer> input) {
        List<Integer> copy = new ArrayList<>(input);
        dataSet = new ArrayList<>(copy);
        steps = 0;
        oddEvenSort(copy, (SortStep step) -> {});
        return copy;
    }

    @Override
    public void sortWithCallback(List<Integer> input, Consumer<SortStep> stepCallback) {
        dataSet = new ArrayList<>(input);
        steps = 0;
        // Sende initialen Zustand
        stepCallback.accept(new SortStep(new ArrayList<>(input), Collections.emptySet(), Collections.emptySet()));
        oddEvenSort(input, stepCallback);
    }

    /**
     * Führt Odd-Even Sort "in-place" aus. Wechselt zwischen Vergleichen
     * von Paaren an ungeraden und geraden Indizes. Meldet jeden Vergleich/Swap.
     * @param arr Die zu sortierende Liste.
     * @param stepCallback Der Callback für die Visualisierung.
     */
    private void oddEvenSort(List<Integer> arr, Consumer<SortStep> stepCallback) {
        int n = arr.size();
        if (n <= 1) {
            stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), Collections.emptySet())); // Endzustand
            return;
        }

        boolean sorted = false;
        while (!sorted) {
            sorted = true; // Annahme, dass sortiert ist, wird bei Swap widerlegt

            // Ungerader Durchlauf (Vergleiche Indizes 1&2, 3&4, ...)
            for (int i = 1; i < n - 1; i += 2) {
                Set<Integer> accessed = new HashSet<>();
                Set<Integer> changed = new HashSet<>();
                accessed.add(i);
                accessed.add(i + 1);
                steps++; // Zähle Vergleich

                if (arr.get(i) > arr.get(i + 1)) {
                    int temp = arr.get(i);
                    arr.set(i, arr.get(i + 1));
                    arr.set(i + 1, temp);
                    sorted = false; // Tausch fand statt -> nicht sortiert
                    changed.add(i);
                    changed.add(i + 1);
                    steps++; // Zähle Swap (optional)
                }
                // Sende Zustand nach jedem Vergleich/Swap
                stepCallback.accept(new SortStep(new ArrayList<>(arr), accessed, changed));
            }

            // Gerader Durchlauf (Vergleiche Indizes 0&1, 2&3, ...)
            for (int i = 0; i < n - 1; i += 2) {
                Set<Integer> accessed = new HashSet<>();
                Set<Integer> changed = new HashSet<>();
                accessed.add(i);
                accessed.add(i + 1);
                steps++; // Zähle Vergleich

                if (arr.get(i) > arr.get(i + 1)) {
                    int temp = arr.get(i);
                    arr.set(i, arr.get(i + 1));
                    arr.set(i + 1, temp);
                    sorted = false; // Tausch fand statt -> nicht sortiert
                    changed.add(i);
                    changed.add(i + 1);
                    steps++; // Zähle Swap (optional)
                }
                // Sende Zustand nach jedem Vergleich/Swap
                stepCallback.accept(new SortStep(new ArrayList<>(arr), accessed, changed));
            }
        }
        // Sende finalen Zustand (kann redundant sein)
        stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), Collections.emptySet()));
    }
}