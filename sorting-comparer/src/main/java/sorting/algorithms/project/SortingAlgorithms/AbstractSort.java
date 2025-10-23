package sorting.algorithms.project.SortingAlgorithms;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import java.util.function.Consumer;
import sorting.algorithms.project.dto.SortStep;

/**
 * An abstract base class for sorting algorithms that operate on a copy of the input list.
 * Subclasses must implement the `sortWithCallback` method to provide the actual sorting logic
 * and step-by-step visualization details.
 */
@Component
public abstract class AbstractSort implements SortingAlgorithm {

    /**
     * Sorts a copy of the input list.
     * This method creates a copy of the input list and then calls the
     * `sortWithCallback` method (implemented by subclasses) with a no-op callback.
     * This ensures the original list remains unmodified for the basic sort operation.
     *
     * @param input The list of integers to be sorted.
     * @return A new list containing the sorted elements.
     */
    @Override
    public List<Integer> sort(List<Integer> input) {
        // Create a mutable copy of the input list to sort.
        List<Integer> copy = new ArrayList<>(input);
        // Call the abstract sortWithCallback method with a consumer that does nothing.
        // The actual sorting logic is defined in the concrete subclass.
        sortWithCallback(copy, (SortStep step) -> {});
        // Return the sorted copy.
        return copy;
    }

    /**
     * Abstract method to be implemented by concrete sorting algorithm subclasses.
     * This method should perform the sorting operation in-place on the provided list
     * and utilize the `stepCallback` consumer to report the state of the array
     * and relevant indices (accessed, changed) after each significant step
     * (e.g., comparison, swap, insertion) for visualization purposes.
     *
     * @param input The list of integers to be sorted (will be modified in-place).
     * @param stepCallback A consumer function that accepts a {@link SortStep} object,
     * allowing the algorithm to report its progress for visualization.
     */
    @Override
    public abstract void sortWithCallback(List<Integer> input, Consumer<SortStep> stepCallback);

}