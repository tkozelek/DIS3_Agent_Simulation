package gui.view;

import javax.swing.*;
import java.awt.*;

public class AnimationWindow extends JFrame {
    private JButton buttonExit;
    private JButton buttonCreateAnim;
    private JButton buttonRemoveAnim;
    private JPanel panelCanvas;
    private JPanel panel1;

    public AnimationWindow() {
        setTitle("Diskretna simulacia");
        setMinimumSize(new Dimension(1800, 950));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        add(panel1);
        pack();
    }

    public JButton getButtonExit() {
        return buttonExit;
    }

    public JButton getButtonCreateAnim() {
        return buttonCreateAnim;
    }

    public JButton getButtonRemoveAnim() {
        return buttonRemoveAnim;
    }

    public JPanel getPanelCanvas() {
        return panelCanvas;
    }
}
