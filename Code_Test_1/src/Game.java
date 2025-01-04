import java.awt.Color;

public class Game extends Kaestchen {

    private float[][] matrix = new float[100][100];
    private final int START = 20;

    public static void main(String[] args) {
        new Game().start();
    }

    public Game() {
        super(20,20,40,40);
    }

    public void start() {
        initialize();
        //move(START,START);
        print();
    }

    public void print() {
        for (int i = 1; i < 40; i++) {
            for (int j = 1; j < 40; j++) {
                farbeSetzen(i, j, getColor(matrix[i-1][j-1]));
                System.out.print(matrix[i-1][j-1] + "|");
            }
            System.out.println();
        }
    }

    public Color getColor(float input) {
        switch ((int) (input*10)) {
            case 10:
                return Color.GREEN;
            default:
                return Color.WHITE;
        }
    }

    public void initialize() {
        //Blob placement
        matrix[START][START] = 1;
    }

    public float[][] getNeighbours(int x, int y) {
        float[][] result = new float[3][3];
        result[0][0] = matrix[x-1][y-1];
        result[1][0] = matrix[x][y-1];
        result[2][0] = matrix[x+1][y-1];
        result[0][1] = matrix[x-1][y];
        result[1][1] = matrix[x][y];
        result[2][1] = matrix[x+1][y];
        result[0][2] = matrix[x-1][y+1];
        result[1][2] = matrix[x][y+1];
        result[2][2] = matrix[x+1][y+1];
        return result;
    }

    public void move(int x, int y) {
        // matrix[x][y] => für 1 in 3 oder 4 Richtungen, für weniger in 2 Richtungen...
        // move ausführen für nachbaren in den genannten richtungen
    }

}