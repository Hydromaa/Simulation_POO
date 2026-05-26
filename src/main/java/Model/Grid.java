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

    public Cell getCells(int x, int y) {
        return cells[x][y];
    }

    public boolean isInside(int x, int y) {
        //Vérif si on est dans la grille (Gauche/droite/haut/bas)
        return x >= 0 && x < length && y >= 0 && y < width;
    }

}
