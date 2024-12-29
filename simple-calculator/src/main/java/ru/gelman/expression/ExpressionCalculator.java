package ru.gelman.expression;

import ru.gelman.exceptions.expression.InvalidExpressionException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;

public class ExpressionCalculator {
    public static BigDecimal calculate(List<String> tokens) {
        Stack<BigDecimal> operands = new Stack<>();

        for (String token : tokens) {
            if (NumberUtils.isNumber(token)) {
                operands.push(new BigDecimal(token));
            } else {
                try {
                    BigDecimal second = operands.pop();
                    BigDecimal first = operands.pop();

                    switch (token) {
                        case "+" -> operands.push(first.add(second));
                        case "-" -> operands.push(first.subtract(second));
                        case "*" -> operands.push(first.multiply(second));
                        case "/" -> operands.push(first.divide(second, 2, RoundingMode.DOWN));
                        default ->
                                throw new UnsupportedOperationException("Данная операция не поддерживается: " + token);
                    }
                } catch (EmptyStackException e) {
                    throw new InvalidExpressionException("Выражение введено неверно");
                }
            }
        }

        if (operands.size() != 1) {
            throw new InvalidExpressionException("Выражение введено неверно");
        }

        return operands.pop();
    }
}
