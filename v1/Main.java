package v1;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VariableManager manager = new VariableManager();
            new VariableEditorUI(manager);
        });
    }
}
