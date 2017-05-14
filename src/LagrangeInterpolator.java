
import java.util.List;

/**
 * Created by stasbar on 11.05.2017.
 */

public class LagrangeInterpolator implements Interpolatable {
    private List<Double> x;
    private List<Double> y;

    public LagrangeInterpolator(List<Double> x, List<Double> y) {
        this.x = x;
        this.y = y;
    }
    @Override
    public double interpolate(double a) {
        if (x.size() != y.size())
            throw new IllegalStateException("Different arrays length");
        int size = x.size();
        double sum = 0;
        for (int i = 0; i < size; i++) {
            double result = y.get(i);
            for (int j = 0; j < size; j++) {
                if (i == j)
                    continue;
                result *= a - x.get(j);
                result /= x.get(i) - x.get(j);
            }
            sum += result;
        }
        return sum;
    }

    @Override
    public String getMethodName() {
        return "Lagrange";
    }


}
