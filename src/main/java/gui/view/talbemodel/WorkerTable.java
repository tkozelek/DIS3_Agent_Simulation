package gui.view.talbemodel;

import entity.worker.Worker;

import java.util.List;

public class WorkerTable extends Table<Worker> {

    public WorkerTable() {
        super(new String[]{"ID", "Work", "Pos", "ks"},
                List.of(
                        Worker::getId,
                        Worker::getCurrentWork,
                        Worker::getLocation,
//                        w -> String.format("%.2f", w.getStatisticWorkload().getMean() * 100),
                        Worker::getFinishedTasks
                ));
    }
}
