package Model;

public class Cell {

    private int grassLevel;
    private Entity occupant;

    private static final int GRASS_MAX = 100;

    public Cell(int grassLevel, Entity occupant) {
        this.grassLevel = grassLevel;
        this.occupant = occupant;
    }

    public int getGrassLevel() {
        return grassLevel;
    }

    public Entity getOccupant() {
        return occupant;
    }

    public void setOccupant(Entity occupant) {
        this.occupant = occupant;
    }

    public boolean isFree() {
        return occupant == null;
    }

    public void eatGrass(int amount) {
        this.grassLevel = Math.max(grassLevel - amount, 0);
    }

    public void growGrass() {
        this.grassLevel = Math.min(grassLevel + 10, GRASS_MAX);
    }

}
