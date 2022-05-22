import engine.*;
import engine.roads.DeadEndRoad;
import engine.roads.Road;
import engine.roads.Straight;
import engine.roads.YCross;

public class Main {
    public static void main(String[] args) throws Exception {
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
        RoadsUpdater roadUpdater1 = new RoadsUpdater(start, restart);
        RoadsUpdater roadUpdater2 = new RoadsUpdater(ycross, straight);
        Scenario scenario = new Scenario(roadUpdater1, roadUpdater2);
        scenario.run(10);
        /*Road road = new Straight(3, 10, 0, 3, 0.05, 1, 1, new DeadEndRoad());
        road.insertVehicle(new Car(), 0, 0);
        road.insertVehicle(new Car(), 0, 1);
        road.insertVehicle(new Car(), 2, 0);
        road.insertVehicle(new Car(), 2, 1);
        for (int i=0;i<8;i++) {
            road.insertVehicle(new Car(), 0, i);
        }
        System.out.println(road+"\n");
        for (int i=0;i<5;i++) {
            road.runStep();
            System.out.println(road+"\n");
        }*/
    }
}
