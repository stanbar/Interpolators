import java.util.List;

/**
 * Created by stasbar on 14.05.2017.
 * https://en.wikipedia.org/wiki/Monotone_cubic_interpolation
 */
public class CubicSplineInterpolator implements Interpolatable {

    private final List<Double> distances;
    private final List<Double> heights;
    private double[] m;

    public CubicSplineInterpolator(List<Double> x, List<Double> y) {
        distances = x;
        heights = y;
        setup(x, y);
    }

    public void setup(List<Double> x, List<Double> y) {
        if (x == null || y == null || x.size() != y.size() || x.size() < 2) {
            throw new IllegalArgumentException("There must be at least two control " +
                    "points and the arrays must be of equal length.");
        }

        final int size = x.size();
        double[] slopes = new double[size - 1];
        m = new double[size];

        for (int i = 0; i < size - 1; i++) {
            double dx = x.get(i + 1) - x.get(i);
            if (dx <= 0f) {
                throw new IllegalArgumentException("X points must be increasing");
            }
            slopes[i] = (y.get(i + 1) - y.get(i)) / dx;
        }


        m[0] = slopes[0];
        for (int i = 1; i < size - 1; i++) {
            m[i] = (slopes[i - 1] + slopes[i]) / 2d;
        }
        m[size - 1] = slopes[size - 2];

        for (int i = 0; i < size - 1; i++) {
            if (slopes[i] == 0f) {
                m[i] = 0f;
                m[i + 1] = 0f;
            } else {
                double alpha = m[i] / slopes[i];
                double beta = m[i + 1] / slopes[i];
                double h = Math.hypot(alpha, beta);
                if (h > 9f) {
                    double t = 3f / h;
                    m[i] = t * alpha * slopes[i];
                    m[i + 1] = t * beta * slopes[i];
                }
            }
        }
    }

    @Override
    public double interpolate(double x) {
        // Handle the boundary cases.
        final int n = distances.size();
        if (Double.isNaN(x))
            return x;

        if (x <= distances.get(0))
            return heights.get(0);

        if (x >= distances.get(n - 1))
            return heights.get(n - 1);


        int i = 0;
        //Since the Xes are increasing we can limit iteration
        while (x >= distances.get(i + 1)) {
            i += 1;
            if (x == distances.get(i))
                return heights.get(i);
        }

        //Cubic Hermite spline interpolation.
        //now we know that the x value is between i and i+1
        double xupper = distances.get(i + 1);
        double xlower = distances.get(i);
        double h = xupper - xlower;
        double t = (x - xlower) / h;
        return (heights.get(i) * (3 * t) + h * m[i] * t) * Math.pow(1 - t, 2)
                + (heights.get(i + 1) * t + h * m[i + 1] * (t - 1)) * Math.pow(t, 2);
    }

    @Override
    public String getMethodName() {
        return "Cubic Spline";
    }

}
