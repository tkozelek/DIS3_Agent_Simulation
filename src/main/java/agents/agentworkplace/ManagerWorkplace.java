package agents.agentworkplace;

import OSPABA.Agent;
import OSPABA.MessageForm;
import OSPABA.Simulation;
import simulation.Id;
import simulation.Mc;

//meta! id="3"
public class ManagerWorkplace extends OSPABA.Manager {
    public ManagerWorkplace(int id, Simulation mySim, Agent myAgent) {
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

	//meta! sender="AgentBoss", id="24", type="Request"
	public void processRequestResponseOrderArrived(MessageForm message) {
        // nova objednavka, poslal workboss, posli workerovi
        message.setCode(Mc.requestResponseWorkOnOrderWorkplace);
		message.setAddressee(Id.agentWorker);
		this.request(message);
    }

	//meta! sender="AgentWorker", id="40", type="Response"
	public void processRequestResponseWorkOnOrderWorkplace(MessageForm message) {
    	// objednavka dokoncena, posli vyssie na vymazanie zo systemu
		message.setCode(Mc.requestResponseWorkOnOrderWorkplace);
		message.setAddressee(Id.agentBoss);
		this.response(message);
	}

	//meta! sender="AgentWorker", id="45", type="Request"
	public void processRequestResponseOrderFreeWorkstation(MessageForm message) {
		// worker A vyziadal workstation pre objednavku
		message.setCode(Mc.requestResponseFreeWorkstation);
		message.setAddressee(Id.agentWorkstation);
		this.request(message);
    }

	//meta! sender="AgentBoss", id="11", type="Notice"
	public void processNoticeInitWorkplace(MessageForm message) {
    }

	//meta! sender="AgentMove", id="38", type="Response"
	public void processRequestResponseMoveWorkerAgentMove(MessageForm message) {
		// agent bol presunuty posli response dalej agentovi workerovi
		message.setCode(Mc.requestResponseMoveWorker);
		message.setAddressee(Id.agentWorker);
		this.response(message);
    }

	//meta! sender="AgentWorkstation", id="37", type="Response"
	public void processRequestResponseFreeWorkstation(MessageForm message) {
		// free workstation bol prideleny
		message.setCode(Mc.requestResponseOrderFreeWorkstation);
		message.setAddressee(Id.agentWorker);
		this.response(message);
    }

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message) {
        switch (message.code()) {
        }
    }

	//meta! sender="AgentWorker", id="75", type="Request"
	public void processRequestResponseMoveWorkerAgentWorker(MessageForm message) {
		// agent dal request na move
		message.setCode(Mc.requestResponseMoveWorker);
		message.setAddressee(Id.agentMove);
		this.request(message);
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	public void init() {
	}

	@Override
	public void processMessage(MessageForm message) {
		switch (message.code()) {
		case Mc.noticeInitWorkplace:
			processNoticeInitWorkplace(message);
		break;

		case Mc.requestResponseWorkOnOrderWorkplace:
			processRequestResponseWorkOnOrderWorkplace(message);
		break;

		case Mc.requestResponseOrderFreeWorkstation:
			processRequestResponseOrderFreeWorkstation(message);
		break;

		case Mc.requestResponseMoveWorker:
			switch (message.sender().id()) {
			case Id.agentMove:
				processRequestResponseMoveWorkerAgentMove(message);
			break;

			case Id.agentWorker:
				processRequestResponseMoveWorkerAgentWorker(message);
			break;
			}
		break;

		case Mc.requestResponseOrderArrived:
			processRequestResponseOrderArrived(message);
		break;

		case Mc.requestResponseFreeWorkstation:
			processRequestResponseFreeWorkstation(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

    @Override
    public AgentWorkplace myAgent() {
        return (AgentWorkplace) super.myAgent();
    }

}