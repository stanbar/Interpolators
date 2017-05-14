import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static DataVector data;
    private static DataVector staggeredData;
    private static int stepSize = 2;

    public static void main(String[] args) throws IOException {
        loadMountains();
        DataVector dataVectorLagrange = interpolateData(new LagrangeInterpolator(staggeredData.getDistances(), staggeredData.getHeights()));
        dataVectorLagrange.print("Lagrange");

        DataVector dataVectorCubic = interpolateData(new CubicSplineInterpolator(staggeredData.getDistances(), staggeredData.getHeights()));
        dataVectorCubic.print("CubicSpline");

    }



    private static DataVector interpolateData(Interpolatable interpolatable) {
        DataVector results = new DataVector(new ArrayList<>(data.getDistances()), new ArrayList<>(data.getHeights()));

        double dm = 0;
        for (int i = 1; i < data.getDistances().size(); i += stepSize) {
            double interpolatedValue = interpolatable.interpolate(data.getDistances().get(i));
            double delta = Math.abs(interpolatedValue - data.getHeights().get(i));
            dm += Math.pow(delta, 2);
            results.getHeights().set(i, interpolatedValue);
        }

        System.out.printf("%s Error\t %f\n",interpolatable.getMethodName(), Math.sqrt(dm));
        return results;
    }

    private static void loadMountains() {
        try {
            data = loadData("/Users/admin1/Downloads/Maps/data/Mountains50.csv");
            int size = data.getHeights().size() / 2;
            double[] staggeredDistances = new double[size];
            double[] staggeredHeights = new double[size];
            for (int i = 0; i < data.getHeights().size(); i += 2) {
                staggeredDistances[i / 2] = data.getDistances().get(i);
                staggeredHeights[i / 2] = data.getHeights().get(i);
            }
            staggeredData = new DataVector(staggeredDistances, staggeredHeights);
            System.out.printf("Loaded %d items and staggered to %d \n", data.getDistances().size(), staggeredData.getDistances().size());
        } catch (IOException ignored) {
        }
    }
    private static DataVector loadData(String path) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(path));
        double[] x = new double[lines.size()];
        double[] y = new double[lines.size()];
        int index = 0;
        for (String line : lines) {
            String[] values = line.split(",");
            double distance = Double.parseDouble(values[0]);
            double height = Double.parseDouble(values[1]);
            x[index] = distance;
            y[index] = height;
            index++;

        }
        return new DataVector(x, y);
    }

}
