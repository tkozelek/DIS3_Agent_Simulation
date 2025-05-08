package agents.agentgroupc.continualassistants;

import OSPABA.CommonAgent;
import OSPABA.MessageForm;
import OSPABA.Simulation;
import agents.agentgroupc.AgentGroupC;
import config.Constants;
import entity.product.Product;
import entity.product.ProductActivity;
import entity.product.ProductType;
import entity.worker.Worker;
import entity.worker.WorkerWork;
import generator.continuos.ContinuosUniformGenerator;
import simulation.Mc;
import simulation.MyMessage;
import simulation.MySimulation;

//meta! id="81"
public class ProcessMorenie extends OSPABA.Process {

    private final ContinuosUniformGenerator morenieTableGenerator;
    private final ContinuosUniformGenerator morenieChairGenerator;
    private final ContinuosUniformGenerator morenieCupboardGenerator;


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
        MyMessage productMessage = (MyMessage) message;
        Product product = productMessage.getProduct();

        if (Constants.DEBUG_PROCESS)
            System.out.printf("[%s] [%s] P. staining start %s\n", ((MySimulation) mySim()).workdayTime(), product.getWorker(), product);

        product.setProductActivity(ProductActivity.STAINING);
        product.setStartStainingTime(mySim().currentTime());
        product.getAnimImageItem().setToolTip(product + " STAINING");

        Worker worker = product.getWorker();
        worker.setCurrentWork(WorkerWork.STAINING);

        double offset = this.getSampleBasedOnProductType(product.getProductType());
        productMessage.setCode(Mc.holdMorenie);
        this.hold(offset, productMessage);
    }

    //meta! userInfo="Process messages defined in code", id="0"
    public void processDefault(MessageForm message) {
        switch (message.code()) {
            case Mc.holdMorenie:
                MyMessage productMessage = (MyMessage) message;
                Product product = productMessage.getProduct();

                if (Constants.DEBUG_PROCESS)
                    System.out.printf("[%s] [%s] P. staining finished %s\n", ((MySimulation) mySim()).workdayTime(), product.getWorker(), product);

                product.setProductActivity(ProductActivity.STAINED);
                product.setFinishStainingTime(mySim().currentTime());

                if (!product.getShouldBePainted()) {
                    product.clearWorker();
                }
                product.validateTimes();

                this.assistantFinished(message);
                break;
        }
    }

    //meta! userInfo="Generated code: do not modify", tag="begin"
    @Override
    public void processMessage(MessageForm message) {
        ((MySimulation) mySim()).updateAnimatorTime();
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
        return (AgentGroupC) super.myAgent();
    }

}