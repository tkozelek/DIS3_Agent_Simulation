package agents.agentokolie.continualassistants;

import OSPABA.CommonAgent;
import OSPABA.MessageForm;
import OSPABA.Simulation;
import agents.agentokolie.AgentOkolie;
import config.Constants;
import entity.order.Order;
import entity.product.Product;
import entity.product.ProductType;
import generator.continuos.ContinuosExponentialGenerator;
import simulation.Id;
import simulation.Mc;
import simulation.MyMessage;
import simulation.MySimulation;
import simulation.custommessage.MyMessageOrder;

//meta! id="17"
public class SchedulerOrderArrival extends OSPABA.Scheduler {
    private final ContinuosExponentialGenerator orderArrivalGenerator;

    public SchedulerOrderArrival(int id, Simulation mySim, CommonAgent myAgent) {
        super(id, mySim, myAgent);

        MySimulation sim = (MySimulation) mySim;
        this.orderArrivalGenerator = new ContinuosExponentialGenerator(1 / 1800.0, sim.getSeedGenerator());
    }

    @Override
    public void prepareReplication() {
        super.prepareReplication();
        // Setup component for the next replication
    }

    //meta! sender="AgentOkolie", id="18", type="Start"
    public void processStart(MessageForm message) {
        this.scheduleNextArrival();
    }

    private void scheduleNextArrival() {
        double offsetArrival = this.orderArrivalGenerator.sample();

        if (mySim().currentTime() + offsetArrival < Constants.SIMULATION_TIME) {
            // ak stihne prist

            // vytvor order
            int productAmount = this.myAgent().getProductAmountGenerator().sample();
            Order order = new Order();
            MyMessageOrder customMsg
                    = new MyMessageOrder(this.mySim());
            for (int i = 0; i < productAmount; i++) {
                Product product = new Product((ProductType) this.myAgent().getProductTypeGenerator().sample());
                product.setShouldBePainted(myAgent().shouldBePainted());
                order.addProduct(product);
                product.setOrder(customMsg);
            }
            //
            customMsg.setOrder(order);
            customMsg.setCode(Mc.holdOrderArrival);

            this.hold(offsetArrival, customMsg);
        } else {
            this.assistantFinished(new MyMessage(mySim()));
        }
    }

    //meta! userInfo="Process messages defined in code", id="0"
    public void processDefault(MessageForm message) {
        switch (message.code()) {
            case Mc.holdOrderArrival:
                this.orderArrival(message);
                break;
        }
    }

    private void orderArrival(MessageForm message) {
        if (Constants.DEBUG_SCHEDULER)
            System.out.printf("%.2f: Order arrived\n", mySim().currentTime());

        // objednávka prišla po holde
        MyMessageOrder orderMessage = (MyMessageOrder) message;

        // pošli spravu managerovi o prichode
        orderMessage.setCode(Mc.noticeOrderArrival);
        orderMessage.setAddressee(Id.managerOkolie);
        this.notice(orderMessage);

        // naplanuj dalsi prichod
        this.scheduleNextArrival();
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
    public AgentOkolie myAgent() {
        return (AgentOkolie) super.myAgent();
    }

}