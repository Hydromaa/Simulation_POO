package Model;

public class Sheep extends Entity {

    private int fleeThreshold;

    public Sheep(int fleeThreshold, int x, int y, int energy, int energyMax, int viewRange, int reproduceThreshold, int reproduceCost) {
        super(x, y, energy, energyMax, viewRange, reproduceThreshold, reproduceCost);
        this.fleeThreshold = fleeThreshold;
    }

    public int getFleeThreshold() {
        return fleeThreshold;
    }

    public void setFleeThreshold(int fleeThreshold) {
        this.fleeThreshold = fleeThreshold;
    }

    @Override
    public Entity reproduce(Entity other) {

        //Mouton 1 et mouton 2
        this.setEnergy(this.getEnergy() - getReproduceCost());
        other.setEnergy(other.getEnergy() - other.getReproduceCost());

        Sheep baby_sheep = new Sheep(fleeThreshold,
                0,
                0,
                (int) ((Math.random() * 0.4 + 0.4) * getEnergyMax()),
                getEnergyMax(),
                getViewRange(),
                getReproduceThreshold(),
                getReproduceCost());

        return baby_sheep;
    }

    @Override
    public Entity agir(Grid grid) {

        Wolf predator = (Wolf) findNearestEntity(grid, Wolf.class);

        if (predator != null) {
            flee(predator, grid);
        } else if (getEnergy() < getEnergyMax() * 0.4) {
            graze(grid);
        } else if (getEnergy() >= getReproduceThreshold()) {
            return seekMate(grid, Sheep.class);
        } else {
            move(grid);
        }
        return null;
    }

    private void flee(Wolf predator, Grid grid) {

        int dx = Integer.signum(this.getX() - predator.getX());
        int dy = Integer.signum(this.getY() - predator.getY());

        int newX = getX() + dx;
        int newY = getY() + dy;

        if (grid.isInside(newX, newY) && grid.getCell(newX, newY).isFree()) {
            moveTo(newX, newY, grid);
        } else {
            move(grid);
        }
    }

    private void graze(Grid grid) {
        Cell grassCell = grid.getCell(this.getX(), this.getY());
        //Vérifie la présence de grass sur sa case + mange si oui
        if (grassCell.getGrassLevel() > 0) {
            grid.getCell(this.getX(), this.getY()).eatGrass(((int) (Math.random() * 10)));
            this.setEnergy(this.getEnergy() + (int) (Math.random() * 10));
        } else {
            //Pas d'herbe sur case, cherche à proximité et bouge. Si pas de grass à proxi => move random
            int[] chosen = findNearestGrass(grid);
            if (chosen == null) {
                move(grid);
            } else {
                moveTo(chosen[0], chosen[1], grid);
            }
        }
    }

    @Override
    public String toString() {
        return "Sheep [x=" + getX() + ", y=" + getY() + ", energy=" + getEnergy() + "]";
    }

}
