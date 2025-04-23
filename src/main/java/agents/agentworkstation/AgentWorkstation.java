package agents.agentworkstation;

import OSPABA.*;
import simulation.*;

import entity.workstation.Workstation;

import java.util.ArrayList;
import java.util.List;


//meta! id="4"
public class AgentWorkstation extends OSPABA.Agent {

    private Workstation[] workstations;

    public AgentWorkstation(int id, Simulation mySim, Agent parent) {
        super(id, mySim, parent);
        init();
        int count = ((MySimulation)mySim()).getWorkstationCount();
        workstations = new Workstation[count];
    }

    public List<Workstation> getFreeWorkstations(int amount) {
        List<Workstation> freeWorkstations = new ArrayList<>();
        for (Workstation w : workstations) {
            if (w.getCurrentOrder() == null)
                freeWorkstations.add(w);
            if (freeWorkstations.size() >= amount)
                break;
        }
        return freeWorkstations;
    }

    @Override
    public void prepareReplication() {
        super.prepareReplication();
        // Setup component for the next replication

        for (int i = 0; i < workstations.length; i++) {
            workstations[i] = new Workstation();
        }
    }

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init() {
		new ManagerWorkstation(Id.managerWorkstation, mySim(), this);
		addOwnMessage(Mc.requestResponseFreeWorkstation);
	}

    public Workstation[] getWorkstations() {
        return workstations;
    }
    //meta! tag="end"
}