package agents.agentgroupa;

import OSPABA.*;
import simulation.*;
import agents.agentgroupa.continualassistants.*;
import OSPAnimator.AnimQueue;
import config.Group;
import entity.worker.WorkerGroup;

//meta! id="7"
public class AgentGroupA extends OSPABA.Agent {
    private final Group group;

    private AnimQueue animQueue;

    public AgentGroupA(int id, Simulation mySim, Agent parent) {
        super(id, mySim, parent);
        init();

        this.addOwnMessage(Mc.holdCutting);
        this.addOwnMessage(Mc.holdFitting);
        this.addOwnMessage(Mc.holdPrepareMaterial);

        MySimulation sim = (MySimulation) mySim;

        this.group = new Group(sim.getWorkerCountForGroup(WorkerGroup.GROUP_A), WorkerGroup.GROUP_A, mySim());
    }

    public void initQueue() {
        this.animQueue = new AnimQueue(_mySim.animator(), Data.QUEUE_START, Data.QUEUE_END, 0);
        animQueue.setGap(5);
        this.animQueue.setVisible(true);
    }

    public AnimQueue getAnimQueue() {
        return animQueue;
    }

    public Group group() {
        return group;
    }

    @Override
    public void prepareReplication() {
        super.prepareReplication();
        // Setup component for the next replication

        this.group.reset(mySim());
    }

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new ManagerGroupA(Id.managerGroupA, mySim(), this);
		new ProcessFittingGroupA(Id.processFittingGroupA, mySim(), this);
		new ProcessPreparing(Id.processPreparing, mySim(), this);
		new ProcessCutting(Id.processCutting, mySim(), this);
		addOwnMessage(Mc.noticeWorkstationFreed);
		addOwnMessage(Mc.requestResponseMoveWorker);
		addOwnMessage(Mc.requestResponseWorkAgentA);
		addOwnMessage(Mc.requestResponseTryFitGroupA);
	}
	//meta! tag="end"
}