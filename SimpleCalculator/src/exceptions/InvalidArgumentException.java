package exceptions;

public class InvalidArgumentException extends IllegalArgumentException {
    public InvalidArgumentException(String argument) {
        super(argument);
    }
}
