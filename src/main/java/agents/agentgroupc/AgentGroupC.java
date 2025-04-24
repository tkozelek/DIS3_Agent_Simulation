package agents.agentgroupc;

import OSPABA.*;
import OSPDataStruct.SimQueue;
import entity.product.Product;
import entity.worker.Worker;
import entity.worker.WorkerGroup;
import entity.worker.WorkerWork;
import simulation.*;
import agents.agentgroupc.continualassistants.*;

import java.util.ArrayList;
import java.util.List;


//meta! id="67"
public class AgentGroupC extends OSPABA.Agent {
	private final SimQueue<Product> queue;

	public Worker[] workers;

	public AgentGroupC(int id, Simulation mySim, Agent parent) {
		super(id, mySim, parent);
        init();

		this.addOwnMessage(Mc.holdMorenie);
		this.addOwnMessage(Mc.holdLakovanie);

		this.queue = new SimQueue<>();
	}

	public void addToQueue(Product p) {
		queue.add(p);
	}

	public Product pollQueue() {
		return queue.poll();
	}

	public Product peekFromQueue() {
		return queue.peek();
	}

	public int queueSize() {
		return queue.size();
	}

	public int workersSize() {
		return workers.length;
	}

	public Worker[] getWorkers() {
		return workers;
	}

	private void createWorkers() {
		MySimulation sim = (MySimulation) mySim();
		int amount = sim.getWorkerCountForGroup(WorkerGroup.GROUP_C);
		workers = new Worker[amount];
		for (int i = 0; i < amount; i++) {
			workers[i] = new Worker(WorkerGroup.GROUP_C);
		}
	}

	public List<Worker> getFreeWorkers() {
		List<Worker> freeWorkers = new ArrayList<>();
		for (Worker w : workers) {
			if (w.getCurrentWork() == WorkerWork.IDLE) {
				freeWorkers.add(w);
			}
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

	@Override
	public void prepareReplication() {
		super.prepareReplication();
		// Setup component for the next replication

		this.queue.clear();
		createWorkers();
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init() {
		new ManagerGroupC(Id.managerGroupC, mySim(), this);
		new ProcessLakovanie(Id.processLakovanie, mySim(), this);
		new ProcessMorenie(Id.processMorenie, mySim(), this);
		addOwnMessage(Mc.requestResponseMoveWorker);
		addOwnMessage(Mc.requestResponseWorkAgentC);
	}
	//meta! tag="end"
}