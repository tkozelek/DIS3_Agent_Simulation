package agents.agentgroupa.continualassistants;

import OSPABA.CommonAgent;
import OSPABA.MessageForm;
import OSPABA.Simulation;
import agents.agentgroupa.AgentGroupA;
import config.Constants;
import entity.product.ProductActivity;
import entity.worker.Worker;
import entity.worker.WorkerWork;
import generator.continuos.ContinuosTriangularGenerator;
import simulation.Mc;
import simulation.MySimulation;
import simulation.custommessage.MyMessageProduct;

//meta! id="49"
public class ProcessPreparing extends OSPABA.Process {
	private ContinuosTriangularGenerator materialPreparationGenerator;
    public ProcessPreparing(int id, Simulation mySim, CommonAgent myAgent) {
        super(id, mySim, myAgent);

		this.materialPreparationGenerator = new ContinuosTriangularGenerator(300, 900, 500, ((MySimulation)mySim()).getSeedGenerator());
    }

    @Override
    public void prepareReplication() {
        super.prepareReplication();
        // Setup component for the next replication
    }

	//meta! sender="AgentGroupA", id="50", type="Start"
	public void processStart(MessageForm message) {
		MyMessageProduct productMessage = (MyMessageProduct) message;
		productMessage.getProduct().setProductActivity(ProductActivity.PREPARING);

		if (Constants.DEBUG_PROCESS)
			System.out.printf("[%s] [%s] P. preparing start\n", ((MySimulation)mySim()).workdayTime(), productMessage.getProduct());

		Worker worker = productMessage.getProduct().getCurrentWorker();
		worker.setCurrentWork(WorkerWork.PREPARING_MATERIAL, mySim().currentTime());

		double offset = this.materialPreparationGenerator.sample();
		message.setCode(Mc.holdPrepareMaterial);
		this.hold(offset, message);
    }

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message) {
        switch (message.code()) {
			case Mc.holdPrepareMaterial:
				MyMessageProduct productMessage = (MyMessageProduct) message;
				productMessage.getProduct().setProductActivity(ProductActivity.PREPARED);

				if (Constants.DEBUG_PROCESS)
					System.out.printf("[%s] [%s] P. preparing finished\n", ((MySimulation)mySim()).workdayTime(), productMessage.getProduct());

				Worker worker = productMessage.getProduct().getCurrentWorker();
				worker.setCurrentWork(WorkerWork.IDLE, mySim().currentTime());

				this.assistantFinished(message);
        }
    }

	//meta! userInfo="Generated code: do not modify", tag="begin"
	@Override
	public void processMessage(MessageForm message) {
		switch (message.code()) {
		case Mc.start:
			processStart(message);
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