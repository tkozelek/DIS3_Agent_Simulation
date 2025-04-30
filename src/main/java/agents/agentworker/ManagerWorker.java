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

	//meta! sender="AgentGroupA", id="46", type="Request"
	public void processRequestResponseWorkerFreeWorkstation(MessageForm message) {
		message.setCode(Mc.requestResponseOrderFreeWorkstation);
		message.setAddressee(Id.agentWorkplace);
		this.request(message);
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

	//meta! sender="AgentWorkplace", id="45", type="Response"
	public void processRequestResponseOrderFreeWorkstation(MessageForm message) {
		// message s workstationom
		message.setCode(Mc.requestResponseWorkerFreeWorkstation);
		message.setAddressee(Id.agentGroupA);
		this.response(message);
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
			// skus

		} else {
			// finished
			product.setFinishTime(mySim().currentTime());
			product.setProductActivity(ProductActivity.DONE);

			Workstation workstation = product.getWorkstation();
			workstation.setCurrentProduct(null);

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

	//meta! sender="AgentGroupA", id="89", type="Notice"
	public void processNoticeAgentGroupAFreed(MessageForm message) {
	}

	//meta! sender="AgentGroupC", id="90", type="Notice"
	public void processNoticeAgentGroupCFreed(MessageForm message) {
	}

	//meta! sender="AgentGroupA", id="96", type="Response"
	public void processRequestResponseFittingAssemblyAgentGroupA(MessageForm message) {
	}

	//meta! sender="AgentGroupC", id="97", type="Response"
	public void processRequestResponseFittingAssemblyAgentGroupC(MessageForm message) {
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	public void init() {
	}

	@Override
	public void processMessage(MessageForm message) {
		switch (message.code()) {
		case Mc.requestResponseWorkAgentB:
			processRequestResponseWorkAgentB(message);
		break;

		case Mc.requestResponseOrderFreeWorkstation:
			processRequestResponseOrderFreeWorkstation(message);
		break;

		case Mc.noticeAgentGroupCFreed:
			processNoticeAgentGroupCFreed(message);
		break;

		case Mc.requestResponseMoveWorker:
			switch (message.sender().id()) {
			case Id.agentGroupC:
				processRequestResponseMoveWorkerAgentGroupC(message);
			break;

			case Id.agentGroupA:
				processRequestResponseMoveWorkerAgentGroupA(message);
			break;

			case Id.agentGroupB:
				processRequestResponseMoveWorkerAgentGroupB(message);
			break;

			case Id.agentWorkplace:
				processRequestResponseMoveWorkerAgentWorkplace(message);
			break;
			}
		break;

		case Mc.requestResponseFittingAssembly:
			switch (message.sender().id()) {
			case Id.agentGroupA:
				processRequestResponseFittingAssemblyAgentGroupA(message);
			break;

			case Id.agentGroupC:
				processRequestResponseFittingAssemblyAgentGroupC(message);
			break;
			}
		break;

		case Mc.noticeAgentGroupAFreed:
			processNoticeAgentGroupAFreed(message);
		break;

		case Mc.requestResponseWorkOnOrderWorkplace:
			processRequestResponseWorkOnOrderWorkplace(message);
		break;

		case Mc.requestResponseWorkAgentA:
			processRequestResponseWorkAgentA(message);
		break;

		case Mc.requestResponseWorkAgentC:
			processRequestResponseWorkAgentC(message);
		break;

		case Mc.requestResponseWorkerFreeWorkstation:
			processRequestResponseWorkerFreeWorkstation(message);
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