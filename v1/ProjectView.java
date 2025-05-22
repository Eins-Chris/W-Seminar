package v1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class ProjectView extends JFrame {

    private final VariableManager variable;
    private GridPanel gridPanel;
    private JTextArea outputArea;
    private JTextField inputField;
    private final int margin = 50;

    public ProjectView(VariableManager variable) {
        this.variable = variable;

        setTitle("Christian Scharl - W-Seminar Simulation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // BorderLayout für das Hauptfenster
        getContentPane().setLayout(new BorderLayout());

        initComponents();
        setVisible(true); // Muss nach Komponenteninitialisierung erfolgen
    }

    private void initComponents() {
        // Linker Bereich mit null-Layout (freie Platzierung)
        JPanel leftPanel = new JPanel(null);
        leftPanel.setBackground(Color.WHITE);

        // GridPanel (wird später skaliert)
        gridPanel = new GridPanel(variable.GRIDSIZE);
        gridPanel.setBackground(Color.WHITE);
        leftPanel.add(gridPanel);

        // Rechte Seite
        JPanel rightPanel = buildRightPanel();

        // Komponenten zum Hauptfenster hinzufügen
        getContentPane().add(leftPanel, BorderLayout.WEST);
        getContentPane().add(rightPanel, BorderLayout.CENTER);

        // Automatische Größenanpassung bei Fensteränderung
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizeGridPanel(leftPanel);
            }
        });

        // Initiale Größenberechnung
        resizeGridPanel(leftPanel);
    }

    private void resizeGridPanel(JPanel leftPanel) {
        int contentHeight = getContentPane().getHeight();
        int gridSizePixels = (int) (contentHeight * 0.85);

        // GridPanel bekommt gleichmäßige Ränder
        gridPanel.setBounds(margin, margin, gridSizePixels, gridSizePixels);

        // Gesamtbreite links: Rand + Quadrat + optional zusätzlicher Rand
        int totalLeftWidth = margin + gridSizePixels + margin;
        leftPanel.setPreferredSize(new Dimension(totalLeftWidth, contentHeight));
        leftPanel.revalidate();
    }

    private JPanel buildRightPanel() {
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.DARK_GRAY);

        // Oben: Zwei große Quadrate
        JPanel highlightPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        for (int i = 0; i < 2; i++) {
            JPanel square = new JPanel();
            square.setPreferredSize(new Dimension(300, 300));
            square.setBackground(Color.LIGHT_GRAY);
            highlightPanel.add(square);
        }
        rightPanel.add(highlightPanel, BorderLayout.NORTH);

        // Mitte: Ausgabe-Textfeld (mehrzeilig)
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        rightPanel.add(scrollPane, BorderLayout.CENTER);

        // Unten: Eingabefeld
        inputField = new JTextField();
        inputField.setPreferredSize(new Dimension(400, 30));
        rightPanel.add(inputField, BorderLayout.SOUTH);

        return rightPanel;
    }

    // Zeichnet das Grid
    private static class GridPanel extends JPanel {
        private final int gridSize;

        public GridPanel(int gridSize) {
            this.gridSize = gridSize;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            int size = Math.min(getWidth(), getHeight());
            double cellSize = (double) size / gridSize;

            g.setColor(Color.GRAY);
            // Vertikale Linien
            for (int i = 0; i <= gridSize; i++) {
                int x = (int) (i * cellSize);
                g.drawLine(x, 0, x, size);
            }

            // Horizontale Linien
            for (int i = 0; i <= gridSize; i++) {
                int y = (int) (i * cellSize);
                g.drawLine(0, y, size, y);
            }

            // Sicherstellen, dass die rechte und untere Linie exakt gezogen wird
            int end = (int) (gridSize * cellSize);
            g.drawLine(end - 1, 0, end - 1, size);  // rechte Linie
            g.drawLine(0, end - 1, size, end - 1);  // untere Linie
        }

    }

    // Getter für Input/Output-Felder (optional)
    public JTextArea getOutputArea() {
        return outputArea;
    }

    public JTextField getInputField() {
        return inputField;
    }
}
