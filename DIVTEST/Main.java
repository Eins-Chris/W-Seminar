import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Main extends Kaestchen {
    Random random = new Random();
    public static void main(String[] args) {
        new Main();
    }

    public Main() {
        super(20, 20, 40, 40);
        farbeSetzen(21, 21, Color.GRAY);
        spread(20, 20);
    }

    public void spread(int x, int y) {
        List<int[]> neighbors = getShuffledNeighbors(x, y);

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int[] neighbor : neighbors) {
            int nx = neighbor[0];
            int ny = neighbor[1];
            if (nx >= 0 && ny >= 0 && nx < 40 && ny < 40 && farbeGeben(nx+1, ny+1) != "grau") {
                farbeSetzen(nx+1, ny+1, Color.GRAY);
                repaint();
                spread(nx, ny);
            }
        }
        
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
}
