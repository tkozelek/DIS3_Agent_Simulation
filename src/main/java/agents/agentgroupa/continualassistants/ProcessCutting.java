package agents.agentgroupa.continualassistants;

import OSPABA.CommonAgent;
import OSPABA.MessageForm;
import OSPABA.Simulation;
import agents.agentgroupa.AgentGroupA;
import config.Constants;
import entity.product.Product;
import entity.product.ProductActivity;
import entity.product.ProductType;
import entity.worker.Worker;
import entity.worker.WorkerWork;
import generator.Distribution;
import generator.SeedGenerator;
import generator.continuos.ContinuosEmpiricGenerator;
import generator.continuos.ContinuosUniformGenerator;
import simulation.Mc;
import simulation.MySimulation;
import simulation.custommessage.MyMessageProduct;

//meta! id="26"
public class ProcessCutting extends OSPABA.Process {

	private ContinuosEmpiricGenerator cuttingTableGenerator;
	private ContinuosUniformGenerator cuttingChairGenerator;
	private ContinuosUniformGenerator cuttingcupboardGenerator;

    public ProcessCutting(int id, Simulation mySim, CommonAgent myAgent) {
        super(id, mySim, myAgent);

		SeedGenerator seedGen = ((MySimulation)mySim()).getSeedGenerator();
		int times = 60;
		this.cuttingTableGenerator = new ContinuosEmpiricGenerator(new Distribution[]{
				new Distribution(10 * times, 25 * times, 0.6),
				new Distribution(25 * times, 50 * times, 0.4)
		}, seedGen);
		this.cuttingChairGenerator = new ContinuosUniformGenerator(12 * times, 16 * times, seedGen);
		this.cuttingcupboardGenerator = new ContinuosUniformGenerator(15 * times, 80 * times, seedGen);
    }

    @Override
    public void prepareReplication() {
        super.prepareReplication();
        // Setup component for the next replication
    }

	//meta! sender="AgentGroupA", id="27", type="Start"
	public void processStart(MessageForm message) {
		// zacne rezat
		MyMessageProduct productMessage = (MyMessageProduct) message;
		Product product = productMessage.getProduct();

		if (Constants.DEBUG_PROCESS)
			System.out.printf("[%s] [%s] P. cutting start\n", ((MySimulation)mySim()).workdayTime(), product.getCurrentWorker());

		product.setProductActivity(ProductActivity.CUTTING);
		product.setStartCuttingTime(mySim().currentTime());

		Worker worker = product.getCurrentWorker();
		worker.setCurrentWork(WorkerWork.CUTTING, mySim().currentTime());

		double offset = this.getSampleFromGeneratorBasedOnProductType(product.getProductType());
		productMessage.setCode(Mc.holdCutting);
		this.hold(offset, productMessage);
    }

	public Double getSampleFromGeneratorBasedOnProductType(ProductType type) {
		return switch (type) {
			case ProductType.CHAIR -> this.cuttingChairGenerator.sample();
			case ProductType.TABLE -> this.cuttingTableGenerator.sample();
			case ProductType.CUPBOARD -> this.cuttingcupboardGenerator.sample();
			default -> throw new IllegalStateException("Unexpected value: " + type);
		};
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message) {
        switch (message.code()) {
			case Mc.holdCutting:
				MyMessageProduct productMessage = (MyMessageProduct) message;
				Product product = productMessage.getProduct();

				if (Constants.DEBUG_PROCESS)
					System.out.printf("[%s] [%s] P. cutting finished\n", ((MySimulation)mySim()).workdayTime(), product.getCurrentWorker());

				product.setProductActivity(ProductActivity.CUT);
				product.setFinishCuttingTime(mySim().currentTime());

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
    public AgentGroupA myAgent() {
        return (AgentGroupA) super.myAgent();
    }

}