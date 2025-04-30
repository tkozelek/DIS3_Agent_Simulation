package gui.model;

import gui.interfaces.Observer;
import simulation.MySimulation;

import javax.swing.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class SimulationManager {
    private final Observer observer;
    private final AtomicBoolean isRunning = new AtomicBoolean(false);
    private MySimulation simulation;
    private SwingWorker<Void, Void> worker;

    public SimulationManager(Observer observer) {
        this.observer = observer;
    }

    public void startSimulation(int replicationCount, int[] groups, int workstationCount) {
        if (isRunning.get()) return;
        isRunning.set(true);

        this.simulation = new MySimulation(null, groups, workstationCount);
        this.simulation.addObserver(observer);

        worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                simulation.simulate(replicationCount);
                return null;
            }

            @Override
            protected void done() {
                isRunning.set(false);
                try {
                    get();
                    System.out.println("Simulation finished!");
                } catch (Exception e) {
                    if (e.getMessage() != null) {
                        JOptionPane.showMessageDialog(
                                null,
                                "Error " + e.getMessage(),
                                "Exception",
                                JOptionPane.WARNING_MESSAGE
                        );
                        e.printStackTrace();
                    }
                }
            }
        };
        worker.execute();
    }

    public void stopSimulation() {
        if (simulation != null) {
            simulation.stopSimulation();
            isRunning.set(false);
        }
        if (worker != null) {
            worker.cancel(true);
        }
    }

    public void pauseSimulation() {
        if (simulation != null) {
            simulation.togglePauseSimulation();
        }
    }

    public void setSpeed(int speed) {
        if (simulation != null) {
            simulation.setSpeed(speed);
        }
    }

    public AtomicBoolean getIsRunning() {
        return isRunning;
    }
}
