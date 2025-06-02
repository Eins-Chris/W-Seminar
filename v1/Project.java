package v1;

import java.awt.Color;
import java.util.ArrayList;

public class Project {

    public static void run(VariableManager manager) {
        new Project(manager);
    }

    public VariableManager variable = new VariableManager();
    public Organism[][] organism_matrix = new Organism[variable.GRIDSIZE][variable.GRIDSIZE];
    public ArrayList<Organism> organism_list = new ArrayList<>();
    public boolean project_running = false;
    public ProjectView view;

    public Project(VariableManager variable) {
        this.variable = variable;
        view = new ProjectView(variable);

        init();
        //start();
    }

    public void init() {
        //for (int i = 0; i < variable.QUANTITY; i++) organism_list.add(new Organism(variable));
        view.setInputText("/start");
    }

    public void start() {
        for (int i = 0; i < organism_list.size(); i++) organism_list.get(i).start();
        project_running = true;
        while (project_running) {

            print();

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

    public void print() {
        for (Organism organism : organism_list) {
            organism_matrix[organism.getXPos()][organism.getYPos()] = organism;
        }
        for (int i = 0; i < variable.GRIDSIZE; i++) {
            for (int j = 0; j < variable.GRIDSIZE; j++) {
                if (organism_matrix[i][j] != null) {
                    view.set(i, j, getColorfromState(organism_matrix[i][j].getCellState()));
                }
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
}
