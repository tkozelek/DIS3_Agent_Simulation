package agents.agentgroupa.continualassistants;

import OSPABA.*;
import config.Constants;
import entity.product.Product;
import entity.product.ProductActivity;
import entity.worker.Worker;
import entity.worker.WorkerWork;
import generator.SeedGenerator;
import generator.continuos.ContinuosUniformGenerator;
import simulation.*;
import agents.agentgroupa.*;
import simulation.custommessage.MyMessageProduct;

//meta! id="51"
public class ProcessFittingGroupA extends OSPABA.Process {
	private ContinuosUniformGenerator fittingAssemblyGenerator;
	public ProcessFittingGroupA(int id, Simulation mySim, CommonAgent myAgent) {
		super(id, mySim, myAgent);

		SeedGenerator seedGen = ((MySimulation)mySim()).getSeedGenerator();
		int times = 60;
		this.fittingAssemblyGenerator = new ContinuosUniformGenerator(15 * times, 25 * times, seedGen);
	}

	@Override
	public void prepareReplication() {
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="AgentGroupA", id="52", type="Start"
	public void processStart(MessageForm message) {
		// zacne fittovat
		if (Constants.DEBUG_PROCESS)
			System.out.printf("[%s] P. fitting A start\n", ((MySimulation)mySim()).workdayTime());

		MyMessageProduct productMessage = (MyMessageProduct) message;
		Product product = productMessage.getProduct();
		Worker worker = product.getWorker();

		product.setProductActivity(ProductActivity.FITTING);
		product.setStartFittingAssemblyTime(mySim().currentTime());

		worker.setCurrentWork(WorkerWork.FITTING, mySim().currentTime());
		worker.setCurrentProduct(product);

		double offset = this.fittingAssemblyGenerator.sample();
		productMessage.setCode(Mc.holdFitting);
		this.hold(offset, productMessage);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message) {
		switch (message.code()) {
			case Mc.holdFitting:
				if (Constants.DEBUG_PROCESS)
					System.out.printf("[%s] P. fitting A finished\n", ((MySimulation)mySim()).workdayTime());

				MyMessageProduct productMessage = (MyMessageProduct) message;
				Product product = productMessage.getProduct();

				product.setProductAsDone(mySim().currentTime());

				// finish fitting time
				product.setFinishFittingAssemblyTime(mySim().currentTime());

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
		return (AgentGroupA)super.myAgent();
	}

}