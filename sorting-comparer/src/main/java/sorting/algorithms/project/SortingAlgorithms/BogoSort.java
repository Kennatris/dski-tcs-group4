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
public class BogoSort implements SortingAlgorithm {

    private long steps;
    private List<Integer> dataSet;

    @Override
    public String getName() {
        return "BogoSort";
    }

    @Override
    public String getBestCase() {
        return "O(1)";
    }

    @Override
    public String getAverageCase() {
        return "O(∞)";
    }

    @Override
    public String getWorstCase() {
        return "O(∞)";
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
        bogoSort(copy, (SortStep step) -> {});
        return copy;
    }

    @Override
    public void sortWithCallback(List<Integer> input, Consumer<SortStep> stepCallback) {
        dataSet = new ArrayList<>(input);
        steps = 0;
        // Sende initialen Zustand
        stepCallback.accept(new SortStep(new ArrayList<>(input), Collections.emptySet(), Collections.emptySet()));
        bogoSort(input, stepCallback);
    }

    /**
     * Führt BogoSort "in-place" aus, indem die Liste zufällig gemischt wird,
     * bis sie sortiert ist. Meldet jeden Shuffle und jeden Prüfschritt.
     * @param arr Die zu sortierende Liste.
     * @param stepCallback Der Callback für die Visualisierung.
     */
    private void bogoSort(List<Integer> arr, Consumer<SortStep> stepCallback) {
        while (!isSorted(arr, stepCallback)) {
            Set<Integer> changedIndices = shuffle(arr); // shuffle gibt geänderte Indizes zurück
            steps++; // Zähle Shuffle als einen Schritt
            // Sende Zustand nach dem Shuffle
            stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), changedIndices));
        }
        // Sende finalen sortierten Zustand (wird auch von isSorted gesendet)
        stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), Collections.emptySet()));
    }

    /**
     * Mischt die Liste "in-place" durch wiederholtes zufälliges Tauschen von Elementpaaren.
     * @param arr Die zu mischende Liste.
     * @return Ein Set der Indizes, die potenziell geändert wurden.
     */
    private Set<Integer> shuffle(List<Integer> arr) {
        Set<Integer> changed = new HashSet<>();
        // Einfaches Shuffle: Tausche jedes Element mit einem zufälligen anderen
        for (int i = 0; i < arr.size(); i++) {
            int index1 = i;
            int index2 = (int) (Math.random() * arr.size());

            // Nur tauschen, wenn Indizes unterschiedlich sind, um unnötige Schritte zu vermeiden
            if (index1 != index2) {
                int temp = arr.get(index1); // Lesezugriff
                arr.set(index1, arr.get(index2)); // Schreibzugriff + Lesezugriff
                arr.set(index2, temp); // Schreibzugriff

                changed.add(index1);
                changed.add(index2);
            }
        }
        return changed; // Gibt die Indizes zurück, die getauscht wurden
    }


    /**
     * Überprüft, ob die Liste sortiert ist. Jeder Vergleich wird als Schritt gezählt
     * und ein SortStep mit den verglichenen Indizes gesendet.
     * @param arr Die zu prüfende Liste.
     * @param stepCallback Der Callback für die Visualisierung.
     * @return true, wenn die Liste sortiert ist, sonst false.
     */
    private boolean isSorted(List<Integer> arr, Consumer<SortStep> stepCallback) {
        for (int i = 1; i < arr.size(); i++) {
            Set<Integer> accessed = new HashSet<>();
            accessed.add(i - 1);
            accessed.add(i);
            steps++; // Zähle Vergleich
            // Sende Zustand *vor* der möglichen Rückgabe von false
            stepCallback.accept(new SortStep(new ArrayList<>(arr), accessed, Collections.emptySet()));
            if (arr.get(i - 1) > arr.get(i)) {
                return false; // Liste ist nicht sortiert
            }
        }
        // Wenn die Schleife durchläuft, ist die Liste sortiert
        // Sende den finalen, geprüften Zustand (ist sortiert)
        stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), Collections.emptySet()));
        return true;
    }
}