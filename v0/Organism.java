public class Organism extends Thread {
    
    private int xPos, yPos;
    private int state;

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }

    public int getCellState() {
        return state;
    }

    public Organism() {
        this.xPos = 1 + (int) (Math.random() * (Project.SIZE));
        this.yPos = 1 + (int) (Math.random() * (Project.SIZE));
        this.state = 0;
    }

    public Organism(int xPos, int yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.state = 0;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(Project.STEP_TIME);
            } catch (InterruptedException e) { 
                e.printStackTrace(); 
            }
        
            int[][] directions;
            if (Project.DIRECTIONAL) {
                switch (Project.DIRECTIONS) {
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
    
                // Bounds-Check (nicht außerhalb des Grids gehen)
                if (newXPos>= 0 && newXPos < Project.SIZE && newYPos >= 0 && newYPos < Project.SIZE) {

                    /* 
                        COOPERATIVITÄT
                     */
                    if (Project.COOPERATIVE) {
                        
                    } else {
                        
                    }
                }
            }
        }
    }
}
