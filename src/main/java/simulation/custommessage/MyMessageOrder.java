package simulation.custommessage;

import OSPABA.MessageForm;
import OSPABA.Simulation;
import entity.order.Order;

public class MyMessageOrder extends MessageForm {
    private Order order;

    public MyMessageOrder(Simulation mySim) {
        super(mySim);
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

    public void setOrder(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return this.order;
    }
}
