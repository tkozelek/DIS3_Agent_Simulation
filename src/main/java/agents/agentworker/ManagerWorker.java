package agents.agentworker;

import OSPABA.Agent;
import OSPABA.MessageForm;
import OSPABA.Simulation;

import config.Constants;
import entity.product.Product;
import entity.product.ProductActivity;
import entity.product.ProductType;
import entity.worker.Worker;
import entity.worker.WorkerGroup;
import entity.workstation.Workstation;
import simulation.Id;
import simulation.Mc;
import simulation.MyMessage;
import simulation.MySimulation;
import simulation.custommessage.MyMessageMove;
import simulation.custommessage.MyMessageProduct;


//meta! id="6"
public class ManagerWorker extends OSPABA.Manager {
    public ManagerWorker(int id, Simulation mySim, Agent myAgent) {
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

	//meta! sender="AgentWorkplace", id="40", type="Request"
	public void processRequestResponseWorkOnOrderWorkplace(MessageForm message) {
        // poslal workplace, nova objednavka
        // posleme workerovi A
		if (Constants.DEBUG_MANAGER)
			System.out.printf("[%s] M. worker prisla objednavka\n", ((MySimulation)mySim()).workdayTime());

		message.setCode(Mc.requestResponseWorkAgentA);
		message.setAddressee(Id.agentGroupA);
		this.request(message);
    }

	//meta! sender="AgentGroupA", id="39", type="Response"
	public void processRequestResponseWorkAgentA(MessageForm message) {
        // worker A dokoncil pracu
		// request worker C začiatok práce
		message.setCode(Mc.requestResponseWorkAgentC);
		message.setAddressee(Id.agentGroupC);
		this.request(message);
    }

	//meta! sender="AgentGroupA", id="53", type="Request"
	public void processRequestResponseMoveWorkerAgentGroupA(MessageForm message) {
		// worker dal ziadost o move
		message.setCode(Mc.requestResponseMoveWorker);
		message.setAddressee(Id.agentWorkplace);
		this.request(message);
    }

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message) {
        switch (message.code()) {
        }
    }

	//meta! sender="AgentGroupB", id="63", type="Response"
	public void processRequestResponseWorkAgentB(MessageForm message)
	{
		// worker B dokoncil pracu
		MyMessageProduct msgProduct = (MyMessageProduct) message;
		Product product = msgProduct.getProduct();
		if (product.getProductType() == ProductType.CUPBOARD) {
			// fitting assembly
			// skus A
			if (this.myAgent().group().queueSize() > 0) {
				// uz su v queue pridaj len
				this.myAgent().group().addQueue(msgProduct);
				return;
			}
			// ak nie je skus workera A
			message.setCode(Mc.noticeTryFit);
			message.setAddressee(Id.agentGroupA);
			this.request(message);
		} else {
			// finished
			product.setProductAsDone(mySim().currentTime());

			message.setCode(Mc.requestResponseWorkOnOrderWorkplace);
			message.setAddressee(Id.agentWorkplace);
			this.response(message);

			MyMessage msg = new MyMessage(mySim());

			msg.setCode(Mc.noticeWorkstationFreed);
			msg.setAddressee(Id.agentGroupA);
			this.notice(msg);
		}
	}

	//meta! sender="AgentGroupC", id="71", type="Request"
	public void processRequestResponseMoveWorkerAgentGroupC(MessageForm message) {
		// group C žiadost o move
		message.setCode(Mc.requestResponseMoveWorker);
		message.setAddressee(Id.agentWorkplace);
		this.request(message);
	}

	//meta! sender="AgentGroupB", id="66", type="Request"
	public void processRequestResponseMoveWorkerAgentGroupB(MessageForm message) {
		message.setCode(Mc.requestResponseMoveWorker);
		message.setAddressee(Id.agentWorkplace);
		this.request(message);
	}

	//meta! sender="AgentGroupC", id="72", type="Response"
	public void processRequestResponseWorkAgentC(MessageForm message) {
		// worker C dokoncil pracu
		// zacni B
		if (Constants.DEBUG_MANAGER)
			System.out.printf("[%s] M. worker C finished\n", ((MySimulation)mySim()).workdayTime());

		message.setCode(Mc.requestResponseWorkAgentB);
		message.setAddressee(Id.agentGroupB);
		this.request(message);
	}

