import java.awt.Color;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game extends Kaestchen {

    private float[][] matrix = new float[100][100];
    private final int STARTX = 20;
    private final int STARTY = 20;

    public static void main(String[] args) {
        new Game().start();
    }

    public Game() {
        super(20,20,40,40);
    }

    public void start() {
        initialize();
        move(STARTX,STARTY,STARTX,STARTY, getQuantity(STARTX, STARTY));
        tick();
        print();
    }

    public void initialize() {
        // start placement
        matrix[STARTX][STARTY] = 9.9f;
    }

    public void move(int x, int y, int ox, int oy, int quantity) {
        tick();

        if (getPower(x, y) <= 0) { return; }

        try {
            Thread.sleep(1000); // 1000 Millisekunden = 1 Sekunde
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Setzt den Unterbrechungsstatus wieder
            System.out.println("Thread wurde unterbrochen: " + e.getMessage());
        }

        int[] startup = {-ox+x, -oy+y};
        for (int i=0; i < quantity; i++) {
            int[] neighbour = getNeighbour(x, y, startup);
            if (neighbour[0] == 0 && neighbour[1] == 0) { return; }
            int nx = x + neighbour[0];
            int ny = y + neighbour[1];
            matrix[nx][ny] = getPower(x, y) - 1 + 0.9f;
            move(nx, ny, x, y, getQuantity(nx, ny)+1);
        }
    }

    public int[] getNeighbour(int x, int y, int[] startup) {
        List<int[]> list = getNeighbours(x, y, 8, startup);
        for (int i = 0; i < list.size();) {
            return list.get(i);
        }
        int[] result = {0,0};
        return result;
    }

    public List<int[]> getNeighbours(int x, int y, int quantity, int[] startup) {
        int sx = startup[0]+1;
        int sy = startup[1]+1;

        int[][][] localmatrix = {
            {{-1,-1}, {-1, 0}, {-1, 1}},
            {{ 0,-1}, { 0, 0}, { 0, 1}},
            {{ 1,-1}, { 1, 0}, { 1, 1}},
        };
        int[][] directions = {
            {-1, 0}, {1, 0}, {0, -1}, {0, 1}, // direkte Nachbarn oben, unten, links, rechts
            {-1, -1}, {-1, 1}, {1, -1}, {1, 1} // diagonale Nachbarn
        };

        List<int[]> neighbours = new ArrayList<>();
        for (int[] dir : directions) {
            int nx = sx + dir[0];
            int ny = sy + dir[1];
            if (nx >= 0 && nx < localmatrix.length && ny >= 0 && ny < localmatrix[0].length) {
                neighbours.add(new int[]{localmatrix[nx][ny][0], localmatrix[nx][ny][1]});
            }
        }
        //  System.out.println("Sx: " + sx + " | Sy: " + sy);
        neighbours.add(new int[]{localmatrix[sx][sy][0], localmatrix[sx][sy][1]});

        // remove the possibility to set itself
        neighbours.removeIf(neighbour -> neighbour[0] == 0 && neighbour[1] == 0);

        // remove the possibility to get onto another existing
        neighbours.removeIf(neighbour -> getTime(x+neighbour[0], y+neighbour[1]) != 0 && getPower(x+neighbour[0], y+neighbour[1]) < getPower(x, y));

        Collections.shuffle(neighbours);
        
        return neighbours.subList(0, Math.min(quantity, neighbours.size()));
    }

    public void print() {
        for (int y = 1; y <= 40; y++) {
            for (int x = 1; x <= 40; x++) {
                farbeSetzen(x, y, getColor(x, y));
                System.out.print(matrix[x-1][y-1] + "|");
                //System.out.print(getPower(x, y) + "|");
            }
            System.out.println();
        }
        repaint();
    }

    public Color getColor(int x, int y) {
        switch (getPower(x, y)) {
            case 9:
            case 8:
            case 7:
            case 6:
            case 5:
                return Color.GREEN;
            case 4:
            case 3:
            case 2:
            case 1:
                return Color.YELLOW;
            case 0:
                return Color.WHITE;
            default:
                return Color.LIGHT_GRAY;
        }
    }

    public void tick() {
        for (int y = 1; y <= 40; y++) {
            for (int x = 1; x <= 40; x++) {
                if (getTime(x,y) != 0 && getPower(x, y) > 0) {
                    //BigDecimal value = new BigDecimal(Float.toString(matrix[x][y]));
                    //System.out.println(value + " | " + value.subtract(new BigDecimal("0.1")).floatValue());
                    //matrix[x][y] = value.subtract(new BigDecimal("0.1")).floatValue();
                    matrix[x][y] = getPower(x, y) + ((float) (getTime(x, y)-1) / 10);
                    if (matrix[x][y] < 0) {
                        matrix[x][y] = 0;
                    }
                }
            }
        }
        print();
    }

    public int getPower(int x, int y) {
        return (int) Math.floor(matrix[x][y]);
    }

    public int getTime(int x, int y) {
        return (int) ((matrix[x][y] - ((int) Math.floor(matrix[x][y]))) * 10);
    }

    public int getQuantity(int x, int y) {
        return Math.max(0, ((int) Math.ceil(getPower(x, y))-3)/2);
    }

}