import entity.product.ProductActivity;
import gui.controller.MainController;
import gui.view.MainWindow;
import simulation.MySimulation;

public class Main {
    public static void main(String[] args) {
        MainWindow win = new MainWindow();
        MainController controller = new MainController(win);

//        MySimulation sim = new MySimulation(null, new int[]{5, 5, 20}, 50);
//        sim.simulate(100);
    }
}