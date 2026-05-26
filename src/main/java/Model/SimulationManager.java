package Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class SimulationManager implements SimulationObservable {

    private Grid grid;
    private List<Entity> entities;
    private int turn;

    private List<SimulationListener> listeners;

    public SimulationManager(int length, int width) {
        this.grid = new Grid(length, width);
        this.entities = new ArrayList<>();
        this.turn = 0;
        this.listeners = new ArrayList<>();
    }

    public Grid getGrid() {
        return grid;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public int getTurn() {
        return turn;
    }

    public void nextTurn() {
        turn++;

        Collections.shuffle(entities);
        for (Entity e : new ArrayList<>(entities)) {  // copie pour itérer
            Entity baby = e.agir(grid);
            if (baby != null) {
                placeEntity(baby, e);    // modifie entities original → OK ✅
            }
        }

        for (int i = 0; i < grid.getLength(); i++) {
            for (int j = 0; j < grid.getWidth(); j++) {
                grid.getCells(i, j).growGrass();
            }
        }

        //Copie de la liste + Nettoyage des morts
        new ArrayList<>(entities).stream().filter(e -> !e.isAlive()).forEach(e -> removeEntity(e));

        notifyTurnEnded();
    }

    public void addEntity(Entity e) {
        if (grid.getCells(e.getX(), e.getY()).isFree()) {
            entities.add(e);
            grid.getCells(e.getX(), e.getY()).setOccupant(e);
        }
    }

    private void placeEntity(Entity baby, Entity parent) {
        // cherche une case libre adjacente au parent
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        for (int[] dir : directions) {
            int newX = parent.getX() + dir[0];
            int newY = parent.getY() + dir[1];
            if (grid.isInside(newX, newY) && grid.getCells(newX, newY).isFree()) {
                baby.setPosition(newX, newY);
                addEntity(baby);
                return;
            }
        }
        // pas de case libre → bébé perdu
    }

    public void removeEntity(Entity e) {
        entities.remove(e);

        grid.getCells(e.getX(), e.getY()).setOccupant(null);
    }

    @Override
    public void addSimulationListener(SimulationListener l) {
        listeners.add(l);
    }

    @Override
    public void removeSimulationListener(SimulationListener l) {
        listeners.remove(l);
    }

    @Override
    public void notifyTurnEnded() {
        for (SimulationListener l : listeners) {
            l.onTurnEnded(turn);
        }
    }

    @Override
    public void notifySimulationEnded() {
        for (SimulationListener l : listeners) {
            l.onSimulationEnded();
        }
    }

}
