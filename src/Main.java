import engine.Road;
import engine.Straight;
import engine.*;

public class Main {
    public static void main(String[] args) {
        var restart = new YCross();
        Road straight = new Straight(2, 5, 0.5, 5, 0.05, 0.3, 1, restart);
        Road ycross = new YCross(2, straight);
        Road start = new Straight(2, 20, 0.5, 5, 0.05, 0.3, 1, ycross);
        restart.setOutgoing(start);
        RoadsUpdater roadUpdater1 = new RoadsUpdater(start, ycross);
        RoadsUpdater roadUpdater2 = new RoadsUpdater(straight, restart);
        Scenario scenario = new Scenario(start, roadUpdater1, roadUpdater2);
        //System.out.println(start.toString());
        //System.out.println(straight.toString());
        scenario.run(10);
        /*
        System.out.println(start);
        for (int i=0;i<5;i++) {
            start.runStep();
            System.out.println(start);
        }*/
    }
}
