package agents.agentmove;

import OSPABA.Agent;
import OSPABA.MessageForm;
import OSPABA.Simulation;
import entity.ILocation;
import entity.Storage;
import simulation.Id;
import simulation.Mc;
import simulation.MyMessage;

//meta! id="5"
public class ManagerMove extends OSPABA.Manager {
    public ManagerMove(int id, Simulation mySim, Agent myAgent) {
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

    //meta! sender="AgentWorkplace", id="38", type="Request"
    public void processRequestResponseMoveWorker(MessageForm message) {
        MyMessage msg = (MyMessage) message;
        ILocation target = msg.getTargetLocation();

        if (target == Storage.STORAGE || msg.getWorker().getLocation() == Storage.STORAGE) {
            msg.setAddressee(myAgent().findAssistant(Id.processAgentMoveStorage));
        } else {
            msg.setAddressee(myAgent().findAssistant(Id.processAgentMove));
        }
        this.startContinualAssistant(msg);
    }

    //meta! sender="ProcessAgentMove", id="43", type="Finish"
    public void processFinishProcessAgentMove(MessageForm message) {
        sendResponse(message);
    }

    //meta! sender="ProcessAgentMoveStorage", id="48", type="Finish"
    public void processFinishProcessAgentMoveStorage(MessageForm message) {
        sendResponse(message);
    }

    public void sendResponse(MessageForm message) {
        message.setCode(Mc.requestResponseMoveWorker);
        message.setAddressee(Id.agentWorkplace);
        response(message);
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
            case Mc.requestResponseMoveWorker:
                processRequestResponseMoveWorker(message);
                break;

            case Mc.finish:
                switch (message.sender().id()) {
                    case Id.processAgentMoveStorage:
                        processFinishProcessAgentMoveStorage(message);
                        break;

                    case Id.processAgentMove:
                        processFinishProcessAgentMove(message);
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
    public AgentMove myAgent() {
        return (AgentMove) super.myAgent();
    }

}