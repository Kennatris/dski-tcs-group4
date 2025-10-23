package sorting.algorithms.project.dto;

/**
 * Enthält Metadaten über einen Sortieralgorithmus.
 */
public class AlgorithmInfo {
    private String name;
    private String worstCase;
    private String averageCase;
    private String bestCase;

    /**
     * Erstellt eine neue Informationsinstanz für einen Algorithmus.
     * @param name Der Anzeigename des Algorithmus.
     * @param worstCase Die Komplexität im Worst Case (z.B. "O(n²)")
     * @param averageCase Die Komplexität im Average Case (z.B. "O(n log n)")
     * @param bestCase Die Komplexität im Best Case (z.B. "O(n)")
     */
    public AlgorithmInfo(String name, String worstCase, String averageCase, String bestCase) {
        this.name = name;
        this.worstCase = worstCase;
        this.averageCase = averageCase;
        this.bestCase = bestCase;
    }

    /**
     * @return Der Anzeigename des Algorithmus.
     */
    public String getName() { return name; }
    /**
     * @return Die Komplexität im Worst Case.
     */
    public String getWorstCase() { return worstCase; }
    /**
     * @return Die Komplexität im Average Case.
     */
    public String getAverageCase() { return averageCase; }
    /**
     * @return Die Komplexität im Best Case.
     */
    public String getBestCase() { return bestCase; }
}