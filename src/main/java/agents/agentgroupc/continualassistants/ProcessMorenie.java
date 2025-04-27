package agents.agentgroupc.continualassistants;

import OSPABA.*;
import config.Constants;
import entity.product.Product;
import entity.product.ProductActivity;
import entity.product.ProductType;
import entity.worker.Worker;
import entity.worker.WorkerWork;
import generator.continuos.ContinuosUniformGenerator;
import simulation.*;
import agents.agentgroupc.*;
import simulation.custommessage.MyMessageProduct;

//meta! id="81"
public class ProcessMorenie extends OSPABA.Process {

	private ContinuosUniformGenerator morenieTableGenerator;
	private ContinuosUniformGenerator morenieChairGenerator;
	private ContinuosUniformGenerator morenieCupboardGenerator;


	public ProcessMorenie(int id, Simulation mySim, CommonAgent myAgent) {
		super(id, mySim, myAgent);

		int times = 60;
		MySimulation sim = (MySimulation) mySim;
		this.morenieTableGenerator = new ContinuosUniformGenerator(100 * times, 480 * times, sim.getSeedGenerator());
		this.morenieChairGenerator = new ContinuosUniformGenerator(90 * times, 400 * times, sim.getSeedGenerator());
		this.morenieCupboardGenerator = new ContinuosUniformGenerator(300 * times, 600 * times, sim.getSeedGenerator());
	}

	private Double getSampleBasedOnProductType(ProductType productType) {
		return switch (productType) {
			case ProductType.TABLE -> this.morenieTableGenerator.sample();
			case ProductType.CHAIR -> this.morenieChairGenerator.sample();
			case ProductType.CUPBOARD -> this.morenieCupboardGenerator.sample();
		};
	}

	@Override
	public void prepareReplication() {
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="AgentGroupC", id="82", type="Start"
	public void processStart(MessageForm message) {
		MyMessageProduct productMessage = (MyMessageProduct) message;
		Product product = productMessage.getProduct();

		if (Constants.DEBUG_PROCESS)
			System.out.printf("[%s] [%s] P. staining start\n", ((MySimulation)mySim()).workdayTime(), product.getCurrentWorker());

		product.setProductActivity(ProductActivity.STAINING);
		product.setStartStainingTime(mySim().currentTime());

		Worker worker = product.getCurrentWorker();
		worker.setCurrentWork(WorkerWork.STAINING, mySim().currentTime());

		double offset = this.getSampleBasedOnProductType(product.getProductType());
		productMessage.setCode(Mc.holdMorenie);
		this.hold(offset, productMessage);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message) {
		switch (message.code()) {
			case Mc.holdMorenie:
				MyMessageProduct productMessage = (MyMessageProduct) message;
				Product product = productMessage.getProduct();

				if (Constants.DEBUG_PROCESS)
					System.out.printf("[%s] [%s] P. staining finished\n", ((MySimulation)mySim()).workdayTime(), product.getCurrentWorker());

				product.setProductActivity(ProductActivity.STAINED);
				product.setFinishStainingTime(mySim().currentTime());

				Worker worker = product.getCurrentWorker();
				worker.setCurrentWork(WorkerWork.IDLE, mySim().currentTime());

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
	public AgentGroupC myAgent() {
		return (AgentGroupC)super.myAgent();
	}

}