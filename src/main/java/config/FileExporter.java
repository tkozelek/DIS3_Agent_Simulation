package config;

import entity.product.ProductType;
import entity.worker.Worker;
import gui.model.SimulationData;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import static config.StatFormatter.*;

public class FileExporter {
    public static void exportToCSV(String filename, ArrayList<SimulationData> results) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("GROUPS;STATIONS;WORKLOAD A;WORKLOAD B;WORKLOAD C;TIME;IS_BOTTOM;IS_TOP");
            for (SimulationData result : results) {
                double[] is = result.statOrder()[1].confidenceInterval_95();
                writer.printf("%s;%d;%.1f%%;%.1f%%;%.1f%%;%.2fh;%.2fh;%.2fh\n",
                        result.workerGroups(),
                        result.workstations().size(),
                        result.groups()[0].getWorkloadGroupTotal().mean() * 100,
                        result.groups()[1].getWorkloadGroupTotal().mean() * 100,
                        result.groups()[2].getWorkloadGroupTotal().mean() * 100,
                        result.statOrder()[1].mean() / 3600,
                        is[0] / 3600.0,
                        is[1] / 3600.0);
            }
        } catch (IOException e) {
            System.err.println("Error writing CSV file: " + e.getMessage());
        }
    }

    public static void exportToTXT(ArrayList<SimulationData> results) {
        for (SimulationData result : results) {
            String filename = "stats/" + result.workerGroups().replace(" ", "_") + "_" + result.workstations().size() + ".txt";
            try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
                writer.println("Replications: " + result.currentReplication());
                writer.println("Workstations: " + result.workstations().size());
                writer.println("Groups: " + result.workerGroups());
                writer.println(statToStringTime(result.statOrder()[1], "Order time total"));
                writer.println(statToStringTime(result.statProduct()[1], "Product time total"));
                writer.println(statToStringPercentual(result.statWorkstationWorkloadTotal(), "Workstation workload total"));
                Group[] groups = result.groups();
                for (Group group : groups) {
                    writer.println(statToStringPercentual(group.getWorkloadGroupTotal(), group + " workload group total"));
                }
                writer.println(statToStringTime(result.statQueueTime()[1], "Product average queue time"));
                for (Group group : groups) {
                    writer.println(statToString(group.getStatQueueLengthTotal(), group + " queue length total"));
                }
                for (Group group : groups) {
                    for (Worker w : group.getWorkers()) {
                        writer.println(statToStringPercentual(w.getStatWorkloadTotal(), w.toStringGroupId() + " workload total"));
                    }
                }
            } catch (IOException e) {
                System.err.println("Error writing TXT file: " + e.getMessage());
            }
        }

    }
}
