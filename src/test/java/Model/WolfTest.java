package Model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class WolfTest {

    //pattern AAA
    // Arrange : On crée un état connu (Wolf)
    // Act : Exécution d'une action
    // Assert : Vérifie le résultat
    @Test
    void wolfMortSiEnergieZero() {
        // Arrange
        Wolf wolf = new Wolf(30, 2, 2, 100, 150, 3, 100, 50, 5);
        // Act
        wolf.setEnergy(0);
        // Assert
        assertFalse(wolf.isAlive());
    }

    @Test
    void wolfVivantSiEnergieSup0() {
        // Arrange
        Wolf wolf = new Wolf(30, 2, 2, 100, 150, 3, 100, 50, 5);
        // Assert
        assertTrue(wolf.isAlive());
    }

    @Test
    void energieNePasDepasserEnergyMax() {
        // Arrange
        Wolf wolf = new Wolf(30, 2, 2, 100, 150, 3, 100, 50, 5);
        // Act
        wolf.setEnergy(9999);
        // Assert
        assertEquals(150, wolf.getEnergy());
    }

    @Test
    void constructeurInvalideEnergyMax() {
        // Assert — doit lever une exception
        assertThrows(IllegalArgumentException.class, ()
                -> new Wolf(30, 2, 2, 100, -1, 3, 100, 50, 5)
        );
    }

    @Test
    void sheepIsAliveAfterEat() {

        // Arrange
        Grid grid = new Grid(10, 10);
        Wolf wolf = new Wolf(50, 5, 5, 20, 150, 3, 100, 50, 5);
        Sheep sheep = new Sheep(20, 5, 6, 80, 100, 2, 70, 30, 3);
        grid.getCell(5, 5).setOccupant(wolf);
        grid.getCell(5, 6).setOccupant(sheep);

        // Act
        wolf.agir(grid);

        // Assert
        assertFalse(sheep.isAlive());
    }
}
