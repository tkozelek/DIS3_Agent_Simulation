package gui.model;

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
        int[] queues,
        boolean updateChart) {

}
