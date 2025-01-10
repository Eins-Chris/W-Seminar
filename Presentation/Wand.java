package Presentation;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Wand extends Kaestchen {

    private static final int SIZE = 24;
    private static final int FOODOFFSET = 8;
    private static final int INITIAL_POWER = 20;
    private static final int SPEED = 200;

    private int[][] powerGrid = new int[SIZE][SIZE];
    private Random random = new Random();

    public Wand() {
        super(20, 20, SIZE, 20);
        initialize();
        tickerStart(1, SPEED);
    }

    public void initialize() {
        // starting point
        powerGrid[SIZE/2-5][SIZE/2] = INITIAL_POWER;
        farbeSetzen(SIZE/2+1-5, SIZE/2+1, getColor(INITIAL_POWER));

        // wall 1
        for (int i = 0; i < SIZE; i++) {
            farbeSetzen(12, i+1, getColor(-1));
        }
        farbeSetzen(12, SIZE/2+1, "durchsichtig");

        // wall 2
        /* for (int i = SIZE/2-2; i < SIZE/2+3; i++) {
            farbeSetzen(i+1, 8, getColor(-1));
        } */
    }

    public void tick(int nr) {
        if (nr == 1) { // first grow phase
            grow();
            repaint();
        }
        if (nr == 2) { // backgrowth
            // sendImpulse();
            tickerStop(2);
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
        //testausgabe();
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

    public void sendImpulse() {
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                if (gibFarbe(farbeGeben(x+1, y+1)) == getColor(INITIAL_POWER)) {
                    impulse(x, y);
                }
            }
        }
    }

    public void impulse(int x, int y) {
        int[][] impulseGrid = new int[SIZE][SIZE]; // Hilfs-Array für Impulse
        List<int[]> startImpulse = new ArrayList<>();
        List<int[]> foodImpulse = new ArrayList<>();
    
        // Initialisierung der Impulse von Startpunkt und Food
        startImpulse.add(new int[]{SIZE / 2, SIZE / 2}); // Starting point
        foodImpulse.add(new int[]{SIZE / 2, SIZE / 2 + 7}); // Food point
    
        impulseGrid[SIZE / 2][SIZE / 2] = 1; // Markierung des Startpunkts
        impulseGrid[SIZE / 2][SIZE / 2 + 7] = 2; // Markierung des Food-Punkts
    
        boolean connected = false;
        int[] connectionPoint = null;
    
        while (!connected && (!startImpulse.isEmpty() || !foodImpulse.isEmpty())) {
            // Impulse für Startpunkt ausbreiten
            List<int[]> nextStartImpulse = new ArrayList<>();
            for (int[] current : startImpulse) {
                for (int[] neighbor : getShuffledNeighbors(current[0], current[1])) {
                    int nx = neighbor[0];
                    int ny = neighbor[1];
                    if (nx >= 0 && ny >= 0 && nx < SIZE && ny < SIZE) {
                        if (impulseGrid[nx][ny] == 2) {
                            // Verbindung gefunden
                            connected = true;
                            connectionPoint = new int[]{nx, ny};
                            break;
                        }
                        if (impulseGrid[nx][ny] == 0 && farbeGeben(nx + 1, ny + 1).equals("grün")) {
                            impulseGrid[nx][ny] = 1;
                            nextStartImpulse.add(new int[]{nx, ny});
                        }
                    }
                }
                if (connected) break;
            }
            startImpulse = nextStartImpulse;
    
            // Impulse für Food ausbreiten
            List<int[]> nextFoodImpulse = new ArrayList<>();
            for (int[] current : foodImpulse) {
                for (int[] neighbor : getShuffledNeighbors(current[0], current[1])) {
                    int nx = neighbor[0];
                    int ny = neighbor[1];
                    if (nx >= 0 && ny >= 0 && nx < SIZE && ny < SIZE) {
                        if (impulseGrid[nx][ny] == 1) {
                            // Verbindung gefunden
                            connected = true;
                            connectionPoint = new int[]{nx, ny};
                            break;
                        }
                        if (impulseGrid[nx][ny] == 0 && farbeGeben(nx + 1, ny + 1).equals("grün")) {
                            impulseGrid[nx][ny] = 2;
                            nextFoodImpulse.add(new int[]{nx, ny});
                        }
                    }
                }
                if (connected) break;
            }
            foodImpulse = nextFoodImpulse;
        }
    
        if (connected && connectionPoint != null) {
            tracePath(connectionPoint, impulseGrid);
        }
    }
    
    private void tracePath(int[] connectionPoint, int[][] impulseGrid) {
        int x = connectionPoint[0];
        int y = connectionPoint[1];
    
        // Rückverfolgung zum Startpunkt
        while (impulseGrid[x][y] == 1) {
            farbeSetzen(x + 1, y + 1, Color.YELLOW);
            for (int[] neighbor : getShuffledNeighbors(x, y)) {
                int nx = neighbor[0];
                int ny = neighbor[1];
                if (nx >= 0 && ny >= 0 && nx < SIZE && ny < SIZE && impulseGrid[nx][ny] == 1) {
                    x = nx;
                    y = ny;
                    break;
                }
            }
        }
    
        // Rückverfolgung zum Food-Punkt
        x = connectionPoint[0];
        y = connectionPoint[1];
        while (impulseGrid[x][y] == 2) {
            farbeSetzen(x + 1, y + 1, Color.YELLOW);
            for (int[] neighbor : getShuffledNeighbors(x, y)) {
                int nx = neighbor[0];
                int ny = neighbor[1];
                if (nx >= 0 && ny >= 0 && nx < SIZE && ny < SIZE && impulseGrid[nx][ny] == 2) {
                    x = nx;
                    y = ny;
                    break;
                }
            }
        }
    }    

    public static void main(String[] args) {
        new Wand();
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
