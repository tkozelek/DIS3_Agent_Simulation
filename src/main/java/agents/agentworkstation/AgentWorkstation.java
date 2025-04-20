package agents.agentworkstation;

import OSPABA.Agent;
import OSPABA.Simulation;
import simulation.Id;
import simulation.Mc;


//meta! id="4"
public class AgentWorkstation extends OSPABA.Agent {
    public AgentWorkstation(int id, Simulation mySim, Agent parent) {
        super(id, mySim, parent);
        init();
    }

    @Override
    public void prepareReplication() {
        super.prepareReplication();
        // Setup component for the next replication
    }

    //meta! userInfo="Generated code: do not modify", tag="begin"
    private void init() {
        new ManagerWorkstation(Id.managerWorkstation, mySim(), this);
        addOwnMessage(Mc.requestResponseFreeWorkstation);
    }
    //meta! tag="end"
}