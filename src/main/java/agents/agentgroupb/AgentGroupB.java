package agents.agentgroupb;

import OSPABA.Agent;
import OSPABA.Simulation;
import agents.agentgroupb.continualassistants.ProcessAssembly;
import config.Group;
import entity.worker.WorkerGroup;
import simulation.Id;
import simulation.Mc;
import simulation.MySimulation;



//meta! id="61"
public class AgentGroupB extends OSPABA.Agent
{
	private final Group group;
	public AgentGroupB(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		init();

		this.addOwnMessage(Mc.holdAssembly);

		MySimulation sim = (MySimulation)mySim;
		this.group = new Group(sim.getWorkerCountForGroup(WorkerGroup.GROUP_B), WorkerGroup.GROUP_B);
	}

	public Group group() {
		return group;
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication

		this.group.reset();
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init() {
		new ManagerGroupB(Id.managerGroupB, mySim(), this);
		new ProcessAssembly(Id.processAssembly, mySim(), this);
		addOwnMessage(Mc.requestResponseMoveWorker);
		addOwnMessage(Mc.requestResponseWorkAgentB);
	}
	//meta! tag="end"
}