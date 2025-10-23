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
public class MergeSort implements SortingAlgorithm {
    private long steps;
    private List<Integer> dataSet;

    @Override
    public String getName() {
        return "MergeSort";
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
        mergeSort(copy, 0, copy.size() - 1, (SortStep step) -> {});
        return copy;
    }

    @Override
    public void sortWithCallback(List<Integer> input, Consumer<SortStep> stepCallback) {
        dataSet = new ArrayList<>(input);
        steps = 0;
        // Sende initialen Zustand
        stepCallback.accept(new SortStep(new ArrayList<>(input), Collections.emptySet(), Collections.emptySet()));
        mergeSort(input, 0, input.size() - 1, stepCallback);
        // Sende finalen Zustand nach Abschluss
        stepCallback.accept(new SortStep(new ArrayList<>(input), Collections.emptySet(), Collections.emptySet()));
    }

    /**
     * Startet den rekursiven MergeSort-Prozess "in-place" durch Modifikation der Eingabeliste.
     * @param arr Die zu sortierende Liste (wird modifiziert).
     * @param lower Der Startindex des aktuellen Subarrays.
     * @param upper Der Endindex des aktuellen Subarrays.
     * @param stepCallback Der Callback für die Visualisierung.
     */
    private void mergeSort(List<Integer> arr, int lower, int upper, Consumer<SortStep> stepCallback) {
        // Optional: Schritt senden, der den aktuellen Rekursionsbereich zeigt
        // Set<Integer> accessedRange = IntStream.rangeClosed(lower, upper).boxed().collect(Collectors.toSet());
        // stepCallback.accept(new SortStep(new ArrayList<>(arr), accessedRange, Collections.emptySet()));

        if (lower >= upper) {
            return; // Basisfall: Subarray hat 0 oder 1 Element
        }

        int mid = lower + (upper - lower) / 2; // Vermeidet potenziellen Overflow

        // Rekursiv für linke und rechte Hälfte sortieren
        mergeSort(arr, lower, mid, stepCallback);
        mergeSort(arr, mid + 1, upper, stepCallback);

        // Mische die beiden sortierten Hälften
        merge(arr, lower, mid, upper, stepCallback);
    }

    /**
     * Führt zwei sortierte Teil-Listen (innerhalb von 'arr') zu einer sortierten Liste zusammen.
     * Modifiziert 'arr' "in-place". Meldet jeden Vergleich und jede Platzierung.
     * @param arr Die Hauptliste, die die Subarrays enthält.
     * @param lower Der Startindex des linken Subarrays.
     * @param mid Der Endindex des linken Subarrays.
     * @param upper Der Endindex des rechten Subarrays.
     * @param stepCallback Der Callback für die Visualisierung.
     */
    private void merge(List<Integer> arr, int lower, int mid, int upper, Consumer<SortStep> stepCallback) {
        // Erstelle temporäre Kopien der beiden Subarrays
        List<Integer> left = new ArrayList<>(arr.subList(lower, mid + 1));
        List<Integer> right = new ArrayList<>(arr.subList(mid + 1, upper + 1));

        int i = 0; // Index für linkes Subarray
        int j = 0; // Index für rechtes Subarray
        int k = lower; // Index für das Hauptarray 'arr'

        // Mische die Elemente aus left und right zurück in arr
        while (i < left.size() && j < right.size()) {
            Set<Integer> accessed = new HashSet<>();
            Set<Integer> changed = new HashSet<>();

            // Indizes in den *temporären* Listen (i, j)
            // Indizes im *Hauptarray* (lower + i, mid + 1 + j, k)
            accessed.add(lower + i);   // Element in arr, das left[i] entspricht
            accessed.add(mid + 1 + j); // Element in arr, das right[j] entspricht

            steps++; // Zähle Vergleich

            // Sende Zustand *vor* der Platzierung
            stepCallback.accept(new SortStep(new ArrayList<>(arr), new HashSet<>(accessed), new HashSet<>(changed)));

            if (left.get(i) <= right.get(j)) {
                arr.set(k, left.get(i));
                changed.add(k); // Index k im Hauptarray wurde geändert
                i++;
            } else {
                arr.set(k, right.get(j));
                changed.add(k); // Index k im Hauptarray wurde geändert
                j++;
            }
            // Sende Zustand *nach* der Platzierung
            stepCallback.accept(new SortStep(new ArrayList<>(arr), accessed, changed)); // Zeige Zugriff und Änderung
            k++;
        }

        // Kopiere verbleibende Elemente von left (falls vorhanden)
        while (i < left.size()) {
            Set<Integer> changed = new HashSet<>();
            Set<Integer> accessed = new HashSet<>();
            accessed.add(lower+i); // Virtueller Lesezugriff aus Subliste
            arr.set(k, left.get(i));
            changed.add(k);
            steps++; // Zähle als Platzierungs-Schritt
            stepCallback.accept(new SortStep(new ArrayList<>(arr), accessed, changed));
            i++;
            k++;
        }

        // Kopiere verbleibende Elemente von right (falls vorhanden)
        while (j < right.size()) {
            Set<Integer> changed = new HashSet<>();
            Set<Integer> accessed = new HashSet<>();
            accessed.add(mid + 1 + j); // Virtueller Lesezugriff aus Subliste
            arr.set(k, right.get(j));
            changed.add(k);
            steps++; // Zähle als Platzierungs-Schritt
            stepCallback.accept(new SortStep(new ArrayList<>(arr), accessed, changed));
            j++;
            k++;
        }
    }
}