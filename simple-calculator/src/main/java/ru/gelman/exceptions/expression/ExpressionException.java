package ru.gelman.exceptions.expression;

import ru.gelman.output.target.Target;

public class ExpressionException extends IllegalArgumentException {
    private Target source;

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
