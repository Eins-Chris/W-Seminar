import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ParameterWindow extends JFrame implements Variables {
    private JTextField[] intFields = new JTextField[5];
    private ToggleSwitch[] boolSwitches = new ToggleSwitch[5];
    private String[] intNames = {"MAX_USERS", "TIMEOUT_SECONDS", "RETRY_COUNT", "PORT", "BUFFER_SIZE"};
    int[] intValues = {MAX_USERS, TIMEOUT_SECONDS, RETRY_COUNT, PORT, BUFFER_SIZE};
    private String[] boolNames = {"ENABLE_LOGGING", "DEBUG_MODE", "AUTO_SAVE", "FULLSCREEN", "USE_CACHE"};
    boolean[] boolValues = {ENABLE_LOGGING, DEBUG_MODE, AUTO_SAVE, FULLSCREEN, USE_CACHE};

    public ParameterWindow() {
        setTitle("Parameter Einstellungen");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(750, 450);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Linke Spalte – Int-Werte
        JPanel intPanel = new JPanel(new GridLayout(6, 2, 10, 15));
        intPanel.setBorder(BorderFactory.createTitledBorder("Integer Parameter"));

        for (int i = 0; i < 5; i++) {
            intPanel.add(new JLabel(intNames[i] + ":"));

            JTextField field = new JTextField(String.valueOf(intValues[i]));
            field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(180, 180, 180), 1, true),
                    BorderFactory.createEmptyBorder(5, 10, 5, 10)
            ));
            field.setBackground(new Color(245, 245, 245));
            field.setFont(new Font("SansSerif", Font.PLAIN, 14));
            intFields[i] = field;

            intPanel.add(field);
        }

        // Rechte Spalte – Boolean-Werte
        JPanel boolPanel = new JPanel(new GridLayout(6, 2, 10, 15));
        boolPanel.setBorder(BorderFactory.createTitledBorder("Boolean Parameter"));

        for (int i = 0; i < 5; i++) {
            boolPanel.add(new JLabel(boolNames[i] + ":"));

            ToggleSwitch toggle = new ToggleSwitch(boolValues[i]);
            boolSwitches[i] = toggle;

            boolPanel.add(toggle);
        }

        contentPanel.add(intPanel);
        contentPanel.add(boolPanel);

        // Button unten
        JButton startSim = new JButton("Simulation starten");
        startSim.setFont(new Font("SansSerif", Font.BOLD, 14));
        startSim.setPreferredSize(new Dimension(0, 40));
        startSim.addActionListener(e -> {
            save_variables();
            dispose();
            System.out.println("Weiter?");
            Project.variables_defined = true;
        });

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(startSim, BorderLayout.SOUTH);
        add(mainPanel);

        setVisible(true);
    }

    private void save_variables() {
        Variables.MAX_USERS = intValues[0];
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ParameterWindow::new);
    }

    // --------------------------
    // Custom Toggle-Switch-Klasse
    // --------------------------
    class ToggleSwitch extends JPanel {
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
}
