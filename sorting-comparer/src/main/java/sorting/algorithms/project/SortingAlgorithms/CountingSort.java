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
public class CountingSort implements SortingAlgorithm {

    private long steps;
    private List<Integer> dataSet;

    @Override
    public String getName() {
        return "CountingSort";
    }

    @Override
    public String getBestCase() {
        return "O(n + k)";
    }

    @Override
    public String getAverageCase() {
        return "O(n + k)";
    }

    @Override
    public String getWorstCase() {
        return "O(n + k)";
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
        countingSort(copy, (SortStep step) -> {});
        return copy;
    }

    @Override
    public void sortWithCallback(List<Integer> input, Consumer<SortStep> stepCallback) {
        dataSet = new ArrayList<>(input);
        steps = 0;
        // Sende initialen Zustand
        stepCallback.accept(new SortStep(new ArrayList<>(input), Collections.emptySet(), Collections.emptySet()));
        countingSort(input, stepCallback);
    }

    /**
     * Führt CountingSort "in-place" aus (modifiziert die Eingabeliste am Ende).
     * Meldet jeden Schritt beim Kopieren in das finale Array.
     * Annahme: Nur nicht-negative Zahlen.
     * @param arr Die zu sortierende Liste.
     * @param stepCallback Der Callback für die Visualisierung.
     */
    private void countingSort(List<Integer> arr, Consumer<SortStep> stepCallback) {
        int n = arr.size();
        if (n == 0) {
            stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), Collections.emptySet()));
            return;
        }

        // Finde Maximum (ohne explizite Visualisierung dieser Phase)
        int max = 0; // Annahme: Nicht-negative Zahlen
        for (int num : arr) {
            if (num < 0) {
                System.err.println("CountingSort requires non-negative integers.");
                // Fehlerbehandlung: Beende oder wirf Exception
                stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), Collections.emptySet())); // Unveränderter Zustand
                return;
            }
            if (num > max) {
                max = num;
            }
        }

        int[] output = new int[n];
        int[] count = new int[max + 1];

        // 1. Zähle Vorkommen (keine Änderung an 'arr', kein Callback)
        for (int i = 0; i < n; i++) {
            int num = arr.get(i);
            count[num]++;
            steps++; // Zähle Lesezugriff und Inkrement
        }


        // 2. Kumuliere Zählungen (keine Änderung an 'arr', kein Callback)
        for (int i = 1; i <= max; i++) {
            count[i] += count[i - 1];
            steps++; // Zähle Addition
        }

        // 3. Baue Output-Array (keine Änderung an 'arr', kein Callback)
        // Von hinten durchgehen, um Stabilität zu gewährleisten
        for (int i = n - 1; i >= 0; i--) {
            int num = arr.get(i);
            output[count[num] - 1] = num;
            count[num]--;
            steps++; // Zähle Lesezugriffe, Dekrement, Schreibzugriff
        }

        // 4. Kopiere Output zurück in 'arr' und visualisiere jeden Schritt
        for (int i = 0; i < n; i++) {
            if (arr.get(i) != output[i]) { // Nur senden, wenn sich was ändert
                Set<Integer> changed = new HashSet<>();
                arr.set(i, output[i]);
                changed.add(i);
                steps++; // Zähle Schreibzugriff
                stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), changed));
            } else {
                // Optional: Sende auch, wenn nichts geändert wird, um Fortschritt zu zeigen
                // steps++; // Zähle Vergleich
                // Set<Integer> accessed = Set.of(i);
                // stepCallback.accept(new SortStep(new ArrayList<>(arr), accessed, Collections.emptySet()));
            }
        }
        // Sende finalen Zustand (kann redundant sein)
        stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), Collections.emptySet()));
    }
}