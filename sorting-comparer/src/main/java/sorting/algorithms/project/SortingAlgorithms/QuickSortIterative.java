package sorting.algorithms.project.SortingAlgorithms;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import sorting.algorithms.project.dto.SortStep;

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
		// Sicherstellen, dass dataSet nicht null ist
		return dataSet != null ? dataSet : SortingAlgorithm.super.getData();
	}

	@Override
	public List<Integer> sort(List<Integer> input) {
		List<Integer> copy = new ArrayList<>(input);
		dataSet = new ArrayList<>(copy);
		steps = 0;
		quickSort(copy, 0, copy.size() - 1, (SortStep step) -> {});
		return copy;
	}

	@Override
	public void sortWithCallback(List<Integer> input, Consumer<SortStep> stepCallback) {
		dataSet = new ArrayList<>(input);
		steps = 0;
		// Sende initialen Zustand
		stepCallback.accept(new SortStep(new ArrayList<>(input), Collections.emptySet(), Collections.emptySet()));
		quickSort(input, 0, input.size() - 1, stepCallback);
		// Sende finalen Zustand nach Abschluss
		stepCallback.accept(new SortStep(new ArrayList<>(input), Collections.emptySet(), Collections.emptySet()));
	}

	/**
	 * Führt QuickSort "in-place" iterativ mithilfe eines Stacks aus.
	 * Meldet jeden Vergleich und Swap innerhalb der Partitionierung.
	 * @param arr Die zu sortierende Liste.
	 * @param low Der Startindex des initialen Bereichs.
	 * @param high Der Endindex des initialen Bereichs.
	 * @param stepCallback Der Callback für die Visualisierung.
	 */
	private void quickSort(List<Integer> arr, int low, int high, Consumer<SortStep> stepCallback) {
		if (arr == null || arr.size() <= 1 || low >= high) {
			stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), Collections.emptySet())); // Endzustand
			return;
		}


		// Stack für Indexpaare (low, high) der zu sortierenden Subarrays
		Stack<Integer> stack = new Stack<>();

		// Initiales Paar auf den Stack legen
		stack.push(low);
		stack.push(high);

		// Hauptschleife, solange noch Bereiche zu sortieren sind
		while (!stack.isEmpty()) {
			// Hole high und low vom Stack (umgekehrte Reihenfolge wie push)
			high = stack.pop();
			low = stack.pop();

			// Optional: Sende Schritt, der den aktuellen Bereich zeigt
			// Set<Integer> accessedRange = IntStream.rangeClosed(low, high).filter(i -> i >= 0 && i < arr.size()).boxed().collect(Collectors.toSet());
			// stepCallback.accept(new SortStep(new ArrayList<>(arr), accessedRange, Collections.emptySet()));


			// Überspringe ungültige oder bereits sortierte Bereiche
			if (low >= high) continue;

			// Partitioniere den aktuellen Bereich [low, high]
			int pi = partition(arr, low, high, stepCallback);

			// Lege den linken Teilbereich auf den Stack (wenn Elemente vorhanden)
			if (pi - 1 > low) {
				stack.push(low);
				stack.push(pi - 1);
			}

			// Lege den rechten Teilbereich auf den Stack (wenn Elemente vorhanden)
			if (pi + 1 < high) {
				stack.push(pi + 1);
				stack.push(high);
			}
		}
	}

	/**
	 * Partitioniert einen Teil der Liste für QuickSort und meldet Schritte.
	 * Verwendet das letzte Element als Pivot. (Identisch zur rekursiven Version)
	 * @param arr Die zu partitionierende Liste.
	 * @param low Startindex des Bereichs.
	 * @param high Endindex des Bereichs (Pivot-Index).
	 * @param stepCallback Der Callback für die Visualisierung.
	 * @return Der finale Index des Pivots.
	 */
	private int partition(List<Integer> arr, int low, int high, Consumer<SortStep> stepCallback) {
		// Diese Methode ist identisch zur `partition`-Methode in `QuickSort.java`
		// Kopieren Sie den Code von dort oder stellen Sie sicher, dass er hier korrekt ist.

		Set<Integer> accessed = new HashSet<>();
		Set<Integer> changed = new HashSet<>();

		// Wähle Pivot (letztes Element)
		accessed.add(high);
		int pivotValue = arr.get(high);
		stepCallback.accept(new SortStep(new ArrayList<>(arr), new HashSet<>(accessed), new HashSet<>(changed)));

		int i = low - 1; // Index des letzten Elements < Pivot

		for (int j = low; j < high; j++) {
			accessed.clear();
			changed.clear();
			accessed.add(j);
			accessed.add(high); // Pivot-Index für Vergleichswert
			steps++;

			stepCallback.accept(new SortStep(new ArrayList<>(arr), new HashSet<>(accessed), new HashSet<>(changed))); // Vor Vergleich

			if (arr.get(j) < pivotValue) {
				i++;
				accessed.add(i); // Für get/set
				int temp = arr.get(i);
				arr.set(i, arr.get(j));
				arr.set(j, temp);
				changed.add(i);
				changed.add(j);
				steps++; // Swap (optional)
				stepCallback.accept(new SortStep(new ArrayList<>(arr), new HashSet<>(accessed), new HashSet<>(changed))); // Nach Swap
			}
		}

		// Platziere Pivot
		accessed.clear();
		changed.clear();
		int pivotFinalIndex = i + 1;

		if (pivotFinalIndex < arr.size() && high < arr.size()) {
			accessed.add(pivotFinalIndex);
			accessed.add(high);
			int temp = arr.get(pivotFinalIndex);
			arr.set(pivotFinalIndex, arr.get(high));
			arr.set(high, temp);
			changed.add(pivotFinalIndex);
			changed.add(high);
			steps++; // Swap
			stepCallback.accept(new SortStep(new ArrayList<>(arr), accessed, changed));
		} else {
			stepCallback.accept(new SortStep(new ArrayList<>(arr), accessed, changed));
		}

		return pivotFinalIndex;
	}
}