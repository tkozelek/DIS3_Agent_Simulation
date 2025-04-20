package agents.agentworkstation;

import OSPABA.*;
import simulation.*;

//meta! id="4"
public class ManagerWorkstation extends OSPABA.Manager
{
	public ManagerWorkstation(int id, Simulation mySim, Agent myAgent)
	{
		super(id, mySim, myAgent);
		init();
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication

		if (petriNet() != null)
		{
			petriNet().clear();
		}
	}

	//meta! sender="AgentWorkplace", id="37", type="Request"
	public void processRequestResponseFreeWorkstation(MessageForm message)
	{
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
		}
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	public void init()
	{
	}

	@Override
	public void processMessage(MessageForm message)
	{
		switch (message.code())
		{
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
	public AgentWorkstation myAgent()
	{
		return (AgentWorkstation)super.myAgent();
	}

}
