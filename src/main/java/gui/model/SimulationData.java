package gui.model;

import OSPStat.Stat;
import config.Group;
import entity.order.Order;
import entity.worker.Worker;
import entity.workstation.Workstation;

import java.util.ArrayList;

public record SimulationData(
        double time,
        Worker[][] workers,
        ArrayList<Order> orders,
        ArrayList<Workstation> workstations,
        int currentReplication,
        Group[] groups,
        Stat[] statProduct,
        Stat[] statOrder,
        Stat statWorkstationWorkloadTotal,
        Stat statOrderNotWorkerOnTotal,
        boolean updateChart,
        String workerGroups) {
}
