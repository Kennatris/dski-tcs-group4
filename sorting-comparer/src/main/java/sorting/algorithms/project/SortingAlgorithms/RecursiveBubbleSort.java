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
public class RecursiveBubbleSort implements SortingAlgorithm {
    private long steps;
    private List<Integer> dataSet;

    @Override
    public String getName() {
        return "RecursiveBubbleSort";
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
        bubbleSortRecursive(copy, copy.size(), (SortStep step) -> {});
        return copy;
    }

    @Override
    public void sortWithCallback(List<Integer> input, Consumer<SortStep> stepCallback) {
        dataSet = new ArrayList<>(input);
        steps = 0;
        // Sende initialen Zustand
        stepCallback.accept(new SortStep(new ArrayList<>(input), Collections.emptySet(), Collections.emptySet()));
        bubbleSortRecursive(input, input.size(), stepCallback);
        // Sende finalen Zustand nach Abschluss
        stepCallback.accept(new SortStep(new ArrayList<>(input), Collections.emptySet(), Collections.emptySet()));
    }

    /**
     * Führt einen Durchlauf von BubbleSort für die ersten 'n' Elemente durch
     * und ruft sich dann rekursiv für 'n-1' auf. Meldet jeden Vergleich/Swap.
     * @param arr Die zu sortierende Liste.
     * @param n Die Anzahl der aktuell zu betrachtenden Elemente (Größe des Subproblems).
     * @param stepCallback Der Callback für die Visualisierung.
     */
    private void bubbleSortRecursive(List<Integer> arr, int n, Consumer<SortStep> stepCallback) {
        // Basisfall: Wenn nur noch 1 Element betrachtet wird, ist dieser Teil sortiert
        if (n <= 1) {
            return;
        }

        // Ein Durchlauf: Bringe das größte Element der ersten 'n' an Position n-1
        for (int i = 0; i < n - 1; i++) {
            Set<Integer> accessed = new HashSet<>();
            Set<Integer> changed = new HashSet<>();
            accessed.add(i);
            accessed.add(i + 1);
            steps++; // Zähle Vergleich

            if (arr.get(i) > arr.get(i + 1)) {
                int temp = arr.get(i);
                arr.set(i, arr.get(i + 1));
                arr.set(i + 1, temp);
                changed.add(i);
                changed.add(i + 1);
                steps++; // Zähle Swap (optional)
            }
            // Sende Zustand nach jedem Vergleich/Swap
            stepCallback.accept(new SortStep(new ArrayList<>(arr), accessed, changed));
        }

        // Rekursiver Aufruf für den Rest des Arrays (ohne das letzte, jetzt sortierte Element)
        bubbleSortRecursive(arr, n - 1, stepCallback);
    }
}