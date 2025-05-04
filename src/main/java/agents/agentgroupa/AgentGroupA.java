package agents.agentgroupa;

import OSPABA.Agent;
import OSPABA.Simulation;
import agents.agentgroupa.continualassistants.ProcessCutting;
import agents.agentgroupa.continualassistants.ProcessFittingGroupA;
import agents.agentgroupa.continualassistants.ProcessPreparing;
import config.Group;
import entity.worker.WorkerGroup;
import simulation.Id;
import simulation.Mc;
import simulation.MySimulation;


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

        this.group = new Group(sim.getWorkerCountForGroup(WorkerGroup.GROUP_A), WorkerGroup.GROUP_A, mySim());
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
    private void init() {
        new ManagerGroupA(Id.managerGroupA, mySim(), this);
        new ProcessPreparing(Id.processPreparing, mySim(), this);
        new ProcessFittingGroupA(Id.processFittingGroupA, mySim(), this);
        new ProcessCutting(Id.processCutting, mySim(), this);
        addOwnMessage(Mc.noticeWorkstationFreed);
        addOwnMessage(Mc.requestResponseMoveWorker);
        addOwnMessage(Mc.requestResponseWorkAgentA);
        addOwnMessage(Mc.requestResponseTryFitGroupA);
    }
    //meta! tag="end"
}