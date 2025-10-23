package sorting.algorithms.project.SortingAlgorithms;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Component
public class Bozosort implements SortingAlgorithm {

	private long steps;
	private List<Integer> dataSet;

	@Override
	public String getName() {
		return "BozoSort";
	}

	@Override
	public String getBestCase() {
		return "O(1)";
	}

	@Override
	public String getAverageCase() {
		return "O(∞)";
	}

	@Override
	public String getWorstCase() {
		return "O(∞)";
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
		bozoSort(copy, step -> {}); // Default: keine Callback
		return copy;
	}

	@Override
	public void sortWithCallback(List<Integer> input, Consumer<List<Integer>> stepCallback) {
		steps = 0;
		bozoSort(input, stepCallback);
	}

	// 🔹 Interne BozoSort-Implementierung mit Callback
	private void bozoSort(List<Integer> arr, Consumer<List<Integer>> stepCallback) {
		boolean sorted = false;

		while (!sorted) {
			// Swap zwei zufällige Elemente
			int index1 = randomIndex(arr.size());
			int index2 = randomIndex(arr.size());
			int temp = arr.get(index1);
			arr.set(index1, arr.get(index2));
			arr.set(index2, temp);

			steps++; // Zähle jeden Swap als Schritt
			stepCallback.accept(new ArrayList<>(arr));

			// Prüfen, ob sortiert
			sorted = true;
			for (int i = 1; i < arr.size(); i++) {
				steps++; // Vergleich arr[i-1] > arr[i]
				if (arr.get(i - 1) > arr.get(i)) {
					sorted = false;
					break;
				}
			}
		}
	}

	private int randomIndex(int range) {
		return (int) (Math.random() * range);
	}
}
