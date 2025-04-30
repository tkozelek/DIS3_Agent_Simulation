package agents.agentgroupb.continualassistants;

import OSPABA.*;
import config.Constants;
import entity.product.Product;
import entity.product.ProductActivity;
import entity.product.ProductType;
import entity.worker.Worker;
import entity.worker.WorkerWork;
import generator.continuos.ContinuosUniformGenerator;
import simulation.*;
import agents.agentgroupb.*;
import simulation.custommessage.MyMessageProduct;

//meta! id="78"
public class ProcessAssembly extends OSPABA.Process {

	private ContinuosUniformGenerator assemblyTableGenerator;
	private ContinuosUniformGenerator assemblyChairGenerator;
	private ContinuosUniformGenerator assemblyCupboardGenerator;


	public ProcessAssembly(int id, Simulation mySim, CommonAgent myAgent) {
		super(id, mySim, myAgent);

		int times = 60;
		MySimulation sim = (MySimulation) mySim;

		this.assemblyTableGenerator = new ContinuosUniformGenerator(30 * times, 60 * times, sim.getSeedGenerator());
		this.assemblyChairGenerator = new ContinuosUniformGenerator(14 * times, 24 * times, sim.getSeedGenerator());
		this.assemblyCupboardGenerator = new ContinuosUniformGenerator(35 * times, 75 * times, sim.getSeedGenerator());
	}

	public Double getSampleBasedOnProductType(ProductType type) {
		return switch (type) {
			case ProductType.CHAIR -> this.assemblyTableGenerator.sample();
			case ProductType.TABLE -> this.assemblyChairGenerator.sample();
			case ProductType.CUPBOARD -> this.assemblyCupboardGenerator.sample();
			default -> throw new IllegalStateException("Unexpected value: " + type);
		};
	}

	@Override
	public void prepareReplication() {
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="AgentGroupB", id="79", type="Start"
	public void processStart(MessageForm message) {
		MyMessageProduct productMessage = (MyMessageProduct) message;
		Product product = productMessage.getProduct();

		if (Constants.DEBUG_PROCESS)
			System.out.printf("[%s] [%s] P. assembling start %s\n", ((MySimulation)mySim()).workdayTime(), product.getWorker(), product);

		if (product.getProductActivity() != ProductActivity.PAINTED && product.getProductActivity() != ProductActivity.STAINED)
			throw new IllegalStateException("Product activity not painted or stained");

		product.setProductActivity(ProductActivity.ASSEMBLING);
		product.setStartAssemblyTime(mySim().currentTime());

		Worker worker = product.getWorker();
		worker.setCurrentWork(WorkerWork.ASSEMBLING, mySim().currentTime());

		double offset = this.getSampleBasedOnProductType(product.getProductType());
		productMessage.setCode(Mc.holdAssembly);
		this.hold(offset, productMessage);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message) {
		switch (message.code()) {
			case Mc.holdAssembly:
				MyMessageProduct productMessage = (MyMessageProduct) message;
				Product product = productMessage.getProduct();

				if (Constants.DEBUG_PROCESS)
					System.out.printf("[%s] [%s] P. assembling finished %s\n", ((MySimulation)mySim()).workdayTime(), product.getWorker(), product);

				if (product.getProductType() != ProductType.CUPBOARD) {
					// nie je cupboard
					// produkt je hotovy
					product.setProductAsDone(mySim().currentTime());
				} else {
					// je to cupboard, este nie je hotovy
					// nechame workstation
					product.setProductActivity(ProductActivity.ASSEMBLED);
					product.clearWorker(mySim().currentTime());
				}
				product.setFinishAssemblyTime(mySim().currentTime());

				this.assistantFinished(message);
				break;
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
	public AgentGroupB myAgent() {
		return (AgentGroupB)super.myAgent();
	}

}