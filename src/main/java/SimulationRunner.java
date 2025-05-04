import config.Constants;
import config.FileExporter;
import gui.model.SimulationData;
import simulation.MySimulation;

import java.util.*;
import java.util.concurrent.*;

public class SimulationRunner {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        int a_min = 4, a_max = 5;
        int b_min = 4, b_max = 5;
        int c_min = 40, c_max = 40;
        int w_min = 60, w_max = 60;

        List<Future<SimulationData>> futures = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        for (int a = a_min; a <= a_max; a++) {
            for (int b = b_min; b <= b_max; b++) {
                for (int c = c_min; c <= c_max; c++) {
                    for (int w = w_min; w <= w_max; w++) {
                        final int aFinal = a, bFinal = b, cFinal = c, wFinal = w;
                        futures.add(executor.submit(() -> {
                            MySimulation sim = new MySimulation(null, new int[]{aFinal, bFinal, cFinal}, wFinal);
                            sim.setSpeed(Constants.MAX_SPEED);
                            sim.simulate(500, Constants.SIMULATION_TIME);
                            return sim.getSimulationData();
                        }));
                    }
                }
            }
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.HOURS);

        ArrayList<SimulationData> allData = new ArrayList<>();
        for (Future<SimulationData> f : futures) {
            allData.add(f.get());
        }

        FileExporter.exportToTXT(allData);
        FileExporter.exportToCSV("stats/blabla_" + System.currentTimeMillis() + ".csv", allData);
    }
}
