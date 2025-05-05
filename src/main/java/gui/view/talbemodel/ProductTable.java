package gui.view.talbemodel;

import config.Helper;
import entity.product.Product;

import java.util.List;

public class ProductTable extends Table<Product> {

    public ProductTable() {
        super(new String[]{"OID", "ID", "Type", "Wid", "Stat", "Arr", "S Cut", "E Cut", "S Stain", "E Stain", "S Paint", "E Paint", "S Ass", "E Ass", "S Fit", "E Fit", "Fin"},
                List.of(
                        p -> p.getOrder().getId(),
                        Product::getId,
                        Product::getProductType,
                        p -> p.getWorker() != null ? p.getWorker().toStringGroupId() : null,
                        Product::getProductActivity,
                        w -> String.format("%s", Helper.timeToDateString(w.getArrivalTime(), 6)),
                        w -> String.format("%s", Helper.timeToDateString(w.getStartCuttingTime(), 6)),
                        w -> String.format("%s", Helper.timeToDateString(w.getFinishCuttingTime(), 6)),
                        w -> String.format("%s", Helper.timeToDateString(w.getStartStainingTime(), 6)),
                        w -> String.format("%s", Helper.timeToDateString(w.getFinishStainingTime(), 6)),
                        w -> String.format("%s", Helper.timeToDateString(w.getStartPaintingTime(), 6)),
                        w -> String.format("%s", Helper.timeToDateString(w.getFinishPaintingTime(), 6)),
                        w -> String.format("%s", Helper.timeToDateString(w.getStartAssemblyTime(), 6)),
                        w -> String.format("%s", Helper.timeToDateString(w.getFinishAssemblyTime(), 6)),
                        w -> String.format("%s", Helper.timeToDateString(w.getStartFittingAssemblyTime(), 6)),
                        w -> String.format("%s", Helper.timeToDateString(w.getFinishFittingAssemblyTime(), 6)),
                        w -> String.format("%s", Helper.timeToDateString(w.getFinishTime(), 6))
                ));
    }
}
