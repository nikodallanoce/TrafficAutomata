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
        Scenario scenario = new Scenario(roadUpdater1, roadUpdater2);
        scenario.run(10);
    }
}
