package Model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GridTest {
    //pattern AAA
    // Arrange : On crée un état connu (Wolf)
    // Act : Exécution d'une action
    // Assert : Vérifie le résultat

    @Test
    void insideGrid() {
        Grid grid = new Grid(10, 10);

        assertFalse(grid.isInside(11, 25));
    }

    @Test
    void xIsInside() {
        Grid grid = new Grid(10, 10);

        assertFalse(grid.isInside(-5, 9));
    }

    @Test
    void yIsInside() {
        Grid grid = new Grid(10, 10);

        assertFalse(grid.isInside(9, -9));
    }

    @Test
    void validPosition() {
        Grid grid = new Grid(10, 10);
        assertTrue(grid.isInside(5, 5));
    }

    @Test
    void outOfGridRight() {
        Grid grid = new Grid(10, 10);

        assertFalse(grid.isInside(grid.getLength() + 1, grid.getWidth() + 1));
    }

    @Test
    void outOfGridLeft() {
        Grid grid = new Grid(10, 10);

        assertFalse(grid.isInside(grid.getLength() - grid.getLength() - 1, grid.getWidth() - grid.getWidth() - 1));
    }

    @Test
    void cellNull() {
        Grid grid = new Grid(10, 10);

        assertNotNull(grid.getCell(1, 1));
    }

    @Test
    void cornerTopLeft() {
        Grid grid = new Grid(10, 10);
        assertTrue(grid.isInside(0, 0)); // coin valide
    }

    @Test
    void cornerBottomRight() {
        Grid grid = new Grid(10, 10);
        assertTrue(grid.isInside(9, 9)); // dernier index valide
    }
}
