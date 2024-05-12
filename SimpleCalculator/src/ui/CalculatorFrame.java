package ui;

import expression.ExpressionCalculator;
import expression.ExpressionParser;
import output.printers.OutputPrinter;
import output.printers.PrinterFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class CalculatorFrame extends JFrame {
    private final JPanel mainPanel;
    private final OutputPrinter printer;
    private String expression = "";

    public CalculatorFrame(int width, int height) {
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int xLocation = (screenSize.width - getWidth()) / 2;
        int yLocation = (screenSize.height - getHeight()) / 2;
        setLocation(xLocation, yLocation);

        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setFocusable(true);
        mainPanel.addKeyListener(new KeyHandler());

        JTextArea expressionField = new JTextArea(2, 45);
        expressionField.setFocusable(false);
        expressionField.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        expressionField.setFont(new Font("SansSerif", Font.BOLD, 20));

        printer = PrinterFactory.getPrinter(PrinterFactory.UI, expressionField);

        var constraints = new GridBagConstraints();
        constraints.weighty = 1.0;
        constraints.weightx = 1.0;
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridheight = 1;
        constraints.insets = new Insets(2, 0, 0, 0);
        constraints.fill = GridBagConstraints.BOTH;
        mainPanel.add(expressionField, constraints);

        var buttonsPanel = getButtonsPanel();
        constraints.weightx = 1.0;
        constraints.weighty = 9.0;
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridheight = 0;
        constraints.insets = new Insets(0, 0, 0, 0);
        mainPanel.add(buttonsPanel, constraints);

        add(mainPanel, BorderLayout.CENTER);
    }


    private JPanel getButtonsPanel() {
        JPanel buttonsPanel = new JPanel(new GridLayout(4, 4));
        char[][] signs = new char[][]{
                {'7', '8', '9', '/'},
                {'4', '5', '6', '*'},
                {'1', '2', '3', '-'},
                {'.', '0', '=', '+'}
        };

        for (int i = 0; i < signs.length; i++) {
            for (int j = 0; j < signs[i].length; j++) {
                StringBuilder builder = new StringBuilder();
                char charToAdd = signs[i][j];
                var action = new ChangeExpressionAction(String.valueOf(signs[i][j]));

                JButton btn = new JButton(action);
                btn.setFont(new Font("SansSerif", Font.BOLD, 20));
                buttonsPanel.add(btn);
            }
        }
        return buttonsPanel;
    }

    private class ChangeExpressionAction extends AbstractAction {
        public ChangeExpressionAction(String c) {
            putValue(Action.NAME, c);
            putValue("new char", c);
            putValue(Action.SHORT_DESCRIPTION, c);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String c = String.valueOf(getValue("new char"));
            if (c.equals("=") && !expression.isEmpty()) {
                ExpressionParser parser = new ExpressionParser(expression);
                expression = ExpressionCalculator.calculate(parser.getPostfixForm()).toString();
            } else {
                expression += c;
            }
            printer.print(expression);
            mainPanel.requestFocus();
        }
    }

    private class KeyHandler extends AbstractKeyHandler {
        @Override
        public void keyPressed(KeyEvent event) {
            int code = event.getKeyCode();
            char c = event.getKeyChar();

            switch (c) {
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                case '0':
                case '.':
                case '/':
                case '*':
                case ':':
                case '-':
                case '+':
                case ' ': {
                    expression += c;
                }
                default: {
                    break;
                }
            }

            switch (code) {
                case 8: {
                    if (!expression.isEmpty()) {
                        expression = expression.substring(0, expression.length() - 1);
                    }
                    break;
                }
                case 10: {
                    if (!expression.isEmpty()) {
                        ExpressionParser parser = new ExpressionParser(expression);
                        expression = ExpressionCalculator.calculate(parser.getPostfixForm()).toString();
                    }
                    break;
                }
                case 61: {
                    if (previousPressedKeyCode != 16 && !expression.isEmpty()) {
                        ExpressionParser parser = new ExpressionParser(expression);
                        expression = ExpressionCalculator.calculate(parser.getPostfixForm()).toString();
                    }
                    break;
                }
            }
            printer.print(expression);
            previousPressedKeyCode = event.getKeyCode();
        }
    }
}
