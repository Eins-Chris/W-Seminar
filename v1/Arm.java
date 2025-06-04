package v1;

public class Arm {
    
    private int xPos, yPos;
    private int state;
    private int[] direction;
    
    public Arm(int xPos, int yPos, int[] direction) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.state = 2;
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
        
    }

}
