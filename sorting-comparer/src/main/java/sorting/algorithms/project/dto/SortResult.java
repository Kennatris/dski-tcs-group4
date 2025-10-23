package sorting.algorithms.project.dto;

import java.util.List;

public class SortResult {
    private String algorithm;
    private long steps;
    private long durationMillis;
    private List<Integer> unsorted;
    private List<Integer> sorted;
    private String worstCase;
    private String averageCase;
    private String bestCase;


    // Konstruktor
    public SortResult(String algorithm, long durationMillis, long steps, List<Integer> unsorted, List<Integer> sorted, // UM KORRIGIERT
                      String worstCase, String averageCase, String bestCase) {
        this.algorithm = algorithm;
        this.durationMillis = durationMillis;
        this.steps = steps; // UM KORRIGIERT
        this.unsorted = unsorted;
        this.sorted = sorted;
        this.worstCase = worstCase;
        this.averageCase = averageCase;
        this.bestCase = bestCase;
    }

    // Getter & Setter
    public String getAlgorithm() { return algorithm; }
    public void setAlgorithm(String algorithm) { this.algorithm = algorithm; }

    // GETTER KORRIGIERT
    public long getSteps() { return steps; }
    // SETTER KORRIGIERT (Name und Logik)
    public void setSteps(long steps) { this.steps = steps; }

    public long getDurationMillis() { return durationMillis; }
    public void setDurationMillis(long durationMillis) { this.durationMillis = durationMillis; }

    public List<Integer> getUnsorted() { return unsorted; }
    public void getUnsorted(List<Integer> unsorted) { this.unsorted = unsorted; }

    public List<Integer> getSorted() { return sorted; }
    public void setSorted(List<Integer> sorted) { this.sorted = sorted; }

    public String getWorstCase() { return worstCase; }
    public void setWorstCase(String worstCase) { this.worstCase = worstCase; }

    public String getAverageCase() { return averageCase; }
    public void setAverageCase(String averageCase) { this.averageCase = averageCase; }

    public String getBestCase() { return bestCase; }
    public void setBestCase(String bestCase) { this.bestCase = bestCase; }
}