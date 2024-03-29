package calculator.operations;

public class Multiplication extends Operation {
    @Override
    public double compute() {
        return operand1 * operand2;
    }

    @Override
    public void setOperand1(double operand) {
        this.operand1 = operand;
    }

    @Override
    public void setOperand2(double operand) {
        this.operand2 = operand;
    }
}
