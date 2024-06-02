package output.target.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class SimpleCalculatorFrame extends CalculatorFrame {
    private final JPanel mainPanel;
    private final JTextArea expressionField;

    public SimpleCalculatorFrame(int width, int height, String expr) {
        super(width, height);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setFocusable(true);
        mainPanel.addKeyListener(new KeyAdapter() {
            int previousPressedKeyCode;

            @Override
            public void keyPressed(KeyEvent event) {
                char c = event.getKeyChar();
                String expression = expressionField.getText();
                String oldValue = expression;
                switch (c) {
                    case '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '.', '/', '*', '-', '+', ' ': {
                        expression += c;
                        break;
                    }
                    case KeyEvent.VK_BACK_SPACE: {
                        if (!expression.isEmpty()) {
                            expression = expression.substring(0, expression.length() - 1);
                        }
                        break;
                    }
                    case KeyEvent.VK_ENTER: {
                        var p = getPropertyChangeListeners("expression");
                        for (PropertyChangeListener prop : p) {
                            prop.propertyChange(new PropertyChangeEvent(this, "expression", oldValue, expression));
                        }
                        break;
                    }
                    case KeyEvent.VK_EQUALS: {
                        if (previousPressedKeyCode != KeyEvent.VK_SHIFT) {
                            var p = getPropertyChangeListeners("expression");
                            for (PropertyChangeListener prop : p) {
                                prop.propertyChange(new PropertyChangeEvent(this, "expression", oldValue, expression));
                            }
                        }
                        break;
                    }
                }
                firePropertyChange("expression", oldValue, expression);
                previousPressedKeyCode = event.getKeyCode();
            }
        });

        expressionField = new JTextArea(expr, 2, 45);
        expressionField.setFocusable(false);
        expressionField.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        expressionField.setFont(new Font("SansSerif", Font.BOLD, 20));

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
        setVisible(true);
    }

    @Override
    public void setExpression(String expression) {
        this.expressionField.setText(expression);
    }

    private JPanel getButtonsPanel() {
        JPanel buttonsPanel = new JPanel(new GridLayout(4, 4));
        char[][] signs = new char[][]{
                {'7', '8', '9', '/'},
                {'4', '5', '6', '*'},
                {'1', '2', '3', '-'},
                {'.', '0', '=', '+'}
        };

        for (char[] sign : signs) {
            for (char c : sign) {
                JButton btn = new JButton(new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String newChar = String.valueOf(c);
                        String expression = expressionField.getText();
                        String oldValue = expression;
                        if (!newChar.equals("=")) {
                            expression += c;
                        }
                        firePropertyChange("expression", oldValue, expression);
                        mainPanel.requestFocus();
                    }
                });
                btn.setText(String.valueOf(c));
                btn.setFont(new Font("SansSerif", Font.BOLD, 20));
                buttonsPanel.add(btn);
            }
        }
        return buttonsPanel;
    }
}
