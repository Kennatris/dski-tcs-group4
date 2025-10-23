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
public class SelectionSort implements SortingAlgorithm {
    private long steps;
    private List<Integer> dataSet;

    @Override
    public String getName() {
        return "SelectionSort";
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
        selectionSort(copy, (SortStep step) -> {});
        return copy;
    }

    @Override
    public void sortWithCallback(List<Integer> input, Consumer<SortStep> stepCallback) {
        dataSet = new ArrayList<>(input);
        steps = 0;
        // Sende initialen Zustand
        stepCallback.accept(new SortStep(new ArrayList<>(input), Collections.emptySet(), Collections.emptySet()));
        selectionSort(input, stepCallback);
        // Sende finalen Zustand
        stepCallback.accept(new SortStep(new ArrayList<>(input), Collections.emptySet(), Collections.emptySet()));
    }

    /**
     * Führt SelectionSort "in-place" aus. Sucht in jedem Durchlauf das Minimum
     * im unsortierten Teil und tauscht es an den Anfang dieses Teils.
     * Meldet jeden Vergleich bei der Suche und jeden finalen Tausch.
     * @param arr Die zu sortierende Liste.
     * @param stepCallback Der Callback für die Visualisierung.
     */
    private void selectionSort(List<Integer> arr, Consumer<SortStep> stepCallback) {
        int n = arr.size();
        if (n <= 1) {
            stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), Collections.emptySet())); // Endzustand
            return;
        }


        // Äußere Schleife: Geht durch das Array, um die Position für das nächste Minimum zu bestimmen
        for (int i = 0; i < n - 1; i++) {
            // Annahme: Das aktuelle Element ist das Minimum
            int minIndex = i;
            Set<Integer> accessedInSearch = new HashSet<>(); // Sammelt Zugriffe während der Suche
            accessedInSearch.add(i); // Erster Zugriff für minIndex

            // Sende Zustand zu Beginn der Suche für Position 'i'
            stepCallback.accept(new SortStep(new ArrayList<>(arr), new HashSet<>(accessedInSearch), Collections.emptySet()));


            // Innere Schleife: Sucht das tatsächliche Minimum im restlichen unsortierten Teil (ab i+1)
            for (int j = i + 1; j < n; j++) {
                Set<Integer> accessedCurrent = new HashSet<>();
                accessedCurrent.add(j);        // Index j wird gelesen
                accessedCurrent.add(minIndex); // Aktuelles Minimum wird gelesen
                accessedInSearch.add(j);       // Füge zum Gesamtset der Suche hinzu
                steps++; // Zähle Vergleich

                // Sende Zustand *vor* der potenziellen Änderung von minIndex
                stepCallback.accept(new SortStep(new ArrayList<>(arr), new HashSet<>(accessedCurrent), Collections.emptySet()));


                if (arr.get(j) < arr.get(minIndex)) {
                    minIndex = j; // Neuer Minimum-Index gefunden
                    // Optional: Sende Zustand nach Änderung von minIndex, um neuen Kandidaten zu zeigen
                    stepCallback.accept(new SortStep(new ArrayList<>(arr), new HashSet<>(accessedCurrent), Collections.emptySet()));
                }
            }

            // Tausche das gefundene Minimum (arr[minIndex]) mit dem ersten Element
            // des unsortierten Teils (arr[i]), *wenn* sie nicht schon gleich sind.
            if (minIndex != i) {
                Set<Integer> accessedSwap = new HashSet<>();
                Set<Integer> changedSwap = new HashSet<>();
                accessedSwap.add(i);
                accessedSwap.add(minIndex);

                int temp = arr.get(i);
                arr.set(i, arr.get(minIndex));
                arr.set(minIndex, temp);
                changedSwap.add(i);
                changedSwap.add(minIndex);
                steps++; // Zähle Swap

                // Sende Zustand nach dem Swap
                stepCallback.accept(new SortStep(new ArrayList<>(arr), accessedSwap, changedSwap));
            } else {
                // Sende Zustand auch wenn kein Swap, um Abschluss der Iteration zu zeigen
                // Zeige alle Indizes, die in der Suche betrachtet wurden
                Set<Integer> finalAccessed = IntStream.range(i, n).boxed().collect(Collectors.toSet());
                stepCallback.accept(new SortStep(new ArrayList<>(arr), finalAccessed, Collections.emptySet()));
            }
        }
    }
}