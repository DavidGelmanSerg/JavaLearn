package operations;

public class Sum extends Operation {
    @Override
    public double compute(double[] args) {
        return args[0] + args[1];
    }
}
