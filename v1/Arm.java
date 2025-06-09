package v1;

public class Arm {
    
    private ProjectView view;
    private VariableManager variable;
    private int xPos, yPos;
    private int state;
    private int[] direction;
    private int finalSize; // zu erreichende größe
    private int size; // aktuelle größe
    
    public Arm(ProjectView view, int xPos, int yPos, int state, int[] direction, int size) {
        this.view = view;
        this.variable = view.getVariable();
        this.xPos = xPos;
        this.yPos = yPos;
        this.state = state;
        this.direction = direction;
        this.finalSize = size;
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

    public void step() {
        if (size >= finalSize) return;
        // alle 2 steps soll die richtung um eine richtung geändert werden, entweder eins nach links, eins nach rechts oder weiter gerade
        // steps so viele wie size;
    }

}
