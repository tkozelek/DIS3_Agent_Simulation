package agents.agentworker;

import OSPABA.Agent;
import OSPABA.MessageForm;
import OSPABA.Simulation;
import config.Constants;
import entity.product.Product;
import entity.product.ProductActivity;
import entity.product.ProductType;
import entity.worker.Worker;
import entity.worker.WorkerGroup;
import simulation.Id;
import simulation.Mc;
import simulation.MyMessage;
import simulation.MySimulation;
import simulation.custommessage.MyMessageMove;
import simulation.custommessage.MyMessageProduct;


//meta! id="6"
public class ManagerWorker extends OSPABA.Manager {
    public ManagerWorker(int id, Simulation mySim, Agent myAgent) {
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

    //meta! sender="AgentWorkplace", id="40", type="Request"
    public void processRequestResponseWorkOnOrderWorkplace(MessageForm message) {
        // poslal workplace, nova objednavka
        // posleme workerovi A
        if (Constants.DEBUG_MANAGER)
            System.out.printf("[%s] M. worker prisla objednavka\n", ((MySimulation) mySim()).workdayTime());

        message.setCode(Mc.requestResponseWorkAgentA);
        message.setAddressee(Id.agentGroupA);
        this.request(message);
    }

    //meta! sender="AgentGroupA", id="39", type="Response"
    public void processRequestResponseWorkAgentA(MessageForm message) {
        // worker A dokoncil pracu
        // request worker C začiatok práce
        message.setCode(Mc.requestResponseWorkAgentC);
        message.setAddressee(Id.agentGroupC);
        this.request(message);
    }

    //meta! sender="AgentGroupA", id="53", type="Request"
    public void processRequestResponseMoveWorkerAgentGroupA(MessageForm message) {
        // worker dal ziadost o move
        message.setCode(Mc.requestResponseMoveWorker);
        message.setAddressee(Id.agentWorkplace);
        this.request(message);
    }

    //meta! userInfo="Process messages defined in code", id="0"
    public void processDefault(MessageForm message) {
        switch (message.code()) {
        }
    }

    //meta! sender="AgentGroupB", id="63", type="Response"
    public void processRequestResponseWorkAgentB(MessageForm message) {
        // worker B dokoncil pracu
        // assembled
        MyMessageProduct msgProduct = (MyMessageProduct) message;
        Product product = msgProduct.getProduct();
        if (product.getProductType() == ProductType.CUPBOARD) {
            // fitting assembly
            // skus A
            if (this.myAgent().group().queueSize() > 0) {
                // už sú v queue nejaké
                this.myAgent().group().addQueue(msgProduct, mySim().currentTime());
                return;
            }
            msgProduct.setCode(Mc.requestResponseTryFitGroupA);
            msgProduct.setAddressee(Id.agentGroupA);
            this.request(msgProduct);
        } else {
            // finished
            product.setProductAsDone(mySim().currentTime());

            message.setCode(Mc.requestResponseWorkOnOrderWorkplace);
            message.setAddressee(Id.agentWorkplace);
            this.response(message);

            MyMessage msg = new MyMessage(mySim());

            msg.setCode(Mc.noticeWorkstationFreed);
            msg.setAddressee(Id.agentGroupA);
            this.notice(msg);
        }
    }

    //meta! sender="AgentGroupC", id="71", type="Request"
    public void processRequestResponseMoveWorkerAgentGroupC(MessageForm message) {
        // group C žiadost o move
        message.setCode(Mc.requestResponseMoveWorker);
        message.setAddressee(Id.agentWorkplace);
        this.request(message);
    }

    //meta! sender="AgentGroupB", id="66", type="Request"
    public void processRequestResponseMoveWorkerAgentGroupB(MessageForm message) {
        message.setCode(Mc.requestResponseMoveWorker);
        message.setAddressee(Id.agentWorkplace);
        this.request(message);
    }

    //meta! sender="AgentGroupC", id="72", type="Response"
    public void processRequestResponseWorkAgentC(MessageForm message) {
        // worker C dokoncil pracu
        // zacni B
        if (Constants.DEBUG_MANAGER)
            System.out.printf("[%s] M. worker C finished\n", ((MySimulation) mySim()).workdayTime());

        message.setCode(Mc.requestResponseWorkAgentB);
        message.setAddressee(Id.agentGroupB);
        this.request(message);
    }

    //meta! sender="AgentWorkplace", id="75", type="Response"
    public void processRequestResponseMoveWorkerAgentWorkplace(MessageForm message) {
        MyMessageMove msgMove = (MyMessageMove) message;
        Worker worker = msgMove.getWorker();
        int agentId = switch (worker.getGroup()) {
            case WorkerGroup.GROUP_A -> Id.agentGroupA;
            case WorkerGroup.GROUP_B -> Id.agentGroupB;
            case WorkerGroup.GROUP_C -> Id.agentGroupC;
        };
        msgMove.setAddressee(agentId);
        this.response(message);
    }

    //meta! sender="AgentGroupA", id="119", type="Notice"
    public void processNoticeAgentAFreed(MessageForm message) {
        if (myAgent().group().queueSize() > 0) {
            MyMessageProduct msgProduct = myAgent().group().pollQueue(mySim().currentTime());
            msgProduct.setCode(Mc.requestResponseTryFitGroupA);
            msgProduct.setAddressee(Id.agentGroupA);
            this.request(msgProduct);
            return;
        }
        MyMessageProduct msgProduct = new MyMessageProduct(mySim(), null);
        msgProduct.setCode(Mc.requestResponseTryFitGroupA);
        msgProduct.setAddressee(Id.agentGroupA);
        this.request(msgProduct);
    }

    //meta! sender="AgentGroupA", id="117", type="Response"
    public void processRequestResponseTryFitGroupA(MessageForm message) {
        MyMessageProduct msgProduct = (MyMessageProduct) message;
        Product product = msgProduct.getProduct();

        if (product == null) return;

        if (product.getProductActivity() == ProductActivity.DONE) {
            // je dokonceny
            if (Constants.DEBUG_MANAGER)
                System.out.println("FITTED A");
            msgProduct.setCode(Mc.requestResponseWorkOnOrderWorkplace);
            msgProduct.setAddressee(Id.agentWorkplace);
            this.response(msgProduct);
            return;
        }

        if (product.getProductActivity() == ProductActivity.ASSEMBLED) {
            // skus C
            msgProduct.setCode(Mc.requestResponseTryFitGroupC);
            msgProduct.setAddressee(Id.agentGroupC);
            this.request(msgProduct);
        }
    }

    //meta! sender="AgentGroupC", id="118", type="Response"
    public void processRequestResponseTryFitGroupC(MessageForm message) {
        MyMessageProduct msgProduct = (MyMessageProduct) message;
        Product product = msgProduct.getProduct();

        if (product == null) return;

        if (product.getProductActivity() == ProductActivity.DONE) {
            // je dokonceny
            if (Constants.DEBUG_MANAGER)
                System.out.println("FITTED C");
            msgProduct.setCode(Mc.requestResponseWorkOnOrderWorkplace);
            msgProduct.setAddressee(Id.agentWorkplace);
            this.response(msgProduct);
            return;
        }

        if (product.getProductActivity() == ProductActivity.ASSEMBLED) {
            // nevyšlo ani C, vlož do queue
            myAgent().group().addQueue(msgProduct, mySim().currentTime());
        }
    }

    //meta! sender="AgentGroupC", id="120", type="Notice"
    public void processNoticeAgentCFreed(MessageForm message) {
        if (myAgent().group().queueSize() > 0) {
            MyMessageProduct msgProduct = myAgent().group().pollQueue(mySim().currentTime());
            msgProduct.setCode(Mc.requestResponseTryFitGroupC);
            msgProduct.setAddressee(Id.agentGroupC);
            this.request(msgProduct);
            return;
        }
        MyMessageProduct msgProduct = new MyMessageProduct(mySim(), null);
        msgProduct.setCode(Mc.requestResponseTryFitGroupC);
        msgProduct.setAddressee(Id.agentGroupC);
        this.request(msgProduct);
    }

    //meta! userInfo="Generated code: do not modify", tag="begin"
    public void init() {
    }

    @Override
    public void processMessage(MessageForm message) {
        switch (message.code()) {
            case Mc.requestResponseTryFitGroupA:
                processRequestResponseTryFitGroupA(message);
                break;

            case Mc.requestResponseWorkAgentB:
                processRequestResponseWorkAgentB(message);
                break;

            case Mc.requestResponseMoveWorker:
                switch (message.sender().id()) {
                    case Id.agentWorkplace:
                        processRequestResponseMoveWorkerAgentWorkplace(message);
                        break;

                    case Id.agentGroupA:
                        processRequestResponseMoveWorkerAgentGroupA(message);
                        break;

                    case Id.agentGroupC:
                        processRequestResponseMoveWorkerAgentGroupC(message);
                        break;

                    case Id.agentGroupB:
                        processRequestResponseMoveWorkerAgentGroupB(message);
                        break;
                }
                break;

            case Mc.requestResponseTryFitGroupC:
                processRequestResponseTryFitGroupC(message);
                break;

            case Mc.requestResponseWorkOnOrderWorkplace:
                processRequestResponseWorkOnOrderWorkplace(message);
                break;

            case Mc.requestResponseWorkAgentA:
                processRequestResponseWorkAgentA(message);
                break;

            case Mc.noticeAgentCFreed:
                processNoticeAgentCFreed(message);
                break;

            case Mc.requestResponseWorkAgentC:
                processRequestResponseWorkAgentC(message);
                break;

            case Mc.noticeAgentAFreed:
                processNoticeAgentAFreed(message);
                break;

            default:
                processDefault(message);
                break;
        }
    }
    //meta! tag="end"

    @Override
    public AgentWorker myAgent() {
        return (AgentWorker) super.myAgent();
    }

}