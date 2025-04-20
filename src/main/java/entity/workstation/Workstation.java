package entity.workstation;


public class Workstation {
    private final int id;
    private entity.product.Product currentProduct;

    public Workstation(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public entity.product.Product getCurrentOrder() {
        return currentProduct;
    }

    public void setCurrentOrder(entity.product.Product currentProduct) {
        this.currentProduct = currentProduct;
    }
}
