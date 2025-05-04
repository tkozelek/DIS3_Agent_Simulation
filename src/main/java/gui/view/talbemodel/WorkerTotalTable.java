package gui.view.talbemodel;

import entity.worker.Worker;

import java.util.List;

public class WorkerTotalTable extends Table<Worker> {

    public WorkerTotalTable() {
        super(new String[]{"Id", "WL%", "IS<B, T>"}, List.of(
                Worker::getId,
                w -> String.format("%.2f", w.getStatWorkloadTotal().mean() * 100),
                w -> String.format("<%.2f%% | %.2f%%>",
                        w.getStatWorkloadTotal().sampleSize() > 2 ? w.getStatWorkloadTotal().confidenceInterval_95()[0] * 100 : 0,
                        w.getStatWorkloadTotal().sampleSize() > 2 ? w.getStatWorkloadTotal().confidenceInterval_95()[1] * 100: 0)
        ));
    }
}
