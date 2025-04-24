package agents.agentgroupa.continualassistants;

import OSPABA.CommonAgent;
import OSPABA.MessageForm;
import OSPABA.Simulation;
import agents.agentgroupa.AgentGroupA;
import config.Constants;
import entity.product.ProductActivity;
import generator.SeedGenerator;
import generator.continuos.ContinuosUniformGenerator;
import simulation.Mc;
import simulation.MySimulation;
import simulation.custommessage.MyMessageProduct;

//meta! id="51"
public class ProcessFitting extends OSPABA.Process {

	private ContinuosUniformGenerator fittingAssemblyGenerator;

    public ProcessFitting(int id, Simulation mySim, CommonAgent myAgent) {
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
			System.out.printf("[%s] P. fitting start\n", ((MySimulation)mySim()).workdayTime());

		MyMessageProduct productMessage = (MyMessageProduct) message;
		productMessage.getProduct().setProductActivity(ProductActivity.FITTING);

		double offset = this.fittingAssemblyGenerator.sample();
		productMessage.setCode(Mc.holdFitting);
		this.hold(offset, productMessage);
    }

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message) {
        switch (message.code()) {
			case Mc.holdFitting:
				if (Constants.DEBUG_PROCESS)
					System.out.printf("[%s] P. fitting finished\n", ((MySimulation)mySim()).workdayTime());

				MyMessageProduct productMessage = (MyMessageProduct) message;
				productMessage.getProduct().setProductActivity(ProductActivity.FITTING);

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