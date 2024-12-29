package ru.gelman.output.printers;

import ru.gelman.output.printers.console.ConsolePrinter;
import ru.gelman.output.printers.console.PrintStreamFactory;
import ru.gelman.output.target.TargetType;
import ru.gelman.output.target.ui.CalculatorFrame;

import java.io.File;
import java.io.PrintStream;

public class PrinterFactory {
    public static OutputPrinter getDefaultPrinter() {
        return new ConsolePrinter(PrintStreamFactory.getDefaultPrintStream());
    }

    public static OutputPrinter getPrinter(TargetType targetType, Object option) {
        return switch (targetType) {
            case FILE -> new FilePrinter((File) option);
            case CONSOLE -> new ConsolePrinter((PrintStream) option);
            case UI -> new CalculatorFramePrinter((CalculatorFrame) option);
        };
    }
}


