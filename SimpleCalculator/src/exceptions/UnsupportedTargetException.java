package exceptions;

import output.target.Target;

public class UnsupportedTargetException extends IllegalArgumentException {
    private Target source;

    public UnsupportedTargetException(String message) {
        super(message);
    }
}
