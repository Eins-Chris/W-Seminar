import java.util.ArrayList;

public class Project extends V0Kaestchen {


    /* 
        WACHSTUMSDYNAMIKEN UND AUSBREITUNGSSTRATEGIEN
    */
    public static final boolean DIRECTIONAL = true;
    public static final int DIRECTIONS = 2;


    /* 
        RÄUMLICHE GEGEBENHEITEN
    */
    //public static final int A = 0;
    

    /* 
        VARIABLEN
    */
    public ArrayList<Organism> organisms = new ArrayList<>();
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

    



    public static void main(String[] args) {
        new Project();
    }

    public Project() {
        super(10,10,100,100);
        init();
        start();
    }

    public void init() {
        for (int i = 0; i < QUANTITY; i++) organisms.add(new Organism());
    }

    public void start() {
        for (int i = 0; i < organisms.size(); i++) organisms.get(i).start();
        project_running = true;
        while (project_running) {
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {

                }
            }
        }
    }
}
