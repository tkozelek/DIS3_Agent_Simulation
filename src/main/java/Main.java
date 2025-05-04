import config.Constants;
import entity.product.ProductActivity;
import gui.controller.MainController;
import gui.view.MainWindow;
import simulation.MySimulation;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        MainWindow win = new MainWindow();
        MainController controller = new MainController(win);

//        MySimulation sim = new MySimulation(null, new int[]{10, 5, 40}, 80);
//        sim.setSpeed(Constants.MAX_SPEED);
//        sim.simulate(100, Constants.SIMULATION_TIME);
    }
}