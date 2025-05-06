package simulation;

import OSPABA.MessageForm;
import OSPABA.Simulation;
import entity.ILocation;
import entity.order.Order;
import entity.product.Product;
import entity.worker.Worker;

public class MyMessage extends OSPABA.MessageForm {
    private Order order;
    private ILocation targetLocation;
    private Worker worker;
    private Product product;


    public MyMessage(Simulation mySim) {
        super(mySim);
    }

    public MyMessage(MessageForm original) {
        super(original);
        // copy() is called in superclass

        MyMessage org = (MyMessage) original;
        this.order = org.order;
        this.targetLocation = org.targetLocation;
        this.worker = org.worker;
        this.product = org.product;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public ILocation getTargetLocation() {
        return targetLocation;
    }

    public void setTargetLocation(ILocation targetLocation) {
        this.targetLocation = targetLocation;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public MessageForm createCopy() {
        return new MyMessage(this);
    }

    @Override
    protected void copy(MessageForm message) {
        super.copy(message);
        MyMessage original = (MyMessage) message;
        // Copy attributes
    }
}