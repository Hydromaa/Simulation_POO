package Model;

import Model.Grid;
import Model.Sheep;

public class Wolf extends Entity {

    private int huntThreshold;

    public Wolf(int huntThreshold, int x, int y, int energy, int energyMax, int viewRange, int reproduceThreshold, int reproduceCost) {
        super(x, y, energy, energyMax, viewRange, reproduceThreshold, reproduceCost);
        this.huntThreshold = huntThreshold;
    }

    public int getHuntThreshold() {
        return huntThreshold;
    }

    public void setHuntThreshold(int huntThreshold) {
        this.huntThreshold = huntThreshold;
    }

    @Override
    public Entity reproduce(Entity other) {

        //Loup 1 et loup 2
        this.setEnergy(this.getEnergy() - getReproduceCost());
        other.setEnergy(other.getEnergy() - other.getReproduceCost());

        Wolf baby_wolf = new Wolf(huntThreshold,
                0,
                0,
                (int) ((Math.random() * 0.4 + 0.4) * getEnergyMax()),
                getEnergyMax(),
                getViewRange(),
                getReproduceThreshold(),
                getReproduceCost());

        return baby_wolf;
    }

    @Override
    public Entity agir(Grid grid) {

        if (this.getEnergy() < huntThreshold) {
            hunt(grid);
        } else if (getEnergy() >= getReproduceThreshold()) {
            return seekMate(grid, Wolf.class);
        } else {
            move(grid);
        }
        return null;
    }

    private void hunt(Grid grid) {

        Sheep targetSheep = (Sheep) findNearestEntity(grid, Sheep.class);

        if (targetSheep == null) {
            move(grid);
        } else {
            //dx et dy = direction à prendre. Target en 2;5, this en 1;4 => 2-1 = 1 et 5-4 = 1. X et Y doivent faire +1 pour aller vers la cible
            // signum = +1, 0 ou -1 en fonction du résultat
            int dx = Integer.signum(targetSheep.getX() - this.getX());
            int dy = Integer.signum(targetSheep.getY() - this.getY());
            int newX = getX() + dx;
            int newY = getY() + dy;

            if (newX == targetSheep.getX() && newY == targetSheep.getY()) {
                eat(targetSheep);
            } else if (grid.isInside(newX, newY) && grid.getCell(newX, newY).isFree()) {
                moveTo(newX, newY, grid);
            } else {
                move(grid);
            }
        }
    }

    private void eat(Sheep sheep) {
        //20 à 40
        int x = (int) ((Math.random() * 0.2 + 0.2) * getEnergyMax());

        this.setEnergy(this.getEnergy() + x);
        sheep.setEnergy(0);
    }

    @Override
    public String toString() {
        return "Wolf [x=" + getX() + ", y=" + getY() + ", energy=" + getEnergy() + "]";
    }

}
