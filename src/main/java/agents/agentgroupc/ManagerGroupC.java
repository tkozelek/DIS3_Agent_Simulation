package agents.agentgroupc;

import OSPABA.*;
import entity.ILocation;
import entity.product.Product;
import entity.worker.Worker;
import entity.worker.WorkerWork;
import simulation.*;
import simulation.custommessage.MyMessageMove;
import simulation.custommessage.MyMessageProduct;

import java.util.Random;

//meta! id="67"
public class ManagerGroupC extends OSPABA.Manager {

	private Random randomStaining;

	public ManagerGroupC(int id, Simulation mySim, Agent myAgent) {
		super(id, mySim, myAgent);
		init();

		MySimulation sim = (MySimulation) mySim;
		this.randomStaining = new Random(sim.getSeedGenerator().sample());
	}

	@Override
	public void prepareReplication() {
		super.prepareReplication();
		// Setup component for the next replication

		if (petriNet() != null) {
			petriNet().clear();
		}
	}

	//meta! sender="AgentWorker", id="71", type="Response"
	public void processRequestResponseMoveWorker(MessageForm message) {
		MyMessageMove msg = (MyMessageMove) message;
		this.startProcess(message, msg.getWorker().getCurrentProduct(), Id.processMorenie);
	}

	//meta! sender="AgentWorker", id="72", type="Request"
	public void processRequestResponseWorkAgentC(MessageForm message) {
		// žiadosť o obslhuu od agentaworkera -> A dokončené
		myAgent().group().addQueue((MyMessageProduct) message);

		this.tryStartWorkOnOrder();
	}

	private void tryStartWorkOnOrder() {
		Worker worker = myAgent().group().getFreeWorker();

		if (worker == null) return;

		MyMessageProduct messageProduct = myAgent().group().pollQueue();
		Product product = messageProduct.getProduct();

		worker.setCurrentWorkstation(product.getWorkstation());
		worker.setCurrentProduct(product);

		product.setWorker(worker);

		if (worker.getLocation() != product.getWorkstation()) {
			this.moveWorkerRequest(messageProduct, worker, product.getWorkstation());
		} else {
			this.startProcess(messageProduct, product, Id.processMorenie);
		}
	}

	private void startProcess(MessageForm message, Product product, int processId) {
		MyMessageProduct msgProduct = new MyMessageProduct(message);
		msgProduct.setProduct(product);
		msgProduct.setAddressee(myAgent().findAssistant(processId));
		startContinualAssistant(msgProduct);
	}

	private void moveWorkerRequest(MyMessageProduct message, Worker worker, ILocation location) {
		if (worker.getLocation() == location)
			throw new IllegalStateException("Worker and location is the same");

		MyMessageMove msgMove = new MyMessageMove(message);
		msgMove.setTargetLocation(location);
		msgMove.setWorker(worker);

		msgMove.setCode(Mc.requestResponseMoveWorker);
		msgMove.setAddressee(Id.agentWorker);
		this.request(msgMove);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message) {
		switch (message.code()) {
		}
	}

	//meta! sender="ProcessLakovanie", id="84", type="Finish"
	public void processFinishProcessLakovanie(MessageForm message) {
		message.setCode(Mc.requestResponseWorkAgentC);
		message.setAddressee(Id.agentWorker);
		this.response(message);
	}

	//meta! sender="ProcessMorenie", id="82", type="Finish"
	public void processFinishProcessMorenie(MessageForm message) {
		// je nalakovany
		MyMessageProduct msgProduct = (MyMessageProduct) message;
		Product product = msgProduct.getProduct();

		if (product.getShouldBePainted()) {
			// treba aj nalakovat
			message.setAddressee(myAgent().findAssistant(Id.processLakovanie));
			this.startContinualAssistant(message);
		} else {
			message.setCode(Mc.requestResponseWorkAgentC);
			message.setAddressee(Id.agentWorker);
			this.response(message);
		}
		if (myAgent().group().queueSize() > 0)
			this.tryStartWorkOnOrder();
	}

	//meta! sender="AgentWorker", id="97", type="Request"
	public void processRequestResponseFittingAssembly(MessageForm message) {
		Worker worker = myAgent().group().getFreeWorker();
		if (worker == null) {
			// worker nie je free
			message.setCode(Mc.requestResponseFittingAssembly);
			message.setAddressee(Id.agentWorker);
			this.response(message);
			return;
		}
		MyMessageProduct msgProduct = (MyMessageProduct) message;
		Product product = msgProduct.getProduct();

		product.setWorker(worker);
		worker.setCurrentProduct(product);

		msgProduct.setAddressee(myAgent().findAssistant(Id.processFittingGroupC));
		this.startContinualAssistant(msgProduct);
	}

	//meta! sender="ProcessFittingGroupC", id="95", type="Finish"
	public void processFinishProcessFittingGroupC(MessageForm message) {
		message.setCode(Mc.requestResponseWorkAgentC);
		message.setAddressee(Id.agentWorker);
		this.response(message);
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	public void init() {
	}

	@Override
	public void processMessage(MessageForm message) {
		switch (message.code()) {
		case Mc.finish:
			switch (message.sender().id()) {
			case Id.processFittingGroupC:
				processFinishProcessFittingGroupC(message);
			break;

			case Id.processMorenie:
				processFinishProcessMorenie(message);
			break;

			case Id.processLakovanie:
				processFinishProcessLakovanie(message);
			break;
			}
		break;

		case Mc.requestResponseWorkAgentC:
			processRequestResponseWorkAgentC(message);
		break;

		case Mc.requestResponseMoveWorker:
			processRequestResponseMoveWorker(message);
		break;

		case Mc.requestResponseFittingAssembly:
			processRequestResponseFittingAssembly(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public AgentGroupC myAgent() {
		return (AgentGroupC)super.myAgent();
	}

}