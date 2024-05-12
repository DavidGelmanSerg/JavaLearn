package output.printers.exceptions;

public class UnsupportedPrinterException extends IllegalArgumentException {
    public UnsupportedPrinterException(String message) {
        super(message);
    }
}
