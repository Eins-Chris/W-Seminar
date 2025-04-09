import javax.swing.JFrame;

public class Visualisation extends JFrame {

    private int size;
    private int cesssize;
    private int[][] matrix;
    
    public Visualisation() {
        this.size = Project.SIZE;
        this.size = 10;
        this.matrix = new int[Project.SIZE][Project.SIZE];
    }

}
