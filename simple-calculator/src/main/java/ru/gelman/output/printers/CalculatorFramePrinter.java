package ru.gelman.output.printers;

import ru.gelman.output.target.ui.CalculatorFrame;

public class CalculatorFramePrinter implements OutputPrinter {
    private final CalculatorFrame field;

    public CalculatorFramePrinter(CalculatorFrame uiField) {
        field = uiField;
    }

    @Override
    public void print(String value) {
        field.setExpression(value);
    }
}
