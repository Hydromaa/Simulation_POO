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

    /**
     * Modifie la valeur de grassLevel quand une entité mange de l'herbe. La
     * valeur sera toujours : Valeur du niveau d'herbe actuel moins la quantitée
     * mangée OU 0.
     *
     * @param amount quantité mangée par une entité
     */
    public void eatGrass(int amount) {
        this.grassLevel = Math.max(grassLevel - amount, 0);
    }

    /**
     * Modifie la valeur de grassLevel quand elle pousse. La valeur sera
     * toujours : Le niveau actuel + 10 OU son niveau maximum définit dans les
     * attributs (GRASS_MAX)
     */
    public void growGrass() {
        this.grassLevel = Math.min(grassLevel + (int) (Math.random() * 2) + 1, GRASS_MAX);
    }

}
