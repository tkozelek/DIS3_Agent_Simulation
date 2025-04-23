import gui.controller.MainController;
import gui.view.MainWindow;
import simulation.MySimulation;

public class Main {
    public static void main(String[] args) {
        MainWindow win = new MainWindow();
        MainController controller = new MainController(win);
    }
}