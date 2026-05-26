package Model;

public interface SimulationNotifier {

    void addSimulationListener(SimulationListener l);

    void removeSimulationListener(SimulationListener l);

    void notifyTurnEnded();

    void notifySimulationEnded();
}
