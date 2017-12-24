import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.function.DoubleUnaryOperator;

public class AverageTest {
    private static DoubleUnaryOperator mapper;
    private static RandomBoundedInteger random;

    @BeforeClass
    public static void init() {
        mapper = new ProbabilityCurveMapper(2.5);
        random = new RandomBoundedInteger(null, 4, mapper);
    }

    @Test
    public void testAverageComputation() {
        double approach = approachAverage(1_000_000);
        double compute = random.computeAverage();
        double difference = Math.abs(approach - compute);
        Assert.assertTrue(Math.log10(difference) < -5);
    }

    private static double approachAverage(int sampleCount) {
        long total = 0;
        for (int i = 0; i < sampleCount; i++) {
            double input = ((double) i) / sampleCount;
            total = total + (long) random.random(input);
        }
        return total / (double) sampleCount;
    }

}
