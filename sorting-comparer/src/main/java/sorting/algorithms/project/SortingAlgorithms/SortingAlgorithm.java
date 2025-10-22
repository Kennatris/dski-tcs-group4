package sorting.algorithms.project.SortingAlgorithms;

import java.util.List;
import java.util.function.Consumer;

public interface SortingAlgorithm {

    String getName();
    List<Integer> sort(List<Integer> input);

    default String getWorstCase() { return "unbekannt"; }
    default String getAverageCase() { return "unbekannt"; }
    default String getBestCase() { return "unbekannt"; }
    default long getSteps() { return 0; }
    default List<Integer> getData() { return List.of(5, 2, 8, 0, 4, 1, 7, 3, 9, 6);}


    // 🔹 Neu: Methode für Live-Visualisierung
    default void sortWithCallback(List<Integer> input, Consumer<List<Integer>> stepCallback) {
        // Standardmäßig nur normale Sortierung ohne Callback
        sort(input);
    }
}
