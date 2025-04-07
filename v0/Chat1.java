import java.util.Random;
import javax.swing.*;
import java.awt.*;

public class CellularAutomaton extends JPanel {
    private static final int GRID_SIZE = 50;
    private static final int CELL_SIZE = 10;
    private static final int STEPS = 100;
    private static final int EMPTY = 0, OCCUPIED = 1, RESOURCE = 2;
    private int[][] grid;
    private Random random = new Random();
    private String strategy = "cluster"; // "random", "density", "cluster"

    public CellularAutomaton() {
        grid = new int[GRID_SIZE][GRID_SIZE];
        initializeGrid();
        new Timer(100, e -> step()).start();
    }

    private void initializeGrid() {
        for (int x = 0; x < GRID_SIZE; x++) {
            for (int y = 0; y < GRID_SIZE; y++) {
                if (random.nextDouble() < 0.2) {
                    grid[x][y] = RESOURCE;
                }
            }
        }
        grid[GRID_SIZE / 2][GRID_SIZE / 2] = OCCUPIED;
    }

    private void step() {
        int[][] newGrid = new int[GRID_SIZE][GRID_SIZE];
        for (int x = 0; x < GRID_SIZE; x++) {
            for (int y = 0; y < GRID_SIZE; y++) {
                newGrid[x][y] = grid[x][y];
            }
        }

        for (int x = 0; x < GRID_SIZE; x++) {
            for (int y = 0; y < GRID_SIZE; y++) {
                if (grid[x][y] == OCCUPIED) {
                    int[][] neighbors = {{x-1, y}, {x+1, y}, {x, y-1}, {x, y+1}};
                    
                    if (strategy.equals("random")) {
                        shuffleArray(neighbors);
                    } else if (strategy.equals("density")) {
                        sortByResourceDensity(neighbors);
                    } else if (strategy.equals("cluster")) {
                        sortByClusterGrowth(neighbors, x, y);
                    }
                    
                    for (int[] n : neighbors) {
                        int nx = n[0], ny = n[1];
                        if (nx >= 0 && nx < GRID_SIZE && ny >= 0 && ny < GRID_SIZE && grid[nx][ny] == RESOURCE) {
                            newGrid[nx][ny] = OCCUPIED;
                            break;
                        }
                    }
                }
            }
        }
        grid = newGrid;
        repaint();
    }

    private void shuffleArray(int[][] array) {
        for (int i = array.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            int[] temp = array[i];
            array[i] = array[index];
            array[index] = temp;
        }
    }
    
    private void sortByResourceDensity(int[][] neighbors) {
        java.util.Arrays.sort(neighbors, (a, b) -> countResources(b) - countResources(a));
    }

    private int countResources(int[] cell) {
        int x = cell[0], y = cell[1];
        int count = 0;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int nx = x + dx, ny = y + dy;
                if (nx >= 0 && nx < GRID_SIZE && ny >= 0 && ny < GRID_SIZE && grid[nx][ny] == RESOURCE) {
                    count++;
                }
            }
        }
        return count;
    }

    private void sortByClusterGrowth(int[][] neighbors, int x, int y) {
        java.util.Arrays.sort(neighbors, (a, b) -> countOccupiedNeighbors(b[0], b[1]) - countOccupiedNeighbors(a[0], a[1]));
    }
    
    private int countOccupiedNeighbors(int x, int y) {
        int count = 0;
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] dir : directions) {
            int nx = x + dir[0], ny = y + dir[1];
            if (nx >= 0 && nx < GRID_SIZE && ny >= 0 && ny < GRID_SIZE && grid[nx][ny] == OCCUPIED) {
                count++;
            }
        }
        return count;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int x = 0; x < GRID_SIZE; x++) {
            for (int y = 0; y < GRID_SIZE; y++) {
                if (grid[x][y] == OCCUPIED) {
                    g.setColor(Color.BLUE);
                } else if (grid[x][y] == RESOURCE) {
                    g.setColor(Color.GREEN);
                } else {
                    g.setColor(Color.WHITE);
                }
                g.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                g.setColor(Color.BLACK);
                g.drawRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Cellular Automaton");
        CellularAutomaton ca = new CellularAutomaton();
        frame.add(ca);
        frame.setSize(GRID_SIZE * CELL_SIZE + 20, GRID_SIZE * CELL_SIZE + 40);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}