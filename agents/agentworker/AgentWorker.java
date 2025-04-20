package agents.agentworker;

import OSPABA.*;
import simulation.*;

//meta! id="6"
public class AgentWorker extends OSPABA.Agent
{
	public AgentWorker(int id, Simulation mySim, Agent parent)
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
		new ManagerWorker(Id.managerWorker, mySim(), this);
		addOwnMessage(Mc.requestResponseWorkerFreeWorkstation);
		addOwnMessage(Mc.requestResponseOrderFreeWorkstation);
		addOwnMessage(Mc.requestResponseWorkOnOrder);
		addOwnMessage(Mc.requestResponseMoveWorker);
		addOwnMessage(Mc.noticeInitWorker);
	}
	//meta! tag="end"
}
