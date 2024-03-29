package calculator;

import calculator.exceptions.DivisionByZeroException;
import calculator.operations.Operation;

public class Calculator {
    private Operation operation;

    public Calculator(Operation operation) {
        this.operation = operation;
    }

    public double calculate() throws DivisionByZeroException {
        double value = operation.compute();
        if (value == Double.POSITIVE_INFINITY || value == Double.NEGATIVE_INFINITY) {
            throw new DivisionByZeroException();
        }
        return operation.compute();
    }
}
