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
public class HeapSort implements SortingAlgorithm {
    private long steps;
    private List<Integer> dataSet;

    @Override
    public String getName() {
        return "HeapSort";
    }

    @Override
    public String getWorstCase() {
        return "O(n log n)";
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
        heapSort(copy, (SortStep step) -> {});
        return copy;
    }

    @Override
    public void sortWithCallback(List<Integer> input, Consumer<SortStep> stepCallback) {
        dataSet = new ArrayList<>(input);
        steps = 0;
        // Sende initialen Zustand
        stepCallback.accept(new SortStep(new ArrayList<>(input), Collections.emptySet(), Collections.emptySet()));
        heapSort(input, stepCallback);
    }

    /**
     * Führt HeapSort "in-place" aus. Baut zuerst einen Max-Heap auf
     * und extrahiert dann nacheinander das größte Element.
     * @param arr Die zu sortierende Liste.
     * @param stepCallback Der Callback für die Visualisierung.
     */
    private void heapSort(List<Integer> arr, Consumer<SortStep> stepCallback) {
        int n = arr.size();
        if (n <= 1) {
            stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), Collections.emptySet())); // Endzustand
            return;
        }


        // 1. Baue Max-Heap auf (heapify)
        makeMaxHeap(arr, n, stepCallback);

        // 2. Extrahiere Elemente vom Heap
        for (int i = n - 1; i > 0; i--) {
            Set<Integer> accessed = new HashSet<>();
            Set<Integer> changed = new HashSet<>();

            // Tausche Wurzel (größtes Element) mit dem letzten Element
            accessed.add(0); // Für get(0)
            accessed.add(i); // Für get(i)
            int temp = arr.get(0);
            arr.set(0, arr.get(i));
            arr.set(i, temp);
            changed.add(0);
            changed.add(i);
            steps++; // Zähle Swap

            // Sende Zustand nach dem Swap
            stepCallback.accept(new SortStep(new ArrayList<>(arr), accessed, changed));

            // Stelle Heap-Eigenschaft für reduzierten Heap wieder her
            heapAdjust(arr, 0, i, stepCallback); // Größe ist jetzt 'i'
        }
        // Sende finalen Zustand (kann redundant sein)
        stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), Collections.emptySet()));
    }

    /**
     * Erstellt einen Max-Heap aus dem gegebenen Array.
     * @param arr Die Liste, die in einen Heap umgewandelt wird.
     * @param n Die Größe des Heaps (oft arr.size()).
     * @param stepCallback Der Callback für die Visualisierung.
     */
    private void makeMaxHeap(List<Integer> arr, int n, Consumer<SortStep> stepCallback) {
        // Starte beim letzten Nicht-Blattknoten und gehe rückwärts
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapAdjust(arr, i, n, stepCallback);
        }
    }

    /**
     * Stellt die Max-Heap-Eigenschaft für einen Teilbaum sicher, dessen Wurzel bei Index 'i' liegt.
     * Geht davon aus, dass die Teilbäume unter 'i' bereits Heaps sind. (Max-Heapify)
     * @param arr Die Liste, die den Heap repräsentiert.
     * @param i Der Index der Wurzel des Teilbaums.
     * @param n Die Größe des Heaps.
     * @param stepCallback Der Callback für die Visualisierung.
     */
    private void heapAdjust(List<Integer> arr, int i, int n, Consumer<SortStep> stepCallback) {
        Set<Integer> accessed = new HashSet<>();
        Set<Integer> changed = new HashSet<>();

        int largest = i; // Initialisiere größten als Wurzel
        int left = 2 * i + 1; // Linkes Kind
        int right = 2 * i + 2; // Rechtes Kind

        accessed.add(i); // Wurzel wird gelesen

        // Wenn linkes Kind existiert und größer als Wurzel ist
        if (left < n) {
            accessed.add(left); // Linkes Kind wird gelesen
            steps++; // Zähle Vergleich
            if (arr.get(left) > arr.get(largest)) {
                largest = left;
            }
        }

        // Wenn rechtes Kind existiert und größer als bisher größtes ist
        if (right < n) {
            accessed.add(right); // Rechtes Kind wird gelesen
            // Zugriff auf 'largest' Index wurde schon gezählt
            steps++; // Zähle Vergleich
            if (arr.get(right) > arr.get(largest)) {
                largest = right;
            }
        }

        // Sende Zustand nach den Vergleichen
        stepCallback.accept(new SortStep(new ArrayList<>(arr), new HashSet<>(accessed), new HashSet<>(changed)));


        // Wenn größtes nicht die Wurzel ist
        if (largest != i) {
            accessed.add(largest); // 'largest' wird für get benötigt
            accessed.add(i);       // 'i' wird für get benötigt
            // Tausche Wurzel mit größtem Kind
            int swap = arr.get(i);
            arr.set(i, arr.get(largest));
            arr.set(largest, swap);
            changed.add(i);
            changed.add(largest);
            steps++; // Zähle Swap

            // Sende Zustand nach dem Swap
            stepCallback.accept(new SortStep(new ArrayList<>(arr), accessed, changed));

            // Rekursiv Heap-Eigenschaft für betroffenen Teilbaum wiederherstellen
            heapAdjust(arr, largest, n, stepCallback);
        }
        // Sende Zustand auch wenn kein Swap stattfand, um den Abschluss des Adjusts zu zeigen
        else if (changed.isEmpty()) {
            stepCallback.accept(new SortStep(new ArrayList<>(arr), accessed, changed));
        }
    }
}