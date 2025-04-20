package agents.agentworkplace;

import OSPABA.Agent;
import OSPABA.MessageForm;
import OSPABA.Simulation;
import simulation.Id;
import simulation.Mc;

//meta! id="3"
public class ManagerWorkplace extends OSPABA.Manager {
    public ManagerWorkplace(int id, Simulation mySim, Agent myAgent) {
        super(id, mySim, myAgent);
        init();
    }

    @Override
    public void prepareReplication() {
        super.prepareReplication();
        // Setup component for the next replication

        if (petriNet() != null) {
            petriNet().clear();
        }
    }

    //meta! sender="AgentBoss", id="24", type="Request"
    public void processRequestResponseWorkOnOrderAgentBoss(MessageForm message) {
        // nova objednavka, poslal workboss, posli workerovi
        message.setCode(Mc.requestResponseWorkOnOrder);
    }

    //meta! sender="AgentWorker", id="40", type="Response"
    public void processRequestResponseWorkOnOrderAgentWorker(MessageForm message) {
    }

    //meta! sender="AgentWorker", id="45", type="Request"
    public void processRequestResponseOrderFreeWorkstation(MessageForm message) {
    }

    //meta! sender="AgentBoss", id="11", type="Notice"
    public void processNoticeInitWorkplace(MessageForm message) {
    }

    //meta! sender="AgentMove", id="38", type="Response"
    public void processRequestResponseMoveWorker(MessageForm message) {
    }

    //meta! sender="AgentWorkstation", id="37", type="Response"
    public void processRequestResponseFreeWorkstation(MessageForm message) {
    }

    //meta! userInfo="Process messages defined in code", id="0"
    public void processDefault(MessageForm message) {
        switch (message.code()) {
        }
    }

    //meta! userInfo="Generated code: do not modify", tag="begin"
    public void init() {
    }

    @Override
    public void processMessage(MessageForm message) {
        switch (message.code()) {
            case Mc.requestResponseOrderFreeWorkstation:
                processRequestResponseOrderFreeWorkstation(message);
                break;

            case Mc.noticeInitWorkplace:
                processNoticeInitWorkplace(message);
                break;

            case Mc.requestResponseFreeWorkstation:
                processRequestResponseFreeWorkstation(message);
                break;

            case Mc.requestResponseMoveWorker:
                processRequestResponseMoveWorker(message);
                break;

            case Mc.requestResponseWorkOnOrder:
                switch (message.sender().id()) {
                    case Id.agentWorker:
                        processRequestResponseWorkOnOrderAgentWorker(message);
                        break;

                    case Id.agentBoss:
                        processRequestResponseWorkOnOrderAgentBoss(message);
                        break;
                }
                break;

            default:
                processDefault(message);
                break;
        }
    }
    //meta! tag="end"

    @Override
    public AgentWorkplace myAgent() {
        return (AgentWorkplace) super.myAgent();
    }

}