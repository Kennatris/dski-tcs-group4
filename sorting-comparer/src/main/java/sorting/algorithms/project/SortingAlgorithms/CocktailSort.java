package sorting.algorithms.project.SortingAlgorithms;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class CocktailSort implements SortingAlgorithm {

    private long steps;

    @Override
    public String getName() {
        return "CocktailSort";
    }

    @Override
    public String getBestCase() {
        return "O(n)";
    }

    @Override
    public String getAverageCase() {
        return "O(n²)";
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
    public List<Integer> sort(List<Integer> input) {
        List<Integer> arr = new ArrayList<>(input);
        steps = 0;

        boolean swapped = true;
        int start = 0;
        int end = arr.size();

        while (swapped) {
            swapped = false;

            // Forward pass
            for (int i = start; i < end - 1; i++) {
                steps++; // Vergleich arr[i] > arr[i+1]
                if (arr.get(i) > arr.get(i + 1)) {
                    int temp = arr.get(i);
                    arr.set(i, arr.get(i + 1));
                    arr.set(i + 1, temp);
                    swapped = true;
                }
            }

            if (!swapped) break;

            swapped = false;
            end--;

            // Backward pass
            for (int i = end - 1; i >= start; i--) {
                steps++; // Vergleich arr[i] > arr[i+1]
                if (arr.get(i) > arr.get(i + 1)) {
                    int temp = arr.get(i);
                    arr.set(i, arr.get(i + 1));
                    arr.set(i + 1, temp);
                    swapped = true;
                }
            }

            start++;
        }

        return arr;
    }
}
