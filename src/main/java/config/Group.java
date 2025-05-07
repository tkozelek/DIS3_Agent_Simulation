package config;

import OSPABA.Simulation;
import OSPDataStruct.SimQueue;
import OSPStat.Stat;
import OSPStat.WStat;
import entity.worker.Worker;
import entity.worker.WorkerGroup;
import entity.worker.WorkerWork;
import simulation.Data;
import simulation.MyMessage;
import simulation.MySimulation;

import java.util.ArrayList;

public class Group {
    private final SimQueue<MyMessage> queue;
    private final WorkerGroup workerGroup;
    private final int amount;
    private final Stat statWorkloadGroupTotal;
    private final Stat statQueueLengthTotal;
    private final WStat statQueueLength;
    private Worker[] workers;

    public Group(int workerAmount, WorkerGroup workerGroup, Simulation sim) {
        this.queue = new SimQueue<>();
        this.workers = new Worker[workerAmount];
        this.workerGroup = workerGroup;
        this.amount = workerAmount;

        this.statWorkloadGroupTotal = new Stat();
        this.statQueueLengthTotal = new Stat();

        this.statQueueLength = new WStat(sim);
        this.createWorkers(sim);
    }

    public WStat getStatQueueLength() {
        return statQueueLength;
    }

    public Stat getWorkloadGroupTotal() {
        return statWorkloadGroupTotal;
    }

    public Stat getStatQueueLengthTotal() {
        return statQueueLengthTotal;
    }

    public void collectStats() {
        for (Worker worker : workers) {
            statWorkloadGroupTotal.addSample(worker.getStatWorkload().mean());
        }
        this.statQueueLengthTotal.addSample(statQueueLength.mean());
    }

    public void addQueue(MyMessage p, double time) {
        queue.add(p);
        this.statQueueLength.addSample(queue.size());
        p.getProduct().enterQueue(time);
    }

    public MyMessage pollQueue(double time) {
        MyMessage p = queue.poll();
        if (p != null) {
            p.getProduct().exitQueue(time);
        }
        this.statQueueLength.addSample(queue.size());
        return p;
    }

    public int queueSize() {
        return queue.size();
    }

    public Worker[] getWorkers() {
        return workers;
    }

    private void createWorkers(Simulation sim) {
        workers = new Worker[amount];
        for (int i = 0; i < amount; i++) {
            workers[i] = new Worker(workerGroup, sim);
        }
    }

    public ArrayList<Worker> getFreeWorkers(int amount) {
        ArrayList<Worker> freeWorkers = new ArrayList<>();
        for (Worker w : workers) {
            if (w.getCurrentWork() == WorkerWork.IDLE)
                freeWorkers.add(w);
            if (freeWorkers.size() >= amount)
                break;
        }
        return freeWorkers;
    }

    public Worker getFreeWorker() {
        for (Worker worker : workers) {
            if (worker.getCurrentWork() == WorkerWork.IDLE) {
                return worker;
            }
        }
        return null;
    }

    public void reset(Simulation sim) {
        this.queue.clear();
        for (Worker w : workers) {
            w.reset(sim);
        }
        this.statQueueLength.clear();
    }

    @Override
    public String toString() {
        return workerGroup != null ? workerGroup.toString() : "Fitting";
    }

    public void initWorkers(MySimulation sim) {
        if (sim.animatorExists()) {
            for (Worker w : workers) {
                w.getAnimImageItem().setPosition(Data.getRandomStoragePoiunt());
                sim.animator().register(w.getAnimImageItem());
            }
        }
    }
}
