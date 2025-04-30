package agents.agentworker;

import OSPABA.*;
import config.Group;
import simulation.*;



//meta! id="6"
public class AgentWorker extends OSPABA.Agent {
	private Group group;
    public AgentWorker(int id, Simulation mySim, Agent parent) {
        super(id, mySim, parent);
        init();

		this.group = new Group(0, null);
    }

	public Group group() {
		return group;
	}

    @Override
    public void prepareReplication() {
        super.prepareReplication();
        // Setup component for the next replication
    }

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init() {
		new ManagerWorker(Id.managerWorker, mySim(), this);
		addOwnMessage(Mc.requestResponseOrderFreeWorkstation);
		addOwnMessage(Mc.requestResponseWorkerFreeWorkstation);
		addOwnMessage(Mc.requestResponseFittingAssembly);
		addOwnMessage(Mc.requestResponseMoveWorker);
		addOwnMessage(Mc.noticeAgentGroupAFreed);
		addOwnMessage(Mc.requestResponseWorkOnOrderWorkplace);
		addOwnMessage(Mc.requestResponseWorkAgentA);
		addOwnMessage(Mc.requestResponseWorkAgentB);
		addOwnMessage(Mc.requestResponseWorkAgentC);
		addOwnMessage(Mc.noticeAgentGroupCFreed);
	}
	//meta! tag="end"
}