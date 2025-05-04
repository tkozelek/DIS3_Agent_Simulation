import config.Constants;
import simulation.MySimulation;

public class Main {
    public static void main(String[] args) {
//        MainWindow win = new MainWindow();
//        MainController controller = new MainController(win);

        MySimulation sim = new MySimulation(null, new int[]{2, 2, 10}, 10);
        sim.setSpeed(Constants.MAX_SPEED);
        double start = System.currentTimeMillis();
        sim.simulate(100, Constants.SIMULATION_TIME);
        double end = System.currentTimeMillis();
        System.out.println("Simulation took " + (end - start) + "ms");
    }
}