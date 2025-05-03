package entity.worker;

import entity.ILocation;
import entity.Ids;
import entity.Storage;
import entity.product.Product;
import entity.workstation.Workstation;

public class Worker {
    private final int id;
    private final WorkerGroup group;

    private WorkerWork currentWork;
    private ILocation location;
    private Product currentProduct;
    private int finishedTasks = 0;

    public Worker(WorkerGroup group) {
        this.id = Ids.getWorkerId();
        this.group = group;
        this.currentWork = WorkerWork.IDLE;
        this.location = Storage.STORAGE;
    }

    public Product getCurrentProduct() {
        return currentProduct;
    }

    public void setCurrentProduct(Product currentProduct) {
        this.currentProduct = currentProduct;
        if (currentProduct != null) finishedTasks++;
    }

    public ILocation getLocation() {
        return location;
    }

    public void setLocation(ILocation location) {
        this.location = location;
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

    public int getId() {
        return id;
    }

    public String toStringGroupId() {
        String gr = group.toString();
        return String.format("%s%d", gr.charAt(gr.length() - 1), this.id);
    }

    @Override
    public String toString() {
        String gr = group.toString();
        return String.format("W(%s) #%d", gr.charAt(gr.length() - 1), this.id);
    }
}
