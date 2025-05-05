package agents.agentmove.continualassistants;

import OSPABA.CommonAgent;
import OSPABA.MessageForm;
import OSPABA.Simulation;
import agents.agentmove.AgentMove;
import config.Constants;
import entity.worker.Worker;
import entity.worker.WorkerWork;
import generator.SeedGenerator;
import generator.continuos.ContinuosTriangularGenerator;
import simulation.Mc;
import simulation.MyMessage;
import simulation.MySimulation;

//meta! id="42"
public class ProcessAgentMove extends OSPABA.Process {

    private final ContinuosTriangularGenerator moveStationsGenerator;

    public ProcessAgentMove(int id, Simulation mySim, CommonAgent myAgent) {
        super(id, mySim, myAgent);
        SeedGenerator seedGen = ((MySimulation) mySim).getSeedGenerator();
        this.moveStationsGenerator = new ContinuosTriangularGenerator(120, 500, 150, seedGen);
    }

    @Override
    public void prepareReplication() {
        super.prepareReplication();
        // Setup component for the next replication
    }

    //meta! sender="AgentMove", id="43", type="Start"
    public void processStart(MessageForm message) {
        MyMessage msg = (MyMessage) message;
        msg.getWorker().setCurrentWork(WorkerWork.MOVING);

        if (Constants.DEBUG_PROCESS)
            System.out.printf("[%s] [%s] P. move started -> %s\n", ((MySimulation) mySim()).workdayTime(), msg.getWorker(), msg.getTargetLocation());

        double offset = this.moveStationsGenerator.sample();
        message.setCode(Mc.holdMove);
        this.hold(offset, message);
    }

    //meta! userInfo="Process messages defined in code", id="0"
    public void processDefault(MessageForm message) {
        switch (message.code()) {
            case Mc.holdMove:
                MyMessage msg = (MyMessage) message;
                Worker worker = msg.getWorker();
                worker.setLocation(msg.getTargetLocation());

                if (Constants.DEBUG_PROCESS)
                    System.out.printf("[%s] [%s] P. move finished -> %s\n", ((MySimulation) mySim()).workdayTime(), msg.getWorker(), msg.getTargetLocation());

                this.assistantFinished(message);
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