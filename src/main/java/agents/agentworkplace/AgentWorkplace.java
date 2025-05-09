package agents.agentworkplace;

import OSPABA.Agent;
import OSPABA.Simulation;
import OSPAnimator.AnimImageItem;
import OSPAnimator.AnimTextItem;
import config.Helper;
import simulation.Data;
import simulation.Id;
import simulation.Mc;

import java.awt.*;


//meta! id="3"
public class AgentWorkplace extends OSPABA.Agent {

    private AnimImageItem imgStorage;
    private AnimTextItem textQueueSize;
    private AnimTextItem textSimulationTime;

    public AgentWorkplace(int id, Simulation mySim, Agent parent) {
        super(id, mySim, parent);
        init();
    }

    public void initAnimator() {
        imgStorage = new AnimImageItem(Data.STORAGE, Data.STORAGE_WIDTH, Data.STORAGE_HEIGHT);
        imgStorage.setPosition(Data.STORAGE_POSITION);
        imgStorage.setZIndex(10);

        textSimulationTime = new AnimTextItem("Čas: ", Color.BLACK, new Font("Arial", Font.BOLD, 30));

        textQueueSize = new AnimTextItem("Počet produktov v rade: ", Color.BLACK, new Font("Arial", Font.BOLD, 30));
        textQueueSize.setPosition(Data.TEXT_QUEUE_POSITION);
        textQueueSize.setZIndex(100);

        textSimulationTime.setPosition(Data.TEXT_SIMULATIONTIME_POSITION);
        textSimulationTime.setZIndex(100);

        if (mySim().animatorExists()) {
            _mySim.animator().register(imgStorage);
            _mySim.animator().register(textQueueSize);
            _mySim.animator().register(textSimulationTime);
        }
    }

    public void updateTime() {
        if (this.textSimulationTime != null)
            this.textSimulationTime.setText("Čas: " + Helper.timeToDateString(mySim().currentTime(), 6));
    }

    @Override
    public void prepareReplication() {
        super.prepareReplication();
        // Setup component for the next replication
    }

    public void updateQueueSize(int newQueueSize) {
        this.textQueueSize.setText("Počet produktov v rade: " + newQueueSize);
    }

    //meta! userInfo="Generated code: do not modify", tag="begin"
    private void init() {
        new ManagerWorkplace(Id.managerWorkplace, mySim(), this);
        addOwnMessage(Mc.requestResponseOrderArrived);
        addOwnMessage(Mc.requestResponseMoveWorker);
        addOwnMessage(Mc.requestResponseWorkOnOrderWorkplace);
    }
    //meta! tag="end"
}