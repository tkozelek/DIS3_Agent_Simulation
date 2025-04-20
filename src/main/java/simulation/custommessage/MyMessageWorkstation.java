package simulation.custommessage;

import OSPABA.MessageForm;
import OSPABA.Simulation;
import entity.workstation.Workstation;

public class MyMessageWorkstation extends MessageForm {
    private Workstation workstation;

    public MyMessageWorkstation(Simulation mySim, Workstation workstation) {
        super(mySim);
        this.workstation = workstation;
    }

    protected MyMessageWorkstation(MessageForm original) {
        super(original);
    }

    @Override
    public MessageForm createCopy() {
        return new MyMessageWorkstation(this);
    }

    @Override
    public String toString() {
        return "MyMessageMove{" +
                "workstation=" + workstation +
                '}';
    }

    public Workstation getWorkstation() {
        return workstation;
    }
}
