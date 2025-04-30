package agents.agentgroupc.continualassistants;

import OSPABA.*;
import config.Constants;
import entity.product.Product;
import entity.product.ProductActivity;
import entity.product.ProductType;
import entity.worker.Worker;
import entity.worker.WorkerWork;
import generator.Distribution;
import generator.continuos.ContinuosEmpiricGenerator;
import generator.continuos.ContinuosUniformGenerator;
import simulation.*;
import agents.agentgroupc.*;
import simulation.custommessage.MyMessageProduct;

//meta! id="83"
public class ProcessLakovanie extends OSPABA.Process {

	private ContinuosEmpiricGenerator lakovanieTableGenerator;
	private ContinuosUniformGenerator lakovanieChairGenerator;
	private ContinuosUniformGenerator lakovanieCupboardGenerator;

	public ProcessLakovanie(int id, Simulation mySim, CommonAgent myAgent) {
		super(id, mySim, myAgent);
		int times = 60;
		MySimulation sim = (MySimulation) mySim;

		this.lakovanieTableGenerator = new ContinuosEmpiricGenerator(new Distribution[]{
				new Distribution(50 * times, 70 * times, 0.1),
				new Distribution(70 * times, 150 * times, 0.6),
				new Distribution(150 * times, 200 * times, 0.3)
		}, sim.getSeedGenerator());

		this.lakovanieChairGenerator = new ContinuosUniformGenerator(40 * times, 200 * times, sim.getSeedGenerator());
		this.lakovanieCupboardGenerator = new ContinuosUniformGenerator(250 * times, 560 * times, sim.getSeedGenerator());
	}

	private Double getSampleBasedOnProductType(ProductType productType) {
		return switch (productType) {
			case ProductType.TABLE -> this.lakovanieTableGenerator.sample();
			case ProductType.CHAIR -> this.lakovanieChairGenerator.sample();
			case ProductType.CUPBOARD -> this.lakovanieCupboardGenerator.sample();
		};
	}

	@Override
	public void prepareReplication() {
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="AgentGroupC", id="84", type="Start"
	public void processStart(MessageForm message) {
		MyMessageProduct productMessage = (MyMessageProduct) message;
		Product product = productMessage.getProduct();

		if (Constants.DEBUG_PROCESS)
			System.out.printf("[%s] [%s] P. painting start %s\n", ((MySimulation)mySim()).workdayTime(), product.getWorker(), product);

		product.setProductActivity(ProductActivity.PAINTING);
		product.setStartPaintingTime(mySim().currentTime());

		Worker worker = product.getWorker();
		worker.setCurrentWork(WorkerWork.PAINTING, mySim().currentTime());

		double offset = this.getSampleBasedOnProductType(product.getProductType());
		productMessage.setCode(Mc.holdLakovanie);
		this.hold(offset, productMessage);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message) {
		switch (message.code()) {
			case Mc.holdLakovanie:
				MyMessageProduct productMessage = (MyMessageProduct) message;
				Product product = productMessage.getProduct();

				if (Constants.DEBUG_PROCESS)
					System.out.printf("[%s] [%s] P. painting finished %s\n", ((MySimulation)mySim()).workdayTime(), product.getWorker(), product);

				product.setProductActivity(ProductActivity.PAINTED);
				product.setFinishPaintingTime(mySim().currentTime());

				product.clearWorker(mySim().currentTime());

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