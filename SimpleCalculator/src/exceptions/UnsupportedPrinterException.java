package exceptions;

public class UnsupportedPrinterException extends IllegalArgumentException {
    public UnsupportedPrinterException(String message) {
        super(message);
    }
}
