package sorting.algorithms.project.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set; // Import Set

/**
 * Kapselt einen einzelnen Schritt in einem Sortiervorgang für die Visualisierung.
 * Enthält den Array-Zustand sowie Indizes, die gelesen oder geschrieben wurden.
 */
public class SortStep {

    private List<Integer> currentArray;
    private List<Integer> accessedIndices; // Neu: Indizes, die gelesen/verglichen wurden
    private List<Integer> changedIndices;  // Neu: Indizes, die geschrieben/getauscht wurden

    /**
     * Erstellt einen neuen Sortierschritt nur mit dem Array-Zustand.
     * @param currentArray Der aktuelle Zustand des Arrays.
     */
    public SortStep(List<Integer> currentArray) {
        this(currentArray, Collections.emptyList(), Collections.emptyList());
    }

    /**
     * Erstellt einen neuen Sortierschritt mit Array-Zustand und hervorgehobenen Indizes.
     * @param currentArray Der aktuelle Zustand des Arrays.
     * @param accessedIndices Eine Liste von Indizes, die in diesem Schritt gelesen/verglichen wurden.
     * @param changedIndices Eine Liste von Indizes, die in diesem Schritt geschrieben/getauscht wurden.
     */
    // Überladener Konstruktor für einfacheren Aufruf mit Sets
    public SortStep(List<Integer> currentArray, Set<Integer> accessedIndices, Set<Integer> changedIndices) {
        this(currentArray, new ArrayList<>(accessedIndices), new ArrayList<>(changedIndices));
    }

    /**
     * Hauptkonstruktor.
     * @param currentArray Der aktuelle Zustand des Arrays.
     * @param accessedIndices Eine Liste von Indizes, die gelesen/verglichen wurden.
     * @param changedIndices Eine Liste von Indizes, die geschrieben/getauscht wurden.
     */
    public SortStep(List<Integer> currentArray, List<Integer> accessedIndices, List<Integer> changedIndices) {
        this.currentArray = currentArray;
        // Sicherstellen, dass die Listen nicht null sind
        this.accessedIndices = accessedIndices != null ? new ArrayList<>(accessedIndices) : new ArrayList<>();
        this.changedIndices = changedIndices != null ? new ArrayList<>(changedIndices) : new ArrayList<>();
    }


    /**
     * @return Der Zustand des Arrays in diesem Schritt.
     */
    public List<Integer> getCurrentArray() {
        return currentArray;
    }

    /**
     * @return Die Indizes, die in diesem Schritt gelesen/verglichen wurden.
     */
    public List<Integer> getAccessedIndices() {
        return accessedIndices;
    }

    /**
     * @return Die Indizes, die in diesem Schritt geschrieben/getauscht wurden.
     */
    public List<Integer> getChangedIndices() {
        return changedIndices;
    }
}