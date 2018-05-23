import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class KMeans {
    private static final int REPLICATION_FACTOR = 200;

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: KMeans <INPUT_FILE> <K>");
            System.exit(-1);
        }
        String inputFile = args[0];
        int k = Integer.valueOf(args[1]);

        List<Point2D> dataset = null;
        try {
            dataset = getDataset(inputFile);
        } catch (Exception e) {
            System.err.println("ERROR: Could not read file " + inputFile);
            System.exit(-1);
        }

        List<Point2D> centers = initializeRandomCenters(k, 0, 1000000);
        long start = System.currentTimeMillis();
        List<Point2D> finalCenters = kmeans(centers, dataset, k);
        System.out.println("Final clusters for file: " + inputFile + ":");
        System.out.println(finalCenters);
        System.out.println();
        System.out.println("Time elapsed: " + (System.currentTimeMillis() - start) + "ms");
    }

    /**
     * Receives an input file, splits the file by comma (",") and returns each coordinate as a float and create a new
     * Point2D instance.
     *
     * @param inputFile
     * @return
     * @throws Exception
     */
    public static List<Point2D> getDataset(String inputFile) throws Exception {
        List<Point2D> dataset = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(inputFile));
        String line;

        while ((line = br.readLine()) != null) {
            String[] tokens = line.split(",");
            float x = Float.valueOf(tokens[0]);
            float y = Float.valueOf(tokens[1]);
            Point2D point = new Point2D(x, y);
            //Only add a max amount to better observe parallelization
            for (int i = 0; i < REPLICATION_FACTOR; i++)
            {
                dataset.add(point);
            }
        }
        br.close();
        return dataset;
    }

    /**
     * Randomly initialize n centers/stages
     * @param n
     * @param lowerBound
     * @param upperBound
     * @return
     */
    public static List<Point2D> initializeRandomCenters(int n, int lowerBound, int upperBound) {
        List<Point2D> centers = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            float x = (float) (Math.random() * (upperBound - lowerBound) + lowerBound);
            float y = (float) (Math.random() * (upperBound - lowerBound) + lowerBound);
            Point2D point = new Point2D(x, y);
            centers.add(point);
        }
        return centers;
    }

    /**
     * Compute the final centers. The do/while loop checks if the algorithm converged. Which means it only finishes
     * when the new centers are qual to the old center
     * @param centers randomly initialized centers
     * @param dataset set of point
     * @param k number of clusters
     * @return list of final centers
     */
    public static List<Point2D> kmeans(List<Point2D> centers, List<Point2D> dataset, int k) {
        boolean converged;
        do {
            List<Point2D> newCenters = getNewCenters(dataset, centers);
            double dist = getDistance(centers, newCenters);
            centers = newCenters;
            converged = dist == 0;
        } while (!converged);
        return centers;
    }


    /**
     *
     * @param dataset
     * @param centers list of clusters
     * @return a list with the mean of each cluster
     */
    public static List<Point2D> getNewCenters(List<Point2D> dataset, List<Point2D> centers) {
        List<List<Point2D>> clusters = new ArrayList<>(centers.size());

        for (int i = 0; i < centers.size(); i++) {
            clusters.add(new ArrayList<Point2D>());
        }

        for (Point2D data : dataset) {
            int index = data.getNearestPointIndex(centers);
            clusters.get(index).add(data);
        }

        List<Point2D> newCenters = new ArrayList<>(centers.size());
        for (List<Point2D> cluster : clusters) {
            newCenters.add(Point2D.getMean(cluster));
        }

        return newCenters;
    }

    public static double getDistance(List<Point2D> oldCenters, List<Point2D> newCenters) {
        double accumDist = 0;
        for (int i = 0; i < oldCenters.size(); i++) {
            double dist = oldCenters.get(i).getDistance(newCenters.get(i));
            accumDist += dist;
        }
        return accumDist;
    }

    /**
     * Inner class to representate the data
     */
    public static class Point2D {

        private float x;
        private float y;

        public Point2D(float x, float y) {
            this.x = x;
            this.y = y;
        }

        /**
         * Calculate the eucledian distance between two points
         *
         * @param other
         * @return
         */
        private double getDistance(Point2D other) {
            return Math.sqrt(Math.pow(this.x - other.x, 2)
                    + Math.pow(this.y - other.y, 2));
        }

        /**
         * Retrieve the index of the nearest point in a list
         *
         * @param points
         * @return
         */
        public int getNearestPointIndex(List<Point2D> points) {
            int index = -1;
            double minDist = Double.MAX_VALUE;
            for (int i = 0; i < points.size(); i++) {
                double dist = this.getDistance(points.get(i));
                if (dist < minDist) {
                    minDist = dist;
                    index = i;
                }
            }
            return index;
        }

        /**
         * Retrieves a list of points and returns the main point of that list
         * This is used to calculate new centers
         *
         * @param points
         * @return
         */
        public static Point2D getMean(List<Point2D> points) {
            float accumX = 0;
            float accumY = 0;
            if (points.size() == 0) return new Point2D(accumX, accumY);
            for (Point2D point : points) {
                accumX += point.x;
                accumY += point.y;
            }
            return new Point2D(accumX / points.size(), accumY / points.size());
        }

        @Override
        public String toString() {
            return "[" + this.x + "," + this.y + "]";
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null || !(obj.getClass() != Point2D.class)) {
                return false;
            }
            Point2D other = (Point2D) obj;
            return this.x == other.x && this.y == other.y;
        }

    }


}
