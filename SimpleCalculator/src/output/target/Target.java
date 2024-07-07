package output.target;

import exceptions.UnsupportedTargetException;
import exceptions.expression.EmptyExpressionException;
import output.printers.OutputPrinter;
import output.printers.PrinterFactory;
import output.target.ui.CalculatorFrame;

import java.beans.PropertyChangeListener;

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
        switch (type) {
            case UI -> {
                CalculatorFrame frame = (CalculatorFrame) target;
                frame.addPropertyChangeListener(propName, listener);
            }
        }
    }
}
