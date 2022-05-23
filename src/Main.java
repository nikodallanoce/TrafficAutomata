import engine.*;
import engine.metrics.*;
import engine.roads.DeadEndRoad;
import engine.roads.Road;
import engine.roads.Straight;
import engine.roads.YCross;
import engine.vehicles.Car;

import java.util.LinkedList;
import java.util.List;

public class Main {
/*
    private static void overtakeScenario(int runs) throws Exception {

        //Build roads
        Road firstStraight = new Straight(3, 10, 0, 2, 0.05, 1, 1, null);
        Road secondStraight = new Straight(3, 10, 0.3, 5, 0.05, 1, 1, null);
        Road firstCross = new YCross(3, secondStraight);
        Road secondCross = new YCross(2, firstStraight);
        firstStraight.setOutgoing(firstCross);
        secondStraight.setOutgoing(secondCross);

        //Fill roads
        firstStraight.insertVehicle(new Car(), 0, 9);
        firstStraight.insertVehicle(new Car(), 1, 9);
        firstStraight.insertVehicle(new Car(), 2, 9);
        secondStraight.insertVehicle(new Car(), 0, 0);
        secondStraight.insertVehicle(new Car(), 2, 0);
        secondStraight.insertVehicle(new Car(), 0, 1);
        secondStraight.insertVehicle(new Car(), 2, 1);

        //Run configuration
        Scenario scenario = new Scenario(firstStraight, 2, 0);
        scenario.run(runs);
    }

    private static void pingPongScenario(int runs) throws Exception {
        Road road = new Straight(3, 10, 0, 3, 0.05, 1, 1, new DeadEndRoad());

        //Fill road
        for (int i=0;i<8;i++) {
            road.insertVehicle(new Car(), 0, i);
        }

        //Run configuration
        Scenario scenario = new Scenario(road, 1, 0);
        scenario.run(runs);
    }*/

    public static void main(String[] args) throws Exception {
        int recInterval = 5;
        List<Metric<Straight, Double>> metrics = new LinkedList<>();
        metrics.add(new AvgSpeed(recInterval));
        metrics.add(new Density(recInterval));
        metrics.add(new Flow(recInterval));
        metrics.add(new ChangesOfLane(1000));
        Road firstStraight = new Straight(3, 50, null, metrics);
        Road secondStraight = new Straight(2, 50, 0.3, 5, 0.1, 0.5, 1, null, metrics);
        Road firstCross = new YCross(3, secondStraight);
        Road secondCross = new YCross(2, firstStraight);
        firstStraight.setOutgoing(firstCross);
        secondStraight.setOutgoing(secondCross);
        Scenario scenario = new Scenario(firstStraight, 2, 0);
        scenario.run(1000);
        System.out.println();
    }
}
