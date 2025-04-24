package agents.agentgroupc;

import OSPABA.*;
import simulation.*;

import java.util.Random;

//meta! id="67"
public class ManagerGroupC extends OSPABA.Manager {

	private Random randomStaining;

	public ManagerGroupC(int id, Simulation mySim, Agent myAgent) {
		super(id, mySim, myAgent);
		init();

		MySimulation sim = (MySimulation) mySim;
		this.randomStaining = new Random(sim.getSeedGenerator().sample());
	}

	@Override
	public void prepareReplication() {
		super.prepareReplication();
		// Setup component for the next replication

		if (petriNet() != null) {
			petriNet().clear();
		}
	}

	//meta! sender="AgentWorker", id="71", type="Response"
	public void processRequestResponseMoveWorker(MessageForm message) {
	}

	//meta! sender="AgentWorker", id="72", type="Request"
	public void processRequestResponseWorkAgentC(MessageForm message) {
		// žiadosť o obslhuu od agentaworkera -> A dokončené
		System.out.println(message);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message) {
		switch (message.code()) {
		}
	}

	//meta! sender="ProcessLakovanie", id="84", type="Finish"
	public void processFinishProcessLakovanie(MessageForm message) {
	}

	//meta! sender="ProcessMorenie", id="82", type="Finish"
	public void processFinishProcessMorenie(MessageForm message) {
		// je nalakovany
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	public void init() {
	}

	@Override
	public void processMessage(MessageForm message) {
		switch (message.code()) {
		case Mc.requestResponseWorkAgentC:
			processRequestResponseWorkAgentC(message);
		break;

		case Mc.requestResponseMoveWorker:
			processRequestResponseMoveWorker(message);
		break;

		case Mc.finish:
			switch (message.sender().id()) {
			case Id.processLakovanie:
				processFinishProcessLakovanie(message);
			break;

			case Id.processMorenie:
				processFinishProcessMorenie(message);
			break;
			}
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public AgentGroupC myAgent() {
		return (AgentGroupC)super.myAgent();
	}

}