package gui.view.talbemodel;

import entity.workstation.Workstation;

import java.util.List;

public class WorkstationTable extends Table<Workstation> {

    public WorkstationTable() {
        super(new String[]{"ID", "Order", "Work", "Worker", "WL%"},
                List.of(
                        Workstation::getId,
                        Workstation::getCurrentProduct,
                        w -> (w.getCurrentProduct() != null) ? w.getCurrentProduct().getProductActivity() : "",
                        w -> (w.getCurrentProduct() != null) ? w.getCurrentProduct().getWorker() : "",
                        w -> w.getStatWorkload() != null ? String.format("%.2f", w.getStatWorkload().mean() * 100) : ""
                ));
    }
}
