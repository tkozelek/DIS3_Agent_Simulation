package agents.agentworker;

import OSPABA.*;
import simulation.*;

//meta! id="6"
public class ManagerWorker extends OSPABA.Manager
{
	public ManagerWorker(int id, Simulation mySim, Agent myAgent)
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

	//meta! sender="AgentGroupA", id="46", type="Request"
	public void processRequestResponseWorkerFreeWorkstation(MessageForm message)
	{
	}

	//meta! sender="AgentWorkplace", id="45", type="Response"
	public void processRequestResponseOrderFreeWorkstation(MessageForm message)
	{
	}

	//meta! sender="AgentGroupA", id="39", type="Response"
	public void processRequestResponseWorkOnOrderAgentGroupA(MessageForm message)
	{
	}

	//meta! sender="AgentWorkplace", id="40", type="Request"
	public void processRequestResponseWorkOnOrderAgentWorkplace(MessageForm message)
	{
	}

	//meta! sender="AgentGroupA", id="53", type="Request"
	public void processRequestResponseMoveWorker(MessageForm message)
	{
	}

	//meta! sender="AgentWorkplace", id="13", type="Notice"
	public void processNoticeInitWorker(MessageForm message)
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
		case Mc.requestResponseWorkerFreeWorkstation:
			processRequestResponseWorkerFreeWorkstation(message);
		break;

		case Mc.noticeInitWorker:
			processNoticeInitWorker(message);
		break;

		case Mc.requestResponseOrderFreeWorkstation:
			processRequestResponseOrderFreeWorkstation(message);
		break;

		case Mc.requestResponseWorkOnOrder:
			switch (message.sender().id())
			{
			case Id.agentGroupA:
				processRequestResponseWorkOnOrderAgentGroupA(message);
			break;

			case Id.agentWorkplace:
				processRequestResponseWorkOnOrderAgentWorkplace(message);
			break;
			}
		break;

		case Mc.requestResponseMoveWorker:
			processRequestResponseMoveWorker(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public AgentWorker myAgent()
	{
		return (AgentWorker)super.myAgent();
	}

}
