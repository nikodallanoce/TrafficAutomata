import engine.*;
import engine.roads.Road;
import engine.roads.Straight;
import engine.roads.YCross;

public class Main {
    public static void main(String[] args) {
        var restart = new YCross();
        Road straight = new Straight(3, 10, 0.2, 2, 0.05, 1, 1, restart);
        Road ycross = new YCross(5, straight);
        Road start = new Straight(3, 10, 0.2, 2, 0.05, 1, 1, ycross);
        restart.setOutgoing(start);
        RoadsUpdater roadUpdater1 = new RoadsUpdater(start, ycross);
        RoadsUpdater roadUpdater2 = new RoadsUpdater(straight, restart);
        Scenario scenario = new Scenario(roadUpdater1, roadUpdater2);
        scenario.run(30);
    }
}
