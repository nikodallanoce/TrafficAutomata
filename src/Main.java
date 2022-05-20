import engine.Road;
import engine.Straight;
import engine.*;

public class Main {
    public static void main(String[] args) {
        Road straight = new Straight(2, 10, 0.0, 5, 0.05, 0.3, 1, new DeadRoad());
        Road ycross = new YCross(2, straight);
        Road start = new Straight(2, 20, 0.5, 5, 0.05, 0.3, 1, ycross);
        RoadsUpdater roadUpdater1 = new RoadsUpdater(start, ycross, straight);
        Scenario scenario = new Scenario(roadUpdater1);
        System.out.println(start.toString());
        System.out.println(straight.toString());
        scenario.run(1);
        /*
        System.out.println(start);
        for (int i=0;i<5;i++) {
            start.runStep();
            System.out.println(start);
        }*/
    }
}
