package expression;

import expression.exceptions.EmptyExpressionException;
import expression.exceptions.UnexpectedTokenException;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.StringTokenizer;


public class ExpressionParser {
    private static final String SEPARATORS = "*+-/";

    private final String expression;

    public ExpressionParser(String expression) {
        if (expression.isEmpty()) {
            throw new EmptyExpressionException("Попытка инициализировать пустое выражение!");
        }
        this.expression = expression.replace(" ", "");
    }

    public List<String> getTokens() {
        StringTokenizer tokenizer = new StringTokenizer(expression, SEPARATORS, true);
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
        Stack<String> opertationStack = new Stack<>();
        List<String> tokens = getTokens();

        for (String token : tokens) {
            if (NumberUtils.isNumber(token)) {
                postfixForm.add(token);
            } else if (SEPARATORS.contains(token)) {
                while (!opertationStack.isEmpty() && getOperationPriority(opertationStack.peek()) >= getOperationPriority(token)) {
                    postfixForm.add(opertationStack.pop());
                }
                opertationStack.push(token);
            }
        }

        while (!opertationStack.isEmpty()) {
            postfixForm.add(opertationStack.pop());
        }

        return postfixForm;
    }

    private int getOperationPriority(String operationToken) {
        switch (operationToken) {
            case "+", "-" -> {
                return 1;
            }
            case "*", "/", ":" -> {
                return 2;
            }
            default -> throw new UnsupportedOperationException("Данная операция не поддерживается: " + operationToken);
        }
    }
}
