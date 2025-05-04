package agents.agentgroupc.continualassistants;

import OSPABA.*;
import config.Constants;
import entity.product.Product;
import entity.product.ProductActivity;
import entity.worker.Worker;
import entity.worker.WorkerWork;
import generator.SeedGenerator;
import generator.continuos.ContinuosUniformGenerator;
import simulation.*;
import agents.agentgroupc.*;
import simulation.custommessage.MyMessageProduct;

//meta! id="94"
public class ProcessFittingGroupC extends OSPABA.Process {
	private ContinuosUniformGenerator fittingAssemblyGenerator;
	public ProcessFittingGroupC(int id, Simulation mySim, CommonAgent myAgent) {
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

	//meta! sender="AgentGroupC", id="95", type="Start"
	public void processStart(MessageForm message) {
		// zacne fittovat
		MyMessageProduct productMessage = (MyMessageProduct) message;
		Product product = productMessage.getProduct();
		Worker worker = product.getWorker();

		if (Constants.DEBUG_PROCESS)
			System.out.printf("[%s] [%s] P. fitting C start %s\n", ((MySimulation)mySim()).workdayTime(), product.getWorker(), product);

		if (product.getProductActivity() != ProductActivity.ASSEMBLED)
			throw new IllegalStateException("Manager C product isnt assembled");

		product.setProductActivity(ProductActivity.FITTING);
		product.setStartFittingAssemblyTime(mySim().currentTime());

		worker.setCurrentWork(WorkerWork.FITTING);
		worker.setCurrentProduct(product);

		double offset = this.fittingAssemblyGenerator.sample();
		productMessage.setCode(Mc.holdFitting);
		this.hold(offset, productMessage);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message) {
		switch (message.code()) {
			case Mc.holdFitting:
				MyMessageProduct productMessage = (MyMessageProduct) message;
				Product product = productMessage.getProduct();

				if (Constants.DEBUG_PROCESS)
					System.out.printf("[%s] [%s] P. fitting C finished %s\n", ((MySimulation)mySim()).workdayTime(), product.getWorker(), product);

				product.setProductAsDone(mySim().currentTime());

				// finish fitting time
				product.setFinishFittingAssemblyTime(mySim().currentTime());
				product.validateTimes();

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
	public AgentGroupC myAgent() {
		return (AgentGroupC)super.myAgent();
	}

}