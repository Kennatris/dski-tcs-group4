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
        // Sicherstellen, dass dataSet nicht null ist
        return dataSet != null ? dataSet : SortingAlgorithm.super.getData();
    }

    @Override
    public List<Integer> sort(List<Integer> input) {
        List<Integer> copy = new ArrayList<>(input);
        dataSet = new ArrayList<>(copy);
        steps = 0;
        combSort(copy, (SortStep step) -> {});
        return copy;
    }

    @Override
    public void sortWithCallback(List<Integer> input, Consumer<SortStep> stepCallback) {
        dataSet = new ArrayList<>(input);
        steps = 0;
        // Sende initialen Zustand
        stepCallback.accept(new SortStep(new ArrayList<>(input), Collections.emptySet(), Collections.emptySet()));
        combSort(input, stepCallback);
    }

    /**
     * Führt CombSort "in-place" aus und meldet jeden Vergleichs-/Swap-Schritt.
     * @param arr Die zu sortierende Liste.
     * @param stepCallback Der Callback für die Visualisierung.
     */
    private void combSort(List<Integer> arr, Consumer<SortStep> stepCallback) {
        int n = arr.size();
        int gap = n;
        boolean swapped = true;

        while (gap != 1 || swapped) {
            gap = getNextGap(gap);
            swapped = false;

            for (int i = 0; i < n - gap; i++) {
                Set<Integer> accessed = new HashSet<>();
                Set<Integer> changed = new HashSet<>();
                accessed.add(i);
                accessed.add(i + gap);
                steps++; // Zähle Vergleich

                if (arr.get(i) > arr.get(i + gap)) {
                    int temp = arr.get(i);
                    arr.set(i, arr.get(i + gap));
                    arr.set(i + gap, temp);
                    swapped = true;
                    changed.add(i);
                    changed.add(i + gap);
                    steps++; // Zähle Swap (optional)
                }
                // Sende Zustand nach jedem Vergleich/Swap
                stepCallback.accept(new SortStep(new ArrayList<>(arr), accessed, changed));
            }
        }
        // Sende finalen Zustand (kann redundant sein)
        stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), Collections.emptySet()));
    }

    /**
     * Berechnet die nächste Lückengröße (gap) für den CombSort Algorithmus.
     * @param gap Die aktuelle Lückengröße.
     * @return Die neue, reduzierte Lückengröße (mindestens 1).
     */
    private int getNextGap(int gap) {
        // Verkleinere den Gap um den Shrink Factor (ca. 1.3)
        gap = (gap * 10) / 13;
        if (gap < 1) {
            return 1; // Gap darf nicht kleiner als 1 sein
        }
        return gap;
    }
}