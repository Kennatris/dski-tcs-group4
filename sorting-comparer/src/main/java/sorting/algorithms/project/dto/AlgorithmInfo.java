package sorting.algorithms.project.dto;

// (Erstelle diese neue Datei im 'dto' Verzeichnis)

public class AlgorithmInfo {
    private String name;
    private String worstCase;
    private String averageCase;
    private String bestCase;

    // Konstruktor
    public AlgorithmInfo(String name, String worstCase, String averageCase, String bestCase) {
        this.name = name;
        this.worstCase = worstCase;
        this.averageCase = averageCase;
        this.bestCase = bestCase;
    }

    // Getter
    public String getName() { return name; }
    public String getWorstCase() { return worstCase; }
    public String getAverageCase() { return averageCase; }
    public String getBestCase() { return bestCase; }

    // (Setter werden nicht benötigt, wenn nur vom Server gesendet wird)
}