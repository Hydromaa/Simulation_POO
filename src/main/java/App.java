
import Model.Entity;
import Model.Grid;
import Model.Sheep;
import Model.SimulationManager;
import Model.Wolf;

class App {

    public static void main(String[] args) {

        SimulationManager manager = new SimulationManager(10, 10);

        manager.addEntity(new Wolf(30, 2, 2, 100, 150, 3, 100, 50));
        manager.addEntity(new Sheep(20, 5, 5, 80, 100, 2, 70, 30));
        manager.addEntity(new Sheep(20, 7, 3, 80, 100, 2, 70, 30));

        for (int i = 0; i < 10; i++) {
            System.out.println("--- Tour " + i + " ---");
            System.out.println("Entités : " + manager.getEntities().size());
            for (Entity e : manager.getEntities()) {
                System.out.println(e);
            }
            manager.nextTurn();
        }
    }

}
