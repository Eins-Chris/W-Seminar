public interface Variables {

    /* 
        WACHSTUMSDYNAMIKEN UND AUSBREITUNGSSTRATEGIEN
    */
    public boolean DIRECTIONAL = true;
    public int DIRECTIONS = 2;


    /* 
        RÄUMLICHE GEGEBENHEITEN
    */
    //
    

    /* 
        GRAPHIK VARIABLEN
    */
    public int CELLSIZE = 10;
    public int GRIDSIZE = 100;
    
    
    /* 
    SIMULATION VARIABLEN
    */
    public int STEP_TIME = 1000; // in Millisekunden
    public int QUANTITY = 5;
    

    /* 
        SPÄTER NOCH UMZUSETZEN
    */
        // Cooperation zwischen Organismen beim Zusammentreffen
            public boolean COOPERATIVE = false;
        // Wenn keine Cooperation besteht, wer gewinnt unter welchen Bedingungen
        // Wie abhängig ist der Organismus von ursprungskriterien (Startpunkt)
        // Unterschiede freie Flächen | eingeschränkte Räume | vlt Labyrinth
        // interne Ressourcenweitergabe (Ressourcenaufnahme auf einer Seite / einem Arm -> Ausbreitung nur an diesem Arm oder auch an anderen Armen?)
        // bevorzugt "breitere" Passagen oder offene Wege eher, bevor sich durch Passagen der Dicke ? ausgebreitet wird.
        // aggressive oder passive Ausbreitungsart (state)


    
            // Int-Variablen
            int MAX_USERS = 100;
            int TIMEOUT_SECONDS = 30;
            int RETRY_COUNT = 5;
            int PORT = 8080;
            int BUFFER_SIZE = 1024;
        
            // Boolean-Variablen
            boolean ENABLE_LOGGING = true;
            boolean DEBUG_MODE = false;
            boolean AUTO_SAVE = true;
            boolean FULLSCREEN = false;
            boolean USE_CACHE = true;       

}
