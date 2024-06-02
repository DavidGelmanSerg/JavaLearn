package exceptions.expression;

import output.target.Target;

public class InvalidExpressionException extends ExpressionException {
    public InvalidExpressionException(String message, Target t) {
        super(message, t);
    }

    public InvalidExpressionException(String message) {
        super(message);
    }
}
