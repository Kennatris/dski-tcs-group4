package sorting.algorithms.project.dto;

import java.util.List;

public class CompareRequest {
    private List<String> algorithms;

    public CompareRequest() {}

    public CompareRequest(List<String> algorithms) {
        this.algorithms = algorithms;
    }

    public List<String> getAlgorithms() {
        return algorithms;
    }

    public void setAlgorithms(List<String> algorithms) {
        this.algorithms = algorithms;
    }
}