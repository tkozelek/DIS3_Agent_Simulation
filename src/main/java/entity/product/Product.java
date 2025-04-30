package entity.product;


import entity.Ids;
import entity.order.Order;
import entity.worker.Worker;
import entity.worker.WorkerGroup;
import entity.workstation.Workstation;

public class Product implements Comparable<Product> {
    private final int id;
    private final ProductType productType;
    private ProductActivity productActivity;
    private Workstation workstation;
    private Worker currentWorker;

    private double arrivalTime;
    private double finishTime;

    private double startCuttingTime;
    private double finishCuttingTime;

    private double startStainingTime;
    private double finishStainingTime;

    private double startPaintingTime;
    private double finishPaintingTime;

    private double startAssemblyTime;
    private double finishAssemblyTime;

    private double startFittingAssemblyTime;
    private double finishFittingAssemblyTime;

    private Order order;

    private boolean shouldBePainted;

    public Product(ProductType productType) {
        this.id = Ids.getProductId();
        this.productType = productType;
        this.productActivity = ProductActivity.EMTPY;
    }

    public WorkerGroup getNextNeededGroup() {
        return switch (productActivity) {
            case EMTPY -> WorkerGroup.GROUP_A;
            case CUT, ASSEMBLED -> WorkerGroup.GROUP_C;
            case PAINTED -> WorkerGroup.GROUP_B;
            default -> throw new IllegalStateException("Unexpected value: " + productActivity);
        };
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

    public Worker getCurrentWorker() {
        return currentWorker;
    }

    public void setCurrentWorker(Worker currentWorker) {
        this.currentWorker = currentWorker;
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

    @Override
    public String toString() {
        return String.format("%d - %s", id, productType);
    }

    @Override
    public int compareTo(Product o) {
        int en = this.productActivity.compareTo(o.productActivity);
        return en == 0 ? Integer.compare(id, o.id) : en;
    }

    public void addOrder(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }
}
