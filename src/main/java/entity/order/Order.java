package entity.order;

import entity.Ids;
import entity.product.Product;

import java.util.ArrayList;

public class Order {
    private final int id;
    private ArrayList<Product> products;

    private double arrivalTime;
    private double finishTime;

    public Order() {
        this.id = Ids.getOrderId();
        this.arrivalTime = 0;
        this.products = new ArrayList<>();
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
        for (Product product : products) {
            product.setArrivalTime(arrivalTime);
        }
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void addProduct(Product product) {
        this.products.add(product);
    }

    public int getProductsCount() {
        return products.size();
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("Order %d: products: %s", id, products);
    }
}
