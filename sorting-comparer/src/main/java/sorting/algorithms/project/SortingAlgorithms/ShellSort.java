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
public class ShellSort implements SortingAlgorithm {
    private long steps;
    private List<Integer> dataSet;

    @Override
    public String getName() {
        return "ShellSort";
    }

    @Override
    public String getWorstCase() {
        // Abhängig von der Gap Sequence, hier eine übliche Annahme
        return "O(n^(3/2))"; // oder O(n log^2 n) für bessere Sequences
    }

    @Override
    public String getAverageCase() {
        // Ebenfalls abhängig von der Gap Sequence
        return "O(n^(3/2))"; // oder besser
    }

    @Override
    public String getBestCase() {
        return "O(n log n)"; // Für einige Gap Sequences
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
        shellSort(copy, (SortStep step) -> {});
        return copy;
    }

    @Override
    public void sortWithCallback(List<Integer> input, Consumer<SortStep> stepCallback) {
        dataSet = new ArrayList<>(input);
        steps = 0;
        // Sende initialen Zustand
        stepCallback.accept(new SortStep(new ArrayList<>(input), Collections.emptySet(), Collections.emptySet()));
        shellSort(input, stepCallback);
        // Sende finalen Zustand
        stepCallback.accept(new SortStep(new ArrayList<>(input), Collections.emptySet(), Collections.emptySet()));
    }

    /**
     * Führt ShellSort "in-place" mit einer Gap Sequence (hier: Knuth's Sequence h = 3*h + 1, dann /3) aus.
     * Führt h-sort (Insertion Sort mit Lücken) für abnehmende h durch.
     * Meldet jeden Vergleich, jede Verschiebung und jede Einfügung im h-sort.
     * @param arr Die zu sortierende Liste.
     * @param stepCallback Der Callback für die Visualisierung.
     */
    private void shellSort(List<Integer> arr, Consumer<SortStep> stepCallback) {
        int n = arr.size();
        if (n <= 1) {
            stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), Collections.emptySet())); // Endzustand
            return;
        }


        // Berechne initiale Lücke 'h' (Knuth's Sequence: 1, 4, 13, 40, ...)
        int h = 1;
        while (h < n / 3) {
            h = 3 * h + 1;
        }

        // Reduziere h, bis es 1 ist (letzter Durchlauf ist normaler Insertion Sort)
        while (h >= 1) {
            // Führe h-sort für diese Lücke durch
            // (Insertion Sort für jedes h-te Element)
            for (int i = h; i < n; i++) {
                Set<Integer> accessedOuter = new HashSet<>();
                Set<Integer> changedOuter = new HashSet<>();
                accessedOuter.add(i); // Für get(i)
                int temp = arr.get(i); // Das Element, das eingefügt wird
                int j = i;

                // Sende Zustand beim Auswählen von 'temp'
                stepCallback.accept(new SortStep(new ArrayList<>(arr), new HashSet<>(accessedOuter), new HashSet<>(changedOuter)));

                // Verschiebe frühere h-sortierte Elemente nach oben, bis die korrekte Position für temp gefunden ist
                while (j >= h) {
                    Set<Integer> accessedInner = new HashSet<>(accessedOuter);
                    Set<Integer> changedInner = new HashSet<>(changedOuter);
                    accessedInner.add(j - h); // Element links (mit Lücke h) wird gelesen
                    steps++; // Zähle Vergleich

                    // Sende Zustand *vor* der potenziellen Verschiebung
                    stepCallback.accept(new SortStep(new ArrayList<>(arr), new HashSet<>(accessedInner), new HashSet<>(changedInner)));

                    if (arr.get(j - h) > temp) {
                        accessedInner.add(j - h); // Für get(j-h) beim Verschieben
                        arr.set(j, arr.get(j - h)); // Verschieben
                        changedInner.add(j); // Index j wurde geändert
                        steps++; // Zähle Verschiebung (optional)

                        // Sende Zustand *nach* der Verschiebung
                        stepCallback.accept(new SortStep(new ArrayList<>(arr), new HashSet<>(accessedInner), new HashSet<>(changedInner)));

                        j -= h; // Gehe zur nächsten Position links mit Lücke h
                    } else {
                        // Korrekte Position gefunden (arr[j-h] <= temp)
                        break; // Verlasse die innere while-Schleife
                    }
                }

                // Füge temp an der korrekten Position ein
                // Nur senden, wenn sich die Position tatsächlich ändert (j != i)
                if (j != i) {
                    Set<Integer> changedInsert = new HashSet<>();
                    arr.set(j, temp);
                    changedInsert.add(j);
                    steps++; // Zähle Einfügung
                    stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), changedInsert));
                } else {
                    // temp war bereits an der richtigen h-Position
                    Set<Integer> accessedInsert = new HashSet<>();
                    accessedInsert.add(j);
                    stepCallback.accept(new SortStep(new ArrayList<>(arr), accessedInsert, Collections.emptySet()));
                }
            }
            // Gehe zur nächsten (kleineren) Lücke
            h = h / 3; // Nächste Lücke in Knuth's Sequence rückwärts
        }
    }
}