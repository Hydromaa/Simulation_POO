package Model;

import java.util.ArrayList;
import java.util.List;

public abstract class Entity {

    private int x;
    private int y;
    private int energy;
    private int energyMax;
    private int viewRange;
    private int reproduceThreshold;
    private int reproduceCost;

    public Entity(int x, int y, int energy, int energyMax, int viewRange, int reproduceThreshold, int reproduceCost) {
        this.x = x;
        this.y = y;
        this.energy = energy;
        this.energyMax = energyMax;
        this.viewRange = viewRange;
        this.reproduceThreshold = reproduceThreshold;
        this.reproduceCost = reproduceCost;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        //Plus grande valeur entre energy et 0 => Plus petite valeur entre la précédente et energyMax
        //Minimum 0 et maximum energyMax à la fin
        //Un peu tordu mais efficace (Merci l'IA...)
        this.energy = Math.min(Math.max(energy, 0), energyMax);
    }

    public int getEnergyMax() {
        return energyMax;
    }

    public void setEnergyMax(int energyMax) {
        this.energyMax = energyMax;
    }

    public int getReproduceThreshold() {
        return reproduceThreshold;
    }

    public void setReproduceThreshold(int reproduceThreshold) {
        this.reproduceThreshold = reproduceThreshold;
    }

    public int getReproduceCost() {
        return reproduceCost;
    }

    public void setReproduceCost(int reproduceCost) {
        this.reproduceCost = reproduceCost;
    }

    public int getViewRange() {
        return viewRange;
    }

    public void setViewRange(int viewRange) {
        this.viewRange = viewRange;
    }

    public abstract Entity reproduce(Entity other);

    public boolean isAlive() {
        return energy > 0;
    }

    public void move(Grid grid) {
        List<int[]> validPositions = new ArrayList<>();
        //Direction possible pour X et Y
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        //Vérifications
        for (int[] dir : directions) {
            int newX = x + dir[0];
            int newY = y + dir[1];

            //Si la case est dans la grille ET si la case est libre
            if (grid.isInside(newX, newY) && grid.getCells(newX, newY).isFree()) {
                //Save des positions possibles
                validPositions.add(new int[]{newX, newY});
            }
        }

        if (validPositions.isEmpty()) {
            return;
        }

        int[] chosen = validPositions.get((int) (Math.random() * validPositions.size()));

        int newX = chosen[0];
        int newY = chosen[1];

        grid.getCells(this.getX(), this.getY()).setOccupant(null);
        this.setPosition(newX, newY);
        grid.getCells(newX, newY).setOccupant(this);
    }

    public abstract Entity agir(Grid grid);

    public Entity findNearestEntity(Grid grid, Class<?> type) {
        //Recherche autour de l'entité
        for (int dx = -getViewRange(); dx <= getViewRange(); dx++) {
            for (int dy = -getViewRange(); dy <= getViewRange(); dy++) {
                int checkX = getX() + dx;
                int checkY = getY() + dy;

                if (grid.isInside(checkX, checkY)) {
                    Entity occupant = grid.getCells(checkX, checkY).getOccupant();
                    if (type.isInstance(occupant) && occupant != this) {
                        return occupant;
                    }
                }

            }
        }
        return null;
    }

    protected int[] findNearestGrass(Grid grid) {

        List<int[]> grassPositions = new ArrayList<>();

        for (int dx = -getViewRange(); dx <= getViewRange(); dx++) {
            for (int dy = -getViewRange(); dy <= getViewRange(); dy++) {
                int checkX = getX() + dx;
                int checkY = getY() + dy;

                if (grid.isInside(checkX, checkY)) {
                    Cell grass = grid.getCells(checkX, checkY);
                    if (grass.getGrassLevel() > 0) {
                        grassPositions.add(new int[]{checkX, checkY});
                    }
                }

            }
        }
        //Renvoi une case aléatoire (Sinon mouvement presque tout le temps similaire)
        if (!grassPositions.isEmpty()) {
            int[] chosen = grassPositions.get((int) (Math.random() * grassPositions.size()));
            return chosen;
        }
        return null;
    }

    protected void moveTo(int newX, int newY, Grid grid) {

        grid.getCells(getX(), getY()).setOccupant(null);
        setPosition(newX, newY);
        grid.getCells(newX, newY).setOccupant(this);
    }
}
