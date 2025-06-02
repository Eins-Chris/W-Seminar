package v1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Organism extends Thread {
    
    private int xPos, yPos;
    private int state;
    private ProjectView view;
    private VariableManager variable;
    private boolean threadRunning = true;
    public boolean touched = false;

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }

    public int getCellState() {
        return state;
    }

    public Organism(ProjectView view) {
        this.view = view;
        this.variable = view.getVariable();
        this.xPos = (int) (Math.random() * (variable.GRIDSIZE));
        this.yPos = (int) (Math.random() * (variable.GRIDSIZE));
        this.state = 1;
    }

    public Organism(ProjectView view, int xPos, int yPos) {
        this.view = view;
        this.variable = view.getVariable();
        this.xPos = xPos;
        this.yPos = yPos;
        this.state = 1;
    }

    public Organism(ProjectView view, int xPos, int yPos, int cellState) {
        this(view, xPos, yPos);
        this.state = cellState;
    }

    public Organism(Organism old) {
        this(old.view, old.getXPos(), old.getYPos(), old.state);
    }

    public void pause() {
        threadRunning = false;
    }

    @Override
    public void run() {
        touched = true;
        while (threadRunning) {
            try {
                Thread.sleep(variable.STEP_TIME);
            } catch (InterruptedException e) { 
                e.printStackTrace(); 
            }

            int[][] directions = new int[][] {
                {1, 0}, {-1, 0}, {0, 1}, {0, -1},
                {1, 1}, {1, -1}, {-1, 1}, {-1, -1}
            };
            if (variable.DIRECTIONAL) {
                List<Integer> numbers = new ArrayList<>();
                for (int i = 0; i < directions.length; i++) numbers.add(i);
                Collections.shuffle(numbers);

                int [][] newdir = new int[variable.DIRECTIONS][2];
                for (int i = 0; i < variable.DIRECTIONS; i++) {
                    newdir[i][0] = directions[numbers.get(i)][0];
                    newdir[i][1] = directions[numbers.get(i)][1];
                }
                directions = newdir;
            }

            for (int[] dir : directions) {
                int newXPos = xPos + dir[0];
                int newYPos = yPos + dir[1];
                /* if (newXPos>= 0 && newXPos < variable.GRIDSIZE) xPos = newXPos;
                if (newYPos>= 0 && newYPos < variable.GRIDSIZE) yPos = newYPos; */

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
