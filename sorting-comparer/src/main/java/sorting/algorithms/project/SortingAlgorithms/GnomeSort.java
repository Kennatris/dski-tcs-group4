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
        // Sicherstellen, dass dataSet nicht null ist
        return dataSet != null ? dataSet : SortingAlgorithm.super.getData();
    }

    @Override
    public List<Integer> sort(List<Integer> input) {
        List<Integer> copy = new ArrayList<>(input);
        dataSet = new ArrayList<>(copy);
        steps = 0;
        gnomeSort(copy, (SortStep step) -> {});
        return copy;
    }

    @Override
    public void sortWithCallback(List<Integer> input, Consumer<SortStep> stepCallback) {
        dataSet = new ArrayList<>(input);
        steps = 0;
        // Sende initialen Zustand
        stepCallback.accept(new SortStep(new ArrayList<>(input), Collections.emptySet(), Collections.emptySet()));
        gnomeSort(input, stepCallback);
    }

    /**
     * Führt GnomeSort (Stupid Sort) "in-place" aus.
     * Meldet jeden Vergleich und jeden Swap.
     * @param arr Die zu sortierende Liste.
     * @param stepCallback Der Callback für die Visualisierung.
     */
    private void gnomeSort(List<Integer> arr, Consumer<SortStep> stepCallback) {
        int index = 0; // Starte bei 0 für den ersten Vergleich bei index 1
        int n = arr.size();

        while (index < n) {
            Set<Integer> accessed = new HashSet<>();
            Set<Integer> changed = new HashSet<>();

            if (index == 0) {
                // Am Anfang gibt es nichts zu vergleichen, gehe einfach weiter
                accessed.add(index); // Markiere aktuellen Index als "betrachtet"
                index++;
            } else {
                accessed.add(index - 1); // Für get(index - 1)
                accessed.add(index);     // Für get(index)
                steps++; // Zähle Vergleich

                if (arr.get(index - 1) <= arr.get(index)) {
                    // Elemente sind in Ordnung, gehe einen Schritt vorwärts
                    index++;
                } else {
                    // Elemente tauschen
                    int temp = arr.get(index - 1);
                    arr.set(index - 1, arr.get(index));
                    arr.set(index, temp);
                    changed.add(index - 1);
                    changed.add(index);
                    steps++; // Zähle Swap (optional)
                    // Gehe einen Schritt zurück
                    index--;
                }
            }
            // Sende Zustand nach der Aktion (Vorrücken oder Tauschen/Zurückgehen)
            stepCallback.accept(new SortStep(new ArrayList<>(arr), accessed, changed));
        }
        // Sende finalen Zustand (kann redundant sein)
        stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), Collections.emptySet()));
    }
}