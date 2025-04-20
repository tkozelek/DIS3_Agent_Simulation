package agents.agentboss;

import OSPABA.*;
import simulation.*;

//meta! id="2"
public class ManagerBoss extends OSPABA.Manager
{
	public ManagerBoss(int id, Simulation mySim, Agent myAgent)
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

	//meta! sender="AgentWorkplace", id="24", type="Response"
	public void processRequestResponseWorkOnOrder(MessageForm message)
	{
	}

	//meta! sender="AgentOkolie", id="21", type="Request"
	public void processRequestResponseOrderArrival(MessageForm message)
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
		case Mc.requestResponseOrderArrival:
			processRequestResponseOrderArrival(message);
		break;

		case Mc.requestResponseWorkOnOrder:
			processRequestResponseWorkOnOrder(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public AgentBoss myAgent()
	{
		return (AgentBoss)super.myAgent();
	}

}
