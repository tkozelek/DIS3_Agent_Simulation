package agents.agentgroupa;

import OSPABA.*;
import config.Group;
import entity.worker.WorkerGroup;
import simulation.*;
import agents.agentgroupa.continualassistants.*;



//meta! id="7"
public class AgentGroupA extends OSPABA.Agent {
	private final Group group;

	public AgentGroupA(int id, Simulation mySim, Agent parent) {
		super(id, mySim, parent);
		init();

		this.addOwnMessage(Mc.holdCutting);
		this.addOwnMessage(Mc.holdFitting);
		this.addOwnMessage(Mc.holdPrepareMaterial);

		MySimulation sim = (MySimulation) mySim;

		this.group = new Group(sim.getWorkerCountForGroup(WorkerGroup.GROUP_A), WorkerGroup.GROUP_A);
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
		new ManagerGroupA(Id.managerGroupA, mySim(), this);
		new ProcessCutting(Id.processCutting, mySim(), this);
		new ProcessPreparing(Id.processPreparing, mySim(), this);
		new ProcessFittingGroupA(Id.processFittingGroupA, mySim(), this);
		addOwnMessage(Mc.noticeWorkstationFreed);
		addOwnMessage(Mc.requestResponseMoveWorker);
		addOwnMessage(Mc.requestResponseWorkAgentA);
		addOwnMessage(Mc.noticeTryFit);
		addOwnMessage(Mc.requestResponseWorkerAFree);
	}
	//meta! tag="end"
}