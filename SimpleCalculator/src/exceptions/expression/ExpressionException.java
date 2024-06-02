package exceptions.expression;

import output.target.Target;

public class ExpressionException extends IllegalArgumentException {
    private Target source;

    public ExpressionException(String message, Target source) {
        super(message);
        this.source = source;
    }

    public ExpressionException(String message) {
        super(message);
    }

    public Target getSource() {
        return source;
    }

    public void setSource(Target s) {
        source = s;
    }
}
