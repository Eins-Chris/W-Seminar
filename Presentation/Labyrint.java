package Presentation;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Labyrint extends Kaestchen {

    private static final int SIZE = 15;
    private static final int FOODOFFSET = 5;
    private static final int INITIAL_POWER = 50;
    private static final int SPEED = 200;

    private int[][] powerGrid = new int[SIZE][SIZE];
    private Random random = new Random();

    public Labyrint() {
        super(20, 20, SIZE, SIZE);
        initialize();
        tickerStart(1, SPEED);
    }

    public void initialize() {
        // wall
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                farbeSetzen(i+1, j+1, getColor(-1));
            }
        }
        for (int i = SIZE/2-1; i < SIZE/2+2; i++) {
            for (int j = 1; j < 4; j++) {
                farbeSetzen(i+1, j+1, "durchsichtig");
            }
        }
        for (int i = SIZE/2-1; i < SIZE/2+2; i++) {
            for (int j = 11; j < 14; j++) {
                farbeSetzen(i+1, j+1, "durchsichtig");
            }
        }

        // starting point
        powerGrid[SIZE/2][2] = INITIAL_POWER;
        farbeSetzen(SIZE/2+1, 2+1, getColor(INITIAL_POWER));

                // Tunnel zwischen den freien Flächen (Labyrinth)
        farbeSetzen(8, 4, "durchsichtig");
        farbeSetzen(8, 5, "durchsichtig");
        farbeSetzen(8, 6, "durchsichtig");
        farbeSetzen(8, 7, "durchsichtig");
        farbeSetzen(8, 8, "durchsichtig");
        farbeSetzen(9, 8, "durchsichtig");
        farbeSetzen(10, 8, "durchsichtig");
        farbeSetzen(10, 7, "durchsichtig");
        farbeSetzen(10, 6, "durchsichtig");
        farbeSetzen(9, 6, "durchsichtig");
        farbeSetzen(8, 6, "durchsichtig");
        farbeSetzen(7, 6, "durchsichtig");
        farbeSetzen(6, 6, "durchsichtig");
        farbeSetzen(6, 7, "durchsichtig");
        farbeSetzen(6, 8, "durchsichtig");

        // Zusätzliche komplexe Labyrinth-Pfade
        farbeSetzen(5, 8, "durchsichtig");
        farbeSetzen(4, 8, "durchsichtig");
        farbeSetzen(4, 7, "durchsichtig");
        farbeSetzen(4, 6, "durchsichtig");
        farbeSetzen(5, 6, "durchsichtig");
        farbeSetzen(6, 6, "durchsichtig");
        farbeSetzen(7, 6, "durchsichtig");
        farbeSetzen(7, 5, "durchsichtig");
        farbeSetzen(7, 4, "durchsichtig");

        farbeSetzen(8, 9, "durchsichtig");
        farbeSetzen(9, 9, "durchsichtig");
        farbeSetzen(10, 9, "durchsichtig");
        farbeSetzen(11, 9, "durchsichtig");
        farbeSetzen(11, 8, "durchsichtig");
        farbeSetzen(11, 7, "durchsichtig");
        farbeSetzen(10, 7, "durchsichtig");
        farbeSetzen(10, 6, "durchsichtig");

        farbeSetzen(3, 8, "durchsichtig");
        farbeSetzen(3, 9, "durchsichtig");
        farbeSetzen(3, 10, "durchsichtig");
        farbeSetzen(4, 10, "durchsichtig");
        farbeSetzen(5, 10, "durchsichtig");
        farbeSetzen(6, 10, "durchsichtig");
        farbeSetzen(6, 11, "durchsichtig");
        farbeSetzen(6, 12, "durchsichtig");
        farbeSetzen(5, 12, "durchsichtig");
        farbeSetzen(4, 12, "durchsichtig");

        farbeSetzen(12, 8, "durchsichtig");
        farbeSetzen(12, 9, "durchsichtig");
        farbeSetzen(12, 10, "durchsichtig");
        farbeSetzen(13, 10, "durchsichtig");
        farbeSetzen(14, 10, "durchsichtig");
        farbeSetzen(14, 9, "durchsichtig");
        farbeSetzen(14, 8, "durchsichtig");
        farbeSetzen(13, 8, "durchsichtig");

        // Verbindung der Sackgassen
        farbeSetzen(9, 4, "durchsichtig");
        farbeSetzen(10, 4, "durchsichtig");
        farbeSetzen(11, 4, "durchsichtig");
        farbeSetzen(11, 5, "durchsichtig");
        farbeSetzen(12, 5, "durchsichtig");
        farbeSetzen(12, 6, "durchsichtig");
        farbeSetzen(13, 6, "durchsichtig");
        farbeSetzen(13, 7, "durchsichtig");

        farbeSetzen(7, 13, "durchsichtig");
        farbeSetzen(8, 13, "durchsichtig");
        farbeSetzen(9, 13, "durchsichtig");
        farbeSetzen(10, 13, "durchsichtig");
        farbeSetzen(10, 12, "durchsichtig");
        farbeSetzen(9, 12, "durchsichtig");
        farbeSetzen(8, 12, "durchsichtig");


        // food
        farbeSetzen(SIZE/2+1, SIZE/2+1+FOODOFFSET, Color.ORANGE);
    }

    public void tick(int nr) {
        if (nr == 1) { // first grow phase
            grow();
            repaint();
        }
        if (nr == 2) {
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
        new Labyrint();
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
