package Model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CellTest {
    //pattern AAA
    // Arrange : On crée un état connu (Wolf)
    // Act : Exécution d'une action
    // Assert : Vérifie le résultat

    @Test
    void CellIsFree() {
        //Arrange
        Cell cell = new Cell(50, null);

        assertTrue(cell.isFree());
    }

    @Test
    void CellIsNotFree() {
        Wolf wolf = new Wolf(30, 2, 2, 100, 150, 3, 10, 50, 5);
        Cell cell = new Cell(50, wolf);

        assertFalse(cell.isFree());
    }

    @Test
    void GrassLevelAfterEat() {

        //Arrange
        Cell cell = new Cell(100, null);
        //Act
        cell.eatGrass(10);
        //Assert
        assertEquals(90, cell.getGrassLevel());
    }

    @Test
    void GrassLevelMin() {
        //Arrange
        Cell cell = new Cell(100, null);
        //Act
        cell.eatGrass(120);
        //Assert
        assertEquals(0, cell.getGrassLevel());
    }

    @Test
    void GrassGrowMax() {
        //Arrange
        Cell cell = new Cell(100, null);
        //Act
        cell.growGrass();
        //Assert
        assertTrue(cell.getGrassLevel() == 100);
    }

}
