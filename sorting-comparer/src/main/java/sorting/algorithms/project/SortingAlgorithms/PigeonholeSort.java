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
public class PigeonholeSort implements SortingAlgorithm {
    private long steps;
    private List<Integer> dataSet;

    @Override
    public String getName() {
        return "PigeonholeSort";
    }

    @Override
    public String getWorstCase() {
        return "O(n + range)";
    }

    @Override
    public String getAverageCase() {
        return "O(n + range)";
    }

    @Override
    public String getBestCase() {
        return "O(n + range)";
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
        pigeonholeSort(copy, (SortStep step) -> {});
        return copy;
    }

    @Override
    public void sortWithCallback(List<Integer> input, Consumer<SortStep> stepCallback) {
        dataSet = new ArrayList<>(input);
        steps = 0;
        // Sende initialen Zustand
        stepCallback.accept(new SortStep(new ArrayList<>(input), Collections.emptySet(), Collections.emptySet()));
        pigeonholeSort(input, stepCallback);
    }

    /**
     * Führt PigeonholeSort "in-place" aus (modifiziert die Eingabeliste am Ende).
     * Geeignet für Listen mit einer begrenzten Wertespanne (Range).
     * Meldet Schritte beim Finden von Min/Max, beim Zählen und beim Wiederaufbau.
     * @param arr Die zu sortierende Liste.
     * @param stepCallback Der Callback für die Visualisierung.
     */
    private void pigeonholeSort(List<Integer> arr, Consumer<SortStep> stepCallback) {
        int n = arr.size();
        if (n <= 1) {
            stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), Collections.emptySet())); // Endzustand
            return;
        }


        // 1. Finde Minimum und Maximum (mit Visualisierung)
        int min = arr.get(0);
        int max = arr.get(0);
        Set<Integer> accessedMinMax = new HashSet<>();
        accessedMinMax.add(0);
        stepCallback.accept(new SortStep(new ArrayList<>(arr), new HashSet<>(accessedMinMax), Collections.emptySet()));

        for (int i = 1; i < n; i++) {
            accessedMinMax.clear();
            accessedMinMax.add(i); // Aktueller Index wird gelesen
            int currentVal = arr.get(i);
            steps++; // Zähle Lesezugriff

            // Sende Zustand vor den Vergleichen
            stepCallback.accept(new SortStep(new ArrayList<>(arr), new HashSet<>(accessedMinMax), Collections.emptySet()));

            steps++; // Zähle Vergleich mit max
            if (currentVal > max) {
                max = currentVal;
            }
            steps++; // Zähle Vergleich mit min
            if (currentVal < min) {
                min = currentVal;
            }
            // Sende Zustand nach den Vergleichen (zeigt ggf. neuen Min/Max-Wert implizit)
            // oder explizit senden, wenn sich was geändert hat
            // stepCallback.accept(new SortStep(new ArrayList<>(arr), new HashSet<>(accessedMinMax), Collections.emptySet()));

        }
        // Sende Zustand nach Abschluss der Min/Max-Suche
        Set<Integer> allAccessed = IntStream.range(0, n).boxed().collect(Collectors.toSet());
        stepCallback.accept(new SortStep(new ArrayList<>(arr), allAccessed, Collections.emptySet()));


        // 2. Erstelle und befülle die "Pigeonholes" (Zähleimer)
        int range = max - min + 1;
        // Sicherheitscheck für sehr große Ranges
        if (range > n * 10 && range > 1000000) { // Heuristik: Range viel größer als n
            System.err.println("PigeonholeSort range (" + range + ") is very large compared to size (" + n + "). Consider a different algorithm.");
            // Optional: Fallback auf anderen Algo oder Fehler
            stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), Collections.emptySet())); // Unverändert beenden
            return;
        }
        if (range <= 0) { // Sollte nicht passieren bei gültigen Min/Max
            stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), Collections.emptySet())); // Unverändert beenden
            return;
        }

        int[] holes = new int[range];

        // Zähle Elemente (keine Änderung an 'arr', kein Callback hier)
        for (int i = 0; i < n; i++) {
            int holeIndex = arr.get(i) - min;
            if(holeIndex >= 0 && holeIndex < range) { // Index-Check
                holes[holeIndex]++;
                steps++; // Zähle Lesezugriff und Inkrement
            } else {
                System.err.println("Value " + arr.get(i) + " resulted in invalid hole index " + holeIndex);
            }
        }

        // 3. Rekonstruiere das sortierte Array
        int index = 0; // Index im Zielarray 'arr'
        for (int j = 0; j < range; j++) {
            while (holes[j] > 0) {
                Set<Integer> changed = new HashSet<>();
                // Berechne den Wert, der an 'index' geschrieben wird
                int valueToWrite = j + min;

                // Nur schreiben und senden, wenn sich der Wert ändert oder der Index neu ist
                if (index >= arr.size() || arr.get(index) != valueToWrite) {
                    // Wenn Index außerhalb -> add, sonst set
                    if (index >= arr.size()) arr.add(valueToWrite); else arr.set(index, valueToWrite);

                    changed.add(index);
                    steps++; // Zähle Schreibzugriff/Hinzufügen und Dekrement
                    holes[j]--;
                    // Sende Zustand nach der Änderung
                    stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), changed));
                } else {
                    // Wert ist bereits korrekt, nur Zähler dekrementieren
                    holes[j]--;
                    steps++; // Zähle Dekrement
                    // Optional: Sende Schritt, der zeigt, dass die Position übersprungen wird
                    // Set<Integer> accessed = Set.of(index);
                    // stepCallback.accept(new SortStep(new ArrayList<>(arr), accessed, Collections.emptySet()));
                }
                index++;
            }
        }
        // Sende finalen Zustand
        stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), Collections.emptySet()));
    }
}