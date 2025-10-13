package sorting.algorithms.project.dto;

import java.util.List;

public class SortResult {
    private String algorithm;
    private long durationMillis;
    private List<Integer> sorted;

    public SortResult() {}

    public SortResult(String algorithm, long durationMillis, List<Integer> sorted) {
        this.algorithm = algorithm;
        this.durationMillis = durationMillis;
        this.sorted = sorted;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public long getDurationMillis() {
        return durationMillis;
    }

    public void setDurationMillis(long durationMillis) {
        this.durationMillis = durationMillis;
    }

    public List<Integer> getSorted() {
        return sorted;
    }

    public void setSorted(List<Integer> sorted) {
        this.sorted = sorted;
    }
}