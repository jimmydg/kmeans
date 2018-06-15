package task;

import KMeans.KMeans;

import java.io.Serializable;
import java.util.List;

public class KMeansReturn implements Serializable {
    private Long runTime;
    private List<KMeans.Point2D> result;


    private int numThreads;
    private int amountOfClusters;
    private String inputFile;

    public Long getRunTime() {
        return runTime;
    }

    public void setRunTime(Long runTime) {
        this.runTime = runTime;
    }

    public List<KMeans.Point2D> getResult() {
        return result;
    }

    public void setResult(List<KMeans.Point2D> result) {
        this.result = result;
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

    public String getInputFile() {
        return inputFile;
    }

    public void setInputFile(String inputFile) {
        this.inputFile = inputFile;
    }
}
