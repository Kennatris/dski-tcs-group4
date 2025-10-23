package sorting.algorithms.project.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set; // Import Set for constructor convenience

/**
 * Data Transfer Object (DTO) encapsulating a single step in a sorting process,
 * intended for visualization purposes. It includes the state of the array at that step
 * and highlights indices that were accessed (read/compared) or changed (written/swapped).
 */
public class SortStep {

    // The state of the entire list at this specific step.
    private List<Integer> currentArray;
    // Indices that were read or compared during this step.
    private List<Integer> accessedIndices;
    // Indices that were written to or involved in a swap during this step.
    private List<Integer> changedIndices;

    /**
     * Constructs a SortStep with only the array state.
     * Assumes no specific indices were accessed or changed (e.g., initial/final state).
     * @param currentArray The current state of the list.
     */
    public SortStep(List<Integer> currentArray) {
        // Delegate to the main constructor with empty lists for indices.
        this(currentArray, Collections.emptyList(), Collections.emptyList());
    }

    /**
     * Convenience constructor that accepts Sets for indices.
     * Converts the Sets to ArrayLists.
     * @param currentArray The current state of the list.
     * @param accessedIndices A Set of indices accessed in this step.
     * @param changedIndices A Set of indices changed in this step.
     */
    public SortStep(List<Integer> currentArray, Set<Integer> accessedIndices, Set<Integer> changedIndices) {
        // Delegate to the main constructor, converting Sets to ArrayLists.
        this(currentArray, new ArrayList<>(accessedIndices), new ArrayList<>(changedIndices));
    }

    /**
     * Main constructor for creating a SortStep.
     * @param currentArray The current state of the list. Should be a snapshot/copy.
     * @param accessedIndices A List of indices accessed (read/compared) in this step.
     * @param changedIndices A List of indices changed (written/swapped) in this step.
     */
    public SortStep(List<Integer> currentArray, List<Integer> accessedIndices, List<Integer> changedIndices) {
        // Store the provided array state.
        this.currentArray = currentArray;
        // Ensure index lists are non-null; create new ArrayLists to prevent external modification.
        this.accessedIndices = accessedIndices != null ? new ArrayList<>(accessedIndices) : new ArrayList<>();
        this.changedIndices = changedIndices != null ? new ArrayList<>(changedIndices) : new ArrayList<>();
    }


    // --- Getters ---

    /**
     * Gets the state of the list at this step.
     * @return The list representing the array state.
     */
    public List<Integer> getCurrentArray() {
        return currentArray;
    }

    /**
     * Gets the list of indices accessed (read/compared) in this step.
     * @return The list of accessed indices.
     */
    public List<Integer> getAccessedIndices() {
        return accessedIndices;
    }

    /**
     * Gets the list of indices changed (written/swapped) in this step.
     * @return The list of changed indices.
     */
    public List<Integer> getChangedIndices() {
        return changedIndices;
    }

    // No setters needed as this DTO is immutable after creation.
}