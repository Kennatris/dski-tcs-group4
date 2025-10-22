package sorting.algorithms.project.SortingAlgorithms;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public abstract class AbstractSort implements SortingAlgorithm {

    @Override
    public List<Integer> sort(List<Integer> input) {
        List<Integer> copy = new ArrayList<>(input);
        sortInPlace(copy);
        return copy;
    }

    protected abstract void sortInPlace(List<Integer> input);
}