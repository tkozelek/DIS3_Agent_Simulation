package org.kozelek.entity.worker;

import org.kozelek.entity.Ids;
import org.kozelek.entity.order.Order;
import org.kozelek.entity.product.Product;
import org.kozelek.entity.workstation.Workstation;

public class Worker {
    private final int id;
    private final WorkerGroup group;

    private WorkerWork currentWork;
    private WorkerPosition currentPosition;
    private Product currentProduct;
    private Workstation currentWorkstation;
    private int finishedTasks = 0;

    public Worker(WorkerGroup group) {
        this.id = Ids.getWorkerId();
        this.group = group;
        this.currentPosition = WorkerPosition.STORAGE;
        this.currentWork = WorkerWork.IDLE;
        this.currentProduct = null;
        this.currentWorkstation = null;
    }

    public Product getCurrentProduct() {
        return currentProduct;
    }

    public void setCurrentProduct(Product currentProduct) {
        this.currentProduct = currentProduct;
        if (currentProduct != null) finishedTasks++;
    }

    public Workstation getCurrentWorkstation() {
        return currentWorkstation;
    }

    public void setCurrentWorkstation(Workstation currentWorkstation) {
        this.currentWorkstation = currentWorkstation;
    }

    public int getFinishedTasks() {
        return finishedTasks;
    }

    public WorkerGroup getGroup() {
        return group;
    }

    public WorkerWork getCurrentWork() {
        return currentWork;
    }

    public void setCurrentWork(WorkerWork currentWork, double time) {
        this.currentWork = currentWork;
    }

    public WorkerPosition getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(WorkerPosition currentPosition) {
        this.currentPosition = currentPosition;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        String gr = group.toString();
        return String.format("W(%s) #%d", gr.charAt(gr.length() - 1), this.id);
    }
}
