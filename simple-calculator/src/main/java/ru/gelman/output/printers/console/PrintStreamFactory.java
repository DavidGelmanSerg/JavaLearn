package ru.gelman.output.printers.console;

import ru.gelman.exceptions.UnsupportedPrinterException;

import java.io.PrintStream;
import java.util.Objects;

public class PrintStreamFactory {
    public static final String SYSTEM = "system";

    public static PrintStream getPrintStream(String type) {
        if(Objects.requireNonNull(type).equals(SYSTEM)){
            return System.out;
        }
        throw new UnsupportedPrinterException("Неподдерживаемый тип принтера консоли " + type);
    }

    public static PrintStream getDefaultPrintStream() {
        return System.out;
    }
}
