import config.Constants;
import config.FileExporter;
import gui.model.SimulationData;
import simulation.MySimulation;

import java.sql.Time;
import java.util.*;
import java.util.concurrent.*;

public class SimulationRunner {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        // rozsah
        int a_min = 4, a_max = 6;
        int b_min = 4, b_max = 6;
        int c_min = 38, c_max = 40;
        int w_min = 55, w_max = 60;

        int repeat = 1;
        // zoznam vysledkov buducich
        List<Future<SimulationData>> futures = new ArrayList<>();
        // vytvorenie vlakien
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        for (int a = a_min; a <= a_max; a++) {
            for (int b = b_min; b <= b_max; b++) {
                for (int c = c_min; c <= c_max; c++) {
                    for (int w = w_min; w <= w_max; w++) {
                        final int aFinal = a, bFinal = b, cFinal = c, wFinal = w;
                        // launch simulacii vo vlakne a vloženie vysledku do listu

                        for (int i = 0; i < repeat; i++) {
                            futures.add(executor.submit(() -> {
                                MySimulation sim = new MySimulation(null, new int[]{aFinal, bFinal, cFinal}, wFinal);
                                sim.setSpeed(Constants.MAX_SPEED);
                                sim.simulate(150, Constants.SIMULATION_TIME);
                                return sim.getSimulationData();
                            }));
                        }
                    }
                }
            }
        }

        // neprijma ulohy
        executor.shutdown();
        // wait na ulohy
        executor.awaitTermination(1, TimeUnit.HOURS);

        ArrayList<SimulationData> allData = new ArrayList<>();
        for (Future<SimulationData> f : futures) {
            allData.add(f.get());
        }

        FileExporter.exportToTXT(allData);
        FileExporter.exportToCSV("stats/blabla_" + new Date().getTime() + ".csv", allData);
    }
}
