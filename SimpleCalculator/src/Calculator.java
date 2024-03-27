import exceptions.ExpressionNotValidException;
import operations.Operation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calculator {
    private String expression;

    public Calculator(String _expression) {
        expression = _expression.replace(" ", "");
    }

    public Calculator() {
    }

    public Double calculate() {
        try {
            return Double.parseDouble(expression);
        } catch (NumberFormatException e) {
            Operation operation = Operation.getOperation(expression.split("-?\\d+(\\.\\d+)?")[1]);

            Pattern pattern = Pattern.compile("-?\\d+");
            Matcher matcher = pattern.matcher(expression);
            double[] args = new double[2];
            int i = 0;
            while (matcher.find()) {
                args[i++] = Double.parseDouble(expression.substring(matcher.start(), matcher.end()));
            }

            return operation.compute(args);
        }
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String newExpression) throws ExpressionNotValidException {
        if (newExpression.matches("-?\\d+(\\.\\d+)?[-+*/:]-?\\d+(\\.\\d+)?") || newExpression.matches(("-?\\d+(\\.\\d+)?")))
            expression = newExpression;
        else
            throw new ExpressionNotValidException("Выражение введено неверно. Попробуйте ввести его в формате: число [-+*/:] число");
    }

}
