package entity.worker;

import OSPABA.Simulation;
import OSPStat.Stat;
import OSPStat.WStat;
import entity.ILocation;
import entity.Ids;
import entity.Storage;
import entity.product.Product;

public class Worker {
    private final int id;
    private final WorkerGroup group;
    private final Stat statWorkloadTotal;
    private WorkerWork currentWork;
    private ILocation location;
    private Product currentProduct;
    private int finishedTasks = 0;
    private WStat statWorkload;

    public Worker(WorkerGroup group, Simulation sim) {
        this.id = Ids.getWorkerId();
        this.group = group;
        this.currentWork = WorkerWork.IDLE;
        this.location = Storage.STORAGE;
        this.statWorkload = new WStat(sim);
        this.statWorkloadTotal = new Stat();
    }

    public void reset(Simulation sim) {
        this.currentWork = WorkerWork.IDLE;
        this.finishedTasks = 0;
        this.location = Storage.STORAGE;
        if (statWorkload.sampleSize() > 0)
            statWorkloadTotal.addSample(statWorkload.mean());
        this.statWorkload = new WStat(sim);
    }

    public Stat getStatWorkloadTotal() {
        return statWorkloadTotal;
    }

    public WStat getStatWorkload() {
        return statWorkload;
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

    public void setCurrentWork(WorkerWork currentWork) {
        this.currentWork = currentWork;
        statWorkload.addSample(currentWork == WorkerWork.IDLE ? 0 : 1);
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
