package agents.agentboss;

import OSPABA.*;
import simulation.*;



//meta! id="2"
public class AgentBoss extends OSPABA.Agent {
    public AgentBoss(int id, Simulation mySim, Agent parent) {
        super(id, mySim, parent);
        init();
    }

    public void initOkolie() {
        MyMessage initOkolie = new MyMessage(mySim());
        initOkolie.setAddressee(Id.agentOkolie);
        initOkolie.setCode(Mc.noticeInitAgentOkolie);
        this.myManager().notice(initOkolie);
    }

    @Override
    public void prepareReplication() {
        super.prepareReplication();
        // Setup component for the next replication
    }

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init() {
		new ManagerBoss(Id.managerBoss, mySim(), this);
		addOwnMessage(Mc.requestResponseOrderArrival);
		addOwnMessage(Mc.requestResponseOrderArrived);
	}
	//meta! tag="end"
}