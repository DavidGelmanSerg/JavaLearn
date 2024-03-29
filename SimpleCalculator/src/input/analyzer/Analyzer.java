package input.analyzer;

import calculator.operations.Operation;


public class Analyzer {
    private final String expression;

    public Analyzer(String expression) {
        this.expression = expression.replace(" ", "");
    }

    public boolean isValidExpression() {
        return expression.matches("-?\\d+(\\.\\d+)?[-+*/:]-?\\d+(\\.\\d+)?") || expression.matches("-?\\d+(\\.\\d+)?");
    }

    public Operation getOperations() {
        String[] splitExpression = expression.split("\\d+(\\.\\d+)?");
        Operation operation = Operation.getOperation(splitExpression[1]);

        operation.setOperand1(Double.parseDouble(expression.split("[-+*/:]")[0]));
        operation.setOperand2(Double.parseDouble(expression.split("[-+*/:]")[1]));

        return operation;
    }
}
