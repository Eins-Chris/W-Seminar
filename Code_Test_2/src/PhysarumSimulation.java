import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PhysarumSimulation extends Kaestchen {
    private static final int SIZE = 40; // Größe des Gitters für ein größeres Feld
    private static final int INITIAL_POWER = 15; // Startkraft
    private static final int SPREAD_FACTOR = 1; // Faktor für die Ausbreitung, angepasst für größere Felder
    private static final int TICK_DELAY = 6; // Ticks bis Zellen beginnen, Power zu verlieren
    private static final int POWER_DECAY_INTERVAL = 3; // Ticks zwischen Power-Verlust

    private int[][] powerGrid = new int[SIZE][SIZE];
    private int[][] tickGrid = new int[SIZE][SIZE]; // Zählt die Ticks pro Zelle
    private Random random = new Random();

    public PhysarumSimulation() {
        super(20, 20, SIZE, SIZE); // Anpassung für größere Pixelanzahl
        initialize();
    }

    // Initialisiert das Gitter
    private void initialize() {
        int centerX = SIZE / 2;
        int centerY = SIZE / 2;
        powerGrid[centerX][centerY] = INITIAL_POWER; // Zentrum initialisieren
        farbeSetzen(centerX + 1, centerY + 1, "gelb");
    }

    // Führt die Simulation aus
    public void runSimulation() {
        boolean hasGrowth = true;

        while (hasGrowth) {
            hasGrowth = spreadGrowth();
            repaint();
            try {
                Thread.sleep(100); // Schnellere Animation
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Nach Beendigung des Wachstums weiterführen, bis der Pilz ausstirbt
        decaySimulation();
    }

    // Simuliert das Wachstum für einen Schritt
    private boolean spreadGrowth() {
        boolean hasGrowth = false;
        int[][] newPowerGrid = new int[SIZE][SIZE]; // Temporäres Gitter für den nächsten Schritt

        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                if (powerGrid[x][y] > 0) {
                    // Inkrementiere Tick für die aktuelle Zelle
                    tickGrid[x][y]++;

                    // Reduziere Power basierend auf TICK_DELAY und POWER_DECAY_INTERVAL
                    if (tickGrid[x][y] > TICK_DELAY && tickGrid[x][y] % POWER_DECAY_INTERVAL == 0) {
                        powerGrid[x][y]--;
                        if (powerGrid[x][y] == 0) {
                            farbeSetzen(x + 1, y + 1, "weiß"); // Zelle stirbt
                            continue;
                        }
                    }

                    List<int[]> neighbors = getShuffledNeighbors(x, y);
                    boolean branched = false; // Erlaubt eine Verzweigung

                    for (int[] neighbor : neighbors) {
                        int nx = neighbor[0];
                        int ny = neighbor[1];
                        if (nx >= 0 && ny >= 0 && nx < SIZE && ny < SIZE) {
                            if (newPowerGrid[nx][ny] == 0 && powerGrid[x][y] - SPREAD_FACTOR > 0) {
                                newPowerGrid[nx][ny] = powerGrid[x][y] - SPREAD_FACTOR;
                                tickGrid[nx][ny] = 0; // Reset der Ticks für neue Zelle
                                farbeSetzen(nx + 1, ny + 1, "grün");
                                hasGrowth = true;

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
        return hasGrowth;
    }

    // Führt die Vergehensphase aus
    private void decaySimulation() {
        boolean stillAlive = true;

        while (stillAlive) {
            stillAlive = decayPilz();
            repaint();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Lässt den Pilz nach dem Wachstum aussterben
    private boolean decayPilz() {
        boolean stillAlive = false;

        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                if (powerGrid[x][y] > 0) {
                    tickGrid[x][y]++;

                    // Reduziere Power basierend auf POWER_DECAY_INTERVAL
                    if (tickGrid[x][y] % POWER_DECAY_INTERVAL == 0) {
                        powerGrid[x][y]--;
                        if (powerGrid[x][y] == 0) {
                            farbeSetzen(x + 1, y + 1, "weiß"); // Zelle stirbt
                        }
                    }

                    if (powerGrid[x][y] > 0) {
                        stillAlive = true;
                    }
                }
            }
        }

        return stillAlive;
    }

    // Gibt die Nachbarn einer Zelle in zufälliger Reihenfolge zurück
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
        PhysarumSimulation simulation = new PhysarumSimulation();
        simulation.runSimulation();
    }
}
