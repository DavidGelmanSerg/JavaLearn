package output.target.ui;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.beans.PropertyChangeListener;

public abstract class CalculatorFrame extends JFrame {
    private JTextArea expressionField;

    public CalculatorFrame(int width, int height) {
        setSize(width, height);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int xLocation = (screenSize.width - getWidth()) / 2;
        int yLocation = (screenSize.height - getHeight()) / 2;
        setLocation(xLocation, yLocation);
    }

    public abstract JTextComponent getExpressionField();

    public abstract void setExpression(String expression);

    @Override
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        super.addPropertyChangeListener(propertyName, listener);
    }
}
