import java.awt.Color;
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
        move(STARTX,STARTY,STARTX,STARTY);
        print();
    }

    public void initialize() {
        // start placement
        matrix[STARTX][STARTY] = 9.9f;
    }

    public void move(int x, int y, int ox, int oy) {
        // getting neighbours
        int[] startup = {-ox+x, -oy+y};
        //  System.out.println("X: "+x+" | Y: "+y);
        //  System.out.println("StartupX: "+-ox+"+"+x +" | StartupY: "+-oy+"+"+y);
        List<int[]> neighbours = getNeighbours(x, y, (int) Math.ceil(getPower(x, y) / 5.0), startup);

        // setting neighbour to -1 power and 9 time & move
        for (int[] neighbour : neighbours) {
            //  System.out.println("neighbour" + Arrays.toString(neighbour));
            //  System.out.println("Calling: x=" + neighbour[0] + ", y=" + neighbour[1] + ", ox=" + x + ", oy=" + y);
            matrix[x + neighbour[0]][y + neighbour[1]] = getPower(x, y) - 1 + 0.9f;
            move(x + neighbour[0], y + neighbour[1], x, y);
        }
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

        neighbours.removeIf(neighbour -> neighbour[0] == 0 && neighbour[1] == 0);

        Collections.shuffle(neighbours);
        
        return neighbours.subList(0, Math.min(quantity, neighbours.size()));
    }

    public void print() {
        for (int y = 1; y <= 40; y++) {
            for (int x = 1; x <= 40; x++) {
                farbeSetzen(x, y, getColor(x, y));
                //System.out.print(matrix[x-1][y-1] + "|");
                System.out.print(getPower(x, y) + "|");
            }
            System.out.println();
        }
    }

    public Color getColor(int x, int y) {
        switch (getPower(x, y)) {
            case 9:
                return Color.GREEN;
            case 8:
            case 7:
            case 6:
                return Color.YELLOW;
            case 5:
            case 4:
            case 3:
            case 2:
            case 1:
                return Color.ORANGE;
            case 0:
                return Color.WHITE;
            default:
                return Color.LIGHT_GRAY;
        }
    }

    public int getPower(int x, int y) {
        return (int) Math.floor(matrix[x][y]);
    }

    public int getTime(int x, int y) {
        return (int) ((matrix[x][y] - ((int) Math.floor(matrix[x][y]))) * 10);
    }

}