package sorting.algorithms.project.SortingAlgorithms;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import sorting.algorithms.project.dto.SortStep;

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
		// Sicherstellen, dass dataSet nicht null ist
		return dataSet != null ? dataSet : SortingAlgorithm.super.getData();
	}

	@Override
	public List<Integer> sort(List<Integer> input) {
		List<Integer> copy = new ArrayList<>(input);
		dataSet = new ArrayList<>(copy);
		steps = 0;
		bozoSort(copy, (SortStep step) -> {});
		return copy;
	}

	@Override
	public void sortWithCallback(List<Integer> input, Consumer<SortStep> stepCallback) {
		dataSet = new ArrayList<>(input);
		steps = 0;
		// Sende initialen Zustand
		stepCallback.accept(new SortStep(new ArrayList<>(input), Collections.emptySet(), Collections.emptySet()));
		bozoSort(input, stepCallback);
	}

	/**
	 * Führt BozoSort "in-place" aus, indem zufällig zwei Elemente getauscht werden,
	 * bis die Liste sortiert ist. Meldet jeden Swap und jeden Prüfschritt.
	 * @param arr Die zu sortierende Liste.
	 * @param stepCallback Der Callback für die Visualisierung.
	 */
	private void bozoSort(List<Integer> arr, Consumer<SortStep> stepCallback) {
		boolean sorted = false;

		while (!sorted) {
			Set<Integer> accessedSwap = new HashSet<>();
			Set<Integer> changedSwap = new HashSet<>();

			int index1 = randomIndex(arr.size());
			int index2 = randomIndex(arr.size());

			// Führe Tausch nur durch, wenn Indizes unterschiedlich sind
			if (index1 != index2) {
				accessedSwap.add(index1); // Für get
				accessedSwap.add(index2); // Für get
				int temp = arr.get(index1);
				arr.set(index1, arr.get(index2));
				arr.set(index2, temp);
				changedSwap.add(index1); // Für set
				changedSwap.add(index2); // Für set
				steps++; // Zähle Swap als Schritt
				// Sende Zustand nach dem Swap
				stepCallback.accept(new SortStep(new ArrayList<>(arr), accessedSwap, changedSwap));
			} else {
				// Sende Zustand auch wenn nicht getauscht wurde, um Fortschritt zu zeigen
				stepCallback.accept(new SortStep(new ArrayList<>(arr), accessedSwap, changedSwap));
			}


			// Prüfen, ob sortiert
			sorted = true;
			for (int i = 1; i < arr.size(); i++) {
				Set<Integer> accessedCheck = new HashSet<>();
				accessedCheck.add(i - 1);
				accessedCheck.add(i);
				steps++; // Zähle Vergleich als Schritt
				// Sende Zustand *vor* der möglichen Unterbrechung
				stepCallback.accept(new SortStep(new ArrayList<>(arr), accessedCheck, Collections.emptySet()));
				if (arr.get(i - 1) > arr.get(i)) {
					sorted = false;
					break; // Unterbreche Prüfung, wenn unsortiert gefunden
				}
			}
			// Wenn die Schleife durchlief *und* 'sorted' noch true ist,
			// sende den finalen, sortierten Zustand.
			if (sorted) {
				stepCallback.accept(new SortStep(new ArrayList<>(arr), Collections.emptySet(), Collections.emptySet()));
			}
		}
	}

	/**
	 * Generiert einen zufälligen Index innerhalb des gültigen Bereichs für die Liste.
	 * @param range Die Größe der Liste (exklusive Obergrenze).
	 * @return Ein zufälliger Index zwischen 0 (inklusiv) und range (exklusiv).
	 */
	private int randomIndex(int range) {
		if (range <= 0) return 0; // Absicherung für leere Liste
		return (int) (Math.random() * range);
	}
}