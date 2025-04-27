package agents.agentworkstation;

import OSPABA.*;
import entity.workstation.Workstation;
import simulation.*;

import java.util.ArrayList;


//meta! id="4"
public class AgentWorkstation extends OSPABA.Agent {

    private ArrayList<Workstation> workstations;
    private int count;

    public AgentWorkstation(int id, Simulation mySim, Agent parent) {
        super(id, mySim, parent);
        init();
        count = ((MySimulation)mySim()).getWorkstationCount();
        workstations = new ArrayList<>();
    }

    public ArrayList<Workstation> getWorkstations() {
        return this.workstations;
    }

    public ArrayList<Workstation> getFreeWorkstations(int amount) {
        ArrayList<Workstation> freeWorkstations = new ArrayList<>();
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

        workstations.clear();
        for (int i = 0; i < count; i++) {
            workstations.add(new Workstation());
        }
    }

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init() {
		new ManagerWorkstation(Id.managerWorkstation, mySim(), this);
		addOwnMessage(Mc.requestResponseFreeWorkstation);
	}
	//meta! tag="end"
}