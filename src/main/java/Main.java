import simulation.MySimulation;

public class Main {
    public static void main(String[] args) {
        MySimulation mySimulation = new MySimulation(null, new int[]{5, 6, 7});
        mySimulation.simulate(50);
    }
}