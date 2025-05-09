package gui.model;

import OSPStat.Stat;
import config.Group;
import entity.worker.Worker;
import entity.workstation.Workstation;

import java.util.ArrayList;

public record SimulationData(
        double time,
        Worker[][] workers,
        ArrayList<simulation.MyMessage> orders,
        ArrayList<Workstation> workstations,
        int currentReplication,
        Group[] groups,
        Stat[] statProduct,
        Stat[] statOrder,
        Stat[] statQueueTime,
        Stat statWorkstationWorkloadTotal,
        Stat statOrderNotWorkerOnTotal,
        boolean updateChart,
        String workerGroups) {
}
