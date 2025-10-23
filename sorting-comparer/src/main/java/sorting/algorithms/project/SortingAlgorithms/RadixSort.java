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
public class RadixSort implements SortingAlgorithm {
    private long steps;
    private List<Integer> dataSet;

    @Override
    public String getName() {
        return "RadixSort";
    }

    @Override
    public String getWorstCase() {
        return "O(nk)";
    }

    @Override
    public String getAverageCase() {
        return "O(nk)";
    }

    @Override
    public String getBestCase() {
        return "O(nk)";
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
        radixSort(copy, (SortStep step) -> {});
        return copy;
    }

    @Override
    public void sortWithCallback(List<Integer> input, Consumer<SortStep> stepCallback) {
        dataSet = new ArrayList<>(input);
        steps = 0;
        // Sende initialen Zustand
        stepCallback.accept(new SortStep(new ArrayList<>(input), Collections.emptySet(), Collections.emptySet()));
        radixSort(input, stepCallback);
        // Sende finalen Zustand
        stepCallback.accept(new SortStep(new ArrayList<>(input), Collections.emptySet(), Collections.emptySet()));
    }

    /**
     * Führt RadixSort (LSD - Least Significant Digit) "in-place" aus,
     * indem CountingSort für jede Ziffernstelle aufgerufen wird.
     * Annahme: Nur nicht-negative Zahlen.
     * @param arr Die zu sortierende Liste.
     * @param stepCallback Der Callback für die Visualisierung.
     */
    private void radixSort(List<Integer> arr, Consumer<SortStep> stepCallback) {
        int n = arr.size();
        if (n <= 1) {
            stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), Collections.emptySet())); // Endzustand
            return;
        }


        // Finde Maximum, um Anzahl der Durchläufe zu bestimmen (ohne Visualisierung hier)
        int max = 0;
        boolean hasNonNegative = false;
        for (int num : arr) {
            if (num < 0) {
                System.err.println("RadixSort (LSD) requires non-negative integers.");
                stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), Collections.emptySet())); // Unverändert
                return;
            }
            hasNonNegative = true;
            if (num > max) {
                max = num;
            }
        }
        // Wenn Liste leer oder nur negative Zahlen (was oben abgefangen wird)
        if (!hasNonNegative && n > 0) {
            stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), Collections.emptySet())); // Unverändert
            return;
        }


        // Führe CountingSort für jede Ziffernstelle durch (von rechts nach links)
        for (int exp = 1; max / exp > 0; exp *= 10) {
            countSortByDigit(arr, n, exp, stepCallback);
        }
    }


    /**
     * Führt einen stabilen CountingSort basierend auf einer bestimmten Ziffernstelle (exp) durch.
     * Modifiziert 'arr' "in-place". Meldet jeden Schritt beim Zurückkopieren.
     * @param arr Die zu sortierende Liste.
     * @param n Die Größe der Liste.
     * @param exp Die aktuelle Ziffernstelle (1, 10, 100, ...).
     * @param stepCallback Der Callback für die Visualisierung.
     */
    private void countSortByDigit(List<Integer> arr, int n, int exp, Consumer<SortStep> stepCallback) {
        int[] output = new int[n];
        int[] count = new int[10]; // Zähler für Ziffern 0-9

        // 1. Zähle Vorkommen der Ziffern an der Stelle 'exp'
        for (int i = 0; i < n; i++) {
            int num = arr.get(i);
            int digit = (num / exp) % 10;
            if (digit >= 0 && digit < 10) { // Sicherstellen, dass Ziffer gültig ist
                count[digit]++;
                steps++; // Zähle Lesezugriff, Berechnung und Inkrement
            }
        }

        // 2. Kumuliere Zählungen für die Positionen im Output-Array
        for (int i = 1; i < 10; i++) {
            count[i] += count[i - 1];
            steps++; // Zähle Addition
        }

        // 3. Baue das Output-Array (von hinten für Stabilität)
        for (int i = n - 1; i >= 0; i--) {
            int num = arr.get(i);
            int digit = (num / exp) % 10;
            if (digit >= 0 && digit < 10) {
                int outputIndex = count[digit] - 1;
                if (outputIndex >= 0 && outputIndex < n) { // Index-Check
                    output[outputIndex] = num;
                    count[digit]--;
                    steps++; // Zähle Lesezugriffe, Dekrement, Schreibzugriff
                }
            }
        }

        // 4. Kopiere Output zurück in 'arr' und visualisiere jeden Schritt
        for (int i = 0; i < n; i++) {
            if (i >= output.length) break; // Sicherheitscheck
            // Nur senden, wenn sich der Wert ändert
            if (!arr.get(i).equals(output[i])) {
                Set<Integer> changed = new HashSet<>();
                arr.set(i, output[i]);
                changed.add(i);
                steps++; // Zähle Schreibzugriff
                stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), changed));
            } else {
                // Optional: Schritt senden, um Fortschritt anzuzeigen
                // Set<Integer> accessed = Set.of(i);
                // stepCallback.accept(new SortStep(new ArrayList<>(arr), accessed, Collections.emptySet()));
            }
        }
    }
}