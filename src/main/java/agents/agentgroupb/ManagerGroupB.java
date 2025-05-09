package agents.agentgroupb;

import OSPABA.Agent;
import OSPABA.MessageForm;
import OSPABA.Simulation;
import config.Constants;
import entity.ILocation;
import entity.product.Product;
import entity.worker.Worker;
import simulation.Id;
import simulation.Mc;
import simulation.MyMessage;

//meta! id="61"
public class ManagerGroupB extends OSPABA.Manager {
    public ManagerGroupB(int id, Simulation mySim, Agent myAgent) {
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

	//meta! sender="AgentWorker", id="63", type="Request"
	public void processRequestResponseWorkAgentB(MessageForm message) {
        if (Constants.DEBUG_MANAGER)
            System.out.format("[%s] M. B received order", mySim().currentTime());

        myAgent().group().addQueue((MyMessage) message, mySim().currentTime());

        this.tryStartWorkOnOrder();
    }

    private void tryStartWorkOnOrder() {
        Worker worker = myAgent().group().getFreeWorker();

        if (worker == null) return;

        MyMessage messageProduct = myAgent().group().pollQueue(mySim().currentTime());
        Product product = messageProduct.getProduct();

        worker.setCurrentProduct(product);

        product.setWorker(worker);

        if (worker.getLocation() != product.getWorkstation()) {
            this.moveWorkerRequest(messageProduct, worker, product.getWorkstation());
        } else {
            this.startProcess(messageProduct, product, Id.processAssembly);
        }
    }

    private void moveWorkerRequest(MessageForm message, Worker worker, ILocation location) {
        if (worker.getLocation() == location)
            throw new IllegalStateException("Worker and location is the same");

        MyMessage msgMove = new MyMessage(message);
        msgMove.setTargetLocation(location);
        msgMove.setWorker(worker);

        msgMove.setCode(Mc.requestResponseMoveWorker);
        msgMove.setAddressee(Id.agentWorker);
        this.request(msgMove);
    }

    private void startProcess(MessageForm message, Product product, int processId) {
        MyMessage msgProduct = new MyMessage(message);
        msgProduct.setProduct(product);
        msgProduct.setAddressee(myAgent().findAssistant(processId));
        startContinualAssistant(msgProduct);
    }

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message) {
        switch (message.code()) {
        }
    }

	//meta! sender="AgentWorker", id="66", type="Response"
	public void processRequestResponseMoveWorker(MessageForm message) {
        MyMessage msg = (MyMessage) message;
        this.startProcess(message, msg.getWorker().getCurrentProduct(), Id.processAssembly);
    }

	//meta! sender="ProcessAssembly", id="79", type="Finish"
	public void processFinish(MessageForm message) {
        MyMessage msg = (MyMessage) message;

        message.setCode(Mc.requestResponseWorkAgentB);
        message.setAddressee(Id.agentWorker);
        this.response(message);

        if (this.myAgent().group().queueSize() > 0)
            this.tryStartWorkOnOrder();
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
		case Mc.requestResponseWorkAgentB:
			processRequestResponseWorkAgentB(message);
		break;

		case Mc.requestResponseMoveWorker:
			processRequestResponseMoveWorker(message);
		break;

		case Mc.finish:
			processFinish(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

    @Override
    public AgentGroupB myAgent() {
        return (AgentGroupB) super.myAgent();
    }

}