package sorting.algorithms.project.SortingAlgorithms;

import java.util.List;

public interface SortingAlgorithm {
    String getName();
    List<Integer> sort(List<Integer> input);
}