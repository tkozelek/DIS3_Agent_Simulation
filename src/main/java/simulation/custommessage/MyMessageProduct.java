package simulation.custommessage;

import OSPABA.MessageForm;
import OSPABA.Simulation;
import entity.product.Product;

public class MyMessageProduct extends MessageForm {
    private Product product;

    public MyMessageProduct(Simulation mySim, Product product) {
        super(mySim);
        this.product = product;
    }

    protected MyMessageProduct(MessageForm original) {
        super(original);
    }

    @Override
    public MessageForm createCopy() {
        return new MyMessageProduct(this);
    }

    @Override
    public String toString() {
        return "MsgProduct{" +
                "product=" + product +
                '}';
    }

    public Product getProduct() {
        return product;
    }
}
