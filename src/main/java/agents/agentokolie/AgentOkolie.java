package agents.agentokolie;

import OSPABA.*;
import simulation.*;
import agents.agentokolie.continualassistants.*;
import OSPRNG.UniformDiscreteRNG;
import config.Constants;
import entity.product.ProductType;
import generator.EnumGenerator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

//meta! id="1"
public class AgentOkolie extends OSPABA.Agent {
    private final UniformDiscreteRNG productAmountGenerator;

    private final ArrayList<MyMessage> ordersInSystem;

    private final Random randPaintingGenerator;
    private EnumGenerator productTypeGenerator;

    public AgentOkolie(int id, Simulation mySim, Agent parent) {
        super(id, mySim, parent);
        init();
        addOwnMessage(Mc.holdOrderArrival);

        MySimulation sim = (MySimulation) mySim;

        this.ordersInSystem = new ArrayList<>();

        this.randPaintingGenerator = new Random(sim.getSeedGenerator().sample());

        this.productAmountGenerator = new UniformDiscreteRNG(Constants.PRODUCT_AMOUNT_MIN, Constants.PRODUCT_AMOUNT_MAX, sim.getSeedGenerator().getRandom());
        createEnumGenerator(sim);
    }

    public UniformDiscreteRNG getProductAmountGenerator() {
        return productAmountGenerator;
    }

    public EnumGenerator getProductTypeGenerator() {
        return productTypeGenerator;
    }

    public void addOrder(MyMessage message) {
        message.getOrder().setArrivalTime(this.mySim().currentTime());
        ordersInSystem.add(message);
    }

    public void removeOrder(MyMessage message) {
        ordersInSystem.remove(message);
    }


    public ArrayList<MyMessage> getOrdersInSystem() {
        return ordersInSystem;
    }

    public boolean shouldBePainted() {
        return this.randPaintingGenerator.nextDouble() < Constants.PAINT_CHANCE;
    }

    private void createEnumGenerator(MySimulation sim) {
        HashMap<ProductType, Double> probabilities = new HashMap<>();
        probabilities.put(ProductType.TABLE, 0.5);
        probabilities.put(ProductType.CHAIR, 0.15);
        probabilities.put(ProductType.CUPBOARD, 0.35);

        this.productTypeGenerator = new EnumGenerator(probabilities, sim.getSeedGenerator());
    }

    @Override
    public void prepareReplication() {
        super.prepareReplication();
        // Setup component for the next replication

        this.ordersInSystem.clear();
    }

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new ManagerOkolie(Id.managerOkolie, mySim(), this);
		new SchedulerOrderArrival(Id.schedulerOrderArrival, mySim(), this);
		addOwnMessage(Mc.requestResponseOrderArrival);
		addOwnMessage(Mc.noticeInitAgentOkolie);
		addOwnMessage(Mc.noticeOrderArrival);
	}
	//meta! tag="end"
}