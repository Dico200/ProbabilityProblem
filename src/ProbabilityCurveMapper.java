import java.util.function.DoubleUnaryOperator;

public class ProbabilityCurveMapper implements DoubleUnaryOperator {
    private final double modifier;

    public ProbabilityCurveMapper(double modifier) {
        this.modifier = modifier;
    }

    public static double map(double random, double modifier) {
        return random / (1 - modifier * (random - 1));
    }

    public double getModifier() {
        return modifier;
    }

    @Override
    public double applyAsDouble(double random) {
        return map(random, modifier);
    }

}
