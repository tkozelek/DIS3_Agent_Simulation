package simulation.custommessage;

import OSPABA.MessageForm;
import OSPABA.Simulation;
import entity.workstation.Workstation;

public class MyMessageMove extends MessageForm {
    private Workstation targetWorkstation;

    public MyMessageMove(Simulation mySim, Workstation workstation) {
        super(mySim);
        this.targetWorkstation = workstation;
    }

    protected MyMessageMove(MessageForm original) {
        super(original);
    }

    @Override
    public MessageForm createCopy() {
        return new MyMessageMove(this);
    }

    @Override
    public String toString() {
        return "MyMessageMove{" +
                "targetWorkstation=" + targetWorkstation +
                '}';
    }

    public Workstation getTargetWorkstation() {
        return targetWorkstation;
    }
}
