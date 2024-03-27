package operations;

public abstract class Operation {
    public Operation() {
    }

    public static Operation getOperation(String operation) {
        return switch (operation) {
            case "+" -> new Sum();
            case "-" -> new Substraction();
            case "*" -> new Multiplication();
            default -> new Division();
        };
    }

    public abstract double compute(double[] args);
}
