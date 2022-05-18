import pkg.*;

public class Main {
    public static void main(String[] args) {
        Scenario scenario = new Scenario();

        Road vcross = new Straight(1,6, new RoadFinish());
        Road road = new Straight(1, 10, vcross);
        Road start = new Straight(1,10, vcross);

        Vehicle v = new Car("pippo", 1, new Position(start, 0, 9));
        Vehicle v1 = new Car("pippo2", 1, new Position(road, 0, 9));
        v.setSpeed(2);
        v1.setSpeed(2);

        scenario.addEntry(new RoadStatusUpdater(start));
        scenario.addEntry(new RoadStatusUpdater(road));
        scenario.addEntry(new RoadStatusUpdater(vcross));
        scenario.placeVehicles(v, v1);
        scenario.start();
        //scenario.addEntry(new RoadStatusUpdater(road));


    }
}
