import java.util.Random;
import java.util.function.DoubleUnaryOperator;

public class RandomBoundedInteger {
    private final Random random;
    private final int bound;
    private final DoubleUnaryOperator mapper;

    public RandomBoundedInteger(Random random, int bound, DoubleUnaryOperator mapper) {
        this.random = random == null ? new Random() : random;
        if (bound <= 0) throw new IllegalArgumentException("bound <= 0");
        this.bound = bound;
        this.mapper = mapper;
    }

    public static double computeAverage(int bound, double modifier) {
        // computes average by derivation of the euler sum
        // into a simpler form. Should have significantly the same result as preciseAverage(bound, modifier)

        int section = bound + 1;
        double primary = bound * sectionStart(bound, modifier, section);

        double eulerSum = 0;
        for (section = 1; section <= bound; section++) {
            eulerSum += sectionStart(bound, modifier, section);
        }

        return primary - eulerSum;
    }

    /*
    public static double preciseAverage(int bound, double modifier) {
        // computes average by adding each section's area together
        double eulerSum = 0;
        for (int section = 1; section <= bound; section++) {
            eulerSum += sectionArea(bound, modifier, section);
        }
        return eulerSum;
    }

    private static double sectionArea(int bound, double modifier, int section) {
        return section * sectionLength(bound, modifier, section);
    }

    private static double sectionLength(int bound, double modifier, int section) {
        return sectionStart(bound, modifier, section + 1) - sectionStart(bound, modifier, section);
    }
    */

    private static double sectionStart(int bound, double modifier, int section) {
        // (ax + x) / (b + ax + 1)
        // x: section
        // a: modifier
        // b: bound
        // solution for x in mapper.applyAsDouble(x) = section / (bound + 1)
        double denominator = (double) bound + modifier * (double) section + 1;
        return (double) section * (modifier + 1) / denominator;
    }

    public double computeAverage() {
        double modifier;
        if (mapper instanceof ProbabilityCurveMapper) {
            modifier = ((ProbabilityCurveMapper) mapper).getModifier();
        } else if (mapper == null) {
            modifier = 0;
        } else {
            throw new IllegalStateException();
        }
        return computeAverage(bound, modifier);
    }

    public int random() {
        return random(random.nextDouble());
    }

    public int random(double input) {
        return (int) (mapper.applyAsDouble(input) * (bound + 1));
    }

}
