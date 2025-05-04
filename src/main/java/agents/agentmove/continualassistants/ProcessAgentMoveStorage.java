package agents.agentmove.continualassistants;

import OSPABA.CommonAgent;
import OSPABA.MessageForm;
import OSPABA.Simulation;
import agents.agentmove.AgentMove;
import config.Constants;
import entity.worker.WorkerWork;
import generator.SeedGenerator;
import generator.continuos.ContinuosTriangularGenerator;
import simulation.Mc;
import simulation.MySimulation;
import simulation.custommessage.MyMessageMove;

//meta! id="47"
public class ProcessAgentMoveStorage extends OSPABA.Process {
    private final ContinuosTriangularGenerator moveToStorageGenerator;

    public ProcessAgentMoveStorage(int id, Simulation mySim, CommonAgent myAgent) {
        super(id, mySim, myAgent);

        SeedGenerator seedGen = ((MySimulation) mySim).getSeedGenerator();

        this.moveToStorageGenerator = new ContinuosTriangularGenerator(60, 480, 120, seedGen);
    }

    @Override
    public void prepareReplication() {
        super.prepareReplication();
        // Setup component for the next replication
    }

    //meta! sender="AgentMove", id="48", type="Start"
    public void processStart(MessageForm message) {
        MyMessageMove msg = (MyMessageMove) message;
        msg.getWorker().setCurrentWork(WorkerWork.MOVING);

        if (Constants.DEBUG_PROCESS)
            System.out.printf("[%s] [%s] P. move storage started -> %s\n", ((MySimulation) mySim()).workdayTime(), msg.getWorker(), msg.getTargetLocation());

        double offset = moveToStorageGenerator.sample();
        message.setCode(Mc.holdMoveStorage);
        this.hold(offset, message);
    }

    //meta! userInfo="Process messages defined in code", id="0"
    public void processDefault(MessageForm message) {
        switch (message.code()) {
            case Mc.holdMoveStorage:
                MyMessageMove msg = (MyMessageMove) message;
                msg.getWorker().setLocation(msg.getTargetLocation());

                if (Constants.DEBUG_PROCESS)
                    System.out.printf("[%s] [%s] P. move storage finished -> %s\n", ((MySimulation) mySim()).workdayTime(), msg.getWorker(), msg.getTargetLocation());

                assistantFinished(message);
        }
    }

    //meta! userInfo="Generated code: do not modify", tag="begin"
    @Override
    public void processMessage(MessageForm message) {
        switch (message.code()) {
            case Mc.start:
                processStart(message);
                break;

            default:
                processDefault(message);
                break;
        }
    }
    //meta! tag="end"

    @Override
    public AgentMove myAgent() {
        return (AgentMove) super.myAgent();
    }

}