package agents.agentokolie;

import OSPABA.Agent;
import OSPABA.MessageForm;
import OSPABA.Simulation;
import config.Constants;
import config.Helper;
import entity.order.Order;
import entity.product.Product;
import simulation.Id;
import simulation.Mc;
import simulation.MyMessage;
import simulation.MySimulation;
import simulation.custommessage.MyMessageOrder;
import simulation.custommessage.MyMessageProduct;

//meta! id="1"
public class ManagerOkolie extends OSPABA.Manager {
    public ManagerOkolie(int id, Simulation mySim, Agent myAgent) {
        super(id, mySim, myAgent);
        init();
    }

    @Override
    public void prepareReplication() {
        super.prepareReplication();
        // Setup component for the next replication

        if (petriNet() != null) {
            petriNet().clear();
        }
    }

    //meta! sender="AgentBoss", id="21", type="Response"
    public void processRequestResponseOrderArrival(MessageForm message) {
        // response na prichod objednavky
        // tzn. objednavka hotova cela
        if (Constants.DEBUG_MANAGER)
            System.out.printf("[%s] Manazer okolie: odchod objednavky\n", Helper.timeToDateString(mySim().currentTime(), 6));
        MySimulation sim = (MySimulation) mySim();

        MyMessageProduct msgProduct = (MyMessageProduct) message;
        Product product = msgProduct.getProduct();

        sim.getStatProductTimeInSystemReplication().addSample(product.getFinishTime() - product.getArrivalTime());

        sim.getStatProductsTimeInSystemReplication()[product.getProductType().ordinal()].addSample(product.getFinishTime() - product.getArrivalTime());
        sim.getStatAverageTimeInQueueReplication().addSample(product.getTotalQueueTime());

        Order order = product.getMessageOrder().getOrder();
        double maxFinishTime = 0;
        for (Product p : order.getProducts()) {
            if (p.getFinishTime() == 0) {
                maxFinishTime = 0;
                return;
            }
            if (p.getFinishTime() > maxFinishTime)
                maxFinishTime = p.getFinishTime();
        }
        order.setFinishTime(maxFinishTime);

        sim.getStatOrderTimeInSystemReplication().addSample(order.getFinishTime() - order.getArrivalTime());
    }


    //meta! sender="SchedulerOrderArrival", id="18", type="Finish"
    public void processFinish(MessageForm message) {
        if (Constants.DEBUG_MANAGER)
            System.out.println("Scheduler finished");
    }

    //meta! sender="SchedulerOrderArrival", id="20", type="Notice"
    public void processNoticeOrderArrival(MessageForm message) {
        if (Constants.DEBUG_MANAGER)
            System.out.printf("[%s] M. okolie objednávka prišla\n", Helper.timeToDateString(mySim().currentTime(), 6));

        MyMessageOrder orderMessage = (MyMessageOrder) message;

        if (this.mySim().currentTime() > Constants.SIMULATION_TIME) {
            throw new IllegalStateException("Manazer okolie prisiel zakaznik po case simulacie");
        }

        myAgent().addOrder(orderMessage.getOrder());

        // musíme ju poslat agentovi bossovi
        message.setCode(Mc.requestResponseOrderArrival);
        message.setAddressee(Id.agentBoss);
        this.request(message);
    }

    //meta! userInfo="Process messages defined in code", id="0"
    public void processDefault(MessageForm message) {
        switch (message.code()) {
        }
    }

    //meta! sender="AgentBoss", id="86", type="Notice"
    public void processNoticeInitAgentOkolie(MessageForm message) {
        MyMessage myMessage = new MyMessage(this.mySim());
        myMessage.setAddressee(myAgent().findAssistant(Id.schedulerOrderArrival));
        startContinualAssistant(myMessage);
    }

    //meta! userInfo="Generated code: do not modify", tag="begin"
    public void init() {
    }

    @Override
    public void processMessage(MessageForm message) {
        switch (message.code()) {
            case Mc.requestResponseOrderArrival:
                processRequestResponseOrderArrival(message);
                break;

            case Mc.finish:
                processFinish(message);
                break;

            case Mc.noticeOrderArrival:
                processNoticeOrderArrival(message);
                break;

            case Mc.noticeInitAgentOkolie:
                processNoticeInitAgentOkolie(message);
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