package Model;

import java.util.List;

public interface SimulationObservable extends SimulationNotifier {

    Grid getGrid();

    List<Entity> getEntities();

    int getTurn();

}
