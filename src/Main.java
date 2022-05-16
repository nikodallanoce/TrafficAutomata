import pkg.*;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Scenario scenario = new Scenario();
        Road road = new Straight(1, 5, new RoadFinish());
        Road start = new Straight(1,10, road);
        Vehicle v = new Car("pippo", 1, new Position(start, 0, 9));
        v.setSpeed(2);

        scenario.addEntry(new RoadStatusUpdater(start));
        scenario.addEntry(new RoadStatusUpdater(road));
        scenario.placeVehicles(v);
        scenario.start();
        //scenario.addEntry(new RoadStatusUpdater(road));


    }
}
