package agents.agentgroupb;

import OSPABA.*;
import simulation.*;
import agents.agentgroupb.continualassistants.*;



//meta! id="61"
public class AgentGroupB extends OSPABA.Agent
{
	public AgentGroupB(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		init();
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init() {
		new ManagerGroupB(Id.managerGroupB, mySim(), this);
		new ProcessAssembly(Id.processAssembly, mySim(), this);
		addOwnMessage(Mc.requestResponseMoveWorker);
		addOwnMessage(Mc.requestResponseWorkAgentB);
	}
	//meta! tag="end"
}