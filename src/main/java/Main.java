import gui.controller.MainController;
import gui.view.MainWindow;

public class Main {
    public static void main(String[] args) {
        MainWindow win = new MainWindow();
        MainController controller = new MainController(win);

//
//
//
//        double start = System.currentTimeMillis();
//        sim.simulate(5, Constants.SIMULATION_TIME);
//        simulationData.add(sim.getSimulationData());
//        double end = System.currentTimeMillis();
//        System.out.println("Simulation took " + (end - start) + "ms");
//
//        FileExporter.exportToTXT(simulationData);
//        FileExporter.exportToCSV("stats/blabla.csv", simulationData);
    }
}