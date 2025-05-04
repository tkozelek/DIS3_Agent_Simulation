package simulation.custommessage;

import OSPABA.MessageForm;
import OSPABA.Simulation;
import entity.ILocation;
import entity.worker.Worker;

public class MyMessageMove extends MessageForm {
    private ILocation targetLocation;
    private Worker worker;

    public MyMessageMove(Simulation mySim, Worker worker, ILocation targetLocation) {
        super(mySim);
        this.targetLocation = targetLocation;
        this.worker = worker;
    }

    public MyMessageMove(MessageForm original) {
        super(original);
    }

    @Override
    public MessageForm createCopy() {
        return new MyMessageMove(this);
    }

    @Override
    public String toString() {
        return "MyMessageMove{" +
                "target=" + targetLocation +
                '}';
    }

    public ILocation getTargetLocation() {
        return targetLocation;
    }

    public void setTargetLocation(ILocation targetLocation) {
        this.targetLocation = targetLocation;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }
}
