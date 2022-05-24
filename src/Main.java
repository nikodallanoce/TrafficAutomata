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

    private static void overtakeScenario(int runs) throws Exception {
        List<Metric<Straight, Double>> metrics = new LinkedList<>();
        metrics.add(new ChangesOfLane(1));

        //Build roads
        Road firstStraight = new Straight(3, 10, 0, 2, 0.05, 1, 1, null, metrics);
        Road secondStraight = new Straight(3, 10, 0.3, 5, 0.05, 1, 1, null, metrics);
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
        scenario.run(runs, false);
    }

    private static void pingPongScenario(int runs) throws Exception {
        List<Metric<Straight, Double>> metrics = new LinkedList<>();
        metrics.add(new ChangesOfLane(1));
        Road road = new Straight(3, 10, 0, 3, 0.05, 1, 1, new DeadEndRoad(), metrics);

        //Fill road
        for (int i=0;i<8;i++) {
            road.insertVehicle(new Car(), 0, i);
        }

        //Run configuration
        Scenario scenario = new Scenario(road, 1, 0);
        scenario.run(runs, true);
    }

    private static void changeOfLanesMetric() throws Exception {
        int recInterval = 10;
        int[] lanes = {2, 3, 4, 5};
        double[] densities = {0.1, 0.3, 0.5, 0.8};
        for (int lane: lanes) {
            for (double density: densities) {
                double average_changes = 0;
                System.out.println("Lanes: "+lane+", Density: "+density);
                for (int i=0;i<5;i++) {
                    List<Metric<Straight, Double>> metrics = new LinkedList<>();
                    metrics.add(new ChangesOfLane(recInterval));
                    Road firstStraight = new Straight(lane, 50, density, 5, 0.1, 0.5, 1, null, metrics);
                    Road firstCross = new YCross(lane, firstStraight);
                    firstStraight.setOutgoing(firstCross);
                    Scenario scenario = new Scenario(firstStraight, 1, 0);
                    scenario.run(1000, false);
                    double changesLane = firstStraight.changesOfLane();
                    average_changes += changesLane / 1000;
                    System.out.println("Run "+i+", "+"Changes of lane: "+changesLane+", AverageChanges: "+changesLane/1000);
                }
                average_changes /= 5;
                System.out.println("Average Changes of lane configuration: "+average_changes+"\n");
            }
        }
    }

    public static void main(String[] args) throws Exception {
        //pingPongScenario(5);
        int recInterval = 10;
        List<Metric<Straight, Double>> metrics = new LinkedList<>();
        metrics.add(new AvgSpeed(recInterval));
        metrics.add(new Density(recInterval));
        metrics.add(new Flow(recInterval));
        metrics.add(new ChangesOfLane(recInterval));
        Road firstStraight = new Straight(2, 50, 0.5, 5, 0.1, 0.5, 1, null, metrics);
        Road secondStraight = new Straight(2, 50, 0.5, 5, 0.1, 0.5, 1, null, metrics);
        Road firstCross = new YCross(2, secondStraight);
        Road secondCross = new YCross(2, firstStraight);
        firstStraight.setOutgoing(firstCross);
        secondStraight.setOutgoing(secondCross);
        Scenario scenario = new Scenario(firstStraight, 2, 0);
        scenario.run(500, false);
        scenario.printMetrics();
        System.out.println();
    }
}
