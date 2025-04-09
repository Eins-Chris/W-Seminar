import java.awt.Color;
import java.util.ArrayList;

public class Project extends V0Kaestchen {


    /* 
        WACHSTUMSDYNAMIKEN UND AUSBREITUNGSSTRATEGIEN
    */
    public static final boolean DIRECTIONAL = true;
    public static final int DIRECTIONS = 2;
    public static final boolean COOPERATIVE = true;


    /* 
        RÄUMLICHE GEGEBENHEITEN
    */
    //public static final int A = 0;
    

    /* 
        VARIABLEN
    */
    public Organism[][] organism_matrix = new Organism[SIZE][SIZE];
    public ArrayList<Organism> organism_list = new ArrayList<>();
    public boolean project_running = false;
    public static final int SIZE = 100;
    public static final int QUANTITY = 3;
    public static final int STEP_TIME = 1000; // in Millisekunden
    
    
    /* 
        SPÄTER NOCH UMZUSETZEN
    */
        // Cooperation zwischen Organismen beim Zusammentreffen
            // public static final boolean COOPERATIVE = false;
        // Wenn keine Cooperation besteht, wer gewinnt unter welchen Bedingungen
        // Wie abhängig ist der Organismus von ursprungskriterien (Startpunkt)
        // Unterschiede freie Flächen | eingeschränkte Räume | vlt Labyrinth
        // interne Ressourcenweitergabe (Ressourcenaufnahme auf einer Seite / einem Arm -> Ausbreitung nur an diesem Arm oder auch an anderen Armen?)
        // bevorzugt "breitere" Passagen oder offene Wege eher, bevor sich durch Passagen der Dicke ? ausgebreitet wird.
        // aggressive oder passive Ausbreitungsart (state)

    



    public static void main(String[] args) {
        new Project();
    }

    public Project() {
        super(10,10,100,100);
        init();
        start();
    }

    public void init() {
        for (int i = 0; i < QUANTITY; i++) organism_list.add(new Organism());
    }

    public void print() {
        for (Organism organism : organism_list) {
            organism_matrix[organism.getXPos()][organism.getYPos()] = organism;
        }
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (organism_matrix[i][j] != null) farbeSetzen(i, j, getColorfromState(organism_matrix[i][j].getCellState()));
            }
        }
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
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            print();
        }
    }
}
