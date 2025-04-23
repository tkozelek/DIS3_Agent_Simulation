package simulation;

import agents.agentworkplace.*;
import agents.agentworker.*;
import agents.agentgroupa.*;
import agents.agentboss.*;
import agents.agentgroupb.*;
import agents.agentgroupc.*;
import agents.agentmove.*;
import agents.agentworkstation.*;
import agents.agentokolie.*;
import config.Helper;
import entity.Ids;
import entity.worker.WorkerGroup;
import generator.SeedGenerator;


public class MySimulation extends OSPABA.Simulation {
    private SeedGenerator seedGen;

	private int[] groups;
	private int workstationCount;

    public MySimulation(Long seed, int[] groups, int wCount) {
        seedGen = seed == null ? new SeedGenerator() : new SeedGenerator(seed);

		this.groups = groups;
		this.workstationCount = wCount;

        init();
    }

	public int getWorkstationCount() {
		return this.workstationCount;
	}

	public int getWorkerCountForGroup(WorkerGroup group) {
		return groups[group.ordinal()];
	}

	public SeedGenerator getSeedGenerator() {
        return seedGen;
    }

	public String workdayTime() {
		return Helper.timeToDateString(currentTime(), 6);
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
		Ids.resetAll();
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
		setAgentGroupB(new AgentGroupB(Id.agentGroupB, this, agentWorker()));
		setAgentOkolie(new AgentOkolie(Id.agentOkolie, this, agentBoss()));
		setAgentGroupC(new AgentGroupC(Id.agentGroupC, this, agentWorker()));
	}

	private AgentBoss _agentBoss;

public AgentBoss agentBoss()
	{ return _agentBoss; }

	public void setAgentBoss(AgentBoss agentBoss)
	{_agentBoss = agentBoss; }

	private AgentWorkplace _agentWorkplace;

public AgentWorkplace agentWorkplace()
	{ return _agentWorkplace; }

	public void setAgentWorkplace(AgentWorkplace agentWorkplace)
	{_agentWorkplace = agentWorkplace; }

	private AgentWorkstation _agentWorkstation;

public AgentWorkstation agentWorkstation()
	{ return _agentWorkstation; }

	public void setAgentWorkstation(AgentWorkstation agentWorkstation)
	{_agentWorkstation = agentWorkstation; }

	private AgentMove _agentMove;

public AgentMove agentMove()
	{ return _agentMove; }

	public void setAgentMove(AgentMove agentMove)
	{_agentMove = agentMove; }

	private AgentWorker _agentWorker;

public AgentWorker agentWorker()
	{ return _agentWorker; }

	public void setAgentWorker(AgentWorker agentWorker)
	{_agentWorker = agentWorker; }

	private AgentGroupA _agentGroupA;

public AgentGroupA agentGroupA()
	{ return _agentGroupA; }

	public void setAgentGroupA(AgentGroupA agentGroupA)
	{_agentGroupA = agentGroupA; }

	private AgentGroupB _agentGroupB;

public AgentGroupB agentGroupB()
	{ return _agentGroupB; }

	public void setAgentGroupB(AgentGroupB agentGroupB)
	{_agentGroupB = agentGroupB; }

	private AgentOkolie _agentOkolie;

public AgentOkolie agentOkolie()
	{ return _agentOkolie; }

	public void setAgentOkolie(AgentOkolie agentOkolie)
	{_agentOkolie = agentOkolie; }

	private AgentGroupC _agentGroupC;

public AgentGroupC agentGroupC()
	{ return _agentGroupC; }

	public void setAgentGroupC(AgentGroupC agentGroupC)
	{_agentGroupC = agentGroupC; }
	//meta! tag="end"
}