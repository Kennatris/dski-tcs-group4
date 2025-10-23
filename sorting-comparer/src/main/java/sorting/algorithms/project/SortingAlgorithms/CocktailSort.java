package sorting.algorithms.project.SortingAlgorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import org.springframework.stereotype.Component;
import sorting.algorithms.project.dto.SortStep;

@Component
public class CocktailSort implements SortingAlgorithm {

    private long steps;
    private List<Integer> dataSet;

    @Override
    public String getName() {
        return "CocktailSort";
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
        List<Integer> arr = new ArrayList<>(input);
        dataSet = new ArrayList<>(arr);
        steps = 0;
        sortWithCallback(arr, (SortStep step) -> {});
        return arr;
    }

    @Override
    public void sortWithCallback(List<Integer> arr, Consumer<SortStep> stepCallback) {
        dataSet = new ArrayList<>(arr);
        steps = 0;
        boolean swapped = true;
        int start = 0;
        int end = arr.size();

        // Sende initialen Zustand
        stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), Collections.emptySet()));

        while (swapped) {
            swapped = false;

            // Vorwärtsdurchlauf (wie BubbleSort)
            for (int i = start; i < end - 1; i++) {
                Set<Integer> accessed = new HashSet<>();
                Set<Integer> changed = new HashSet<>();
                accessed.add(i);
                accessed.add(i + 1);
                steps++; // Zähle Vergleich

                if (arr.get(i) > arr.get(i + 1)) {
                    int temp = arr.get(i);
                    arr.set(i, arr.get(i + 1));
                    arr.set(i + 1, temp);
                    swapped = true;
                    changed.add(i);
                    changed.add(i + 1);
                    steps++; // Zähle Swap (optional)
                }
                // Sende Zustand nach jedem Vergleich/Swap
                stepCallback.accept(new SortStep(new ArrayList<>(arr), accessed, changed));
            }

            if (!swapped) break; // Wenn nichts getauscht wurde, ist die Liste sortiert

            swapped = false; // Zurücksetzen für den Rückwärtsdurchlauf
            end--; // Das letzte Element ist jetzt an der richtigen Stelle

            // Rückwärtsdurchlauf
            for (int i = end - 1; i >= start; i--) {
                Set<Integer> accessed = new HashSet<>();
                Set<Integer> changed = new HashSet<>();
                // Vergleich zwischen i und i+1
                if (i < arr.size() - 1) { // Stelle sicher, dass i+1 gültig ist
                    accessed.add(i);
                    accessed.add(i + 1);
                    steps++; // Zähle Vergleich

                    if (arr.get(i) > arr.get(i + 1)) {
                        int temp = arr.get(i);
                        arr.set(i, arr.get(i + 1));
                        arr.set(i + 1, temp);
                        swapped = true;
                        changed.add(i);
                        changed.add(i + 1);
                        steps++; // Zähle Swap (optional)
                    }
                } else {
                    // Wenn i das letzte Element ist, gibt es keinen Vergleich nach rechts
                    // Sende trotzdem einen Schritt, um den Fortschritt zu zeigen
                    accessed.add(i);
                }
                // Sende Zustand nach jedem Vergleich/Swap
                stepCallback.accept(new SortStep(new ArrayList<>(arr), accessed, changed));
            }
            start++; // Das erste Element ist jetzt an der richtigen Stelle
        }
        // Sende finalen Zustand (kann redundant sein, falls letzter Schritt schon gesendet)
        stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), Collections.emptySet()));
    }
}