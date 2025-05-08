package entity.worker;

import OSPABA.Simulation;
import OSPAnimator.AnimImageItem;
import OSPStat.Stat;
import OSPStat.WStat;
import entity.ILocation;
import entity.Ids;
import entity.Storage;
import entity.product.Product;
import entity.workstation.Workstation;
import simulation.Data;

public class Worker {
    private final int id;
    private final WorkerGroup group;
    private final Stat statWorkloadTotal;
    private WorkerWork currentWork;
    private ILocation location;
    private Product currentProduct;
    private int finishedTasks = 0;
    private WStat statWorkload;

    private AnimImageItem animImageItem;

    public Worker(WorkerGroup group, Simulation sim) {
        this.id = Ids.getWorkerId();
        this.group = group;
        this.currentWork = WorkerWork.IDLE;
        this.location = Storage.STORAGE;
        this.statWorkload = new WStat(sim);
        this.statWorkloadTotal = new Stat();
        this.animImageItem = getImage();
        this.animImageItem.setZIndex(10);
        this.animImageItem.setToolTip(this.toString());
    }

    private AnimImageItem getImage() {
        return switch (group) {
            case GROUP_A -> new AnimImageItem(Data.WORKER_A, Data.WORKER_WIDTH, Data.WORKER_HEIGHT);
            case GROUP_B -> new AnimImageItem(Data.WORKER_B, Data.WORKER_WIDTH, Data.WORKER_HEIGHT);
            case GROUP_C -> new AnimImageItem(Data.WORKER_C, Data.WORKER_WIDTH, Data.WORKER_HEIGHT);
        };
    }

    public AnimImageItem getAnimImageItem() {
        return animImageItem;
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

    public void setAnimItemPosition() {
        if (location instanceof Workstation workstation) {
            int x = (int) workstation.getAnimImageItem().getPosX();
            int y = (int) workstation.getAnimImageItem().getPosY();
            this.animImageItem.setPosition(x + (double) Data.WORKSTATION_WIDTH / 2, y + (double) Data.WORKSTATION_HEIGHT / 2 );
        }
        if (location instanceof Storage storage) {
           this.animImageItem.setPosition(Data.getRandomStoragePoiunt());
        }
    }
}
