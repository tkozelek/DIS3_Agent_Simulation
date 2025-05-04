package simulation.custommessage;

import OSPABA.MessageForm;
import entity.workstation.Workstation;

import java.util.ArrayList;

public class MyMessageWorkstation extends MessageForm {
    private ArrayList<Workstation> workstations;
    private int amount;

    public MyMessageWorkstation(MessageForm original) {
        super(original);
    }

    @Override
    public MessageForm createCopy() {
        return new MyMessageWorkstation(this);
    }

    @Override
    public String toString() {
        return "MyMessageMove{" +
                "workstation=" + workstations +
                '}';
    }

    public ArrayList<Workstation> getWorkstations() {
        return workstations;
    }

    public void setWorkstation(ArrayList<Workstation> workstations) {
        this.workstations = workstations;
    }

    public void addWorkstation(Workstation workstation) {
        workstations.add(workstation);
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
