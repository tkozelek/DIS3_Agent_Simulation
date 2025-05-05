package agents.agentgroupa;

import OSPABA.Agent;
import OSPABA.MessageForm;
import OSPABA.Simulation;
import config.Constants;
import entity.ILocation;
import entity.Storage;
import entity.order.Order;
import entity.product.Product;
import entity.product.ProductActivity;
import entity.worker.Worker;
import entity.workstation.Workstation;
import simulation.Id;
import simulation.Mc;
import simulation.MyMessage;
import simulation.MySimulation;

import java.util.ArrayList;

//meta! id="7"
public class ManagerGroupA extends OSPABA.Manager {
    public ManagerGroupA(int id, Simulation mySim, Agent myAgent) {
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

    private void firstWorkOnOrder(ArrayList<Worker> workers) {
        MySimulation sim = (MySimulation) mySim();
        ArrayList<Workstation> workstations = sim.getFreeWorkstations(workers.size());

        ArrayList<MyMessage> msgProducts = new ArrayList<>();

        int b = Math.min(workstations.size(), workers.size());
        int amount = Math.min(b, myAgent().group().queueSize());

        for (int i = 0; i < amount; i++) {
            msgProducts.add(myAgent().group().pollQueue(mySim().currentTime()));
        }

        if (workers.isEmpty() || workstations.isEmpty() || msgProducts.isEmpty()) return;

        // nastavime produktu workstation
        for (int i = 0; i < amount; i++) {
            Workstation workstation = workstations.get(i);
            Worker worker = workers.get(i);
            MyMessage msgProduct = msgProducts.get(i);
            Product product = msgProduct.getProduct();

            worker.setCurrentProduct(product);

            product.setWorkstation(workstation);
            product.setWorker(worker);

            workstation.setCurrentProduct(product);

            // pozrieme ci worker je na mieste
            if (worker.getLocation() != Storage.STORAGE) {
                // nie je v sklade, musi ist do skladu
                // presun -> sklad
                moveWorkerRequest(msgProducts.get(i), worker, Storage.STORAGE);
            } else {
                // je v sklade
                // priprav veci
                startProcess(msgProducts.get(i), product, Id.processPreparing);
            }
        }
    }

    private void startProcess(MessageForm message, Product product, int processId) {
        MyMessage msgProduct = new MyMessage(message);
        msgProduct.setProduct(product);
        msgProduct.setAddressee(myAgent().findAssistant(processId));
        startContinualAssistant(msgProduct);
    }

    private void moveWorkerRequest(MessageForm message, Worker worker, ILocation location) {
        if (worker.getLocation() == location)
            throw new IllegalStateException("Worker A location is the same");

        MyMessage msgMove = new MyMessage(message);
        msgMove.setTargetLocation(location);
        msgMove.setWorker(worker);

        msgMove.setCode(Mc.requestResponseMoveWorker);
        msgMove.setAddressee(Id.agentWorker);
        this.request(msgMove);
    }

    //meta! sender="AgentWorker", id="39", type="Request"
    public void processRequestResponseWorkAgentA(MessageForm message) {
        // prisla objednavka
        MyMessage orderMessage = (MyMessage) message;
        Order order = orderMessage.getOrder();
        if (Constants.DEBUG_MANAGER)
            System.out.printf("[%s] M. group A objednávka prišla: %s\n", ((MySimulation) mySim()).workdayTime(), order);
        // vlozime do queue
        ArrayList<Product> products = order.getProducts();
        for (Product product : products) {
            MyMessage msgProduct = new MyMessage(message);
            msgProduct.setProduct(product);
            myAgent().group().addQueue(msgProduct, mySim().currentTime());
        }

        // je to nova objednávka ked prišla agentovi A
        // ak je free worker, skús vyžiadať workstation
        this.tryStartWork();
    }

    private void tryStartWork() {
        if (myAgent().group().queueSize() == 0) return;

        ArrayList<Worker> freeWorkers = this.myAgent().group().getFreeWorkers(myAgent().group().queueSize());
        if (freeWorkers.isEmpty()) return;

        this.firstWorkOnOrder(freeWorkers);
    }

    //meta! sender="AgentWorker", id="53", type="Response"
    public void processRequestResponseMoveWorker(MessageForm message) {
        // skoncil presun, je bud pri workstatione alebo storage
        MyMessage msgMove = (MyMessage) message;
        Worker worker = msgMove.getWorker();
        Product product = worker.getCurrentProduct();

        // ak sme skončili presun
        if (product.getProductActivity() == ProductActivity.ASSEMBLED && worker.getLocation() == product.getWorkstation()) {
            startProcess(message, product, Id.processFittingGroupA);
            return;
        }

        if (worker.getLocation() == Storage.STORAGE) {
            startProcess(msgMove, product, Id.processPreparing);
        } else if (worker.getLocation() == product.getWorkstation()) {
            startProcess(msgMove, product, Id.processCutting);
        } else {
            throw new IllegalStateException("Manager group A, invalid location");
        }
    }

    //meta! sender="ProcessCutting", id="27", type="Finish"
    public void processFinishProcessCutting(MessageForm message) {
        // skončilo rezanie, agent A končí robotu na produkte
        MyMessage msgProduct = (MyMessage) message;

        msgProduct.setCode(Mc.requestResponseWorkAgentA);
        msgProduct.setAddressee(Id.agentWorker);
        this.response(msgProduct);

//		this.tryStartWork();
        this.sendNoticeToWorker();
    }

    /**
     * Send notice to worker that a worker is freed
     */
    public void sendNoticeToWorker() {
        MyMessage msg = new MyMessage(mySim());
        msg.setCode(Mc.noticeAgentAFreed);
        msg.setAddressee(Id.agentWorker);
        this.notice(msg);
    }

    //meta! sender="ProcessPreparing", id="50", type="Finish"
    public void processFinishProcessPreparing(MessageForm message) {
        MyMessage msgProduct = (MyMessage) message;
        Worker worker = msgProduct.getProduct().getWorker();
        Product product = msgProduct.getProduct();

        // skoncil pracu v sklade -> move to workstation
        this.moveWorkerRequest(message, worker, product.getWorkstation());
    }

    //meta! sender="ProcessFittingGroupA", id="52", type="Finish"
    public void processFinishProcessFittingGroupA(MessageForm message) {
        message.setCode(Mc.requestResponseTryFitGroupA);
        message.setAddressee(Id.agentWorker);
        this.response(message);
        this.sendNoticeToWorker();
    }

    //meta! userInfo="Process messages defined in code", id="0"
    public void processDefault(MessageForm message) {
        switch (message.code()) {
        }
    }

    //meta! sender="AgentWorker", id="99", type="Notice"
    public void processNoticeWorkstationFreed(MessageForm message) {
        if (myAgent().group().queueSize() > 0) {
            this.tryStartWork();
        }
    }

    //meta! sender="AgentWorker", id="117", type="Request"
    public void processRequestResponseTryFitGroupA(MessageForm message) {
        // prišiel req od agent workera
        // skusime fitnut ak je free worker
        Worker worker = myAgent().group().getFreeWorker();
        // pošleme s5 spravu
        if (worker == null) {
            message.setCode(Mc.requestResponseTryFitGroupA);
            message.setAddressee(Id.agentWorker);
            this.response(message);
            return;
        }

        // worker je free
        MyMessage msgProduct = (MyMessage) message;
        Product product = msgProduct.getProduct();
        // tzn. že produkt nečaká na fitting
        if (product == null) {
            message.setCode(Mc.requestResponseTryFitGroupA);
            message.setAddressee(Id.agentWorker);
            this.response(message);
            this.tryStartWork();
            return;
        }
        worker.setCurrentProduct(product);
        product.setWorker(worker);

        // je worker aj product
        // skontrolujeme ci je worker na mieste
        if (worker.getLocation() != product.getWorkstation()) {
            // nie je na mieste, presunieme
            this.moveWorkerRequest(msgProduct, worker, product.getWorkstation());
        } else {
            this.startProcess(msgProduct, product, Id.processFittingGroupA);
        }
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

            case Mc.requestResponseWorkAgentA:
                processRequestResponseWorkAgentA(message);
                break;

            case Mc.requestResponseMoveWorker:
                processRequestResponseMoveWorker(message);
                break;

            case Mc.finish:
                switch (message.sender().id()) {
                    case Id.processCutting:
                        processFinishProcessCutting(message);
                        break;

                    case Id.processPreparing:
                        processFinishProcessPreparing(message);
                        break;

                    case Id.processFittingGroupA:
                        processFinishProcessFittingGroupA(message);
                        break;
                }
                break;

            case Mc.noticeWorkstationFreed:
                processNoticeWorkstationFreed(message);
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