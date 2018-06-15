package task;

import java.io.Serializable;

public class KMeansParams implements Serializable {
    private int numThreads;
    private int amountOfClusters;
    private String inputFile;

    public KMeansParams(int numThreads, int amountOfClusters, String inputFile) {
        this.numThreads = numThreads;
        this.amountOfClusters = amountOfClusters;
        this.inputFile = inputFile;
    }

    public KMeansParams(){}

    public String getInputFile() {
        return inputFile;
    }

    public void setInputFile(String inputFile) {
        this.inputFile = inputFile;
    }

    public int getNumThreads() {
        return numThreads;
    }

    public void setNumThreads(int numThreads) {
        this.numThreads = numThreads;
    }

    public int getAmountOfClusters() {
        return amountOfClusters;
    }

    public void setAmountOfClusters(int amountOfClusters) {
        this.amountOfClusters = amountOfClusters;
    }

}
