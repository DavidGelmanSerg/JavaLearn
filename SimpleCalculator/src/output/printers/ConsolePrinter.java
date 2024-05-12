package output.printers;

import java.io.PrintStream;

public class ConsolePrinter implements OutputPrinter {
    PrintStream out;
    public ConsolePrinter(PrintStream out)
    {
        this.out = out;
    }
    public void print(String value) {
        out.println(value);
    }
}
