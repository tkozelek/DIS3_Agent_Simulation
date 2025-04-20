package agents.agentgroupa;

import OSPABA.Agent;
import OSPABA.MessageForm;
import OSPABA.Simulation;
import entity.order.Order;
import simulation.Id;
import simulation.Mc;
import simulation.custommessage.MyMessageOrder;

//meta! id="7"
public class ManagerGroupA extends OSPABA.Manager {
    public ManagerGroupA(int id, Simulation mySim, Agent myAgent) {
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

	//meta! sender="AgentWorker", id="46", type="Response"
	public void processRequestResponseWorkerFreeWorkstation(MessageForm message) {
    }

	//meta! sender="AgentWorker", id="39", type="Request"
	public void processRequestResponseWorkOnOrder(MessageForm message) {
		// prisla objednavka
		MyMessageOrder orderMessage = (MyMessageOrder) message;
		Order order = orderMessage.getOrder();
		// vlozime do queue
		order.getProducts().forEach(product -> myAgent().addToQueue(product));
    }

	//meta! sender="AgentWorker", id="53", type="Response"
	public void processRequestResponseMoveWorker(MessageForm message) {
    }

	//meta! sender="ProcessCutting", id="27", type="Finish"
	public void processFinishProcessCutting(MessageForm message) {
    }

	//meta! sender="ProcessPreparing", id="50", type="Finish"
	public void processFinishProcessPreparing(MessageForm message) {
    }

	//meta! sender="ProcessFitting", id="52", type="Finish"
	public void processFinishProcessFitting(MessageForm message) {
    }

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message) {
        switch (message.code()) {
        }
    }

	//meta! userInfo="Generated code: do not modify", tag="begin"
	public void init()
	{
	}

	@Override
	public void processMessage(MessageForm message)
	{
		switch (message.code())
		{
		case Mc.requestResponseWorkerFreeWorkstation:
			processRequestResponseWorkerFreeWorkstation(message);
		break;

		case Mc.requestResponseWorkOnOrder:
			processRequestResponseWorkOnOrder(message);
		break;

		case Mc.requestResponseMoveWorker:
			processRequestResponseMoveWorker(message);
		break;

		case Mc.finish:
			switch (message.sender().id())
			{
			case Id.processPreparing:
				processFinishProcessPreparing(message);
			break;

			case Id.processCutting:
				processFinishProcessCutting(message);
			break;

			case Id.processFitting:
				processFinishProcessFitting(message);
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
    public AgentGroupA myAgent() {
        return (AgentGroupA) super.myAgent();
    }

}