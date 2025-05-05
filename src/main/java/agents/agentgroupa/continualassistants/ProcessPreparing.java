package agents.agentgroupa.continualassistants;

import OSPABA.CommonAgent;
import OSPABA.MessageForm;
import OSPABA.Simulation;
import agents.agentgroupa.AgentGroupA;
import config.Constants;
import entity.product.Product;
import entity.product.ProductActivity;
import entity.worker.Worker;
import entity.worker.WorkerWork;
import generator.continuos.ContinuosTriangularGenerator;
import simulation.Mc;
import simulation.MyMessage;
import simulation.MySimulation;

//meta! id="49"
public class ProcessPreparing extends OSPABA.Process {
    private final ContinuosTriangularGenerator materialPreparationGenerator;

    public ProcessPreparing(int id, Simulation mySim, CommonAgent myAgent) {
        super(id, mySim, myAgent);

        this.materialPreparationGenerator = new ContinuosTriangularGenerator(300, 900, 500, ((MySimulation) mySim()).getSeedGenerator());
    }

    @Override
    public void prepareReplication() {
        super.prepareReplication();
        // Setup component for the next replication
    }

    //meta! sender="AgentGroupA", id="50", type="Start"
    public void processStart(MessageForm message) {
        MyMessage productMessage = (MyMessage) message;
        Product product = productMessage.getProduct();

        if (Constants.DEBUG_PROCESS)
            System.out.printf("[%s] [%s] P. preparing start %s\n", ((MySimulation) mySim()).workdayTime(), product.getWorker(), product);

        if (product.getProductActivity() != ProductActivity.EMTPY)
            throw new IllegalStateException("Manager A product isnt empty");

        product.setProductActivity(ProductActivity.PREPARING);
        Worker worker = productMessage.getProduct().getWorker();
        worker.setCurrentWork(WorkerWork.PREPARING_MATERIAL);

        double offset = this.materialPreparationGenerator.sample();
        message.setCode(Mc.holdPrepareMaterial);
        this.hold(offset, message);
    }

    //meta! userInfo="Process messages defined in code", id="0"
    public void processDefault(MessageForm message) {
        switch (message.code()) {
            case Mc.holdPrepareMaterial:
                MyMessage productMessage = (MyMessage) message;
                Product product = productMessage.getProduct();
                product.setProductActivity(ProductActivity.PREPARED);

                product.validateTimes();

                if (Constants.DEBUG_PROCESS)
                    System.out.printf("[%s] [%s] P. preparing finished %s\n", ((MySimulation) mySim()).workdayTime(), product.getWorker(), product);

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