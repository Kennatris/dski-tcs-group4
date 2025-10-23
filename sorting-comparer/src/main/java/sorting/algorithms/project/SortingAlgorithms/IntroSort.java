package sorting.algorithms.project.SortingAlgorithms;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Consumer;
import sorting.algorithms.project.dto.SortStep;

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
		// Sicherstellen, dass dataSet nicht null ist
		return dataSet != null ? dataSet : SortingAlgorithm.super.getData();
	}

	@Override
	public List<Integer> sort(List<Integer> input) {
		List<Integer> copy = new ArrayList<>(input);
		dataSet = new ArrayList<>(copy);
		steps = 0;
		int depth = calcDepth(copy);
		introSortRecursive(copy, 0, copy.size() - 1, depth, (SortStep step) -> {});
		return copy;
	}

	@Override
	public void sortWithCallback(List<Integer> input, Consumer<SortStep> stepCallback) {
		dataSet = new ArrayList<>(input);
		steps = 0;
		// Sende initialen Zustand
		stepCallback.accept(new SortStep(new ArrayList<>(input), Collections.emptySet(), Collections.emptySet()));
		int depth = calcDepth(input);
		introSortRecursive(input, 0, input.size() - 1, depth, stepCallback);
	}

	/**
	 * Führt IntroSort "in-place" rekursiv aus. Wechselt zu HeapSort, wenn
	 * die Rekursionstiefe überschritten wird.
	 * @param arr Die zu sortierende Liste.
	 * @param low Der Startindex des aktuellen Subarrays.
	 * @param high Der Endindex des aktuellen Subarrays.
	 * @param depth Die verbleibende erlaubte Rekursionstiefe.
	 * @param stepCallback Der Callback für die Visualisierung.
	 */
	private void introSortRecursive(List<Integer> arr, int low, int high, int depth, Consumer<SortStep> stepCallback) {
		int size = high - low + 1;
		// Basisfall: Kleines Array oder ungültiger Bereich -> Insertion Sort (oder nichts tun)
		// Hier: Nichts tun für Bereiche < 2
		if (size <= 1) {
			// Optional: Zustand senden, wenn der Bereich betrachtet wurde
			// Set<Integer> accessed = IntStream.rangeClosed(low, high).boxed().collect(Collectors.toSet());
			// stepCallback.accept(new SortStep(new ArrayList<>(arr), accessed, Collections.emptySet()));
			return;
		}

		// Fallback auf HeapSort bei zu tiefer Rekursion
		if (depth <= 0) {
			heapSortSublist(arr, low, high, stepCallback);
			return;
		}

		// QuickSort Partitionierung
		int p = partition(arr, low, high, stepCallback);

		// Rekursive Aufrufe für linke und rechte Teile
		introSortRecursive(arr, low, p - 1, depth - 1, stepCallback);
		introSortRecursive(arr, p + 1, high, depth - 1, stepCallback);
	}

	/**
	 * Führt HeapSort auf einem Teilbereich der Liste aus.
	 * @param arr Die Gesamtliste.
	 * @param low Startindex des Teilbereichs.
	 * @param high Endindex des Teilbereichs.
	 * @param stepCallback Der Haupt-Callback.
	 */
	private void heapSortSublist(List<Integer> arr, int low, int high, Consumer<SortStep> stepCallback) {
		// Erstelle eine temporäre Subliste für HeapSort
		List<Integer> sublist = new ArrayList<>(arr.subList(low, high + 1));

		HeapSort localHeapSort = new HeapSort(); // Eigene Instanz für lokale Schritte

		// Callback, der Schritte aus der Subliste auf das Hauptarray überträgt
		Consumer<SortStep> sublistCallback = (SortStep step) -> {
			List<Integer> stepArray = step.getCurrentArray();
			Set<Integer> stepAccessed = new HashSet<>();
			Set<Integer> stepChanged = new HashSet<>();

			// Übertrage Änderungen und Indizes auf das Hauptarray
			for (int i = 0; i < stepArray.size(); i++) {
				// Nur setzen, wenn sich der Wert geändert hat
				if (!arr.get(low + i).equals(stepArray.get(i))) {
					arr.set(low + i, stepArray.get(i));
				}
			}
			// Konvertiere Indizes aus dem Sub-Callback in globale Indizes
			step.getAccessedIndices().forEach(idx -> stepAccessed.add(low + idx));
			step.getChangedIndices().forEach(idx -> stepChanged.add(low + idx));

			// Sammle die Schritte von localHeapSort *nachdem* der Schritt verarbeitet wurde
			this.steps += localHeapSort.getSteps() - this.steps; // Addiere nur die *neuen* Schritte

			// Sende den Zustand des *gesamten* Arrays mit globalen Indizes
			stepCallback.accept(new SortStep(new ArrayList<>(arr), stepAccessed, stepChanged));
		};

		// Führe HeapSort auf der Subliste aus
		localHeapSort.sortWithCallback(sublist, sublistCallback);

		// Stelle sicher, dass das Hauptarray nach HeapSort korrekt ist
		// (sollte durch den Callback geschehen sein, aber zur Sicherheit)
		for (int i = 0; i < sublist.size(); i++) {
			arr.set(low + i, sublist.get(i));
		}

		// Addiere die Gesamtschritte des lokalen HeapSorts am Ende
		this.steps += localHeapSort.getSteps(); // Addiere die Schritte des Sub-Sorts

		// Sende finalen Zustand des bearbeiteten Bereichs
		Set<Integer> finalChanged = new HashSet<>();
		for(int i=low; i<=high; i++) finalChanged.add(i);
		stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), finalChanged));
	}


	/**
	 * Partitioniert einen Teil der Liste für QuickSort und meldet Schritte.
	 * Verwendet das letzte Element als Pivot.
	 * @param arr Die zu partitionierende Liste.
	 * @param low Startindex des Bereichs.
	 * @param high Endindex des Bereichs (Pivot-Index).
	 * @param stepCallback Der Callback für die Visualisierung.
	 * @return Der finale Index des Pivots.
	 */
	private int partition(List<Integer> arr, int low, int high, Consumer<SortStep> stepCallback) {
		Set<Integer> accessed = new HashSet<>();
		Set<Integer> changed = new HashSet<>();

		accessed.add(high); // Pivot wird gelesen
		int pivotValue = arr.get(high);
		stepCallback.accept(new SortStep(new ArrayList<>(arr), new HashSet<>(accessed), new HashSet<>(changed))); // Pivot gelesen


		int i = low - 1; // Index des letzten Elements kleiner als Pivot

		for (int j = low; j < high; j++) {
			accessed.clear(); // Für diesen Schleifendurchlauf
			changed.clear();
			accessed.add(j); // Element j wird verglichen
			accessed.add(high); // Pivot wird erneut verglichen (implizit)
			steps++; // Zähle Vergleich arr[j] vs pivotValue

			stepCallback.accept(new SortStep(new ArrayList<>(arr), new HashSet<>(accessed), new HashSet<>(changed))); // Vergleichs-Schritt

			if (arr.get(j) < pivotValue) {
				i++;
				// Tausche arr[i] und arr[j]
				accessed.add(i); // Für get(i)
				accessed.add(j); // Für get(j) (schon drin)
				int temp = arr.get(i);
				arr.set(i, arr.get(j));
				arr.set(j, temp);
				changed.add(i);
				changed.add(j);
				steps++; // Zähle Swap (optional)
				// Sende Zustand nach Swap
				stepCallback.accept(new SortStep(new ArrayList<>(arr), new HashSet<>(accessed), new HashSet<>(changed)));
			}
		}

		// Tausche Pivot (arr[high]) mit dem Element nach dem letzten kleineren (arr[i+1])
		accessed.clear();
		changed.clear();
		int pivotFinalIndex = i + 1;
		if (pivotFinalIndex < arr.size() && high < arr.size()) { // Grenzen prüfen
			accessed.add(pivotFinalIndex); // Für get(pivotFinalIndex)
			accessed.add(high); // Für get(high)
			int temp = arr.get(pivotFinalIndex);
			arr.set(pivotFinalIndex, arr.get(high));
			arr.set(high, temp);
			changed.add(pivotFinalIndex);
			changed.add(high);
			steps++; // Zähle Swap
			// Sende Zustand nach finalem Pivot-Swap
			stepCallback.accept(new SortStep(new ArrayList<>(arr), accessed, changed));
		} else {
			// Sende Zustand, auch wenn kein Tausch (Pivot war schon richtig)
			stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), Collections.emptySet()));
		}


		return pivotFinalIndex; // Gib die finale Position des Pivots zurück
	}


	/**
	 * Wählt einen Pivot-Wert (hier: zufällig).
	 * @param arr Die Liste.
	 * @param low Startindex.
	 * @param high Endindex.
	 * @return Den Wert des ausgewählten Pivots.
	 */
	private int getPivot(List<Integer> arr, int low, int high) {
		if (low > high) {
			return low < arr.size() ? arr.get(low) : (arr.isEmpty() ? 0 : arr.get(0)); // Fallback
		}
		Random rd = new Random();
		int pivotIndex = rd.nextInt((high - low) + 1) + low;
		// Optional: Sende einen Schritt, der den Pivot-Zugriff zeigt
		// stepCallback.accept(new SortStep(new ArrayList<>(arr), Set.of(pivotIndex), Collections.emptySet()));
		return arr.get(pivotIndex); // Lese den Wert am zufälligen Index
	}

	/**
	 * Berechnet die maximale Rekursionstiefe basierend auf der Arraygröße.
	 * @param arr Die Liste.
	 * @return Die empfohlene maximale Tiefe.
	 */
	private int calcDepth(List<Integer> arr) {
		if (arr == null || arr.isEmpty()) return 0;
		return (int) (2 * Math.floor(Math.log(arr.size()) / Math.log(2))); // 2 * log2(n)
	}
}