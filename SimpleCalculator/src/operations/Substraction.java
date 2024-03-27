package operations;

public class Substraction extends Operation {
    @Override
    public double compute(double[] args) {
        return args[0] - args[1];
    }
}
