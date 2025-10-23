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
public class InsertionSort implements SortingAlgorithm {
    private long steps;
    private List<Integer> dataSet;

    @Override
    public String getName() {
        return "InsertionSort";
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
        insertionSort(copy, (SortStep step) -> {});
        return copy;
    }

    @Override
    public void sortWithCallback(List<Integer> input, Consumer<SortStep> stepCallback) {
        dataSet = new ArrayList<>(input);
        steps = 0;
        // Sende initialen Zustand
        stepCallback.accept(new SortStep(new ArrayList<>(input), Collections.emptySet(), Collections.emptySet()));
        insertionSort(input, stepCallback);
    }

    /**
     * Führt InsertionSort "in-place" aus. Elemente werden nacheinander
     * an die korrekte Position im bereits sortierten Teil des Arrays eingefügt.
     * Meldet jeden Vergleich, jede Verschiebung und jede Einfügung.
     * @param arr Die zu sortierende Liste.
     * @param stepCallback Der Callback für die Visualisierung.
     */
    private void insertionSort(List<Integer> arr, Consumer<SortStep> stepCallback) {
        int n = arr.size();
        if (n <= 1) {
            stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), Collections.emptySet())); // Endzustand
            return;
        }


        for (int i = 1; i < n; i++) {
            Set<Integer> accessedOuter = new HashSet<>();
            Set<Integer> changedOuter = new HashSet<>();
            accessedOuter.add(i); // Für get(i)
            int key = arr.get(i);
            int j = i - 1;

            // Sende Zustand beim Auswählen des 'key'
            stepCallback.accept(new SortStep(new ArrayList<>(arr), new HashSet<>(accessedOuter), new HashSet<>(changedOuter)));

            // Verschiebe Elemente des sortierten Teils, die größer als key sind
            while (j >= 0) {
                Set<Integer> accessedInner = new HashSet<>(accessedOuter); // Übernehme äußere Zugriffe
                Set<Integer> changedInner = new HashSet<>(changedOuter);
                accessedInner.add(j); // Für get(j)
                steps++; // Zähle Vergleich

                // Sende Zustand *vor* dem potenziellen Verschieben
                stepCallback.accept(new SortStep(new ArrayList<>(arr), new HashSet<>(accessedInner), new HashSet<>(changedInner)));

                if (arr.get(j) > key) {
                    accessedInner.add(j); // Für get(j) beim Verschieben (redundant, aber ok)
                    arr.set(j + 1, arr.get(j)); // Verschieben
                    changedInner.add(j + 1); // Zielindex wurde geändert
                    steps++; // Zähle Verschiebung (optional)
                    // Sende Zustand *nach* dem Verschieben
                    stepCallback.accept(new SortStep(new ArrayList<>(arr), accessedInner, changedInner));
                    j--; // Gehe zum nächsten Element links
                } else {
                    // Element arr[j] ist <= key, key muss nach j eingefügt werden
                    break; // Verlasse die while-Schleife
                }
            }

            // Füge key an der korrekten Position ein (j + 1)
            // Nur senden, wenn sich die Position tatsächlich ändert (also wenn j != i-1)
            if (j + 1 != i) {
                Set<Integer> changedInsert = new HashSet<>();
                arr.set(j + 1, key);
                changedInsert.add(j + 1);
                steps++; // Zähle Einfügung
                stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), changedInsert)); // Nur Änderung zeigen
            } else {
                // Key war bereits an der richtigen Stelle (keine Verschiebung fand statt)
                // Sende Schritt, um zu zeigen, dass die Position geprüft wurde
                Set<Integer> accessedInsert = new HashSet<>();
                accessedInsert.add(j+1); // Position für Einfügen wurde betrachtet
                stepCallback.accept(new SortStep(new ArrayList<>(arr), accessedInsert, Collections.emptySet()));
            }
        }
        // Sende finalen Zustand (kann redundant sein)
        stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), Collections.emptySet()));
    }
}