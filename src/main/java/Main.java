import simulation.MySimulation;

public class Main {
    public static void main(String[] args) {
        MySimulation mySimulation = new MySimulation(null, new int[]{2, 6, 7}, 10);
        mySimulation.simulate(50);
    }
}