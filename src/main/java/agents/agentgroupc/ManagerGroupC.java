package agents.agentgroupc;

import OSPABA.*;
import entity.ILocation;
import entity.product.Product;
import entity.product.ProductActivity;
import entity.worker.Worker;
import simulation.*;
import simulation.custommessage.MyMessageMove;
import simulation.custommessage.MyMessageProduct;

//meta! id="67"
public class ManagerGroupC extends OSPABA.Manager {
	public ManagerGroupC(int id, Simulation mySim, Agent myAgent) {
		super(id, mySim, myAgent);
		init();
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
		Product product = msg.getWorker().getCurrentProduct();

		if (product.getProductActivity() == ProductActivity.CUT)
			this.startProcess(message, product, Id.processMorenie);
		else {
			this.startProcess(message, product, Id.processFittingGroupC);
		}
	}

	//meta! sender="AgentWorker", id="72", type="Request"
	public void processRequestResponseWorkAgentC(MessageForm message) {
		// žiadosť o obslhuu od agentaworkera -> A dokončené
		myAgent().group().addQueue((MyMessageProduct) message);

		this.tryStartWork();
	}

	/**
	 * If a worker is available and any product is in the queue
	 */
	private void tryStartWork() {
		Worker worker = myAgent().group().getFreeWorker();

		if (worker == null || myAgent().group().queueSize() == 0) return;

		MyMessageProduct messageProduct = myAgent().group().pollQueue();
		Product product = messageProduct.getProduct();

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

//		this.tryStartWorkOnOrder();
		this.sendNoticeToWorker();
	}

	/**
	 * Send notice to worker that a worker is freed
	 */
	public void sendNoticeToWorker() {
		MyMessage msg = new MyMessage(mySim());
		msg.setCode(Mc.noticeAgentCFreed);
		msg.setAddressee(Id.agentWorker);
		this.notice(msg);
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

//			this.tryStartWorkOnOrder();
			this.sendNoticeToWorker();
		}
	}

	//meta! sender="ProcessFittingGroupC", id="95", type="Finish"
	public void processFinishProcessFittingGroupC(MessageForm message) {

	}

	//meta! sender="AgentWorker", id="118", type="Request"
	public void processRequestResponseTryFitGroupC(MessageForm message) {
		// prišiel req od agent workera
		// skusime fitnut ak je free worker
		Worker worker = myAgent().group().getFreeWorker();

		// pošleme s5 spravu
		if (worker == null) {
			message.setCode(Mc.requestResponseTryFitGroupC);
			message.setAddressee(Id.agentWorker);
			this.response(message);
			return;
		}

		// worker je free
		MyMessageProduct msgProduct = (MyMessageProduct) message;
		Product product = msgProduct.getProduct();
		if (product == null) {
			message.setCode(Mc.requestResponseTryFitGroupC);
			message.setAddressee(Id.agentWorker);
			this.response(message);
			this.tryStartWork();
			return;
		}
		worker.setCurrentProduct(product);
		product.setWorker(worker);

		// je worker aj product
		// skontrolujeme ci je worker na mieste
		if (worker.getLocation() != product.getWorkstation()) {
			// nie je na mieste, presunieme
			this.moveWorkerRequest(msgProduct, worker, product.getWorkstation());
		} else {
			this.startProcess(msgProduct, product, Id.processFittingGroupC);
		}
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	public void init() {
	}

	@Override
	public void processMessage(MessageForm message) {
		switch (message.code()) {
		case Mc.requestResponseMoveWorker:
			processRequestResponseMoveWorker(message);
		break;

		case Mc.finish:
			switch (message.sender().id()) {
			case Id.processMorenie:
				processFinishProcessMorenie(message);
			break;

			case Id.processLakovanie:
				processFinishProcessLakovanie(message);
			break;

			case Id.processFittingGroupC:
				processFinishProcessFittingGroupC(message);
			break;
			}
		break;

		case Mc.requestResponseWorkAgentC:
			processRequestResponseWorkAgentC(message);
		break;

		case Mc.requestResponseTryFitGroupC:
			processRequestResponseTryFitGroupC(message);
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