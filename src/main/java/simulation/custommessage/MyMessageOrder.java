package simulation.custommessage;

import OSPABA.MessageForm;
import OSPABA.Simulation;
import entity.order.Order;

public class MyMessageOrder extends MessageForm {
    private Order order;

    public MyMessageOrder(Simulation mySim, Order order) {
        super(mySim);
        this.order = order;
    }

    protected MyMessageOrder(MessageForm original) {
        super(original);
    }

    @Override
    public MessageForm createCopy() {
        return new MyMessageOrder(this);
    }

    @Override
    public String toString() {
        return "MsgOrder{" +
                "order=" + order +
                '}';
    }

    public Order getOrder() {
        return this.order;
    }
}
