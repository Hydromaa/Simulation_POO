
import Model.Sheep;
import Model.SimulationManager;
import Model.SimulationRunner;
import Model.Wolf;
import View.ViewSimulation;
import javafx.application.Application;

class App {

    public static void main(String[] args) {

        SimulationManager manager = new SimulationManager(20, 20);
        SimulationRunner simulationRunner = new SimulationRunner(manager);

        ViewSimulation.manager = manager;
        ViewSimulation.runner = simulationRunner;

        manager.addEntity(new Wolf(60, 5, 9, 80, 120, 3, 60, 40, 5));
        manager.addEntity(new Wolf(60, 5, 8, 80, 120, 3, 60, 40, 5));

        manager.addEntity(new Sheep(15, 5, 5, 60, 100, 2, 60, 15, 3));
        manager.addEntity(new Sheep(15, 6, 5, 60, 100, 2, 60, 15, 3));
        manager.addEntity(new Sheep(15, 7, 5, 60, 100, 2, 60, 15, 3));
        manager.addEntity(new Sheep(15, 8, 5, 60, 100, 2, 60, 15, 3));
        manager.addEntity(new Sheep(15, 9, 5, 60, 100, 2, 60, 15, 3));
        manager.addEntity(new Sheep(15, 10, 5, 60, 100, 2, 60, 15, 3));
        manager.addEntity(new Sheep(15, 11, 5, 60, 100, 2, 60, 15, 3));
        manager.addEntity(new Sheep(15, 12, 5, 60, 100, 2, 60, 15, 3));

        Application.launch(ViewSimulation.class, args);

    }

}
