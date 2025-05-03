package config;

import OSPDataStruct.SimQueue;
import entity.product.Product;
import entity.worker.Worker;
import entity.worker.WorkerGroup;
import entity.worker.WorkerWork;
import simulation.custommessage.MyMessageProduct;

import java.util.ArrayList;

public class Group {
    private final SimQueue<MyMessageProduct> queue;
    private Worker[] workers;
    private final WorkerGroup workerGroup;
    private final int amount;

    public Group(int workerAmount, WorkerGroup workerGroup) {
        this.queue = new SimQueue<>();
        this.workers = new Worker[workerAmount];
        this.workerGroup = workerGroup;
        this.amount = workerAmount;
    }

    public void addQueue(MyMessageProduct p) {
        queue.add(p);
    }

    public MyMessageProduct pollQueue() {
        return queue.poll();
    }

    public int queueSize() {
        return queue.size();
    }

    public Worker[] getWorkers() {
        return workers;
    }

    private void createWorkers() {
        workers = new Worker[amount];
        for (int i = 0; i < amount; i++) {
            workers[i] = new Worker(workerGroup);
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

    public void reset() {
        this.queue.clear();
        this.createWorkers();
    }
}
