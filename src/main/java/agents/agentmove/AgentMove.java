package agents.agentmove;

import OSPABA.*;
import simulation.*;
import agents.agentmove.continualassistants.*;



//meta! id="5"
public class AgentMove extends Agent {
    public AgentMove(int id, Simulation mySim, Agent parent) {
        super(id, mySim, parent);
        init();
    }

    @Override
    public void prepareReplication() {
        super.prepareReplication();
        // Setup component for the next replication
    }

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new ManagerMove(Id.managerMove, mySim(), this);
		new ProcessAgentMoveStorage(Id.processAgentMoveStorage, mySim(), this);
		new ProcessAgentMove(Id.processAgentMove, mySim(), this);
		addOwnMessage(Mc.requestResponseMoveWorker);
	}
	//meta! tag="end"
}