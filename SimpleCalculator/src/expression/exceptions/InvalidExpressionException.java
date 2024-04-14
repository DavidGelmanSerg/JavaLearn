package expression.exceptions;

public class InvalidExpressionException extends IllegalArgumentException {
    public InvalidExpressionException(String message) {
        super(message);
    }
}
