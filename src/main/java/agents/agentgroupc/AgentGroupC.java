package agents.agentgroupc;

import OSPABA.*;
import config.Group;
import entity.worker.WorkerGroup;
import simulation.*;
import agents.agentgroupc.continualassistants.*;



//meta! id="67"
public class AgentGroupC extends OSPABA.Agent {
	private final Group group;

	public AgentGroupC(int id, Simulation mySim, Agent parent) {
		super(id, mySim, parent);
        init();

		this.addOwnMessage(Mc.holdMorenie);
		this.addOwnMessage(Mc.holdLakovanie);

		this.addOwnMessage(Mc.holdFitting);

		MySimulation sim = (MySimulation) mySim;

		this.group = new Group(sim.getWorkerCountForGroup(WorkerGroup.GROUP_C), WorkerGroup.GROUP_C);
	}

	public Group group() {
		return group;
	}

	@Override
	public void prepareReplication() {
		super.prepareReplication();
		// Setup component for the next replication

		this.group.reset();
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init() {
		new ManagerGroupC(Id.managerGroupC, mySim(), this);
		new ProcessLakovanie(Id.processLakovanie, mySim(), this);
		new ProcessFittingGroupC(Id.processFittingGroupC, mySim(), this);
		new ProcessMorenie(Id.processMorenie, mySim(), this);
		addOwnMessage(Mc.requestResponseFittingAssembly);
		addOwnMessage(Mc.requestResponseMoveWorker);
		addOwnMessage(Mc.requestResponseWorkAgentC);
	}
	//meta! tag="end"
}