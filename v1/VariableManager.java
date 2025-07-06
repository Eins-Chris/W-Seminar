package v1;

public class VariableManager {
    
    /* 
        WACHSTUMSDYNAMIKEN UND AUSBREITUNGSSTRATEGIEN
    */
    public boolean DIRECTIONAL = true;
    public int DIRECTIONS = 2;
    public boolean COOPERATIVE = false;


    /* 
        RÄUMLICHE GEGEBENHEITEN
    */
    //
    

    /* 
        GRAPHIK VARIABLEN
    */
    public int GRIDSIZE = 10;
    
    
    /* 
    SIMULATION VARIABLEN
    */
    public int STEP_TIME = 500;
    public int QUANTITY = 0;
    public int BODY_SIZE = 6;
    

    /* 
        SPÄTER NOCH UMZUSETZEN
    */
        // Wenn keine Cooperation besteht, wer gewinnt unter welchen Bedingungen
        // Wie abhängig ist der Organismus von ursprungskriterien (Startpunkt)
        // Unterschiede freie Flächen | eingeschränkte Räume | vlt Labyrinth
        // interne Ressourcenweitergabe (Ressourcenaufnahme auf einer Seite / einem Arm -> Ausbreitung nur an diesem Arm oder auch an anderen Armen?)
        // bevorzugt "breitere" Passagen oder offene Wege eher, bevor sich durch Passagen der Dicke ? ausgebreitet wird.
        // aggressive oder passive Ausbreitungsart (state)



    public void updateFromInputs(int[] intValues, boolean[] boolValues) {
        DIRECTIONS = intValues[0];
        GRIDSIZE = intValues[1];
        STEP_TIME = intValues[2];
        QUANTITY = intValues[3];
        BODY_SIZE = intValues[4];

        DIRECTIONAL = boolValues[0];
        COOPERATIVE = boolValues[1];

        ProjectView.run(this);
    }
}
