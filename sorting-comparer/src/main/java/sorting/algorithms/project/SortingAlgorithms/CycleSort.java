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
public class CycleSort implements SortingAlgorithm {

    private long steps;
    private List<Integer> dataSet;

    @Override
    public String getName() {
        return "CycleSort";
    }

    @Override
    public String getBestCase() {
        return "O(n²)";
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
        cycleSort(copy, (SortStep step) -> {});
        return copy;
    }

    @Override
    public void sortWithCallback(List<Integer> input, Consumer<SortStep> stepCallback) {
        dataSet = new ArrayList<>(input);
        steps = 0;
        // Sende initialen Zustand
        stepCallback.accept(new SortStep(new ArrayList<>(input), Collections.emptySet(), Collections.emptySet()));
        cycleSort(input, stepCallback);
    }

    /**
     * Führt CycleSort "in-place" aus und meldet jeden Vergleich und Swap.
     * @param arr Die zu sortierende Liste.
     * @param stepCallback Der Callback für die Visualisierung.
     */
    private void cycleSort(List<Integer> arr, Consumer<SortStep> stepCallback) {
        int n = arr.size();
        if (n <= 1) {
            stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), Collections.emptySet())); // Endzustand
            return;
        }


        // Durchlaufe Array, um Zyklen zu finden
        for (int cycle_start = 0; cycle_start <= n - 2; cycle_start++) {
            Set<Integer> accessedInCycle = new HashSet<>(); // Sammelt alle Zugriffe innerhalb eines Zyklus
            Set<Integer> changedInCycle = new HashSet<>();  // Sammelt alle Änderungen innerhalb eines Zyklus

            int item = arr.get(cycle_start);
            accessedInCycle.add(cycle_start);

            // Finde die Position, an der das Element stehen sollte
            int pos = cycle_start;
            for (int i = cycle_start + 1; i < n; i++) {
                accessedInCycle.add(i); // Index i wird gelesen
                steps++; // Zähle Vergleich
                // Sende Zustand vor dem pos++
                stepCallback.accept(new SortStep(new ArrayList<>(arr), new HashSet<>(accessedInCycle), new HashSet<>(changedInCycle)));
                if (arr.get(i) < item) {
                    pos++;
                }
            }


            // Wenn das Element bereits an der richtigen Position ist, überspringe
            if (pos == cycle_start) {
                // Sende den Zustand, auch wenn nichts passiert
                stepCallback.accept(new SortStep(new ArrayList<>(arr), new HashSet<>(accessedInCycle), new HashSet<>(changedInCycle)));
                continue;
            }

            // Überspringe Duplikate
            while (pos < n && item == arr.get(pos)) {
                accessedInCycle.add(pos); // Index pos wird gelesen
                steps++; // Zähle Vergleich
                // Sende Zustand während des Überspringens
                stepCallback.accept(new SortStep(new ArrayList<>(arr), new HashSet<>(accessedInCycle), new HashSet<>(changedInCycle)));
                pos++;
            }


            // Platziere das Element an die richtige Position
            if (pos != cycle_start && pos < n) { // Sicherstellen, dass pos gültig ist
                accessedInCycle.add(pos); // Index pos für get/set
                int temp = item; // item wird temporär gehalten
                item = arr.get(pos); // item wird überschrieben (Lesezugriff auf pos)
                arr.set(pos, temp); // Schreibzugriff auf pos
                changedInCycle.add(pos);
                steps++; // Zähle den Swap als Schritt
                // Sende Zustand nach dem Platzieren
                stepCallback.accept(new SortStep(new ArrayList<>(arr), new HashSet<>(accessedInCycle), new HashSet<>(changedInCycle)));
            } else if (pos >=n ) {
                // Element gehört ans Ende, was hier nicht direkt behandelt wird
                // oder alle restlichen waren Duplikate. Sende Zustand und continue.
                stepCallback.accept(new SortStep(new ArrayList<>(arr), new HashSet<>(accessedInCycle), new HashSet<>(changedInCycle)));
                continue; // Zum nächsten cycle_start gehen
            }


            // Rotiere den Rest des Zyklus
            while (pos != cycle_start) {
                accessedInCycle.clear(); // Für die innere Schleife neu starten
                changedInCycle.clear();

                pos = cycle_start; // Setze pos zurück, um die *neue* korrekte Position für 'item' zu finden

                // Finde die korrekte Position für das *neue* 'item'
                for (int i = cycle_start + 1; i < n; i++) {
                    accessedInCycle.add(i); // Index i wird gelesen
                    steps++; // Zähle Vergleich
                    // Sende Zustand vor pos++
                    stepCallback.accept(new SortStep(new ArrayList<>(arr), new HashSet<>(accessedInCycle), new HashSet<>(changedInCycle)));
                    if (arr.get(i) < item) {
                        pos++;
                    }
                }

                // Überspringe Duplikate für das neue 'item'
                while (pos < n && item == arr.get(pos)) {
                    accessedInCycle.add(pos); // Index pos wird gelesen
                    steps++; // Zähle Vergleich
                    // Sende Zustand während des Überspringens
                    stepCallback.accept(new SortStep(new ArrayList<>(arr), new HashSet<>(accessedInCycle), new HashSet<>(changedInCycle)));
                    pos++;
                }

                // Platziere das Element, wenn es nicht das gleiche ist und pos gültig ist
                if (pos < n && item != arr.get(pos)) {
                    accessedInCycle.add(pos); // Index pos für get/set
                    int temp = item;
                    item = arr.get(pos);
                    arr.set(pos, temp);
                    changedInCycle.add(pos);
                    steps++; // Zähle Swap
                    // Sende Zustand nach dem Platzieren
                    stepCallback.accept(new SortStep(new ArrayList<>(arr), new HashSet<>(accessedInCycle), new HashSet<>(changedInCycle)));
                } else if (pos >= n || item == arr.get(pos)) {
                    // Entweder Position ungültig oder Duplikat erreicht Start -> Zyklus Ende?
                    // Sende Zustand und brich innere Schleife ab
                    stepCallback.accept(new SortStep(new ArrayList<>(arr), new HashSet<>(accessedInCycle), new HashSet<>(changedInCycle)));
                    break; // Verlasse die innere while-Schleife
                }
            }
        }
        // Sende finalen Zustand
        stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), Collections.emptySet()));
    }
}