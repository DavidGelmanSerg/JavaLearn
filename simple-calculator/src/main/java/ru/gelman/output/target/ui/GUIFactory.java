package ru.gelman.output.target.ui;

import ru.gelman.exceptions.UnsupportedUITypeException;

public class GUIFactory {
    public static CalculatorFrame getCalculatorFrame(String type, int width, int height, String expression) {
        if (type.equals("simple")) {
            return new SimpleCalculatorFrame(width, height, expression);
        }
        throw new UnsupportedUITypeException("Неизвестный тип фрейма: " + type);
    }
}
