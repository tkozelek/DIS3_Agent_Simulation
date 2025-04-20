package agents.agentokolie;

import OSPABA.*;
import simulation.*;

//meta! id="1"
public class ManagerOkolie extends OSPABA.Manager
{
	public ManagerOkolie(int id, Simulation mySim, Agent myAgent)
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

	//meta! sender="AgentBoss", id="21", type="Response"
	public void processRequestResponseOrderArrival(MessageForm message)
	{
	}

	//meta! sender="AgentBoss", id="10", type="Notice"
	public void processNoticeInitAgentOkolie(MessageForm message)
	{
	}

	//meta! sender="SchedulerOrderArrival", id="18", type="Finish"
	public void processFinish(MessageForm message)
	{
	}

	//meta! sender="SchedulerOrderArrival", id="20", type="Notice"
	public void processNoticeOrderArrival(MessageForm message)
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
		case Mc.noticeOrderArrival:
			processNoticeOrderArrival(message);
		break;

		case Mc.requestResponseOrderArrival:
			processRequestResponseOrderArrival(message);
		break;

		case Mc.finish:
			processFinish(message);
		break;

		case Mc.noticeInitAgentOkolie:
			processNoticeInitAgentOkolie(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public AgentOkolie myAgent()
	{
		return (AgentOkolie)super.myAgent();
	}

}
