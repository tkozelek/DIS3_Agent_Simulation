package simulation;

import agents.agentboss.AgentBoss;
import agents.agentgroupa.AgentGroupA;
import agents.agentmove.AgentMove;
import agents.agentokolie.AgentOkolie;
import agents.agentworker.AgentWorker;
import agents.agentworkplace.AgentWorkplace;
import agents.agentworkstation.AgentWorkstation;
import generator.SeedGenerator;


public class MySimulation extends OSPABA.Simulation {
    private SeedGenerator seedGen;
    private AgentBoss _agentBoss;
    private AgentWorkplace _agentWorkplace;
    private AgentWorkstation _agentWorkstation;
    private AgentMove _agentMove;
    private AgentWorker _agentWorker;
    private AgentGroupA _agentGroupA;
    private AgentOkolie _agentOkolie;

    public MySimulation(Long seed) {
        seedGen = seed == null ? new SeedGenerator() : new SeedGenerator(seed);

        init();
    }

    public SeedGenerator getSeedGenerator() {
        return seedGen;
    }

    @Override
    public void prepareSimulation() {
        super.prepareSimulation();
        // Create global statistcis

        this.seedGen = new SeedGenerator();
    }

    @Override
    public void prepareReplication() {
        super.prepareReplication();
        // Reset entities, queues, local statistics, etc...

        _agentBoss.initOkolie();
    }

    @Override
    public void replicationFinished() {
        // Collect local statistics into global, update UI, etc...
        super.replicationFinished();
    }

    @Override
    public void simulationFinished() {
        // Display simulation results
        super.simulationFinished();
    }

    //meta! userInfo="Generated code: do not modify", tag="begin"
    private void init() {
        setAgentBoss(new AgentBoss(Id.agentBoss, this, null));
        setAgentWorkplace(new AgentWorkplace(Id.agentWorkplace, this, agentBoss()));
        setAgentWorkstation(new AgentWorkstation(Id.agentWorkstation, this, agentWorkplace()));
        setAgentMove(new AgentMove(Id.agentMove, this, agentWorkplace()));
        setAgentWorker(new AgentWorker(Id.agentWorker, this, agentWorkplace()));
        setAgentGroupA(new AgentGroupA(Id.agentGroupA, this, agentWorker()));
        setAgentOkolie(new AgentOkolie(Id.agentOkolie, this, agentBoss()));
    }

    public AgentBoss agentBoss() {
        return _agentBoss;
    }

    public void setAgentBoss(AgentBoss agentBoss) {
        _agentBoss = agentBoss;
    }

    public AgentWorkplace agentWorkplace() {
        return _agentWorkplace;
    }

    public void setAgentWorkplace(AgentWorkplace agentWorkplace) {
        _agentWorkplace = agentWorkplace;
    }

    public AgentWorkstation agentWorkstation() {
        return _agentWorkstation;
    }

    public void setAgentWorkstation(AgentWorkstation agentWorkstation) {
        _agentWorkstation = agentWorkstation;
    }

    public AgentMove agentMove() {
        return _agentMove;
    }

    public void setAgentMove(AgentMove agentMove) {
        _agentMove = agentMove;
    }

    public AgentWorker agentWorker() {
        return _agentWorker;
    }

    public void setAgentWorker(AgentWorker agentWorker) {
        _agentWorker = agentWorker;
    }

    public AgentGroupA agentGroupA() {
        return _agentGroupA;
    }

    public void setAgentGroupA(AgentGroupA agentGroupA) {
        _agentGroupA = agentGroupA;
    }

    public AgentOkolie agentOkolie() {
        return _agentOkolie;
    }

    public void setAgentOkolie(AgentOkolie agentOkolie) {
        _agentOkolie = agentOkolie;
    }
    //meta! tag="end"
}