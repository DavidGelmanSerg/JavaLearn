package output.target.ui;

import exceptions.UnsupportedUITypeException;

public class GUIFactory {
    public static CalculatorFrame getCalculatorFrame(String type, int width, int height, String expression) {
        switch (type) {
            case "simple" -> {
                return new SimpleCalculatorFrame(width, height, expression);
            }
            default -> throw new UnsupportedUITypeException("Неизвестный тип фрейма: " + type);
        }
    }
}
