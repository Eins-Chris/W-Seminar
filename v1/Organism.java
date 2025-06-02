package v1;

public class Organism extends Thread {
    
    private int xPos, yPos;
    private int state;
    private VariableManager variable;
    private boolean threadRunning = true;

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }

    public int getCellState() {
        return state;
    }

    public Organism(VariableManager variable) {
        this.variable = variable;
        this.xPos = (int) (Math.random() * (variable.GRIDSIZE));
        this.yPos = (int) (Math.random() * (variable.GRIDSIZE));
        this.state = 1;
    }

    public Organism(VariableManager variable, int xPos, int yPos) {
        this.variable = variable;
        this.xPos = xPos;
        this.yPos = yPos;
        this.state = 1;
    }

    public Organism(VariableManager variable, int xPos, int yPos, int cellState) {
        this(variable, xPos, yPos);
        this.state = cellState;
    }

    public Organism(Organism old) {
        this(old.variable, old.getXPos(), old.getYPos(), old.state);
    }

    public void pause() {
        threadRunning = false;
    }

    @Override
    public void run() {
        while (threadRunning) {
            try {
                Thread.sleep(variable.STEP_TIME);
            } catch (InterruptedException e) { 
                e.printStackTrace(); 
            }

            int[][] directions;
            if (variable.DIRECTIONAL) {
                switch (variable.DIRECTIONS) {
                    case 2:
                        directions = new int[][] {{1, 0}, {-1, 0}};
                        break;
                    case 4:
                        directions = new int[][] {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
                        break;
                    case 8:
                        directions = new int[][] {
                                {1, 0}, {-1, 0}, {0, 1}, {0, -1},
                                {1, 1}, {1, -1}, {-1, 1}, {-1, -1}
                        };
                        break;
                    default:
                        directions = new int[][] {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
                        break;
                }
            } else {
                directions = new int[][] {
                        {1, 0}, {-1, 0}, {0, 1}, {0, -1},
                        {1, 1}, {1, -1}, {-1, 1}, {-1, -1}
                };
            }
    
            for (int[] dir : directions) {
                int newXPos = xPos + dir[0];
                int newYPos = yPos + dir[1];
    
                if (newXPos>= 0 && newXPos < variable.GRIDSIZE && newYPos >= 0 && newYPos < variable.GRIDSIZE) {

                    /* 
                        COOPERATIVITÄT
                    */
                    if (variable.COOPERATIVE) {
                        
                    } else {
                        
                    }
                }
            }
        }
    }
}
