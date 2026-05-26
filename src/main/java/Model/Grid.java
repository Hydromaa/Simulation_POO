package Model;

public class Grid {

    private int length;
    private int width;
    private Cell[][] cells;

    public Grid(int length, int width) {
        this.length = length;
        this.width = width;
        this.cells = new Cell[length][width];
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                this.cells[i][j] = new Cell((int) (Math.random() * 100), null);
            }
        }
    }

    public int getLength() {
        return length;
    }

    public int getWidth() {
        return width;
    }

    /**
     * Renvoi les coordonnées d'une celulle de la grille
     * 
     * @param x position X
     * @param y position Y
     * @return renvoi l'index du tableau (De la grille)
     */
    public Cell getCell(int x, int y) {
        return cells[x][y];
    }

    /**
     * Vérifie si une position se trouve dans la grille
     * 
     * @param x position X
     * @param y position Y
     * @return retourne true si dans la grille, retourne false si c'est en dehors de la grille
     */
    public boolean isInside(int x, int y) {
        //Vérif si on est dans la grille (Gauche/droite/haut/bas)
        return x >= 0 && x < length && y >= 0 && y < width;
    }

}
