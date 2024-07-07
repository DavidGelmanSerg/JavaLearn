package view.ui;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.Objects;

public class StatePanel extends JPanel {
    private final JLabel flags;
    private final JLabel stateSmile;

    public StatePanel() {
        setLayout(new BorderLayout(-50, 0));
        flags = new JLabel("00", SwingConstants.CENTER);
        flags.setFont(new Font("Sans Serif", Font.BOLD, 24));
        flags.setPreferredSize(new Dimension(50, 50));
        flags.setBorder(new LineBorder(Color.BLACK, 1));

        ImageIcon img = new ImageIcon(Objects.requireNonNull(getClass().getResource("pictures/start smile.png")));
        stateSmile = new JLabel(img, SwingConstants.CENTER);
        stateSmile.setPreferredSize(new Dimension(20, 50));

        add(flags, BorderLayout.WEST);
        add(stateSmile, BorderLayout.CENTER);

        setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        setVisible(true);
    }

    public void updateFlags(int flags) {
        String value = String.valueOf(flags);
        if (value.length() == 1) {
            value = "0" + value;
        }

        this.flags.setText(value);
    }

    public void setLooseSmile() {
        ImageIcon img = new ImageIcon(Objects.requireNonNull(getClass().getResource("pictures/death.png")));
        stateSmile.setIcon(img);
    }

    public void setStartSmile() {
        ImageIcon img = new ImageIcon(Objects.requireNonNull(getClass().getResource("pictures/start smile.png")));
        stateSmile.setIcon(img);
    }
}
