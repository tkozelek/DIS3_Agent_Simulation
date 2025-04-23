package gui.model;

import gui.interfaces.Observer;
import simulation.MySimulation;

import javax.swing.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

public class SimulationManager {
    private final Observer observer;
    private final AtomicBoolean isRunning = new AtomicBoolean(false);
    private MySimulation simulation;
    private SwingWorker<Void, Void> worker;

    public SimulationManager(Observer observer) {
        this.observer = observer;
    }

    public void startSimulation(int replicationCount, int[] groups) {
        if (isRunning.get()) return;
        isRunning.set(true);

        this.simulation = new MySimulation((long)replicationCount, groups, 100);
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
                    // Calling get() will re‑throw any exception from doInBackground()
                    get();
                    System.out.println("Simulation finished!");
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    JOptionPane.showMessageDialog(
                            null,
                            "Simulation was interrupted.",
                            "Interrupted",
                            JOptionPane.WARNING_MESSAGE
                    );
                } catch (ExecutionException ee) {
                    Throwable cause = ee.getCause();
                    JOptionPane.showMessageDialog(
                            null,
                            "An error occurred: " + cause.getMessage(),
                            "Simulation Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                    cause.printStackTrace();
                }
            }
        };
        worker.execute();
    }

    public void stopSimulation() {
        if (simulation != null) {
            simulation.stopSimulation();
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
}
