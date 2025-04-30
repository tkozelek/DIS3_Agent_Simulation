package agents.agentgroupa;

import OSPABA.*;
import config.Constants;
import entity.ILocation;
import entity.Storage;
import entity.order.Order;
import entity.product.Product;
import entity.worker.Worker;
import entity.worker.WorkerWork;
import entity.workstation.Workstation;
import simulation.Id;
import simulation.Mc;
import simulation.MySimulation;
import simulation.custommessage.MyMessageMove;
import simulation.custommessage.MyMessageOrder;
import simulation.custommessage.MyMessageProduct;
import simulation.custommessage.MyMessageWorkstation;

import java.util.ArrayList;
import java.util.List;

//meta! id="7"
public class ManagerGroupA extends OSPABA.Manager {
    public ManagerGroupA(int id, Simulation mySim, Agent myAgent) {
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

	//meta! sender="AgentWorker", id="46", type="Response"
	public void processRequestResponseWorkerFreeWorkstation(MessageForm message) {
		// prišla free workstation, tzn. že je aj worker free
		this.firstWorkOnOrder(message);
    }

	private void firstWorkOnOrder(MessageForm message) {
		MyMessageWorkstation myMessage = (MyMessageWorkstation) message;
		ArrayList<Workstation> workstations = myMessage.getWorkstations();

		ArrayList<Worker> workers = myAgent().group().getFreeWorkers();
		ArrayList<MyMessageProduct> msgProducts = new ArrayList<>();
		int b = Math.min(workstations.size(), workers.size());
		int amount = Math.min(b, myAgent().group().queueSize());

		for (int i = 0; i < amount; i++) {
			msgProducts.add(myAgent().group().pollQueue());
		}

		if (workers.isEmpty() || workstations.isEmpty() || msgProducts.isEmpty()) return;

		// nastavime produktu workstation
		for (int i = 0; i < amount; i++) {
			Workstation workstation = workstations.get(i);
			Worker worker = workers.get(i);
			MyMessageProduct msgProduct = msgProducts.get(i);
			Product product = msgProduct.getProduct();

			worker.setCurrentWorkstation(workstation);
			worker.setCurrentProduct(product);

			product.setWorkstation(workstation);
			product.setCurrentWorker(worker);

			workstation.setCurrentProduct(product);

			// pozrieme ci worker je na mieste
			if (worker.getLocation() != Storage.STORAGE) {
				// nie je v sklade, musi ist do skladu
				// presun -> sklad
				moveWorkerRequest(message, worker, Storage.STORAGE);
			} else {
				// je v sklade
				// priprav veci
				startProcess(message, product, Id.processPreparing);
			}
		}
	}

	private void startProcess(MessageForm message, Product product, int processId) {
		MyMessageProduct msgProduct = new MyMessageProduct(message);
		msgProduct.setProduct(product);
		msgProduct.setAddressee(myAgent().findAssistant(processId));
		startContinualAssistant(msgProduct);
	}

	private void moveWorkerRequest(MessageForm message, Worker worker, ILocation location) {
		if (worker.getLocation() == location)
			throw new IllegalStateException("Worker A location is the same");

		MyMessageMove msgMove = new MyMessageMove(message);
		msgMove.setTargetLocation(location);
		msgMove.setWorker(worker);

		msgMove.setCode(Mc.requestResponseMoveWorker);
		msgMove.setAddressee(Id.agentWorker);
		this.request(msgMove);
	}

	//meta! sender="AgentWorker", id="39", type="Request"
	public void processRequestResponseWorkAgentA(MessageForm message) {
		// prisla objednavka
		MyMessageOrder orderMessage = (MyMessageOrder) message;
		Order order = orderMessage.getOrder();
		if (Constants.DEBUG_MANAGER)
			System.out.printf("[%s] M. group A objednávka prišla: %s\n", ((MySimulation)mySim()).workdayTime(), order);
		// vlozime do queue
		ArrayList<Product> products = order.getProducts();
		for (Product product : products) {
			MyMessageProduct msgProduct = new MyMessageProduct(message);
			msgProduct.setProduct(product);
			myAgent().group().addQueue(msgProduct);
		}

		// je to nova objednávka ked prišla agentovi A
		// ak je free worker, skús vyžiadať workstation
		this.startWork(message);
    }

	private void startWork(MessageForm message) {
		List<Worker> freeWorkers = this.myAgent().group().getFreeWorkers();
		if (freeWorkers == null) return;

		int amount = Math.min(freeWorkers.size(), myAgent().group().queueSize());

		this.requestWorkstation(message, amount);
	}

	private void requestWorkstation(MessageForm message, int amount) {
		MyMessageWorkstation msg = new MyMessageWorkstation(message);
		msg.setAmount(amount);
		msg.setCode(Mc.requestResponseWorkerFreeWorkstation);
		msg.setAddressee(Id.agentWorker);
		this.request(msg);
	}

	//meta! sender="AgentWorker", id="53", type="Response"
	public void processRequestResponseMoveWorker(MessageForm message) {
		// skoncil presun, je bud pri workstatione alebo storage
		MyMessageMove msgMove = (MyMessageMove) message;
		Worker worker = msgMove.getWorker();
		if (worker.getLocation() == Storage.STORAGE) {
			startProcess(msgMove, worker.getCurrentProduct(), Id.processPreparing);
		} else if (worker.getLocation() == worker.getCurrentProduct().getWorkstation()) {
			startProcess(msgMove, worker.getCurrentProduct(), Id.processCutting);
		} else {
			throw new IllegalStateException("Manager group A, invalid location");
		}
    }

	//meta! sender="ProcessCutting", id="27", type="Finish"
	public void processFinishProcessCutting(MessageForm message) {
		// skončilo rezanie, agent A končí robotu na produkte
		MyMessageProduct msgProduct = (MyMessageProduct) message;
		if (myAgent().group().queueSize() > 0) {
			this.startWork(message);
		}

		msgProduct.setCode(Mc.requestResponseWorkAgentA);
		msgProduct.setAddressee(Id.agentWorker);
		this.response(msgProduct);
    }

	//meta! sender="ProcessPreparing", id="50", type="Finish"
	public void processFinishProcessPreparing(MessageForm message) {
		MyMessageProduct msgProduct = (MyMessageProduct) message;
		Worker worker = msgProduct.getProduct().getCurrentWorker();

		// skoncil pracu v sklade -> move to workstation
		this.moveWorkerRequest(message, worker, worker.getCurrentWorkstation());
    }

	//meta! sender="ProcessFittingGroupA", id="52", type="Finish"
	public void processFinishProcessFittingGroupA(MessageForm message) {
		MyMessageProduct msgProduct = (MyMessageProduct) message;
		Product product = msgProduct.getProduct();
		Worker worker = product.getCurrentWorker();

		product.setCurrentWorker(null);
		worker.setCurrentWork(WorkerWork.IDLE, mySim().currentTime());
		worker.setCurrentProduct(null);

		msgProduct.setCode(Mc.requestResponseWorkAgentA);
		msgProduct.setAddressee(Id.agentWorker);
		this.response(msgProduct);
    }

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message) {
        switch (message.code()) {
        }
    }

