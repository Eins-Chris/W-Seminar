import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Simulation2 extends Kaestchen {

    private static final int SIZE = 40;
    private static final int INITIAL_POWER = 30;
    private static final int SPEED = 200;

    private int[][] powerGrid = new int[SIZE][SIZE];
    private Random random = new Random();

    public Simulation2() {
        super(20, 20, SIZE, SIZE);
        initialize();
        tickerStart(1, SPEED);
        testausgabe();
    }

    public void initialize() {
        // starting point
        powerGrid[SIZE/2][SIZE/2] = INITIAL_POWER;
        farbeSetzen(SIZE/2+1, SIZE/2+1, getColor(INITIAL_POWER));

        // food
        farbeSetzen(SIZE/2+1, SIZE/2+8, Color.ORANGE);

        //wall
        for (int i = 0; i < SIZE; i++) {
            farbeSetzen(i, SIZE/2+4, getColor(-1));
        }
        farbeSetzen(SIZE/2+1, SIZE/2+4, "durchsichtig");

        for (int i = SIZE/4+5; i < SIZE/4*3-7; i++) {
            farbeSetzen(i, SIZE/2+6, getColor(-1));
        }
    }

    public void tick(int nr) {
        if (nr == 1) { // first grow phase
            grow();
            repaint();
        }
        if (nr == 2) {
            
        }
    }


    public void grow() {
        int[][] newPowerGrid = new int[SIZE][SIZE];
        int idk = 0;
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                // spreading
                if (powerGrid[x][y] > 0) {
                    List<int[]> neighbors = getShuffledNeighbors(x, y);
                    boolean branched = false;

                    newPowerGrid[x][y] = powerGrid[x][y] - 1;

                    for (int[] neighbor : neighbors) {
                        int nx = neighbor[0];
                        int ny = neighbor[1];
                        if (nx >= 0 && ny >= 0 && nx < SIZE && ny < SIZE) {
                            if (newPowerGrid[nx][ny] == 0 && powerGrid[x][y] - 2 > 0 && powerGrid[nx][ny] < powerGrid[x][y]) {
                                newPowerGrid[nx][ny] = powerGrid[x][y] - 2;
                                newPowerGrid[nx][ny] = check(nx, ny, newPowerGrid[nx][ny]);
                                farbeSetzen(nx + 1, ny + 1, getColor(newPowerGrid[nx][ny]));

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
                // backfinding
                // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

                // stop counter
                idk += powerGrid[x][y];
            }
        }
        powerGrid = newPowerGrid;
        if (idk == 0) {
            tickerStop(1);
            tickerStart(2, SPEED);
        }
        testausgabe();
    }

    public int check(int x, int y, int value) {
        if (farbeGeben(x+1, y+1).equals("grün") || farbeGeben(x+1, y+1).equals("weiß") || farbeGeben(x+1, y+1).equals("gelb") || farbeGeben(x+1, y+1).equals("durchsichtig")) {
            return value;
        } else if (farbeGeben(x+1, y+1).equals("orange")) {
            return INITIAL_POWER;
        } else if (farbeGeben(x+1, y+1).equals("schwarz")) {
            return -1;
        }
        return 0;
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

    public Color getColor(int value) {
        switch (value) {
            case INITIAL_POWER:
                return Color.YELLOW;
            case 0:
                return Color.WHITE;
            case -1: 
                return Color.BLACK;
            default:
                return Color.GREEN;
        }
    }

    public static void main(String[] args) {
        new Simulation2();
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
