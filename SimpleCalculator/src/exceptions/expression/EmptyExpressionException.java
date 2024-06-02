package exceptions.expression;

import output.target.Target;

public class EmptyExpressionException extends ExpressionException {

    public EmptyExpressionException(String message) {
        super(message);
    }

    public EmptyExpressionException(String message, Target source) {
        super(message, source);
    }
}
