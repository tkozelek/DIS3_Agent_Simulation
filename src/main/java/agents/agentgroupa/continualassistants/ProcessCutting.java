package agents.agentgroupa.continualassistants;

import OSPABA.CommonAgent;
import OSPABA.MessageForm;
import OSPABA.Simulation;
import agents.agentgroupa.AgentGroupA;
import config.Constants;
import entity.product.Product;
import simulation.Mc;
import simulation.custommessage.MyMessageProduct;

//meta! id="26"
public class ProcessCutting extends OSPABA.Process {
    public ProcessCutting(int id, Simulation mySim, CommonAgent myAgent) {
        super(id, mySim, myAgent);
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
		double offset = this.myAgent().getSampleFromGeneratorBasedOnProductType(product.getProductType());
		if (mySim().currentTime() + offset < Constants.SIMULATION_TIME) {

		}
    }

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message) {
        switch (message.code()) {
        }
    }

	//meta! userInfo="Generated code: do not modify", tag="begin"
	@Override
	public void processMessage(MessageForm message)
	{
		switch (message.code())
		{
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