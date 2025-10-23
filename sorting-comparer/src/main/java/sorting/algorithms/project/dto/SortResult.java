package sorting.algorithms.project.dto;

import java.util.List;

/**
 * Speichert das Ergebnis eines einzelnen Sortiervorgangs.
 */
public class SortResult {
    private String algorithm;
    private long steps;
    private long durationMillis;
    private List<Integer> unsorted;
    private List<Integer> sorted;
    private String worstCase;
    private String averageCase;
    private String bestCase;

    /**
     * Erstellt ein neues Sortierergebnis.
     * @param algorithm Der Name des Algorithmus.
     * @param durationMillis Die Dauer der Sortierung in Millisekunden.
     * @param steps Die Anzahl der durchgeführten Schritte/Operationen.
     * @param unsorted Ein Auszug der unsortierten Liste.
     * @param sorted Ein Auszug der sortierten Liste.
     * @param worstCase Die Komplexität im Worst Case.
     * @param averageCase Die Komplexität im Average Case.
     * @param bestCase Die Komplexität im Best Case.
     */
    public SortResult(String algorithm, long durationMillis, long steps, List<Integer> unsorted, List<Integer> sorted,
                      String worstCase, String averageCase, String bestCase) {
        this.algorithm = algorithm;
        this.durationMillis = durationMillis;
        this.steps = steps;
        this.unsorted = unsorted;
        this.sorted = sorted;
        this.worstCase = worstCase;
        this.averageCase = averageCase;
        this.bestCase = bestCase;
    }

    /**
     * @return Der Name des Algorithmus.
     */
    public String getAlgorithm() { return algorithm; }
    /**
     * @param algorithm Der Name des Algorithmus.
     */
    public void setAlgorithm(String algorithm) { this.algorithm = algorithm; }

    /**
     * @return Die Anzahl der Schritte.
     */
    public long getSteps() { return steps; }
    /**
     * @param steps Die Anzahl der Schritte.
     */
    public void setSteps(long steps) { this.steps = steps; }

    /**
     * @return Die Dauer in Millisekunden.
     */
    public long getDurationMillis() { return durationMillis; }
    /**
     * @param durationMillis Die Dauer in Millisekunden.
     */
    public void setDurationMillis(long durationMillis) { this.durationMillis = durationMillis; }

    /**
     * @return Ein Auszug der unsortierten Liste.
     */
    public List<Integer> getUnsorted() { return unsorted; }
    /**
     * @param unsorted Ein Auszug der unsortierten Liste.
     */
    public void setUnsorted(List<Integer> unsorted) { this.unsorted = unsorted; }

    /**
     * @return Ein Auszug der sortierten Liste.
     */
    public List<Integer> getSorted() { return sorted; }
    /**
     * @param sorted Ein Auszug der sortierten Liste.
     */
    public void setSorted(List<Integer> sorted) { this.sorted = sorted; }

    /**
     * @return Die Komplexität im Worst Case.
     */
    public String getWorstCase() { return worstCase; }
    /**
     * @param worstCase Die Komplexität im Worst Case.
     */
    public void setWorstCase(String worstCase) { this.worstCase = worstCase; }

    /**
     * @return Die Komplexität im Average Case.
     */
    public String getAverageCase() { return averageCase; }
    /**
     * @param averageCase Die Komplexität im Average Case.
     */
    public void setAverageCase(String averageCase) { this.averageCase = averageCase; }

    /**
     * @return Die Komplexität im Best Case.
     */
    public String getBestCase() { return bestCase; }
    /**
     * @param bestCase Die Komplexität im Best Case.
     */
    public void setBestCase(String bestCase) { this.bestCase = bestCase; }
}