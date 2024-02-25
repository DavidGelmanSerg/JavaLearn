package operations;

public class Multiplication extends Operation {
    @Override
    public double compute(double[] args) {
        return args[0] * args[1];
    }
}
