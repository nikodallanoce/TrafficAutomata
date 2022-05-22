import engine.*;
import engine.roads.DeadEndRoad;
import engine.roads.Road;
import engine.roads.Straight;
import engine.roads.YCross;

public class Main {

    private static void overtakeScenario(int runs) throws Exception {
        var restart = new YCross();
        Road straight = new Straight(3, 10, 0, 2, 0.05, 1, 1, restart);
        straight.insertVehicle(new Car(), 0, 0);
        straight.insertVehicle(new Car(), 2, 0);
        straight.insertVehicle(new Car(), 0, 1);
        straight.insertVehicle(new Car(), 2, 1);
        Road ycross = new YCross(5, straight);
        Road start = new Straight(3, 10, 0, 2, 0.05, 1, 1, ycross);
        start.insertVehicle(new Car(), 0, 9);
        start.insertVehicle(new Car(), 1, 9);
        start.insertVehicle(new Car(), 2, 9);
        restart.setOutgoing(start);
        Scenario scenario = new Scenario(start, 2, 0);
        scenario.run(runs);
    }

    private static void pingPongScenario(int runs) throws Exception {
        Road road = new Straight(3, 10, 0, 3, 0.05, 1, 1, new DeadEndRoad());
        for (int i=0;i<8;i++) {
            road.insertVehicle(new Car(), 0, i);
        }
        System.out.println(road+"\n");
        for (int i=0;i<runs;i++) {
            road.runStep();
            System.out.println(road+"\n");
        }
        Scenario scenario = new Scenario(road, 2, 0);
    }

    public static void main(String[] args) throws Exception {
        //overtakeScenario(5);
        //pingPongScenario(5);
        var restart = new YCross();
        Road straight = new Straight(2, 100, 0.1, 5, 0.1, 0.5, 1, restart);
        Road ycross = new YCross(2, straight);
        Road start = new Straight(5, 100, 0.9, 5, 0.1, 0.5, 1, ycross);
        restart.setOutgoing(start);
        Scenario scenario = new Scenario(start, 2, 0);
        scenario.run(1000);
    }
}
