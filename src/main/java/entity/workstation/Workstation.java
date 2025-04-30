package entity.workstation;

import entity.ILocation;
import entity.Ids;
import entity.product.Product;

public class Workstation implements ILocation {
    private final int id;
    private Product currentProduct;

    public Workstation() {
        this.id = Ids.getWorkstationId();
    }

    public int getId() {
        return id;
    }

    public Product getCurrentOrder() {
        return currentProduct;
    }

    public void setCurrentProduct(Product currentProduct) {
        this.currentProduct = currentProduct;
    }

    @Override
    public String toString() {
        return String.format("Workstation %d", id);
    }
}