	//meta! sender="AgentWorkplace", id="75", type="Response"
	public void processRequestResponseMoveWorkerAgentWorkplace(MessageForm message) {
		MyMessageMove msgMove = (MyMessageMove) message;
		Worker worker = msgMove.getWorker();
		int agentId = switch (worker.getGroup()) {
			case WorkerGroup.GROUP_A -> Id.agentGroupA;
			case WorkerGroup.GROUP_B -> Id.agentGroupB;
			case WorkerGroup.GROUP_C -> Id.agentGroupC;
		};
		msgMove.setAddressee(agentId);
		this.response(message);
	}

	//meta! sender="AgentGroupA", id="107", type="Notice"
	public void processNoticeProductFittedAgentGroupA(MessageForm message) {
		if (checkIfDoneOrAddQueue(message)) {
			// skus aj C
			message.setCode(Mc.noticeTryFit);
			message.setAddressee(Id.agentGroupC);
			this.notice(message);
		}
	}

	//meta! sender="AgentGroupC", id="108", type="Notice"
	public void processNoticeProductFittedAgentGroupC(MessageForm message) {
		if (checkIfDoneOrAddQueue(message)) {
			this.myAgent().group().addQueue((MyMessageProduct) message);
		}
	}

	private boolean checkIfDoneOrAddQueue(MessageForm message) {
		MyMessageProduct msgProduct = (MyMessageProduct) message;
		Product product = msgProduct.getProduct();
		if (product.getProductActivity() == ProductActivity.DONE) {
			// worker A bol volny, spraivl
			message.setCode(Mc.requestResponseWorkOnOrderWorkplace);
			message.setAddressee(Id.agentWorkplace);
			this.response(message);
		} else if (product.getProductActivity() == ProductActivity.ASSEMBLED) {
			// worker A nie je volny
			return true;
		} else {
			this.myAgent().group().addQueue(msgProduct);
		}
		return false;
	}

	//meta! sender="AgentGroupA", id="105", type="Request"
	public void processRequestResponseWorkerAFree(MessageForm message) {
		if (this.myAgent().group().queueSize() > 0) {
			MyMessageProduct msgProduct = this.myAgent().group().pollQueue();
			msgProduct.setCode(Mc.requestResponseWorkerAFree);
			msgProduct.setAddressee(Id.agentGroupA);
			this.response(msgProduct);
		}

		message.setCode(Mc.requestResponseWorkerAFree);
		message.setAddressee(Id.agentGroupA);
		this.response(message);
	}

	//meta! sender="AgentGroupC", id="106", type="Request"
	public void processRequestResponseWorkerCFree(MessageForm message) {
		if (this.myAgent().group().queueSize() > 0) {
			MyMessageProduct msgProduct = this.myAgent().group().pollQueue();
			msgProduct.setCode(Mc.requestResponseWorkerCFree);
			msgProduct.setAddressee(Id.agentGroupC);
			this.response(msgProduct);
		}
		message.setCode(Mc.requestResponseWorkerCFree);
		message.setAddressee(Id.agentGroupC);
		this.response(message);
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	public void init() {
	}

	@Override
	public void processMessage(MessageForm message) {
		switch (message.code()) {
		case Mc.requestResponseWorkerAFree:
			processRequestResponseWorkerAFree(message);
		break;

		case Mc.noticeProductFitted:
			switch (message.sender().id()) {
			case Id.agentGroupA:
				processNoticeProductFittedAgentGroupA(message);
			break;

			case Id.agentGroupC:
				processNoticeProductFittedAgentGroupC(message);
			break;
			}
		break;

		case Mc.requestResponseMoveWorker:
			switch (message.sender().id()) {
			case Id.agentWorkplace:
				processRequestResponseMoveWorkerAgentWorkplace(message);
			break;

			case Id.agentGroupC:
				processRequestResponseMoveWorkerAgentGroupC(message);
			break;

			case Id.agentGroupA:
				processRequestResponseMoveWorkerAgentGroupA(message);
			break;

			case Id.agentGroupB:
				processRequestResponseMoveWorkerAgentGroupB(message);
			break;
			}
		break;

		case Mc.requestResponseWorkAgentA:
			processRequestResponseWorkAgentA(message);
		break;

		case Mc.requestResponseWorkAgentC:
			processRequestResponseWorkAgentC(message);
		break;

		case Mc.requestResponseWorkOnOrderWorkplace:
			processRequestResponseWorkOnOrderWorkplace(message);
		break;

		case Mc.requestResponseWorkerCFree:
			processRequestResponseWorkerCFree(message);
		break;

		case Mc.requestResponseWorkAgentB:
			processRequestResponseWorkAgentB(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

    @Override
    public AgentWorker myAgent() {
        return (AgentWorker) super.myAgent();
    }

}