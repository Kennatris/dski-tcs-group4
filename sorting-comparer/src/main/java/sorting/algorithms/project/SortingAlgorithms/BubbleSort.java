package sorting.algorithms.project.SortingAlgorithms;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import sorting.algorithms.project.dto.SortStep; // Import SortStep

@Component
public class BubbleSort implements SortingAlgorithm {
    long steps;
    List<Integer> dataSet;

    /**
     * @return Der Name des Algorithmus: "BubbleSort".
     */
    @Override
    public String getName() {
        return "BubbleSort";
    }

    /**
     * @return Die Zeitkomplexität im Worst Case: O(n²).
     */
    @Override
    public String getWorstCase() { return "O(n²)"; }

    /**
     * @return Die Zeitkomplexität im Average Case: O(n²).
     */
    @Override
    public String getAverageCase() { return "O(n²)"; }

    /**
     * @return Die Zeitkomplexität im Best Case: O(n).
     */
    @Override
    public String getBestCase() { return "O(n)"; }

    /**
     * @return Die Anzahl der Schritte (Vergleiche + Swaps) des letzten Sortiervorgangs.
     */
    @Override
    public long getSteps() { return steps; }

    /**
     * @return Der ursprüngliche Datensatz, der zuletzt sortiert wurde.
     */
    @Override
    public List<Integer> getData() {
        // Sicherstellen, dass dataSet nicht null ist
        return dataSet != null ? dataSet : SortingAlgorithm.super.getData();
    }

    /**
     * Sortiert eine Kopie der Eingabeliste mithilfe von BubbleSort.
     * @param input Die zu sortierende Liste.
     * @return Eine neue, sortierte Liste.
     */
    @Override
    public List<Integer> sort(List<Integer> input) {
        List<Integer> copy = new ArrayList<>(input);
        dataSet = new ArrayList<>(copy); // Original für getData speichern
        steps = 0; // Steps für diesen Lauf zurücksetzen
        // Ruft die Callback-Version mit einem leeren Consumer auf
        bubbleSort(copy, (SortStep step) -> {});
        return copy;
    }

    /**
     * Sortiert die Eingabeliste "in-place" mithilfe von BubbleSort und sendet
     * jeden Schritt (Vergleich oder Swap) an den Callback.
     * @param input Die zu sortierende Liste (wird modifiziert).
     * @param stepCallback Der Consumer, der jeden SortStep empfängt.
     */
    @Override
    public void sortWithCallback(List<Integer> input, Consumer<SortStep> stepCallback) {
        dataSet = new ArrayList<>(input); // Original für getData speichern
        steps = 0; // Steps für diesen Lauf zurücksetzen
        // Sende initialen Zustand
        stepCallback.accept(new SortStep(new ArrayList<>(input), Collections.emptySet(), Collections.emptySet()));
        bubbleSort(input, stepCallback); // Führt die eigentliche Sortierung durch
        // Sende finalen Zustand
        stepCallback.accept(new SortStep(new ArrayList<>(input), Collections.emptySet(), Collections.emptySet()));
    }

    /**
     * Führt die BubbleSort-Logik "in-place" aus und meldet jeden Vergleichs-/Swap-Schritt
     * mit detaillierten Index-Informationen an den Callback.
     * @param arr Die zu sortierende Liste (wird modifiziert).
     * @param stepCallback Der Consumer, der jeden SortStep empfängt.
     */
    public void bubbleSort(List<Integer> arr, Consumer<SortStep> stepCallback) {
        // Steps werden in sortWithCallback oder sort initialisiert
        int n = arr.size();
        if (n <= 1) return; // Bereits sortiert oder leer

        boolean swapped;
        do {
            swapped = false;
            // Der Bereich [n, arr.size()-1] ist bereits sortiert
            for (int i = 1; i < n; i++) {
                // --- Tracking der Indizes ---
                Set<Integer> accessed = new HashSet<>();
                Set<Integer> changed = new HashSet<>();
                accessed.add(i - 1); // Index für arr.get(i-1)
                accessed.add(i);     // Index für arr.get(i)
                steps++;             // Zähle den Vergleich

                // Sende Zustand *vor* potenziellem Swap
                stepCallback.accept(new SortStep(new ArrayList<>(arr), new HashSet<>(accessed), new HashSet<>(changed)));


                if (arr.get(i - 1) > arr.get(i)) {
                    // Tausche Elemente
                    int t = arr.get(i - 1);
                    arr.set(i - 1, arr.get(i));
                    arr.set(i, t);
                    swapped = true;
                    // Bei Swap sind beide Indizes geändert
                    changed.add(i - 1);
                    changed.add(i);
                    steps++; // Zähle Swap als zusätzlichen Schritt (optional)

                    // Sende Zustand *nach* dem Swap
                    stepCallback.accept(new SortStep(new ArrayList<>(arr), accessed, changed));
                }
                // --- Sende SortStep nach dem Vergleich (auch wenn nicht getauscht wurde) ---
                // Entfernt, da schon vor/nach Swap gesendet wird
                // stepCallback.accept(new SortStep(new ArrayList<>(arr), accessed, changed));
            }
            n--; // Reduziere den zu betrachtenden Bereich, da das größte Element am Ende ist
        } while (swapped); // Wiederhole, wenn im letzten Durchlauf getauscht wurde
    }
}