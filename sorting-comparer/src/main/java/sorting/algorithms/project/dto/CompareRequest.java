package sorting.algorithms.project.dto;

import java.util.List;

public class CompareRequest {
    private List<String> algorithms;

    private List<Integer> input;

    public CompareRequest() {}

    public CompareRequest(List<String> algorithms, List<Integer> input) {
        this.algorithms = algorithms;
    }

    public List<String> getAlgorithms() {
        return algorithms;
    }

    public void setAlgorithms(List<String> algorithms) {
        this.algorithms = algorithms;
    }

    public List<Integer> getInput() {
        return input;
    }

    public void setInput(List<Integer> input) {
        this.input = input;
    }
}