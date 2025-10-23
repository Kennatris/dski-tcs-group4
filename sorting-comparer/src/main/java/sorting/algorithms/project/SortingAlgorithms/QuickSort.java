package sorting.algorithms.project.SortingAlgorithms;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import sorting.algorithms.project.dto.SortStep;

@Component
public class QuickSort implements SortingAlgorithm {
    private long steps;
    private List<Integer> dataSet;

    @Override
    public String getName() {
        return "QuickSort";
    }

    @Override
    public String getWorstCase() {
        return "O(n²)";
    }

    @Override
    public String getAverageCase() {
        return "O(n log n)";
    }

    @Override
    public String getBestCase() {
        return "O(n log n)";
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
        quickSortRecursive(copy, 0, copy.size() - 1, (SortStep step) -> {});
        return copy;
    }

    @Override
    public void sortWithCallback(List<Integer> input, Consumer<SortStep> stepCallback) {
        dataSet = new ArrayList<>(input);
        steps = 0;
        // Sende initialen Zustand
        stepCallback.accept(new SortStep(new ArrayList<>(input), Collections.emptySet(), Collections.emptySet()));
        quickSortRecursive(input, 0, input.size() - 1, stepCallback);
        // Sende finalen Zustand nach Abschluss
        stepCallback.accept(new SortStep(new ArrayList<>(input), Collections.emptySet(), Collections.emptySet()));
    }

    /**
     * Startet den rekursiven QuickSort-Prozess "in-place".
     * @param arr Die zu sortierende Liste.
     * @param lower Der Startindex des aktuellen Subarrays.
     * @param upper Der Endindex des aktuellen Subarrays.
     * @param stepCallback Der Callback für die Visualisierung.
     */
    private void quickSortRecursive(List<Integer> arr, int lower, int upper, Consumer<SortStep> stepCallback) {
        // Optional: Schritt senden, der den aktuellen Rekursionsbereich zeigt
        // Set<Integer> accessedRange = IntStream.rangeClosed(lower, upper).filter(i -> i >= 0 && i < arr.size()).boxed().collect(Collectors.toSet());
        // stepCallback.accept(new SortStep(new ArrayList<>(arr), accessedRange, Collections.emptySet()));

        if (lower >= upper || lower < 0 || upper >= arr.size()) {
            return; // Basisfall oder ungültiger Bereich
        }

        // Partitioniere und erhalte den Pivot-Index
        int p = partition(arr, lower, upper, stepCallback);

        // Rekursiv für linke und rechte Teile sortieren
        quickSortRecursive(arr, lower, p - 1, stepCallback);
        quickSortRecursive(arr, p + 1, upper, stepCallback);
    }

    /**
     * Partitioniert einen Teil der Liste für QuickSort und meldet Schritte.
     * Verwendet das letzte Element als Pivot.
     * @param arr Die zu partitionierende Liste.
     * @param lower Startindex des Bereichs.
     * @param upper Endindex des Bereichs (Pivot-Index).
     * @param stepCallback Der Callback für die Visualisierung.
     * @return Der finale Index des Pivots.
     */
    private int partition(List<Integer> arr, int lower, int upper, Consumer<SortStep> stepCallback) {
        Set<Integer> accessed = new HashSet<>();
        Set<Integer> changed = new HashSet<>();

        // Wähle Pivot (letztes Element)
        accessed.add(upper);
        int pivotValue = arr.get(upper);
        // Zeige Pivot-Auswahl
        stepCallback.accept(new SortStep(new ArrayList<>(arr), new HashSet<>(accessed), new HashSet<>(changed)));

        int i = lower - 1; // Index des letzten Elements, das kleiner als der Pivot ist

        // Durchlaufe den Bereich (außer dem Pivot selbst)
        for (int j = lower; j < upper; j++) {
            accessed.clear();
            changed.clear();
            accessed.add(j);      // Element j wird gelesen/verglichen
            accessed.add(upper);  // Pivot wird (implizit) verglichen

            steps++; // Zähle Vergleich

            // Sende Zustand *vor* dem potenziellen Swap
            stepCallback.accept(new SortStep(new ArrayList<>(arr), new HashSet<>(accessed), new HashSet<>(changed)));

            if (arr.get(j) < pivotValue) {
                i++;
                // Tausche arr[i] mit arr[j]
                accessed.add(i); // Wird für get/set benötigt
                // accessed.add(j); // Schon hinzugefügt
                int temp = arr.get(i);
                arr.set(i, arr.get(j));
                arr.set(j, temp);
                changed.add(i);
                changed.add(j);
                steps++; // Zähle Swap (optional)
                // Sende Zustand *nach* dem Swap
                stepCallback.accept(new SortStep(new ArrayList<>(arr), new HashSet<>(accessed), new HashSet<>(changed)));
            }
        }

        // Platziere den Pivot an der korrekten Position (nach dem letzten kleineren Element)
        accessed.clear();
        changed.clear();
        int pivotFinalIndex = i + 1;

        // Tausche Pivot (arr[upper]) mit arr[pivotFinalIndex]
        if (pivotFinalIndex < arr.size() && upper < arr.size()) { // Grenzen prüfen
            accessed.add(pivotFinalIndex); // Für get/set
            accessed.add(upper);          // Für get/set
            int temp = arr.get(pivotFinalIndex);
            arr.set(pivotFinalIndex, arr.get(upper));
            arr.set(upper, temp);
            changed.add(pivotFinalIndex);
            changed.add(upper);
            steps++; // Zähle Swap
            // Sende Zustand nach dem finalen Pivot-Swap
            stepCallback.accept(new SortStep(new ArrayList<>(arr), accessed, changed));
        } else {
            // Sende Zustand, auch wenn kein Tausch nötig/möglich war
            stepCallback.accept(new SortStep(new ArrayList<>(arr), accessed, changed));
        }

        return pivotFinalIndex;
    }
}