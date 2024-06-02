package output.target;

import exceptions.UnsupportedTargetException;
import output.printers.console.PrintStreamFactory;
import output.target.ui.CalculatorFrame;
import output.target.ui.GUIFactory;

import java.io.File;

public class TargetFactory {
    public static Target getTarget(TargetType type, String option) {
        return switch (type) {
            case UI -> {
                CalculatorFrame GUI = GUIFactory.getCalculatorFrame(option, 400, 400, "");
                yield new Target(type, GUI);
            }
            case FILE -> new Target(type, new File(option));
            case CONSOLE -> new Target(type, PrintStreamFactory.getPrintStream(option));
        };
    }

    public static TargetType getType(String key) {
        if (key == null) {
            throw new UnsupportedTargetException("Не удалось найти ни один объект вывода");
        }
        return switch (key) {
            case "-u" -> TargetType.UI;
            case "-f" -> TargetType.FILE;
            case "-c" -> TargetType.CONSOLE;
            default -> throw new UnsupportedTargetException("Неверный тип объекта вывода: " + key);
        };
    }

    public static String[] getTypesAsStringArray() {
        return new String[]{"-f", "-u", "-c"};
    }
}
