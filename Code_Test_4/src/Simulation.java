import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Simulation extends Kaestchen {

    private static final int SIZE = 40;
    private static final int INITIAL_POWER = 20;

    private int[][] powerGrid = new int[SIZE][SIZE];
    private Random random = new Random();

    public Simulation() {
        super(20, 20, SIZE, SIZE);
        initialize();
        tickerStart(1, 200);
        testausgabe();
    }

    public void initialize() {
        // starting point
        powerGrid[SIZE/2][SIZE/2] = INITIAL_POWER;
        farbeSetzen(SIZE/2+1, SIZE/2+1, Color.GREEN);

        // food
        farbeSetzen(SIZE/2+1, SIZE/2+10, Color.ORANGE);
    }

    public void tick(int nr) {
        // Growth (neighbours with "less" power)
        grow();
        

        //check();
        
        repaint();
    }


    public void grow() {
        int[][] newPowerGrid = new int[SIZE][SIZE];
        int idk = 0;
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                if (powerGrid[x][y] > 0) {
                    List<int[]> neighbors = getShuffledNeighbors(x, y);
                    boolean branched = false;

                    for (int[] neighbor : neighbors) {
                        int nx = neighbor[0];
                        int ny = neighbor[1];
                        if (nx >= 0 && ny >= 0 && nx < SIZE && ny < SIZE) {
                            if (newPowerGrid[nx][ny] == 0 && powerGrid[x][y] - 1 > 0) {
                                newPowerGrid[nx][ny] = powerGrid[x][y] - 1;
                                farbeSetzen(nx + 1, ny + 1, Color.GREEN);

                                // Erlaubt gelegentlich eine Verzweigung
                                if (!branched && random.nextDouble() < 0.3) {
                                    branched = true;
                                } else {
                                    break;
                                }
                            }
                        }
                    }
                }
                idk += powerGrid[x][y];
            }
        }
        powerGrid = newPowerGrid;
        System.out.println(idk);
        if (idk == 0) tickerStop(1);
        testausgabe();
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
