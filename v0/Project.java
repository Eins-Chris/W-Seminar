import java.awt.Color;
import java.util.ArrayList;

public class Project extends Visualisation implements Variables {

    public Organism[][] organism_matrix = new Organism[GRIDSIZE+1][GRIDSIZE+1];
    public ArrayList<Organism> organism_list = new ArrayList<>();
    public boolean project_running = false;
    public static boolean variables_defined = false;

    public static void main(String[] args) {
        new Project();
    }

    public Project() {
        new ParameterWindow();
        while (!variables_defined) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Weiter!!!");
        //init();
        //start();
    }

    public void init() {
        for (int i = 0; i < QUANTITY; i++) organism_list.add(new Organism());
    }

    public void print() {
        for (Organism organism : organism_list) {
            organism_matrix[organism.getXPos()][organism.getYPos()] = organism;
        }
        for (int i = 0; i < GRIDSIZE+1; i++) {
            for (int j = 0; j < GRIDSIZE+1; j++) {
                if (organism_matrix[i][j] != null) {}//farbeSetzen(i, j, getColorfromState(organism_matrix[i][j].getCellState()));
            }
        }
        //repaint();
    }

    public Color getColorfromState(int state) {
        switch (state) {
            case 0: return Color.GREEN;
            case 1: return Color.YELLOW;
            default: return Color.GRAY;
        }
    }

    public void start() {
        for (int i = 0; i < organism_list.size(); i++) organism_list.get(i).start();
        project_running = true;
        while (project_running) {

            print();

            System.out.println(Variables.MAX_USERS + "");

            /*
                2 Ticks pro Sekunde 
            */
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
