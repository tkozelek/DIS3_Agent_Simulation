package agents.agentokolie;

import OSPABA.*;
import simulation.*;
import agents.agentokolie.continualassistants.*;

//meta! id="1"
public class AgentOkolie extends OSPABA.Agent
{
	public AgentOkolie(int id, Simulation mySim, Agent parent)
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
	private void init()
	{
		new ManagerOkolie(Id.managerOkolie, mySim(), this);
		new SchedulerOrderArrival(Id.schedulerOrderArrival, mySim(), this);
		addOwnMessage(Mc.requestResponseOrderArrival);
		addOwnMessage(Mc.noticeInitAgentOkolie);
		addOwnMessage(Mc.noticeOrderArrival);
	}
	//meta! tag="end"
}
