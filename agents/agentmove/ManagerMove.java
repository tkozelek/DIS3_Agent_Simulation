package agents.agentmove;

import OSPABA.*;
import simulation.*;

//meta! id="5"
public class ManagerMove extends OSPABA.Manager
{
	public ManagerMove(int id, Simulation mySim, Agent myAgent)
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

	//meta! sender="AgentWorkplace", id="38", type="Request"
	public void processRequestResponseMoveWorker(MessageForm message)
	{
	}

	//meta! sender="ProcessAgentMove", id="43", type="Finish"
	public void processFinishProcessAgentMove(MessageForm message)
	{
	}

	//meta! sender="ProcessAgentMoveStorage", id="48", type="Finish"
	public void processFinishProcessAgentMoveStorage(MessageForm message)
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
		case Mc.finish:
			switch (message.sender().id())
			{
			case Id.processAgentMove:
				processFinishProcessAgentMove(message);
			break;

			case Id.processAgentMoveStorage:
				processFinishProcessAgentMoveStorage(message);
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
	public AgentMove myAgent()
	{
		return (AgentMove)super.myAgent();
	}

}
