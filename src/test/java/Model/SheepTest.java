package Model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SheepTest {

    //pattern AAA
    // Arrange : On crée un état connu (Wolf)
    // Act : Exécution d'une action
    // Assert : Vérifie le résultat
    @Test
    void SheepDeadIfEnergyyZero() {
        //Arrange
        Sheep sheep = new Sheep(5, 3, 3, 60, 100, 2, 50, 50, 3);
        //Act
        sheep.setEnergy(0);
        //Assert
        assertFalse(sheep.isAlive());
    }

    @Test
    void SheepAliveEnergySupp0() {
        //Arrange
        Sheep sheep = new Sheep(5, 3, 3, 60, 100, 2, 50, 50, 3);
        //Assert
        assertTrue(sheep.isAlive());
    }

    @Test
    void energyMaxLimit() {
        // Arrange
        Sheep sheep = new Sheep(30, 2, 2, 100, 150, 3, 100, 50, 3);
        // Act
        sheep.setEnergy(9999);
        // Assert
        assertEquals(150, sheep.getEnergy());
    }

    @Test
    void constructeurInvalideEnergyMax() {
        // Assert — doit lever une exception
        assertThrows(IllegalArgumentException.class, ()
                -> new Sheep(30, 2, 2, 100, -1, 3, 100, 50, 3)
        );
    }

    @Test
    void SheepFlee() {
        //Arrange
        Sheep sheep = new Sheep(5, 3, 5, 60, 100, 2, 50, 50, 3);
        Wolf wolf = new Wolf(1, 3, 3, 10, 100, 2, 50, 50, 5);
        Grid grid = new Grid(10, 10);
        //Act
        grid.getCell(3, 3).setOccupant(wolf);
        grid.getCell(3, 5).setOccupant(sheep);

        sheep.agir(grid);
        //Assert
        assertTrue(sheep.getY() > 5);
    }

    @Test
    void SheepFleeBorder() {
        //Arrange
        Sheep sheep = new Sheep(5, 0, 0, 60, 100, 2, 50, 50, 3);
        Wolf wolf = new Wolf(1, 0, 1, 7, 100, 2, 50, 50, 5);
        Grid grid = new Grid(10, 10);
        //Act
        grid.getCell(0, 1).setOccupant(wolf);
        grid.getCell(0, 0).setOccupant(sheep);
        sheep.agir(grid);
        //Assert
        assertTrue(grid.isInside(sheep.getX(), sheep.getY()));
    }
}
