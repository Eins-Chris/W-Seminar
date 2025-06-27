package v1;

public class Component {
    
    private ProjectView view;
    private Arm mother;
    private VariableManager variable;
    private int xPos, yPos;
    private int state;
    private int[] dir;
    
    public Component(ProjectView view, Arm mother, int xPos, int yPos, int state, int[] dir) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.view = view;
        this.mother = mother;
        this.variable = view.getVariable();
        this.state = state;
        this.dir = dir;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }

    public int getCellState() {
        return state;
    }

    public Arm getMother() {
        return mother;
    }

    public void kill() {
        state = -1;
    }

}
