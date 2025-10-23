package sorting.algorithms.project.dto;

import java.util.List;

/**
 * Repräsentiert eine Anfrage zum Vergleichen von Algorithmen.
 */
public class CompareRequest {
    private List<String> algorithms;
    private List<Integer> input;

    /**
     * Standardkonstruktor.
     */
    public CompareRequest() {}

    /**
     * Erstellt eine neue Vergleichsanfrage.
     * @param algorithms Die Namen der zu vergleichenden Algorithmen.
     * @param input Die Eingabeliste, die sortiert werden soll.
     */
    public CompareRequest(List<String> algorithms, List<Integer> input) {
        this.algorithms = algorithms;
        this.input = input; // Input wird im Konstruktor gesetzt
    }

    /**
     * @return Die Liste der Algorithmusnamen.
     */
    public List<String> getAlgorithms() {
        return algorithms;
    }

    /**
     * @param algorithms Die Liste der Algorithmusnamen.
     */
    public void setAlgorithms(List<String> algorithms) {
        this.algorithms = algorithms;
    }

    /**
     * @return Die Eingabeliste.
     */
    public List<Integer> getInput() {
        return input;
    }

    /**
     * @param input Die Eingabeliste.
     */
    public void setInput(List<Integer> input) {
        this.input = input;
    }
}