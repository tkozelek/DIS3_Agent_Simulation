package simulation;

import OSPABA.ISimDelegate;
import OSPABA.SimState;
import OSPABA.Simulation;
import OSPStat.Stat;
import agents.agentboss.AgentBoss;
import agents.agentgroupa.AgentGroupA;
import agents.agentgroupb.AgentGroupB;
import agents.agentgroupc.AgentGroupC;
import agents.agentmove.AgentMove;
import agents.agentokolie.AgentOkolie;
import agents.agentworker.AgentWorker;
import agents.agentworkplace.AgentWorkplace;
import config.Constants;
import config.Group;
import config.Helper;
import entity.Ids;
import entity.worker.Worker;
import entity.worker.WorkerGroup;
import entity.workstation.Workstation;
import generator.SeedGenerator;
import gui.interfaces.Observable;
import gui.interfaces.Observer;
import gui.model.SimulationData;

import java.util.ArrayList;


public class MySimulation extends OSPABA.Simulation implements ISimDelegate, Observable {
    private final int[] groups;
    private final int workstationCount;
    private final ArrayList<Observer> observers;
    private final SeedGenerator seedGen;
    private final ArrayList<Workstation> workstations;

    private boolean isPaused;
    private int speed;

    // stat
    private Stat statProductTimeInSystemReplication;
    private Stat statProductTimeInSystemTotal;

    private Stat statOrderTimeInSystemReplication;
    private Stat statOrderTimeInSystemTotal;

    private Stat statWorkstationWorkloadTotal;

    private Stat statOrderNotWorkerOnTotal;

    private boolean updateChart;
    private AgentBoss _agentBoss;
    private AgentWorkplace _agentWorkplace;
    private AgentMove _agentMove;
    private AgentWorker _agentWorker;
    private AgentGroupA _agentGroupA;
    private AgentGroupB _agentGroupB;
    private AgentOkolie _agentOkolie;
    private AgentGroupC _agentGroupC;

    public MySimulation(Long seed, int[] groups, int wCount) {
        seedGen = seed == null ? new SeedGenerator() : new SeedGenerator(seed);

        this.groups = groups;
        this.workstationCount = wCount;
        this.observers = new ArrayList<>();
        this.registerDelegate(this);

        this.workstations = new ArrayList<>();
        this.createWorkstations();

        init();
    }

    public void createWorkstations() {
        for (int i = 0; i < workstationCount; i++) {
            workstations.add(new Workstation(this));
        }
    }

    public ArrayList<Workstation> getWorkstations() {
        return this.workstations;
    }

