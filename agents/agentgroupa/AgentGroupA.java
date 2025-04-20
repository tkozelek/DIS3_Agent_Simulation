package agents.agentgroupa;

import OSPABA.*;
import simulation.*;
import agents.agentgroupa.continualassistants.*;

//meta! id="7"
public class AgentGroupA extends OSPABA.Agent
{
	public AgentGroupA(int id, Simulation mySim, Agent parent)
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
		new ManagerGroupA(Id.managerGroupA, mySim(), this);
		new ProcessCutting(Id.processCutting, mySim(), this);
		new ProcessFitting(Id.processFitting, mySim(), this);
		new ProcessPreparing(Id.processPreparing, mySim(), this);
		addOwnMessage(Mc.requestResponseWorkerFreeWorkstation);
		addOwnMessage(Mc.requestResponseWorkOnOrder);
		addOwnMessage(Mc.requestResponseMoveWorker);
	}
	//meta! tag="end"
}
