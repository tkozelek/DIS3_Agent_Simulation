package agents.agentgroupb;

import OSPABA.*;
import simulation.Mc;

//meta! id="61"
public class ManagerGroupB extends OSPABA.Manager
{
	public ManagerGroupB(int id, Simulation mySim, Agent myAgent)
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

	//meta! sender="AgentWorker", id="63", type="Request"
	public void processRequestResponseWorkAgentB(MessageForm message)
	{
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
		}
	}

	//meta! sender="AgentWorker", id="66", type="Response"
	public void processRequestResponseMoveWorker(MessageForm message) {
	}

	//meta! sender="ProcessAssembly", id="79", type="Finish"
	public void processFinish(MessageForm message) {
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	public void init() {
	}

	@Override
	public void processMessage(MessageForm message) {
		switch (message.code()) {
		case Mc.requestResponseWorkAgentB:
			processRequestResponseWorkAgentB(message);
		break;

		case Mc.requestResponseMoveWorker:
			processRequestResponseMoveWorker(message);
		break;

		case Mc.finish:
			processFinish(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public AgentGroupB myAgent()
	{
		return (AgentGroupB)super.myAgent();
	}

}