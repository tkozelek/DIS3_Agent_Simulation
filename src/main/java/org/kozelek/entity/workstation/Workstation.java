package org.kozelek.entity.workstation;


import org.kozelek.entity.product.Product;

public class Workstation {
    private final int id;
    private Product currentProduct;

    public Workstation(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Product getCurrentOrder() {
        return currentProduct;
    }

    public void setCurrentOrder(Product currentProduct) {
        this.currentProduct = currentProduct;
    }
}
