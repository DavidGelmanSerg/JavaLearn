package ru.gelman.output.target;

import ru.gelman.exceptions.UnsupportedTargetException;
import ru.gelman.exceptions.expression.EmptyExpressionException;
import ru.gelman.output.printers.OutputPrinter;
import ru.gelman.output.printers.PrinterFactory;
import ru.gelman.output.target.ui.CalculatorFrame;

import java.beans.PropertyChangeListener;
import java.util.Objects;

public class Target {
    private final TargetType type;
    private final Object target;
    private final OutputPrinter printer;

    public Target(TargetType type, Object target) {
        if (type == null) {
            throw new UnsupportedTargetException("Неизвестный тип объекта назначения " + null);
        }
        this.type = type;
        this.target = target;
        printer = PrinterFactory.getPrinter(type, target);
    }

    public void setExpression(String expression) {
        try {
            printer.print(expression);
        } catch (EmptyExpressionException e) {
            e.setSource(this);
            throw e;
        }
    }

    public void addPropertyChangeListener(String propName, PropertyChangeListener listener) {
        if (Objects.requireNonNull(type) == TargetType.UI) {
            CalculatorFrame frame = (CalculatorFrame) target;
            frame.addPropertyChangeListener(propName, listener);
        }
    }
}