    public ArrayList<Workstation> getFreeWorkstations(int amount) {
        ArrayList<Workstation> freeWorkstations = new ArrayList<>();
        for (Workstation w : workstations) {
            if (w.getCurrentProduct() == null)
                freeWorkstations.add(w);
            if (freeWorkstations.size() >= amount)
                break;
        }
        return freeWorkstations;
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

    public void setSpeed(int newSpeed) {
        this.speed = newSpeed;
        if (newSpeed >= Constants.MAX_SPEED) {
            this.setMaxSimSpeed();
        } else {
            this.setSimSpeed(newSpeed, Constants.TICK);
        }
    }

    public Stat getStatProductTimeInSystemReplication() {
        return statProductTimeInSystemReplication;
    }

    public Stat getStatOrderTimeInSystemReplication() {
        return statOrderTimeInSystemReplication;
    }

    public void togglePauseSimulation() {
        isPaused = !isPaused;
        if (isPaused)
            this.pauseSimulation();
        else
            this.resumeSimulation();
    }

    @Override
    public void prepareSimulation() {
        super.prepareSimulation();
        // Create global statistcis

        this.initTotalStats();
    }

    private void initTotalStats() {
        this.statProductTimeInSystemTotal = new Stat();
        this.statOrderTimeInSystemTotal = new Stat();
        this.statWorkstationWorkloadTotal = new Stat();
        this.statOrderNotWorkerOnTotal = new Stat();
    }

    @Override
    public void prepareReplication() {
        super.prepareReplication();
        // Reset entities, queues, local statistics, etc...

        if (currentReplication() % 50 == 0)
            System.out.println(currentReplication());

        for (Workstation w : workstations) {
            w.reset(this);
        }

        _agentBoss.initOkolie();
        this.initReplicationStats();
        Ids.resetAll();
        this.setSpeed(speed);
        this.notifyObservers();
    }

    private void initReplicationStats() {
        this.statProductTimeInSystemReplication = new Stat();
        this.statOrderTimeInSystemReplication = new Stat();
    }

    @Override
    public void replicationFinished() {
        // Collect local statistics into global, update UI, etc...
        super.replicationFinished();

        this.statProductTimeInSystemTotal.addSample(this.statProductTimeInSystemReplication.mean());
        this.statOrderTimeInSystemTotal.addSample(this.statOrderTimeInSystemReplication.mean());

        for (Workstation w : workstations) {
            this.statWorkstationWorkloadTotal.addSample(w.getStatWorkload().mean());
        }

        this.statOrderNotWorkerOnTotal.addSample(_agentGroupA.group().queueSize());

        _agentGroupA.group().collectStats();
        _agentGroupB.group().collectStats();
        _agentGroupC.group().collectStats();

        this.updateChart = true;
        this.notifyObservers();
        this.updateChart = false;
    }

    @Override
    public void simulationFinished() {
        // Display simulation results
        super.simulationFinished();

        System.out.println("Replications: " + replicationCount());
        System.out.format("Groups: %d %d %d\n", groups[0], groups[1], groups[2]);
        System.out.println(statToString(statOrderTimeInSystemTotal, "Order time total"));
        System.out.println(statToString(statProductTimeInSystemTotal, "Product time total"));
        System.out.println(statToString(statProductTimeInSystemTotal, "Workstation workload total"));
    }

    public String statToString(Stat stat, String name) {
        double[] is = stat.sampleSize() > 2 ? stat.confidenceInterval_95() : new double[]{0, 0};
        return String.format("%s: %.2fh %.2fs <%.2f | %.2f>", name,
                (stat.mean() / 60 / 60),
                (stat.mean()),
                is[0],
                is[1]);
    }

    //meta! userInfo="Generated code: do not modify", tag="begin"
    private void init() {
        setAgentBoss(new AgentBoss(Id.agentBoss, this, null));
        setAgentWorkplace(new AgentWorkplace(Id.agentWorkplace, this, agentBoss()));
        setAgentMove(new AgentMove(Id.agentMove, this, agentWorkplace()));
        setAgentWorker(new AgentWorker(Id.agentWorker, this, agentWorkplace()));
        setAgentGroupA(new AgentGroupA(Id.agentGroupA, this, agentWorker()));
        setAgentGroupB(new AgentGroupB(Id.agentGroupB, this, agentWorker()));
        setAgentOkolie(new AgentOkolie(Id.agentOkolie, this, agentBoss()));
        setAgentGroupC(new AgentGroupC(Id.agentGroupC, this, agentWorker()));
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

    public AgentGroupB agentGroupB() {
        return _agentGroupB;
    }

    public void setAgentGroupB(AgentGroupB agentGroupB) {
        _agentGroupB = agentGroupB;
    }

    public AgentOkolie agentOkolie() {
        return _agentOkolie;
    }

    public void setAgentOkolie(AgentOkolie agentOkolie) {
        _agentOkolie = agentOkolie;
    }

    public AgentGroupC agentGroupC() {
        return _agentGroupC;
    }

    public void setAgentGroupC(AgentGroupC agentGroupC) {
        _agentGroupC = agentGroupC;
    }
    //meta! tag="end"

    @Override
    public void simStateChanged(Simulation simulation, SimState simState) {
    }

    @Override
    public void refresh(Simulation simulation) {
        this.notifyObservers();
    }

    @Override
    public void addObserver(Observer o) {
        this.observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        this.observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for (Observer o : this.observers) {
            o.update(getSimulationData());
        }
    }

    public SimulationData getSimulationData() {
        return new SimulationData(
                currentTime(),
                new Worker[][]{
                        _agentGroupA.group().getWorkers(),
                        _agentGroupB.group().getWorkers(),
                        _agentGroupC.group().getWorkers(),
                },
                _agentOkolie.getOrdersInSystem(),
                this.workstations,
                currentReplication(),
                new Group[]{
                        _agentGroupA.group(),
                        _agentGroupB.group(),
                        _agentGroupC.group(),
                        _agentWorker.group(),
                },
                new Stat[]{
                        statProductTimeInSystemReplication, statProductTimeInSystemTotal
                },
                new Stat[]{
                        statOrderTimeInSystemReplication, statOrderTimeInSystemTotal
                },
                statWorkstationWorkloadTotal,
                statOrderNotWorkerOnTotal,
                updateChart);

    }
}