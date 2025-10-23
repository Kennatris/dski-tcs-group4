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
public class BucketSort implements SortingAlgorithm {

    private long steps;
    private List<Integer> dataSet;

    @Override
    public String getName() {
        return "BucketSort";
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
        bucketSort(copy, (SortStep step) -> {});
        return copy;
    }

    @Override
    public void sortWithCallback(List<Integer> input, Consumer<SortStep> stepCallback) {
        dataSet = new ArrayList<>(input);
        steps = 0;
        // Sende initialen Zustand
        stepCallback.accept(new SortStep(new ArrayList<>(input), Collections.emptySet(), Collections.emptySet()));
        bucketSort(input, stepCallback);
    }

    /**
     * Führt eine vereinfachte Form von BucketSort (ähnlich CountingSort) "in-place" aus.
     * Meldet jeden Schritt beim Finden des Maximums und beim Wiederaufbau des Arrays.
     * @param arr Die zu sortierende Liste (wird modifiziert).
     * @param stepCallback Der Callback für die Visualisierung.
     */
    private void bucketSort(List<Integer> arr, Consumer<SortStep> stepCallback) {
        int n = arr.size();
        if (n == 0) {
            stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), Collections.emptySet())); // Endzustand für leere Liste
            return;
        }

        // Finde Max mit Visualisierungsschritten
        int maxValue = findMaxAndVisualize(arr, stepCallback);
        if (maxValue < 0) { // Zusätzliche Prüfung, falls nur negative Zahlen vorhanden sind
            System.err.println("BucketSort (simple version) doesn't handle negative numbers well.");
            // Hier könnte man eine Exception werfen oder anders darauf reagieren.
            // Vorerst senden wir den unveränderten Zustand und beenden.
            stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), Collections.emptySet()));
            return;
        }

        // Sicherstellen, dass das Bucket-Array groß genug ist
        int[] bucket = new int[maxValue + 1];

        // Zähle Vorkommen (keine Zustandsänderung am 'arr', daher kein Callback hier)
        for (int i = 0; i < n; i++) {
            int num = arr.get(i);
            if (num >= 0 && num <= maxValue) { // Prüfe Array-Grenzen
                bucket[num]++;
                steps++; // Zähle Zugriff und Inkrement
            } else {
                System.err.println("Value " + num + " out of expected range [0..." + maxValue + "]");
                // Optional: Fehlerbehandlung
            }
        }


        // Baue sortiertes Array wieder auf
        arr.clear(); // Leert das Original-Array -> Zustandsänderung
        stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), Collections.emptySet())); // Zustand nach clear()

        int currentArrIndex = 0; // Index im Zielarray 'arr'
        for (int i = 0; i < bucket.length; i++) {
            for (int j = 0; j < bucket[i]; j++) {
                Set<Integer> changed = new HashSet<>();
                arr.add(i); // Fügt Element hinzu
                changed.add(currentArrIndex); // Der neue Index wurde geändert
                steps++; // Zähle das Hinzufügen
                stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), changed));
                currentArrIndex++;
            }
        }
    }

    /**
     * Findet den größten nicht-negativen Wert in der Liste und sendet Visualisierungsschritte.
     * @param arr Die zu durchsuchende Liste.
     * @param stepCallback Der Callback für die Visualisierung.
     * @return Der maximale nicht-negative Wert oder -1, wenn keiner gefunden wurde.
     */
    private int findMaxAndVisualize(List<Integer> arr, Consumer<SortStep> stepCallback) {
        if (arr.isEmpty()) return -1;

        int max = -1; // Starte mit -1, um sicherzustellen, dass nur nicht-negative Werte berücksichtigt werden
        // Initialisiere mit dem ersten nicht-negativen Wert, falls vorhanden
        for(int val : arr) {
            if (val >= 0) {
                max = val;
                break;
            }
        }
        if (max == -1 && !arr.isEmpty()) max = arr.get(0); // Fallback, falls nur negative Zahlen

        Set<Integer> accessed = new HashSet<>();
        accessed.add(0); // Erster Zugriff auf Index 0
        stepCallback.accept(new SortStep(new ArrayList<>(arr), accessed, Collections.emptySet()));


        for (int i = 1; i < arr.size(); i++) {
            accessed.clear();
            accessed.add(i); // Aktueller Index wird gelesen
            int num = arr.get(i);
            if (num > max) {
                max = num;
            }
            steps++; // Zähle Vergleich
            stepCallback.accept(new SortStep(new ArrayList<>(arr), accessed, Collections.emptySet()));
        }
        return max;
    }
}