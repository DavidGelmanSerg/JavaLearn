package output.printers.console;

import exceptions.UnsupportedPrinterException;

import java.io.PrintStream;

public class PrintStreamFactory {
    public static final String SYSTEM = "system";

    public static PrintStream getPrintStream(String type) {
        return switch (type) {
            case SYSTEM -> System.out;
            default -> throw new UnsupportedPrinterException("Неподдерживаемый тип принтера консоли " + type);
        };
    }

    public static PrintStream getDefaultPrintStream() {
        return System.out;
    }
}
