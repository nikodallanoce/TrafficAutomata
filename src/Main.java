import engine.*;
import engine.roads.DeadEndRoad;
import engine.roads.Road;
import engine.roads.Straight;
import engine.roads.YCross;

public class Main {
    public static void main(String[] args) throws Exception {
        var restart = new YCross();
        /*Road straight = new Straight(3, 10, 0.2, 2, 0.05, 1, 1, restart);
        Road ycross = new YCross(5, straight);
        Road start = new Straight(3, 10, 0.2, 2, 0.05, 1, 1, ycross);
        restart.setOutgoing(start);
        RoadsUpdater roadUpdater1 = new RoadsUpdater(start, ycross);
        RoadsUpdater roadUpdater2 = new RoadsUpdater(straight, restart);
        Scenario scenario = new Scenario(roadUpdater1, roadUpdater2);
        scenario.run(30);*/
        Road road = new Straight(2, 10, 0, 3, 0.05, 1, 1, new DeadEndRoad());
        for (int i=0;i<5;i++) {
            road.insertVehicle(new Car(), 0, i);
        }
        System.out.println(road+"\n");
        for (int i=0;i<5;i++) {
            road.runStep();
            System.out.println(road+"\n");
        }
    }
}
