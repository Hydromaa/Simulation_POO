package Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;

public class SimulationManager implements SimulationObservable {

    private Grid grid;
    private List<Entity> entities;
    private int turn;
    private List<Entity> newBorns;

    List<SimulationListener> listeners;

    private boolean running = false;
    private Thread simulationThread;

    
    public SimulationManager(int length, int width) {
        this.grid = new Grid(length, width);
        this.entities = new ArrayList<>();
        this.turn = 0;
        this.newBorns = new ArrayList<>();
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

    public boolean isRunning() {
        return running;
    }

    public void start() {
        running = true;
        //Fonction flechée - Remplace le 'new Runnable() { @override ...'
        simulationThread = new Thread(() -> {
            while (running) {
                nextTurn();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        simulationThread.start();
    }

    public void stop() {
        running = false;
    }

    public void nextTurn() {
        turn++;

        Collections.shuffle(entities);
        for (Entity e : entities) {
            Entity baby = e.agir(grid);
            if (baby != null) {
                newBorns.add(baby);
            }
        }

        newBorns.forEach(b -> addEntity(b));
        newBorns.clear();

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
