package ru.gelman.expression;

import ru.gelman.exceptions.UnexpectedTokenException;
import ru.gelman.exceptions.expression.EmptyExpressionException;

import java.util.*;


public class ExpressionParser {
    private static final Set<String> SEPARATORS = Operations.asSet();

    private final String expression;

    public ExpressionParser(String expression) {
        if (expression.isEmpty()) {
            throw new EmptyExpressionException("Попытка инициализировать пустое выражение!");
        }
        this.expression = expression.replace(" ", "");
    }

    public List<String> getTokens() {
        StringTokenizer tokenizer = new StringTokenizer(expression, SEPARATORS.toString(), true);
        List<String> tokens = new ArrayList<>();

        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            if (NumberUtils.isNumber(token) || SEPARATORS.contains(token)) {
                tokens.add(token);
            } else {
                throw new UnexpectedTokenException("Неизвестный токен: " + token + " в " + expression);
            }
        }

        return tokens;
    }

    public List<String> getPostfixForm() {
        List<String> postfixForm = new ArrayList<>();
        Deque<String> operationStack = new ArrayDeque<>();
        List<String> tokens = getTokens();

        for (String token : tokens) {
            if (NumberUtils.isNumber(token)) {
                postfixForm.add(token);
            } else if (SEPARATORS.contains(token)) {
                while (!operationStack.isEmpty() && getOperationPriority(operationStack.peek()) >= getOperationPriority(token)) {
                    postfixForm.add(operationStack.pop());
                }
                operationStack.push(token);
            }
        }

        while (!operationStack.isEmpty()) {
            postfixForm.add(operationStack.pop());
        }

        return postfixForm;
    }

    private int getOperationPriority(String operationToken) {
        switch (operationToken) {
            case Operations.SUM, Operations.SUBTRACTION -> {
                return 1;
            }
            case Operations.MULTIPLICATION, Operations.DIVISION -> {
                return 2;
            }
            default -> throw new UnsupportedOperationException("Данная операция не поддерживается: " + operationToken);
        }
    }
}
