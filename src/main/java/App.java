
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

        manager.addEntity(new Wolf(80, 0, 2, 100, 150, 4, 120, 30));
        manager.addEntity(new Wolf(80, 1, 2, 100, 150, 4, 120, 30));
        manager.addEntity(new Wolf(80, 2, 2, 100, 150, 4, 120, 30));
        manager.addEntity(new Sheep(20, 5, 5, 80, 100, 2, 70, 30));
        manager.addEntity(new Sheep(20, 4, 5, 80, 100, 2, 70, 30));
        manager.addEntity(new Sheep(20, 3, 5, 80, 100, 2, 70, 30));
        manager.addEntity(new Sheep(20, 2, 5, 80, 100, 2, 70, 30));
        manager.addEntity(new Sheep(20, 1, 5, 80, 100, 2, 70, 30));

        Application.launch(ViewSimulation.class, args);

    }

}
