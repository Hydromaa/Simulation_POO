package Model;

public class SimulationRunner {

    private final SimulationManager manager;
    private boolean running = false;
    private Thread simulationThread;

    public SimulationRunner(SimulationManager manager) {
        this.manager = manager;
    }

    public void start() {
        running = true;
        simulationThread = new Thread(() -> {
            while (running) {
                manager.nextTurn();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        simulationThread.start();
    }

    public void stop() {
        running = false;
    }

    public boolean isRunning() {
        return running;
    }
}
