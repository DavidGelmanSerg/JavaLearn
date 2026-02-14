package ru.gelman.printer;

import java.io.PrintStream;

public class ConsolePrinter implements Printer {
    private final PrintStream console;

    public ConsolePrinter() {
        console = System.out;
    }

    @Override
    public void print(String data) {
        console.print(data);
    }

    @Override
    public void printLine(String data) {
        console.println(data);
    }
}
