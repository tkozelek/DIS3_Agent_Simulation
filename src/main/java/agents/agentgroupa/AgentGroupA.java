package agents.agentgroupa;

import OSPABA.*;
import OSPDataStruct.SimQueue;
import OSPRNG.EmpiricPair;
import OSPRNG.EmpiricRNG;
import OSPRNG.UniformContinuousRNG;
import entity.product.Product;
import entity.product.ProductType;
import entity.worker.Worker;
import entity.worker.WorkerGroup;
import simulation.*;
import agents.agentgroupa.continualassistants.*;



//meta! id="7"
public class AgentGroupA extends OSPABA.Agent {
	private final SimQueue<Product> queue;

	private EmpiricRNG cuttingTableGenerator;
	private UniformContinuousRNG cuttingChairGenerator;
	private UniformContinuousRNG cuttingcupboardGenerator;

	public Worker[] workers;

    public AgentGroupA(int id, Simulation mySim, Agent parent) {
        super(id, mySim, parent);
        init();
		int times = 60;
		MySimulation sim = (MySimulation) mySim;
		this.cuttingTableGenerator = new EmpiricRNG(sim.getSeedGenerator().getRandom(),
				new EmpiricPair(new UniformContinuousRNG(10.0 * times, 25.0 * times), 0.6),
				new EmpiricPair(new UniformContinuousRNG(25.0 * times, 50.0 * times), 0.4)
		);
		this.cuttingChairGenerator = new UniformContinuousRNG(12.0 * times, 24.0 * times);
		this.cuttingcupboardGenerator = new UniformContinuousRNG(15.0 * times, 80.0 * times);

		this.queue = new SimQueue<>();
    }

	public void addToQueue(Product p) {
		queue.add(p);
	}

	public Product removeFromQueue() {
		return queue.poll();
	}

	public int queueSize() {
		return queue.size();
	}

	public EmpiricRNG getCuttingTableGenerator() {
		return cuttingTableGenerator;
	}

	public UniformContinuousRNG getCuttingChairGenerator() {
		return cuttingChairGenerator;
	}

	public UniformContinuousRNG getCuttingcupboardGenerator() {
		return cuttingcupboardGenerator;
	}

	public Double getSampleFromGeneratorBasedOnProductType(ProductType type) {
		return switch (type) {
			case ProductType.CHAIR -> this.cuttingChairGenerator.sample();
			case ProductType.TABLE -> this.cuttingTableGenerator.sample().doubleValue();
			case ProductType.CUPBOARD -> this.cuttingcupboardGenerator.sample();
			default -> throw new IllegalStateException("Unexpected value: " + type);
		};
	}

	private void createWorkers() {
		MySimulation sim = (MySimulation) mySim();
		int amount = sim.getWorkerCountForGroup(WorkerGroup.GROUP_A);
		workers = new Worker[amount];
		for (int i = 0; i < amount; i++) {
			workers[i] = new Worker(WorkerGroup.GROUP_A);
		}
	}

	@Override
    public void prepareReplication() {
        super.prepareReplication();
        // Setup component for the next replication

		this.createWorkers();
    }

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new ManagerGroupA(Id.managerGroupA, mySim(), this);
		new ProcessCutting(Id.processCutting, mySim(), this);
		new ProcessFitting(Id.processFitting, mySim(), this);
		new ProcessPreparing(Id.processPreparing, mySim(), this);
		addOwnMessage(Mc.requestResponseWorkerFreeWorkstation);
		addOwnMessage(Mc.requestResponseWorkOnOrder);
		addOwnMessage(Mc.requestResponseMoveWorker);
	}
	//meta! tag="end"
}