package agents.agentworker;

import OSPABA.Agent;
import OSPABA.Simulation;
import config.Group;
import simulation.Id;
import simulation.Mc;


//meta! id="6"
public class AgentWorker extends OSPABA.Agent {
    private final Group group;

    public AgentWorker(int id, Simulation mySim, Agent parent) {
        super(id, mySim, parent);
        init();

        this.group = new Group(0, null, mySim());
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
        new ManagerWorker(Id.managerWorker, mySim(), this);
        addOwnMessage(Mc.noticeAgentAFreed);
        addOwnMessage(Mc.requestResponseMoveWorker);
        addOwnMessage(Mc.requestResponseWorkOnOrderWorkplace);
        addOwnMessage(Mc.requestResponseTryFitGroupC);
        addOwnMessage(Mc.requestResponseWorkAgentA);
        addOwnMessage(Mc.requestResponseWorkAgentB);
        addOwnMessage(Mc.requestResponseTryFitGroupA);
        addOwnMessage(Mc.requestResponseWorkAgentC);
        addOwnMessage(Mc.noticeAgentCFreed);
    }
    //meta! tag="end"
}