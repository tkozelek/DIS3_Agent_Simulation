package gui.model;

import entity.workstation.Workstation;
import entity.order.Order;
import entity.worker.Worker;

import java.util.List;

public record SimulationData(
        double time,
        Worker[][] workers,
        List<Workstation> workstations,
        List<Order> orders,
        int currentReplication,
        int[] queues,
        boolean updateChart) {

}
