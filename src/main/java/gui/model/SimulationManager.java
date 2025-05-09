package gui.model;

import config.Constants;
import gui.interfaces.Observer;
import simulation.MySimulation;

import javax.swing.*;

public class SimulationManager {
    private final Observer observer;
    private boolean isRunning = false;
    private MySimulation simulation;
    private SwingWorker<Void, Void> worker;

    public SimulationManager(Observer observer) {
        this.observer = observer;
    }

    public void startSimulation(int replicationCount, int[] groups, int workstationCount) {
        if (isRunning) return;
        isRunning = true;

        this.simulation = new MySimulation(null, groups, workstationCount);
        this.simulation.addObserver(observer);

        worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                double start = System.currentTimeMillis();
                simulation.simulate(replicationCount, Constants.SIMULATION_TIME);
                double end = System.currentTimeMillis();
                System.out.println("Simulation took " + (end - start) + "ms");
                return null;
            }

            @Override
            protected void done() {
                isRunning = false;
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
            isRunning = false;
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

    public MySimulation getSimulation() {
        return this.simulation;
    }
}
