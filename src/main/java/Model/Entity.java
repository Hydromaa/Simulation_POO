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

        if (energyMax <= 0) {
            throw new IllegalArgumentException("L'énergie max (energyMax) doit être positif. Reçu : " + energyMax);
        }
        if (viewRange <= 0) {
            throw new IllegalArgumentException("La distance de vision (viewRange) doit être positif. Reçu : " + viewRange);
        }
        if (energy <= 0) {
            throw new IllegalArgumentException("L'énergie (energy) doit être strictement positive (>0). Reçu : " + energy);
        }

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

    /**
     * Vérifie si l'énergie d'une entité est suppérieure à 0
     *
     * @return retourne true si vrai, false si faux
     */
    public boolean isAlive() {
        return energy > 0;
    }

    /**
     * Déplacement d'une entité vers une case adjacente libre choisie
     * aléatoirement. Si aucune case libre, l'entité reste sur place.
     *
     * @param grid Grille de la simulation
     */
    public void move(Grid grid) {
        List<int[]> validPositions = new ArrayList<>();
        //Direction possible pour X et Y
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        //Vérifications
        for (int[] dir : directions) {
            int newX = x + dir[0];
            int newY = y + dir[1];

            //Si la case est dans la grille ET si la case est libre
            if (grid.isInside(newX, newY) && grid.getCell(newX, newY).isFree()) {
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

        grid.getCell(this.getX(), this.getY()).setOccupant(null);
        this.setPosition(newX, newY);
        grid.getCell(newX, newY).setOccupant(this);
    }

    /**
     * Active le comportement d'une entité (Redéfinie pour chacune d'elles)
     *
     * @param grid
     * @return
     */
    public abstract Entity agir(Grid grid);

    /**
     * Cherche l'entité spécifiée la plus proche dans un rayon de 'getViewRange'
     * (Dépendant du type de l'entité) Si plusieurs entitées trouvées, distance
     * euclidiène calculée pour trouver la plus proche
     *
     * @param grid Grille de simulation
     * @param type le type d'entité recherchée (ex: Wolf.class, Sheep.class,
     * ...)
     * @return renvoi l'entité trouvée la plus proche
     */
    public Entity findNearestEntity(Grid grid, Class<?> type) {
        //Recherche autour de l'entité
        double minDistance = Double.MAX_VALUE;
        Entity nearest = null;

        for (int dx = -getViewRange(); dx <= getViewRange(); dx++) {
            for (int dy = -getViewRange(); dy <= getViewRange(); dy++) {

                //Distance euclidienne (Longueur du chemin le plus court en ligne droite)
                double distance = Math.sqrt(dx * dx + dy * dy);

                int checkX = getX() + dx;
                int checkY = getY() + dy;

                if (grid.isInside(checkX, checkY)) {
                    Entity occupant = grid.getCell(checkX, checkY).getOccupant();
                    if (type.isInstance(occupant) && occupant != this) {
                        if (distance < minDistance) {
                            minDistance = distance;
                            nearest = occupant;
                        }
                    }
                }
            }
        }
        return nearest;
    }

    protected int[] findNearestGrass(Grid grid) {

        List<int[]> grassPositions = new ArrayList<>();

        for (int dx = -getViewRange(); dx <= getViewRange(); dx++) {
            for (int dy = -getViewRange(); dy <= getViewRange(); dy++) {
                int checkX = getX() + dx;
                int checkY = getY() + dy;

                if (grid.isInside(checkX, checkY)) {
                    Cell grass = grid.getCell(checkX, checkY);
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

        grid.getCell(getX(), getY()).setOccupant(null);
        setPosition(newX, newY);
        grid.getCell(newX, newY).setOccupant(this);
    }

    protected Entity seekMate(Grid grid, Class<?> type) {

        Entity target = findNearestEntity(grid, type);
        if (target == null) {
            move(grid);
        } else {
            int dx = Integer.signum(target.getX() - this.getX());
            int dy = Integer.signum(target.getY() - this.getY());
            int newX = getX() + dx;
            int newY = getY() + dy;

            if (newX == target.getX() && newY == target.getY() && target.getEnergy() >= target.getReproduceThreshold()) {
                return reproduce(target);
            } else if (grid.isInside(newX, newY) && grid.getCell(newX, newY).isFree()) {
                moveTo(newX, newY, grid);

            } else {
                move(grid);
            }
        }
        return null;
    }
}
