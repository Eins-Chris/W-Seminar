public class Organism extends Thread {
    
    private int xPos, yPos;

    public Organism() {
        this.xPos = 1 + (int) (Math.random() * (Project.SIZE));
        this.yPos = 1 + (int) (Math.random() * (Project.SIZE));
    }

    public Organism(int xPos, int yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(Project.STEP_TIME);
            } catch (InterruptedException e) { 
                e.printStackTrace(); 
            }

            try {
                // Aktuelle Position merken
                int currentX = this.x;
                int currentY = this.y;
        
                // Liste von möglichen Richtungen definieren
                // Vektoren: [dx, dy]
                int[][] directions;
        
                if (Project.DIRECTIONAL) {
                    // Nur DIRECTIONS erlauben, z.B. 2 = horizontal (links/rechts), 4 = NESW, 8 = inkl. Diagonalen
                    switch (Project.DIRECTIONS) {
                        case 2:
                            directions = new int[][]{{1, 0}, {-1, 0}}; // horizontal
                            break;
                        case 4:
                            directions = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}}; // NESW
                            break;
                        case 8:
                            directions = new int[][]{
                                    {1, 0}, {-1, 0}, {0, 1}, {0, -1},
                                    {1, 1}, {1, -1}, {-1, 1}, {-1, -1}
                            }; // NESW + Diagonalen
                            break;
                        default:
                            directions = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}}; // fallback
                            break;
                    }
                } else {
                    // Wenn keine Richtungsvorgabe: alle angrenzenden Zellen
                    directions = new int[][]{
                            {1, 0}, {-1, 0}, {0, 1}, {0, -1},
                            {1, 1}, {1, -1}, {-1, 1}, {-1, -1}
                    };
                }
        
                // Versuche, dich in die angegebenen Richtungen auszubreiten
                for (int[] dir : directions) {
                    int newX = currentX + dir[0];
                    int newY = currentY + dir[1];
        
                    // Bounds-Check (nicht außerhalb des Grids gehen)
                    if (newX >= 0 && newX < Project.SIZE && newY >= 0 && newY < Project.SIZE) {
                        // Prüfe, ob Zelle frei ist
                        if (grid[newX][newY] == null) {
                            // Belege die neue Zelle
                            grid[newX][newY] = new Cell(this); // oder dein Organismus-Referenz
                            // Optional: neue Position merken oder Liste von besetzten Feldern aktualisieren
                        }
                    }
                }
        
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        

        }
    }

}
