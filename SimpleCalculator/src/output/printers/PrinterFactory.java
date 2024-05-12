package output.printers;

import output.printers.exceptions.UnsupportedPrinterException;

import javax.swing.text.JTextComponent;
import java.io.PrintStream;

public class PrinterFactory {
    public static final String UI = "-u";
    public static final String CONSOLE = "-c";
    public static final String FILE = "-f";

    public static OutputPrinter getPrinter(String printerType, Object option) {
        switch (printerType) {
            case FILE -> {
                return new FilePrinter(String.valueOf(option));
            }
            case CONSOLE -> {
                return new ConsolePrinter((PrintStream) option);
            }
            case UI -> {
                return new JTextComponentPrinter((JTextComponent) option);
            }
            default -> throw new UnsupportedPrinterException("Неизвестный тип принтера: " + printerType);
        }
    }
}

