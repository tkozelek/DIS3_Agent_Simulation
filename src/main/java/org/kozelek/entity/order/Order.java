package org.kozelek.entity.order;

import org.kozelek.entity.Ids;
import org.kozelek.entity.product.Product;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private final int id;
    private List<Product> products;

    private double arrivalTime;
    private double finishTime;

    public Order() {
        this.id = Ids.getOrderId();
        this.products = new ArrayList<Product>();
        this.arrivalTime = 0;
    }

    public double getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(double finishTime) {
        this.finishTime = finishTime;
    }

    public double getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(double arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public List<Product> getProducts() {
        return products;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("Order %d: arr time: %.2f\n", id, arrivalTime);
    }
}
