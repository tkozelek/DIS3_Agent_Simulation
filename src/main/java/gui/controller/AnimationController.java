package gui.controller;

import gui.model.SimulationManager;
import gui.view.AnimationWindow;
import simulation.MySimulation;

import java.awt.*;


public class AnimationController {
    private final AnimationWindow view;
    private final SimulationManager simManager;

    public AnimationController(AnimationWindow view, SimulationManager simManager) {
        this.view = view;
        this.simManager = simManager;

        view.getButtonExit().addActionListener(e -> this.exit());
        view.getButtonCreateAnim().addActionListener(_ -> this.createAnimationPanel());
        view.getButtonRemoveAnim().addActionListener(_ -> this.removeAnimationPanel());
    }

    private void removeAnimationPanel() {
        if (this.simManager.getSimulation() == null)
            return;

        MySimulation sim = this.simManager.getSimulation();

        if (!this.simManager.getSimulation().animatorExists())
            return;

        view.getPanelCanvas().remove(sim.animator().canvas());
        sim.removeAnimator();
        view.revalidate();
    }

    private void createAnimationPanel() {
        if (this.simManager.getSimulation() == null)
            return;

        MySimulation sim = this.simManager.getSimulation();

        if (this.simManager.getSimulation().animatorExists())
            return;

        sim.createAnimator();
        
        sim.initAnimator();

        view.getPanelCanvas().setLayout(new BorderLayout());
        view.getPanelCanvas().add(sim.animator().canvas(), BorderLayout.CENTER);

        view.getPanelCanvas().revalidate();
    }

    private void exit() {
        this.removeAnimationPanel();
        this.view.dispose();
    }


}
