package agents.agentgroupc;

import OSPABA.*;
import simulation.*;



//meta! id="67"
public class AgentGroupC extends OSPABA.Agent {
	public AgentGroupC(int id, Simulation mySim, Agent parent) {
		super(id, mySim, parent);
		init();
	}

	@Override
	public void prepareReplication() {
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init() {
		new ManagerGroupC(Id.managerGroupC, mySim(), this);
		addOwnMessage(Mc.requestResponseMoveWorker);
		addOwnMessage(Mc.requestResponseWorkAgentC);
	}
	//meta! tag="end"
}