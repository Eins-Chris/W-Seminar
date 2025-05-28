package v1;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VariableManager manager = new VariableManager();
            new VariableEditorUI(manager);
        });
        /* String test1 = "IchbineinTestString(10,20,30)";
        String test2 = "IchbineinTestString";
        System.out.println(test2.substring(0, test2.indexOf("("))); */
    }
}
