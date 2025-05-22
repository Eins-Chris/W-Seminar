package v1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ToggleSwitch extends JPanel {
    private boolean on;
    private final int width = 50;
    private final int height = 25;

    public ToggleSwitch(boolean initialState) {
        this.on = initialState;
        setPreferredSize(new Dimension(width, height));
        setOpaque(false);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                on = !on;
                repaint();
            }
        });
    }

    public boolean isOn() {
        return on;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color background = on ? new Color(0, 150, 136) : new Color(200, 200, 200);
        Color knob = Color.WHITE;

        g2.setColor(background);
        g2.fillRoundRect(0, 0, width, height, height, height);

        int knobSize = height - 6;
        int x = on ? width - knobSize - 3 : 3;
        g2.setColor(knob);
        g2.fillOval(x, 3, knobSize, knobSize);

        g2.dispose();
    }
}
