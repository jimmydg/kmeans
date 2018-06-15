package task;

import java.io.Serializable;
import java.util.List;

public class KMeansReturn implements Serializable {
    private Long runTime;
    private List<KMeans.Point2D> result;

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
}
