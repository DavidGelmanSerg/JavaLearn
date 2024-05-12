import expression.exceptions.EmptyExpressionException;
import expression.exceptions.InvalidExpressionException;
import ui.CalculatorFrame;

import javax.swing.*;
import java.awt.*;
import java.util.EmptyStackException;

public class Main {

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            var frame = new CalculatorFrame(450, 450);
            frame.setVisible(true);
            Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                public void uncaughtException(Thread t, Throwable e) {
                    String messageToUser = "";
                    if (e instanceof ArithmeticException) {
                        messageToUser = "Деление на 0 запрещено!";
                    } else if (e instanceof InvalidExpressionException) {
                        messageToUser = "Выражение введено неверно";
                    }
                    JOptionPane.showMessageDialog(frame, messageToUser);
                }
            });
        });

    }
}
