import engine.*;
import engine.roads.DeadEndRoad;
import engine.roads.Road;
import engine.roads.Straight;
import engine.roads.YCross;

public class Main {
    /*
    TODO:
        _ creare i thread strutturali
        _ print in ordine
     */
    public static void main(String[] args) {
        var restart = new YCross();
        Road straight = new Straight(3, 10, 0.2, 2, 0.05, 1, 1, restart);
        Road ycross = new YCross(5, straight);
        Road start = new Straight(3, 10, 0.2, 2, 0.05, 1, 1, ycross);
        restart.setOutgoing(start);
        //RoadsUpdater roadUpdater1 = new RoadsUpdater(start, restart);
        //RoadsUpdater roadUpdater2 = new RoadsUpdater(ycross, straight);
        Scenario scenario = new Scenario(start, 2);
        scenario.run(3);
    }
}
