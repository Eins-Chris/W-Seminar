package v1;

import javax.swing.*;
import java.awt.*;

public class VariableEditorUI extends JFrame {
    private final JTextField[] intFields = new JTextField[5];                                                       // int
    private final ToggleSwitch[] boolSwitches = new ToggleSwitch[2];                                                // bool
    private final String[] intNames = {"DIRECTIONS", "GRIDSIZE", "STEP_TIME", "QUANTITY", "BODY_SIZE"};             // int
    private final String[] boolNames = {"DIRECTIONAL", "COOPERATIVE"};                                              // bool
    private final VariableManager manager;

    public VariableEditorUI(VariableManager manager) {
        this.manager = manager;
        initUI();
    }

    private void initUI() {
        setTitle("Parameter Einstellungen");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(750, 450);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Integer Panel
        JPanel intPanel = new JPanel(new GridLayout(6, 2, 10, 15));
        intPanel.setBorder(BorderFactory.createTitledBorder("Integer Parameter"));
        int[] values = {manager.DIRECTIONS, manager.GRIDSIZE, manager.STEP_TIME, manager.QUANTITY, manager.BODY_SIZE};                 // int

        for (int i = 0; i < intFields.length; i++) {
            intPanel.add(new JLabel(intNames[i] + ":"));
            JTextField field = new JTextField(String.valueOf(values[i]));
            field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(180, 180, 180), 1, true),
                    BorderFactory.createEmptyBorder(5, 10, 5, 10)
            ));
            field.setBackground(new Color(245, 245, 245));
            field.setFont(new Font("SansSerif", Font.PLAIN, 14));
            intFields[i] = field;
            intPanel.add(field);
        }

        // Boolean Panel
        JPanel boolPanel = new JPanel(new GridLayout(6, 2, 10, 15));
        boolPanel.setBorder(BorderFactory.createTitledBorder("Boolean Parameter"));
        boolean[] boolValues = {manager.DIRECTIONAL, manager.COOPERATIVE};                                                               // bool

        for (int i = 0; i < boolSwitches.length; i++) {
            boolPanel.add(new JLabel(boolNames[i] + ":"));
            ToggleSwitch toggle = new ToggleSwitch(boolValues[i]);
            boolSwitches[i] = toggle;
            boolPanel.add(toggle);
        }

        contentPanel.add(intPanel);
        contentPanel.add(boolPanel);

        // Button
        JButton startButton = new JButton("Simulation starten");
        startButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        startButton.setPreferredSize(new Dimension(0, 40));
        startButton.addActionListener(e -> onSubmit());
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(startButton, BorderLayout.SOUTH);
        add(mainPanel);
        setVisible(true);
    }

    private void onSubmit() {
        int[] intValues = new int[5];
        boolean[] boolValues = new boolean[5];

        try {
            for (int i = 0; i < intFields.length; i++) {
                intValues[i] = Integer.parseInt(intFields[i].getText());
            }
            for (int i = 0; i < boolSwitches.length; i++) {
                boolValues[i] = boolSwitches[i].isOn();
            }
            dispose();
            
            manager.updateFromInputs(intValues, boolValues);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Bitte gültige Zahlen eingeben.", "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }

}