	//meta! sender="AgentWorker", id="96", type="Request"
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

		product.setCurrentWorker(worker);
		worker.setCurrentProduct(product);

		msgProduct.setAddressee(myAgent().findAssistant(Id.processFittingGroupA));
		this.startContinualAssistant(msgProduct);
	}

	//meta! sender="AgentWorker", id="99", type="Notice"
	public void processNoticeWorkstationFreed(MessageForm message) {
		if (myAgent().group().queueSize() > 0) {
			this.startWork(message);
		}
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	public void init() {
	}

	@Override
	public void processMessage(MessageForm message) {
		switch (message.code()) {
		case Mc.requestResponseWorkAgentA:
			processRequestResponseWorkAgentA(message);
		break;

		case Mc.requestResponseFittingAssembly:
			processRequestResponseFittingAssembly(message);
		break;

		case Mc.finish:
			switch (message.sender().id()) {
			case Id.processCutting:
				processFinishProcessCutting(message);
			break;

			case Id.processPreparing:
				processFinishProcessPreparing(message);
			break;

			case Id.processFittingGroupA:
				processFinishProcessFittingGroupA(message);
			break;
			}
		break;

		case Mc.requestResponseMoveWorker:
			processRequestResponseMoveWorker(message);
		break;

		case Mc.requestResponseWorkerFreeWorkstation:
			processRequestResponseWorkerFreeWorkstation(message);
		break;

		case Mc.noticeWorkstationFreed:
			processNoticeWorkstationFreed(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

    @Override
    public AgentGroupA myAgent() {
        return (AgentGroupA) super.myAgent();
    }
}