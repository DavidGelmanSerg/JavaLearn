package output.printers;

import output.printers.OutputPrinter;

import javax.swing.text.JTextComponent;

public class JTextComponentPrinter implements OutputPrinter {
    private final JTextComponent field;

    public JTextComponentPrinter(JTextComponent uiField) {
        field = uiField;
    }

    @Override
    public void print(String value) {
        field.setText(value);
    }
}
