package gui.view.talbemodel;

import entity.worker.Worker;

import java.util.List;

public class WorkerTable extends Table<Worker> {

    public WorkerTable() {
        super(new String[]{"ID", "Work", "Pos", "WL%", "ks"},
                List.of(
                        Worker::getId,
                        Worker::getCurrentWork,
                        Worker::getLocation,
                        w -> String.format("%.2f", w.getStatWorkload().mean() * 100),
                        Worker::getFinishedTasks
                ));
    }
}
