package sorting.algorithms.project.SortingAlgorithms;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import java.util.function.Consumer;
import sorting.algorithms.project.dto.SortStep;

/**
 * Eine abstrakte Basisklasse für Sortieralgorithmen, die eine Kopie sortieren.
 * Subklassen müssen `sortWithCallback` implementieren.
 */
@Component
public abstract class AbstractSort implements SortingAlgorithm {

    /**
     * Sortiert eine Kopie der Eingabeliste, indem die `sortWithCallback`-Methode
     * mit einem leeren Callback aufgerufen wird.
     * @param input Die zu sortierende Liste.
     * @return Eine neue, sortierte Liste.
     */
    @Override
    public List<Integer> sort(List<Integer> input) {
        List<Integer> copy = new ArrayList<>(input);
        // Ruft die von der Subklasse implementierte Callback-Version auf
        sortWithCallback(copy, (SortStep step) -> {});
        return copy;
    }

    /**
     * Abstrakte Methode, die von Subklassen implementiert werden muss,
     * um die "in-place" Sortierung mit detaillierter Schritt-Visualisierung durchzuführen.
     * @param input Die zu sortierende Liste (wird von der Implementierung modifiziert).
     * @param stepCallback Der Consumer, der jeden SortStep (mit Array-Zustand und
     * Indizes) für die Visualisierung empfängt.
     */
    @Override
    public abstract void sortWithCallback(List<Integer> input, Consumer<SortStep> stepCallback);

}