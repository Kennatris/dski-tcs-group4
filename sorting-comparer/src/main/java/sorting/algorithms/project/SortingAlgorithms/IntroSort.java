package sorting.algorithms.project.SortingAlgorithms;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

@Component
public class IntroSort implements SortingAlgorithm {
	private long steps;
	private List<Integer> dataSet;

	@Override
	public String getName() {
		return "IntroSort";
	}

	@Override
	public String getWorstCase() {
		return "O(n log n)";
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
		int depth = calcDepth(copy);
		introSortRecursive(copy, 0, copy.size() - 1, depth, step -> {});
		return copy;
	}

	@Override
	public void sortWithCallback(List<Integer> input, Consumer<List<Integer>> stepCallback) {
		steps = 0;
		List<Integer> copy = new ArrayList<>(input);
		dataSet = new ArrayList<>(copy);
		int depth = calcDepth(copy);
		introSortRecursive(copy, 0, copy.size() - 1, depth, stepCallback);
	}

	// 🔹 Interne IntroSort-Implementierung mit Callback
	private void introSortRecursive(List<Integer> arr, int low, int high, int depth, Consumer<List<Integer>> stepCallback) {
		if (low >= high) return;

		if (depth <= 0) {
			// Fallback auf HeapSort
			HeapSort heapSort = new HeapSort();
			List<Integer> sublist = new ArrayList<>(arr.subList(low, high + 1));
			heapSort.sortWithCallback(sublist, stepCallback);
			for (int i = 0; i < sublist.size(); i++) {
				arr.set(low + i, sublist.get(i));
			}
			steps += heapSort.getSteps();
			stepCallback.accept(new ArrayList<>(arr));
			return;
		}

		int p = partition(arr, low, high, stepCallback);
		introSortRecursive(arr, low, p - 1, depth - 1, stepCallback);
		introSortRecursive(arr, p + 1, high, depth - 1, stepCallback);
	}

	private int partition(List<Integer> arr, int low, int high, Consumer<List<Integer>> stepCallback) {
		int pivot = getPivot(arr, low, high);
		int j = low;

		for (int i = low; i <= high; i++) {
			steps++; // jeder Vergleich zählt
			if (arr.get(i) < pivot) {
				int temp = arr.get(i);
				arr.set(i, arr.get(j));
				arr.set(j, temp);
				j++;
				stepCallback.accept(new ArrayList<>(arr));
			}
		}

		int temp = arr.get(high);
		arr.set(high, arr.get(j));
		arr.set(j, temp);
		stepCallback.accept(new ArrayList<>(arr));

		return j;
	}

	private int getPivot(List<Integer> arr, int low, int high) {
		Random rd = new Random();
		return arr.get(rd.nextInt((high - low) + 1) + low);
	}

	private int calcDepth(List<Integer> arr) {
		return (int) (2 * Math.log(arr.size()));
	}
}
