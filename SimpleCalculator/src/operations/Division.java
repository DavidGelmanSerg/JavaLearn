package operations;

public class Division extends Operation{
    @Override
    public double compute(double[] args) {
        return args[0] / args[1];
    }
}
