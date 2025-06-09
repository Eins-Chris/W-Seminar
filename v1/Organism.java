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

    private ArrayList<Arm> body = new ArrayList<>();

    public Organism(ProjectView view) {
        this.view = view;
        this.variable = view.getVariable();
        this.xPos = (int) (Math.random() * (variable.GRIDSIZE));
        this.yPos = (int) (Math.random() * (variable.GRIDSIZE));
        this.state = 1;
    }

    public Organism(ProjectView view, int xPos, int yPos) {
        this(view);
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public Organism(ProjectView view, int xPos, int yPos, int cellState) {
        this(view, xPos, yPos);
        this.state = cellState;
    }

    public Organism(ProjectView view, int xPos, int yPos, ArrayList<Arm> body) {
        this(view, xPos, yPos);
        this.body = body;
    }

    public Organism(ProjectView view, int xPos, int yPos, int cellState, ArrayList<Arm> body) {
        this(view, xPos, yPos, cellState);
        this.body = body;
    }

    public Organism(Organism old) {
        this(old.view, old.getXPos(), old.getYPos(), old.state, old.body);
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

    public ArrayList<Arm> getBody() {
        return body;
    }

    public void pause() {
        threadRunning = false;
    }

    @Override
    public void run() {
        touched = true;
        /* while (threadRunning) {
            try {
                Thread.sleep(variable.STEP_TIME);
            } catch (InterruptedException e) { 
                e.printStackTrace(); 
            }

            
        } */
        grow();
    }

    public void grow() {
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

        // Basisgröße: ganzzahlige Division
        int baseSize = variable.BODY_SIZE / variable.DIRECTIONS;
        // Rest, der verteilt werden muss
        int remainder = variable.BODY_SIZE % variable.DIRECTIONS;
        int indexidk = 0;

        int available = variable.BODY_SIZE;
        for (int[] dir : directions) {
            int xArm, yArm;
            int newXPos = xPos + dir[0];
            int newYPos = yPos + dir[1];
            if (newXPos>= 0 && newXPos < variable.GRIDSIZE) xArm = newXPos; else xArm = xPos;
            if (newYPos>= 0 && newYPos < variable.GRIDSIZE) yArm = newYPos; else yArm = yPos;

            /* view.output("Available - 1: " + available);
            if (available >= 0) {
                int idk = (int) Math.ceil(variable.BODY_SIZE / variable.DIRECTIONS);
                int size;
                if (idk < available) size = idk;
                else size = available;
                available -= size;
                
                body.add(new Arm(view, xArm, yArm, 2, dir, size));         
                view.output("Size: " + size);
                view.output("Available - 2: " + available);
            } */

            int size = baseSize + (indexidk < remainder ? 1 : 0);
            view.output("Richtung " + indexidk + ": Größe = " + size);

            body.add(new Arm(view, xArm, yArm, 2, dir, size)); 

            view.output("-----");
            indexidk++;
        }
    }
}
