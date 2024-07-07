package output.printers.console;

import exceptions.expression.EmptyExpressionException;
import output.printers.OutputPrinter;

import java.io.PrintStream;

public class ConsolePrinter implements OutputPrinter {
    private final PrintStream out;

    public ConsolePrinter(PrintStream out) {
        this.out = out;
    }

    public void print(String value) {
        if (value.isEmpty()) {
            throw new EmptyExpressionException("Пустое выражение!");
        }
        out.println(value);
    }
}
