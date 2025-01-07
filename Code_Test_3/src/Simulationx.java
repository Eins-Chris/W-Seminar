import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Simulationx extends Kaestchen {

    private static final int SIZE = 40;
    private static final int INITIAL_POWER = 20;
    private static final int SPREAD_FACTOR = 1;

    private int[][] powerGrid = new int[SIZE][SIZE];
    private Random random = new Random();

    public Simulationx() {
        super(20, 20, SIZE, SIZE);
        initialize();
        tickerStart(1, 200);
        testausgabe();
    }

    public void initialize() {
        powerGrid[SIZE/2][SIZE/2] = INITIAL_POWER;
        farbeSetzen(SIZE/2+1, SIZE/2+1, Color.GREEN);

        farbeSetzen(SIZE, SIZE+10, Color.ORANGE);
    }

    public void tick(int nr) {
        int[][] newPowerGrid = new int[SIZE][SIZE];
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                if (powerGrid[x][y] > 0) {
                    List<int[]> neighbors = getShuffledNeighbors(x, y);
                    boolean branched = false;

                    for (int[] neighbor : neighbors) {
                        int nx = neighbor[0];
                        int ny = neighbor[1];
                        if (nx >= 0 && ny >= 0 && nx < SIZE && ny < SIZE) {
                            if (newPowerGrid[nx][ny] == 0 && powerGrid[x][y] - SPREAD_FACTOR > 0) {
                                newPowerGrid[nx][ny] = powerGrid[x][y] - SPREAD_FACTOR;
                                farbeSetzen(nx + 1, ny + 1, Color.GREEN);

                                // Erlaubt gelegentlich eine Verzweigung
                                if (!branched && random.nextDouble() < 0.3) {
                                    branched = true; // Eine Verzweigung pro Zelle
                                } else {
                                    break; // Stoppe nach einer Richtung
                                }
                            }
                        }
                    }
                }
            }
        }
        powerGrid = newPowerGrid;
        repaint();
    }
    

    private List<int[]> getShuffledNeighbors(int x, int y) {
        List<int[]> neighbors = new ArrayList<>();
        neighbors.add(new int[]{x - 1, y});   // West
        neighbors.add(new int[]{x + 1, y});   // Ost
        neighbors.add(new int[]{x, y - 1});   // Nord
        neighbors.add(new int[]{x, y + 1});   // Süd
        neighbors.add(new int[]{x - 1, y - 1}); // Nordwest
        neighbors.add(new int[]{x + 1, y - 1}); // Nordost
        neighbors.add(new int[]{x - 1, y + 1}); // Südwest
        neighbors.add(new int[]{x + 1, y + 1}); // Südost

        Collections.shuffle(neighbors, random);
        return neighbors;
    }

    public static void main(String[] args) {
        new Simulation();
    }

    public void testausgabe() {
        System.out.println("--");
        for (int y = 1; y <= 40; y++) {
            for (int x = 1; x <= 40; x++) {
                System.out.print(powerGrid[x-1][y-1] + "|");
            }
            System.out.println();
        }
        System.out.println("--");
        repaint();
    }

}
