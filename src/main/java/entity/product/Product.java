package entity.product;


import OSPAnimator.AnimImageItem;
import OSPAnimator.AnimItem;
import config.Constants;
import entity.Ids;
import entity.order.Order;
import entity.worker.Worker;
import entity.worker.WorkerWork;
import entity.workstation.Workstation;
import simulation.Data;

public class Product implements Comparable<Product> {
    private final int id;
    private final ProductType productType;
    private final AnimImageItem animItem;
    private ProductActivity productActivity;
    private Workstation workstation;
    private Worker worker;
    private double arrivalTime;
    private double finishTime;
    private double startCuttingTime;
    private double finishCuttingTime;
    private double startVerifyParts;
    private double finishVerifyParts;
    private double startStainingTime;
    private double finishStainingTime;
    private double startPaintingTime;
    private double finishPaintingTime;
    private double startAssemblyTime;
    private double finishAssemblyTime;
    private double startFittingAssemblyTime;
    private double finishFittingAssemblyTime;
    private double queueEntryTime;
    private double totalQueueTime;
    private Order order;
    private boolean shouldBePainted;

    public Product(ProductType productType) {
        this.id = Ids.getProductId();
        this.productType = productType;
        this.productActivity = ProductActivity.EMTPY;
        this.queueEntryTime = -1;
        this.totalQueueTime = 0;

        this.animItem = this.getAnimImage();
        this.animItem.setToolTip(this.toString());
        this.animItem.setPositionAlignment(AnimItem.PositionAlignment.CENTER);
    }

    private AnimImageItem getAnimImage() {
        return switch (productType) {
            case TABLE -> new AnimImageItem(Data.TABLE, 60, 40);
            case CHAIR -> new AnimImageItem(Data.CHAIR, 50, 50);
            case CUPBOARD -> new AnimImageItem(Data.CUPBOARD, 40, 60);
        };
    }

    public AnimImageItem getAnimImageItem() {
        return animItem;
    }

    public void enterQueue(double time) {
        this.queueEntryTime = time;
    }

    public void exitQueue(double time) {
        if (queueEntryTime != -1) {
            double timeInQueue = time - queueEntryTime;
            totalQueueTime += timeInQueue;
            queueEntryTime = -1;
        }
    }

    public double getStartVerifyParts() {
        return startVerifyParts;
    }

    public void setStartVerifyParts(double startVerifyParts) {
        this.startVerifyParts = startVerifyParts;
    }

    public double getFinishVerifyParts() {
        return finishVerifyParts;
    }

    public void setFinishVerifyParts(double finishVerifyParts) {
        this.finishVerifyParts = finishVerifyParts;
    }

    public double getTotalQueueTime() {
        return totalQueueTime;
    }

    public boolean getShouldBePainted() {
        return shouldBePainted;
    }

    public void setShouldBePainted(boolean shouldBePainted) {
        this.shouldBePainted = shouldBePainted;
    }

    public int getId() {
        return id;
    }

    public ProductType getProductType() {
        return productType;
    }

    public Workstation getWorkstation() {
        return workstation;
    }

    public void setWorkstation(Workstation workstation) {
        this.workstation = workstation;
    }

    public ProductActivity getProductActivity() {
        return productActivity;
    }

    public void setProductActivity(ProductActivity productActivity) {
        this.productActivity = productActivity;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker newWorker) {
        this.worker = newWorker;
    }

    public double getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(double arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public double getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(double finishTime) {
        this.finishTime = finishTime;
    }

    public double getStartCuttingTime() {
        return startCuttingTime;
    }

    public void setStartCuttingTime(double startCuttingTime) {
        this.startCuttingTime = startCuttingTime;
    }

    public double getFinishCuttingTime() {
        return finishCuttingTime;
    }

    public void setFinishCuttingTime(double finishCuttingTime) {
        this.finishCuttingTime = finishCuttingTime;
    }

    public double getStartPaintingTime() {
        return startPaintingTime;
    }

    public void setStartPaintingTime(double startPaintingTime) {
        this.startPaintingTime = startPaintingTime;
    }

    public double getFinishPaintingTime() {
        return finishPaintingTime;
    }

    public void setFinishPaintingTime(double finishPaintingTime) {
        this.finishPaintingTime = finishPaintingTime;
    }

    public double getStartAssemblyTime() {
        return startAssemblyTime;
    }

    public void setStartAssemblyTime(double startAssemblyTime) {
        this.startAssemblyTime = startAssemblyTime;
    }

    public double getFinishAssemblyTime() {
        return finishAssemblyTime;
    }

    public void setFinishAssemblyTime(double finishAssemblyTime) {
        this.finishAssemblyTime = finishAssemblyTime;
    }

    public double getStartFittingAssemblyTime() {
        return startFittingAssemblyTime;
    }

    public void setStartFittingAssemblyTime(double startFittingAssemblyTime) {
        this.startFittingAssemblyTime = startFittingAssemblyTime;
    }

    public double getFinishFittingAssemblyTime() {
        return finishFittingAssemblyTime;
    }

    public void setFinishFittingAssemblyTime(double finishFittingAssemblyTime) {
        this.finishFittingAssemblyTime = finishFittingAssemblyTime;
    }

    public double getStartStainingTime() {
        return startStainingTime;
    }

    public void setStartStainingTime(double startStainingTime) {
        this.startStainingTime = startStainingTime;
    }

    public double getFinishStainingTime() {
        return finishStainingTime;
    }

    public void setFinishStainingTime(double finishStainingTime) {
        this.finishStainingTime = finishStainingTime;
    }

    public void setProductAsDone(double time) {
        this.productActivity = ProductActivity.DONE;
        this.finishTime = time;

        // vyčisti workstation
        if (workstation != null) {
            this.clearWorkstation();
        }

        // vyčisti workera
        if (worker != null) {
            this.clearWorker();
        }
    }

    public void clearWorker() {
        this.worker.setCurrentWork(WorkerWork.IDLE);
        this.worker.setCurrentProduct(null);
        this.worker = null;
    }

    public void clearWorkstation() {
        this.workstation.setCurrentProduct(null);
        this.workstation = null;
    }

    @Override
    public String toString() {
        return String.format("%d: %s", id, productType);
    }

    @Override
    public int compareTo(Product o) {
        int result = o.productActivity.compareTo(this.productActivity);
        return result == 0 ? Integer.compare(this.id, o.id) : result;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    private void checkTimeOrder(double start, double finish, String name) {
        if (start > finish && finish != 0) {
            throw new IllegalStateException(name + ": start time cannot be after finish time.");
        }
    }

    public void validateTimes() {
        if (!Constants.CHECK_TIMES)
            return;
        checkTimeOrder(arrivalTime, finishTime, "Arrival");
        checkTimeOrder(startCuttingTime, finishCuttingTime, "Cutting");
        checkTimeOrder(startStainingTime, finishStainingTime, "Staining");
        checkTimeOrder(startPaintingTime, finishPaintingTime, "Painting");
        checkTimeOrder(startAssemblyTime, finishAssemblyTime, "Assembly");
        checkTimeOrder(startFittingAssemblyTime, finishFittingAssemblyTime, "Fitting Assembly");
    }

}
