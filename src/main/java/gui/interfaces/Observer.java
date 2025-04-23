package gui.interfaces;

import gui.model.SimulationData;

public interface Observer {
    void update(SimulationData data);
}
