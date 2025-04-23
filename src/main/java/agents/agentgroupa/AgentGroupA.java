package agents.agentgroupa;

import OSPABA.*;
import OSPDataStruct.SimQueue;
import simulation.*;
import agents.agentgroupa.continualassistants.*;

import entity.product.Product;
import entity.worker.Worker;
import entity.worker.WorkerGroup;
import entity.worker.WorkerWork;

import java.util.ArrayList;
import java.util.List;

//meta! id="7"
public class AgentGroupA extends OSPABA.Agent {
	private final SimQueue<Product> queue;

	public Worker[] workers;

    public AgentGroupA(int id, Simulation mySim, Agent parent) {
        super(id, mySim, parent);
        init();

		this.addOwnMessage(Mc.holdCutting);
		this.addOwnMessage(Mc.holdFitting);
		this.addOwnMessage(Mc.holdPrepareMaterial);

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

	private void createWorkers() {
		MySimulation sim = (MySimulation) mySim();
		int amount = sim.getWorkerCountForGroup(WorkerGroup.GROUP_A);
		workers = new Worker[amount];
		for (int i = 0; i < amount; i++) {
			workers[i] = new Worker(WorkerGroup.GROUP_A);
		}
	}

	public List<Worker> getFreeWorkers() {
		List<Worker> freeWorkers = new ArrayList<>();
		for (Worker w : workers) {
			if (w.getCurrentWork() == WorkerWork.IDLE && w.getCurrentWork() != WorkerWork.MOVING) {
				freeWorkers.add(w);
			}
		}
		return freeWorkers;
	}

	public Worker getFreeWorker() {
		for (Worker worker : workers) {
			if (worker.getCurrentWork() == WorkerWork.IDLE && worker.getCurrentWork() != WorkerWork.MOVING) {
				return worker;
			}
		}
		return null;
	}

	@Override
    public void prepareReplication() {
        super.prepareReplication();
        // Setup component for the next replication

		this.createWorkers();
    }

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init() {
		new ManagerGroupA(Id.managerGroupA, mySim(), this);
		new ProcessCutting(Id.processCutting, mySim(), this);
		new ProcessFitting(Id.processFitting, mySim(), this);
		new ProcessPreparing(Id.processPreparing, mySim(), this);
		addOwnMessage(Mc.requestResponseWorkerFreeWorkstation);
		addOwnMessage(Mc.requestResponseMoveWorker);
		addOwnMessage(Mc.requestResponseWorkAgentA);
	}
	//meta! tag="end"
}