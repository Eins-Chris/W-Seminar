package v1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Arm {
    
    private ProjectView view;
    private VariableManager variable;
    private Organism mother;
    private int xPos, yPos;
    private int state;
    private int[] dir;
    
    public ArrayList<Component> body = new ArrayList<>();
    public int finalSize;
    private int size = 0;

    public Arm(ProjectView view, Organism mother, int xPos, int yPos, int state, int[] dir, int size) {
        this.mother = mother;
        this.xPos = xPos;
        this.yPos = yPos;
        this.view = view;
        this.variable = view.getVariable();
        this.state = state;
        this.dir = dir;
        this.finalSize = size;
    }

    public int getCellState() {
        return state;
    }

    public Organism getMother() {
        return mother;
    }

    public void step() {
        if (getMother().isDead()) {
            return;
        }
        if (size >= finalSize) return;
        if (size == 0) {
            body.add(new Component(view, this, xPos, yPos, state, dir));
        } else {
            int[][] newDir = getNewDirections(dir);
            List<Integer> numbers = new ArrayList<>();
            for (int i = 0; i < newDir.length; i++) numbers.add(i);
            Collections.shuffle(numbers);
            dir[0] = newDir[numbers.get(0)][0];
            dir[1] = newDir[numbers.get(0)][1];
            
            int newXPos = xPos + dir[0];
            int newYPos = yPos + dir[1];
            Organism enemy = null;
            if (newXPos >= 0 && newXPos < variable.GRIDSIZE && newYPos >= 0 && newYPos < variable.GRIDSIZE) {
                if (view.getOrganismMatrix()[newXPos][newYPos] != null && view.getOrganismMatrix()[newXPos][newYPos].getCellState() == 1 ) {
                    enemy = view.getOrganismMatrix()[newXPos][newYPos];
                } 
                if (view.getArmMatrix()[newXPos][newYPos] != null && view.getArmMatrix()[newXPos][newYPos].getCellState() == 2 ) {
                    enemy = view.getArmMatrix()[newXPos][newYPos].getMother().getMother();
                }
                if (enemy != null) {
                    if (!variable.COOPERATIVE) {
                        if (!enemy.equals(getMother())) {
                            view.output("--- KILLING PHASE ---");
                            int enemySize = 0;
                            for (int i = 0; i < enemy.getBody().size(); i++) {
                                enemySize += enemy.getBody().get(i).size;
                            }
                            if (enemySize <= size) {
                                enemy.kill();
                            } else {
                                getMother().kill();
                                return;
                            } 
                        }
                    }
                }
                body.add(new Component(view, this, body.get(body.size()-1).getXPos() + dir[0], body.get(body.size()-1).getYPos() + dir[1], state, dir));
            } else size--;
        }
        size++;
    }

    public int[][] getNewDirections(int[] dir) {
        int[][] directions = new int[][] {
                {-1, 0},{-1, 1},{0, 1},{1, 1},{1, 0},{1, -1},{0, -1},{-1, -1}
        };
        for (int i = 0; i < directions.length; i++) {
            if (Arrays.equals(directions[i], dir)) {
                int[] left = directions[(i + 7) % 8];
                int[] right = directions[(i + 1) % 8];
                return new int[][] { dir, dir, dir, left, right };
            }
        }
        return new int[][] { dir, dir, dir };
    }

    public void kill() {
        for (int i = 0; i < body.size(); i++) {
            body.get(i).kill();
        }
        state = -1;
    }

}
