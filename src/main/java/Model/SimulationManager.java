package Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class SimulationManager implements SimulationObservable {

    private Grid grid;
    private List<Entity> entities;
    private int turn;

    private List<SimulationListener> listeners;

    private static final double MAX_FILL_RATIO = 0.5;

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

    /**
     * Retourne le numéro du tour en cours
     *
     * @return un entier (Numero du tour)
     */
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
                grid.getCell(i, j).growGrass();
            }
        }

        //Copie de la liste + Nettoyage des morts
        new ArrayList<>(entities).stream().filter(e -> !e.isAlive()).forEach(e -> removeEntity(e));

        notifyTurnEnded();
    }

    /**
     * Vérifie si la position X et Y d'une case est libre (isFree()) Si oui,
     * l'entité est ajoutée à la liste des entitées et est placée sur la grille
     * (setOccuprant())
     *
     * @param e l'entité qu'on veut placer dans la grille
     */
    public void addEntity(Entity e) {
        if (grid.getCell(e.getX(), e.getY()).isFree()) {
            entities.add(e);
            grid.getCell(e.getX(), e.getY()).setOccupant(e);
        }
    }

    private void placeEntity(Entity baby, Entity parent) {
        // cherche une case libre adjacente au parent
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        for (int[] dir : directions) {
            int newX = parent.getX() + dir[0];
            int newY = parent.getY() + dir[1];
            if (grid.isInside(newX, newY) && grid.getCell(newX, newY).isFree()) {
                baby.setPosition(newX, newY);
                addEntity(baby);
                return;
            }
        }
        // pas de case libre → bébé perdu
    }

    public void addEntityAtRAndom(Entity e) {

        if (entities.size() >= grid.getLength() * grid.getWidth() * MAX_FILL_RATIO) {
            throw new IllegalStateException("La grille est pleine, impossible d'ajouter une entité.");
        } else {
            int x, y;

            do {
                x = (int) (Math.random() * grid.getLength());
                y = (int) (Math.random() * grid.getWidth());
            } while (!grid.getCell(x, y).isFree());

            e.setPosition(x, y);
            addEntity(e);
        }
    }

    /**
     * Retire une entité de la liste des entitées et modifie la valeur de la
     * case où elle était à null
     *
     * @param e
     */
    public void removeEntity(Entity e) {
        entities.remove(e);

        grid.getCell(e.getX(), e.getY()).setOccupant(null);
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
