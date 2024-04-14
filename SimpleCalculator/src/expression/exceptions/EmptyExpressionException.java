package expression.exceptions;

public class EmptyExpressionException extends IllegalArgumentException {
    public EmptyExpressionException(String message) {
        super(message);
    }
}
