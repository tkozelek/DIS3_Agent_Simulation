package agents.agentokolie;

import OSPABA.*;
import simulation.*;
import agents.agentokolie.continualassistants.*;



//meta! id="1"
public class AgentOkolie extends OSPABA.Agent {
    private List<Order> ordersInSystem;

    private final UniformDiscreteRNG productAmountGenerator;
    private EnumGenerator productTypeGenerator;

    public AgentOkolie(int id, Simulation mySim, Agent parent) {
        super(id, mySim, parent);
        init();
        addOwnMessage(Mc.holdOrderArrival);

        this.productAmountGenerator = new UniformDiscreteRNG(Constants.PRODUCT_AMOUNT_MIN, Constants.PRODUCT_AMOUNT_MAX);
        createEnumGenerator(mySim);
    }

    public UniformDiscreteRNG getProductAmountGenerator() {
        return productAmountGenerator;
    }

    public EnumGenerator getProductTypeGenerator() {
        return productTypeGenerator;
    }

    public void addOrder(Order order) {
        order.setArrivalTime(this.mySim().currentTime());
        ordersInSystem.add(order);
    }

    public void removeOrder(Order order) {
        order.setFinishTime(this.mySim().currentTime());
        ordersInSystem.remove(order);
    }

    public List<Order> getOrdersInSystem() {
        return ordersInSystem;
    }

    private void createEnumGenerator(Simulation mySim) {
        Map<ProductType, Double> probabilities = new HashMap<>();
        probabilities.put(ProductType.TABLE, 0.5);
        probabilities.put(ProductType.CHAIR, 0.15);
        probabilities.put(ProductType.CUPBOARD, 0.35);

        MySimulation sim = (MySimulation) mySim;
        this.productTypeGenerator = new EnumGenerator(probabilities, sim.getSeedGenerator());
    }

    @Override
    public void prepareReplication() {
        super.prepareReplication();
        // Setup component for the next replication
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