package Model;

public class Wolf extends Entity {

    private int huntThreshold;
    private int huntRange;

    public Wolf(int huntThreshold, int x, int y, int energy, int energyMax, int viewRange, int reproduceThreshold, int reproduceCost, int viewReproduceRange, int huntRange) {
        //moveCost devient aléatoire : Entre 1 et 3
        super(x, y, energy, energyMax, viewRange, reproduceThreshold, reproduceCost, (int) (Math.random() * 3) + 1, viewReproduceRange);
        this.huntThreshold = huntThreshold;
        this.huntRange = huntRange;
    }

    public static Wolf createDefault() {
        return new Wolf(80, 0, 0, 100, 150, 4, 120, 30, 5, 5);
    }

    public int getHuntThreshold() {
        return huntThreshold;
    }

    public void setHuntThreshold(int huntThreshold) {
        this.huntThreshold = huntThreshold;
    }

    public int getHuntRange() {
        return huntRange;
    }

    public void setHuntRange(int huntRange) {
        this.huntRange = huntRange;
    }

    @Override
    public Entity reproduce(Entity other) {

        //Loup 1 et loup 2
        this.setEnergy(this.getEnergy() - getReproduceCost());
        other.setEnergy(other.getEnergy() - other.getReproduceCost());

        Wolf baby_wolf = new Wolf(huntThreshold,
                0,
                0,
                Math.max(1, (int) ((Math.random() * 0.4 + 0.4) * getEnergyMax())),
                getEnergyMax(),
                getViewRange(),
                getReproduceThreshold(),
                getReproduceCost(),
                getViewReproduceRange(),
                getHuntRange());
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
        Sheep targetSheep = (Sheep) findNearestEntity(grid, Sheep.class, getViewRange());

        if (targetSheep == null) {
            move(grid);
            return;
        }

        int distX = Math.abs(this.getX() - targetSheep.getX());
        int distY = Math.abs(this.getY() - targetSheep.getY());

        // Mouton adjacent → manger
        if (distX + distY == 1) {
            eat(targetSheep);
            return;
        }
        
        //Si mouton trop loin => Abandon de la chasse et move()
        if(distX + distY > huntRange){
            move(grid);
            return;
        }

        int dx = Integer.signum(targetSheep.getX() - this.getX());
        int dy = Integer.signum(targetSheep.getY() - this.getY());

        if (distX < distY) {
            if (!tryMove(getX() + dx, getY(), grid)) {
                if (!tryMove(getX(), getY() + dy, grid)) {
                    move(grid);
                }
            }
        } else {
            if (!tryMove(getX(), getY() + dy, grid)) {
                if (!tryMove(getX() + dx, getY(), grid)) {
                    move(grid);
                }
            }
        }
    }

    private boolean tryMove(int newX, int newY, Grid grid) {
        if (grid.isInside(newX, newY) && grid.getCell(newX, newY).isFree()) {
            moveTo(newX, newY, grid);
            return true;
        }
        return false;
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
