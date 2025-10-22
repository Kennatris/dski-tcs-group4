package sorting.algorithms.project.SortingAlgorithms;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Component
public class QuickSortIterative implements SortingAlgorithm {
	private long steps;
	private List<Integer> dataSet;

	@Override
	public String getName() {
		return "QuickSortIterative";
	}

	@Override
	public String getWorstCase() {
		return "O(n²)";
	}

	@Override
	public String getAverageCase() {
		return "O(n log n)";
	}

	@Override
	public String getBestCase() {
		return "O(n log n)";
	}

	@Override
	public long getSteps() {
		return steps;
	}

	@Override
	public List<Integer> getData() {
		return dataSet;
	}

	@Override
	public List<Integer> sort(List<Integer> input) {
		List<Integer> copy = new ArrayList<>(input);
		dataSet = new ArrayList<>(copy);
		steps = 0;
		quickSort(copy, 0, copy.size() - 1, step -> {});
		return copy;
	}

	@Override
	public void sortWithCallback(List<Integer> input, Consumer<List<Integer>> stepCallback) {
		dataSet = new ArrayList<>(input);
		steps = 0;
		quickSort(input, 0, input.size() - 1, stepCallback);
	}

	// 🔹 Interne QuickSort-Implementierung mit Callback
	private void quickSort(List<Integer> arr, int low, int high, Consumer<List<Integer>> stepCallback) {
		if (low < high) {
			int pi = partition(arr, low, high, stepCallback);
			quickSort(arr, low, pi - 1, stepCallback);
			quickSort(arr, pi + 1, high, stepCallback);
		}
	}

	private int partition(List<Integer> arr, int low, int high, Consumer<List<Integer>> stepCallback) {
		int pivot = arr.get(high);
		int i = low - 1;

		for (int j = low; j <= high - 1; j++) {
			steps++; // jeder Vergleich zählt
			if (arr.get(j) <= pivot) {
				i++;
				int temp = arr.get(i);
				arr.set(i, arr.get(j));
				arr.set(j, temp);
				stepCallback.accept(new ArrayList<>(arr));
			}
		}

		int temp = arr.get(i + 1);
		arr.set(i + 1, arr.get(high));
		arr.set(high, temp);
		stepCallback.accept(new ArrayList<>(arr));

		return i + 1;
	}
}
