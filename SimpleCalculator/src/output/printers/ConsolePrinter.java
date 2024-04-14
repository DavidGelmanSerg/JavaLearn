package output.printers;

public class ConsolePrinter implements OutputPrinter {
    public void print(String value) {
        System.out.println(value);
    }
}
