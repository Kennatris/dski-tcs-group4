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
public class StalinSort implements SortingAlgorithm {
    private long steps;
    private List<Integer> dataSet;

    @Override
    public String getName() {
        return "StalinSort";
    }

    @Override
    public String getWorstCase() {
        return "O(n)";
    }

    @Override
    public String getAverageCase() {
        return "O(n)";
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
        // Wichtig: StalinSort modifiziert die Liste stark (durch remove).
        // Für 'sort' immer eine Kopie erstellen!
        List<Integer> copy = new ArrayList<>(input);
        dataSet = new ArrayList<>(input); // Original speichern
        steps = 0;
        stalinSort(copy, (SortStep step) -> {}); // Auf Kopie arbeiten
        return copy; // Modifizierte Kopie zurückgeben
    }

    @Override
    public void sortWithCallback(List<Integer> input, Consumer<SortStep> stepCallback) {
        // Hier wird die Originalliste modifiziert! Stellen Sie sicher, dass dies beabsichtigt ist.
        // Wenn nicht, sollte auch hier eine Kopie erstellt werden.
        dataSet = new ArrayList<>(input); // Kopie des Originals für dataSet
        steps = 0;
        // Sende initialen Zustand
        stepCallback.accept(new SortStep(new ArrayList<>(input), Collections.emptySet(), Collections.emptySet()));
        stalinSort(input, stepCallback); // Modifiziert 'input' direkt
        // Sende finalen Zustand
        stepCallback.accept(new SortStep(new ArrayList<>(input), Collections.emptySet(), Collections.emptySet()));
    }

    /**
     * Führt StalinSort "in-place" aus, indem jedes Element entfernt wird,
     * das kleiner ist als sein Vorgänger.
     * Meldet jeden Vergleich und jede Entfernung.
     * @param arr Die zu "sortierende" Liste (wird modifiziert und potenziell verkürzt).
     * @param stepCallback Der Callback für die Visualisierung.
     */
    private void stalinSort(List<Integer> arr, Consumer<SortStep> stepCallback) {
        if (arr.isEmpty()) {
            stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), Collections.emptySet())); // Endzustand
            return;
        }

        int i = 1; // Beginne Prüfung beim zweiten Element
        while (i < arr.size()) { // Wichtig: arr.size() kann sich ändern!
            Set<Integer> accessed = new HashSet<>();
            Set<Integer> changed = new HashSet<>(); // Wird nur bei remove genutzt

            accessed.add(i);     // Aktuelles Element
            accessed.add(i - 1); // Vorheriges Element
            steps++; // Zähle Vergleich

            // Sende Zustand *vor* der potenziellen Entfernung
            stepCallback.accept(new SortStep(new ArrayList<>(arr), new HashSet<>(accessed), new HashSet<>(changed)));

            if (arr.get(i) < arr.get(i - 1)) {
                arr.remove(i);
                // Wichtig: 'i' wird NICHT erhöht, da das nächste Element an Position 'i' nachrückt
                steps++; // Zähle Entfernung als Schritt (optional)

                // Indizes ab 'i' haben sich geändert (nicht direkt visualisierbar,
                // aber der Zustand des Arrays zeigt es)
                // Man könnte 'changed' hier leer lassen oder alle ab 'i' markieren.
                // Hier lassen wir es leer, da der Array-Zustand die Änderung zeigt.

                // Sende Zustand *nach* der Entfernung
                stepCallback.accept(new SortStep(new ArrayList<>(arr), accessed, changed));

            } else {
                // Element ist in Ordnung, gehe zum nächsten
                i++;
                // Sende Zustand nach dem Weiterrücken (optional, wenn jeder Schritt sichtbar sein soll)
                // stepCallback.accept(new SortStep(new ArrayList<>(arr), accessed, changed));
            }
            // Sende Schritt *nach* der Aktion (entweder remove oder i++)
            // Dieser Schritt ist wichtig, wenn i++ keinen eigenen Send hat.
            // Entfernt, da nach remove bereits gesendet wird und bei i++ oben optional.
            // stepCallback.accept(new SortStep(new ArrayList<>(arr), accessed, changed));
        }
    }
}