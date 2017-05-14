import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

/**
 * Created by stasbar on 12.05.2017.
 */
public class DataVector {
    private List<Double> distances, heights;

    public DataVector(List<Double> distances, List<Double> heights) {
        this.distances = distances;
        this.heights = heights;
    }

    public DataVector(double[] distances, double[] heights) {
        this.distances = DoubleStream.of(distances).boxed().collect(Collectors.toList());
        this.heights = DoubleStream.of(heights).boxed().collect(Collectors.toList());
    }

    public List<Double> getDistances() {
        return distances;
    }

    public List<Double> getHeights() {
        return heights;
    }

    public void print(String title) {
        System.out.println(title);
        for (int i = 0; i < distances.size(); i++)
            System.out.println(heights.get(i));
        System.out.println("End of "+title);

    }
}
