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
public class PancakeSort implements SortingAlgorithm {
    private long steps;
    private List<Integer> dataSet;

    @Override
    public String getName() {
        return "PancakeSort";
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
        return "O(n²)"; // Selbst im besten Fall oft O(n²) Flips
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
        pancakeSort(copy, (SortStep step) -> {});
        return copy;
    }

    @Override
    public void sortWithCallback(List<Integer> input, Consumer<SortStep> stepCallback) {
        dataSet = new ArrayList<>(input);
        steps = 0;
        // Sende initialen Zustand
        stepCallback.accept(new SortStep(new ArrayList<>(input), Collections.emptySet(), Collections.emptySet()));
        pancakeSort(input, stepCallback);
    }

    /**
     * Führt PancakeSort "in-place" aus. Sucht das Maximum im unsortierten Teil,
     * flippt es an den Anfang und flippt es dann an die korrekte Endposition.
     * Meldet jeden Vergleich in findMax und jeden Swap in flip.
     * @param arr Die zu sortierende Liste.
     * @param stepCallback Der Callback für die Visualisierung.
     */
    private void pancakeSort(List<Integer> arr, Consumer<SortStep> stepCallback) {
        int n = arr.size();
        if (n <= 1) {
            stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), Collections.emptySet())); // Endzustand
            return;
        }


        // curr_size ist die Größe des aktuell betrachteten (unsortierten) Präfixes
        for (int curr_size = n; curr_size > 1; --curr_size) {
            // Finde den Index des Maximums im Bereich arr[0...curr_size-1]
            int mi = findMax(arr, curr_size, stepCallback);

            // Wenn Maximum nicht bereits am Ende des aktuellen Bereichs ist
            if (mi != curr_size - 1) {
                // 1. Bringe Maximum an den Anfang (Index 0)
                if (mi != 0) {
                    flip(arr, mi, stepCallback);
                }
                // 2. Bringe Maximum vom Anfang an die korrekte Endposition (curr_size - 1)
                flip(arr, curr_size - 1, stepCallback);
            }
            // Optional: Sende Zustand nach Abschluss einer curr_size Iteration
            // stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), Collections.emptySet()));
        }
        // Sende finalen Zustand
        stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), Collections.emptySet()));
    }

    /**
     * Dreht den Teil des Arrays von Index 0 bis Index 'i' (inklusiv) um.
     * Meldet jeden einzelnen Swap während des Umdrehens.
     * @param arr Die Liste.
     * @param i Der Endindex des zu flippenden Präfixes.
     * @param stepCallback Der Callback für die Visualisierung.
     */
    private void flip(List<Integer> arr, int i, Consumer<SortStep> stepCallback) {
        int start = 0;
        while (start < i) {
            Set<Integer> accessed = new HashSet<>();
            Set<Integer> changed = new HashSet<>();
            accessed.add(start);
            accessed.add(i);

            int temp = arr.get(start);
            arr.set(start, arr.get(i));
            arr.set(i, temp);
            changed.add(start);
            changed.add(i);
            steps++; // Zähle Swap

            // Sende Zustand nach jedem Swap im Flip
            stepCallback.accept(new SortStep(new ArrayList<>(arr), accessed, changed));

            start++;
            i--;
        }
    }

    /**
     * Findet den Index des größten Elements im Array-Präfix der Länge 'n'.
     * Meldet jeden Vergleich.
     * @param arr Die Liste.
     * @param n Die Länge des zu durchsuchenden Präfixes.
     * @param stepCallback Der Callback für die Visualisierung.
     * @return Der Index des Maximums im Bereich [0, n-1].
     */
    private int findMax(List<Integer> arr, int n, Consumer<SortStep> stepCallback) {
        if (n <= 0) return -1; // Ungültiger Bereich

        int maxIndex = 0;
        Set<Integer> accessedOverall = new HashSet<>(); // Sammelt alle Zugriffe in dieser Suche
        accessedOverall.add(0); // Erster Zugriff

        // Sende Zustand beim Start der Suche
        stepCallback.accept(new SortStep(new ArrayList<>(arr), new HashSet<>(accessedOverall), Collections.emptySet()));

        for (int i = 1; i < n; i++) {
            Set<Integer> accessedCurrent = new HashSet<>();
            accessedCurrent.add(i);        // Aktueller Index wird gelesen
            accessedCurrent.add(maxIndex); // Aktuelles Max wird gelesen
            accessedOverall.add(i);        // Zum Gesamtset hinzufügen
            steps++; // Zähle Vergleich

            // Sende Zustand *vor* der potenziellen Änderung von maxIndex
            stepCallback.accept(new SortStep(new ArrayList<>(arr), new HashSet<>(accessedCurrent), Collections.emptySet()));

            if (arr.get(i) > arr.get(maxIndex)) {
                maxIndex = i;
                // Sende Zustand *nach* der Änderung von maxIndex (optional, zeigt den neuen maxIndex an)
                stepCallback.accept(new SortStep(new ArrayList<>(arr), new HashSet<>(accessedCurrent), Collections.emptySet()));
            }
        }
        // Sende Zustand am Ende der Suche, alle Elemente wurden betrachtet
        // Set<Integer> finalAccessed = IntStream.range(0, n).boxed().collect(Collectors.toSet());
        // stepCallback.accept(new SortStep(new ArrayList<>(arr), finalAccessed, Collections.emptySet()));

        return maxIndex;
    }
}