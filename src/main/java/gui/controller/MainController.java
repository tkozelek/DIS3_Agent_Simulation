package gui.controller;

import config.Constants;
import gui.interfaces.Observer;
import gui.model.SimulationData;
import gui.model.SimulationManager;
import gui.view.MainWindow;

import javax.swing.*;
import java.awt.*;

public class MainController implements Observer {
    private final MainWindow view;
    private final SimulationManager simulationManager;
    private int replicationCount;
    private int[] groups;
    private boolean paused = false;

    public MainController(MainWindow view) {
        this.view = view;
        this.simulationManager = new SimulationManager(MainController.this);
        this.view.setVisible(true);

        this.view.getStartButton().addActionListener(_ -> startSimulation());
        this.view.getPauseButton().addActionListener(_ -> pauseSimulation());
        this.view.getStopButton().addActionListener(_ -> stopSimulation());

        this.view.getSliderSpeed().addChangeListener(_ -> this.changeSpeed());
    }

    private void changeSpeed() {
        int speed = view.getSliderSpeed().getValue();
        if (speed == 0) {
            speed = 1;
        }

        this.view.getLabelSpeed().setText(String.valueOf(speed));
        this.simulationManager.setSpeed(speed);
    }

    private void stopSimulation() {
        this.simulationManager.stopSimulation();
        this.view.getStopButton().setBackground(Color.RED);
    }

    private void pauseSimulation() {
        this.simulationManager.pauseSimulation();
        this.paused = !this.paused;
        if (paused)
            this.view.getPauseButton().setBackground(Color.RED);
        else
            this.view.getPauseButton().setBackground(null);
    }

    public void startSimulation() {
        if (!validateInput())
            return;
        this.view.getChart().resetChart();
        simulationManager.startSimulation(replicationCount, groups);
        this.changeSpeed();
        paused = false;
        this.view.getPauseButton().setBackground(null);
        this.view.getStopButton().setBackground(null);
    }

    private boolean validateInput() {
        if (!view.getFieldReplicationCount().getText().isBlank()
                && !view.getFieldWorkerA().getText().isBlank()
                && !view.getFieldWorkerB().getText().isBlank()
                && !view.getFieldWorkerC().getText().isBlank()) {
            replicationCount = Integer.parseInt(view.getFieldReplicationCount().getText());
            groups = new int[]{
                    Integer.parseInt(view.getFieldWorkerA().getText()),
                    Integer.parseInt(view.getFieldWorkerB().getText()),
                    Integer.parseInt(view.getFieldWorkerC().getText())
            };
            return true;
        } else {
            showError("Fields cannot be empty");
            return false;
        }
    }

    @Override
    public void update(SimulationData data) {
        view.updateData(data);
        view.updateChart(data, replicationCount);
    }


    public void showError(String message) {
        JOptionPane.showMessageDialog(view, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
