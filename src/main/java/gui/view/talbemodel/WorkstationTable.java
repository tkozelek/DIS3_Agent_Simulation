package gui.view.talbemodel;

import entity.workstation.Workstation;

import java.util.List;

public class WorkstationTable extends Table<Workstation> {

    public WorkstationTable() {
        super(new String[]{"ID", "Order", "Work", "Worker"},
                List.of(
                        Workstation::getId,
                        Workstation::getCurrentOrder,
                        w -> (w.getCurrentOrder() != null) ? w.getCurrentOrder().getProductActivity() : "",
                        w -> (w.getCurrentOrder() != null) ? w.getCurrentOrder().getWorker() : ""
                ));
    }
}
