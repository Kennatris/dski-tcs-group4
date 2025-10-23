package sorting.algorithms.project.SortingAlgorithms;

import java.util.List;
import java.util.function.Consumer;
import sorting.algorithms.project.dto.SortStep; // Importiert SortStep

/**
 * Definiert die Schnittstelle für einen Sortieralgorithmus.
 */
public interface SortingAlgorithm {

    /**
     * @return Der Anzeigename des Algorithmus.
     */
    String getName();

    /**
     * Sortiert eine Liste von Integern und gibt eine neue sortierte Liste zurück.
     * Die Eingabeliste wird nicht modifiziert.
     * @param input Die zu sortierende Liste.
     * @return Eine neue, sortierte Liste.
     */
    List<Integer> sort(List<Integer> input);

    /**
     * @return Die Komplexität im Worst Case als String.
     */
    default String getWorstCase() { return "unbekannt"; }

    /**
     * @return Die Komplexität im Average Case als String.
     */
    default String getAverageCase() { return "unbekannt"; }

    /**
     * @return Die Komplexität im Best Case als String.
     */
    default String getBestCase() { return "unbekannt"; }

    /**
     * @return Die Anzahl der Schritte (Vergleiche, Swaps etc.) des letzten Sortiervorgangs.
     */
    default long getSteps() { return 0; }

    /**
     * @return Ein Standard-Datensatz, der für diesen Algorithmus verwendet werden kann.
     */
    default List<Integer> getData() { return List.of(5, 2, 8, 0, 4, 1, 7, 3, 9, 6);}


    /**
     * Sortiert eine Liste "in-place" und sendet jeden Schritt an einen Callback-Consumer.
     * @param input Die zu sortierende Liste (wird modifiziert).
     * @param stepCallback Der Consumer, der jeden SortStep empfängt.
     */
    default void sortWithCallback(List<Integer> input, Consumer<SortStep> stepCallback) {
        // Standardimplementierung, falls ein Algorithmus keine Visualisierung bereitstellt.
        sort(input);
    }
}