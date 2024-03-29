package calculator.operations;

public abstract class Operation {
    protected double operand1;
    protected double operand2;

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

    public abstract double compute();

    public abstract void setOperand1(double operand);

    public abstract void setOperand2(double operand);
}
