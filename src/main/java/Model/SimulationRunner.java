package Model;

public class SimulationRunner {

    private final SimulationManager manager;
    private boolean running = false;
    private Thread simulationThread;
    //Vitesse des tours. 1000 = 1 seconde
    private int delay = 1000;

    public SimulationRunner(SimulationManager manager) {
        this.manager = manager;
    }

    public void start() {
        if (running) {
            return;
        }
        running = true;
        simulationThread = new Thread(() -> {
            while (running) {
                manager.nextTurn();
                try {
                    Thread.sleep(delay);
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

    public int getDelay() {
        return delay;
    }

    public void setDelay(int Delay) {
        this.delay = Delay;
    }

}
