package agents.agentworkplace;

import OSPABA.*;
import simulation.*;



//meta! id="3"
public class AgentWorkplace extends OSPABA.Agent {
    public AgentWorkplace(int id, Simulation mySim, Agent parent) {
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
		new ManagerWorkplace(Id.managerWorkplace, mySim(), this);
		addOwnMessage(Mc.requestResponseOrderArrived);
		addOwnMessage(Mc.requestResponseMoveWorker);
		addOwnMessage(Mc.requestResponseWorkOnOrderWorkplace);
	}
	//meta! tag="end"
}