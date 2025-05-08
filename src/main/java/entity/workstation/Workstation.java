package entity.workstation;

import OSPABA.Simulation;
import OSPAnimator.AnimImageItem;
import OSPStat.Stat;
import OSPStat.WStat;
import entity.ILocation;
import entity.Ids;
import entity.product.Product;
import simulation.Data;

public class Workstation implements ILocation {
    private final int id;
    private final Stat statWorkloadTotal;
    private Product currentProduct;
    private WStat statWorkload;

    private final AnimImageItem animImageItem;

    public Workstation(Simulation sim) {
        this.id = Ids.getWorkstationId();
        this.statWorkload = new WStat(sim);
        this.statWorkloadTotal = new Stat();

        this.animImageItem = new AnimImageItem(Data.WORKSTATION, Data.WORKSTATION_WIDTH, Data.WORKSTATION_HEIGHT);
        this.animImageItem.setToolTip(toString());
    }

    public AnimImageItem getAnimImageItem() {
        return animImageItem;
    }

    public Stat getStatWorkloadTotal() {
        return statWorkloadTotal;
    }

    public WStat getStatWorkload() {
        return statWorkload;
    }

    public int getId() {
        return id;
    }

    public Product getCurrentProduct() {
        return currentProduct;
    }

    public void setCurrentProduct(Product currentProduct) {
        this.currentProduct = currentProduct;
        this.statWorkload.addSample(currentProduct == null ? 0 : 1);
    }

    public void reset(Simulation sim) {
        if (statWorkload.sampleSize() > 0)
            statWorkloadTotal.addSample(statWorkload.mean());

        this.statWorkload = new WStat(sim);
        this.currentProduct = null;
    }

    @Override
    public String toString() {
        return String.format("Workstation %d", id);
    }
}
