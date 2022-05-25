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
        //Build roads
        Road firstStraight = new Straight(3, 10, 0, 2, 0.05, 1, 1, null, null);
        Road secondStraight = new Straight(3, 10, 0, 5, 0.05, 1, 1, null, null);
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
        scenario.run(runs, true);
    }

    private static void pingPongScenario(int runs) throws Exception {
        Road road = new Straight(3, 10, 0, 3, 0.05, 1, 1, new DeadEndRoad(), null);

        //Fill road
        for (int i=0;i<8;i++) {
            road.insertVehicle(new Car(), 0, i);
        }

        //Run configuration
        Scenario scenario = new Scenario(road, 1, 0);
        scenario.run(runs, true);
    }

    private static void randomScenario(int runs) throws Exception {
        //Define the metrics
        int recInterval = 10;
        List<Metric<Straight, Double>> metrics = new LinkedList<>();
        metrics.add(new AvgSpeed(recInterval));
        metrics.add(new Density(recInterval));
        metrics.add(new Flow(recInterval));
        metrics.add(new ChangesOfLane(recInterval));

        //Build roads
        Road firstStraight = new Straight(3, 50, 0.5, 5, 0.1, 0.5, 1, null, metrics);
        Road secondStraight = new Straight(2, 50, 0.5, 5, 0.1, 0.5, 1, null, metrics);
        Road firstCross = new YCross(3, secondStraight);
        Road secondCross = new YCross(2, firstStraight);
        firstStraight.setOutgoing(firstCross);
        secondStraight.setOutgoing(secondCross);

        //Run the scenario
        var thr1 = new RoadsUpdater(firstCross, secondStraight);
        var thr2 = new RoadsUpdater(secondCross, firstStraight);
        Scenario scenario = new Scenario(0, thr1, thr2);
        scenario.run(runs, true);
    }

    public static void intersectionScenario(int runs) throws Exception {
        //Define the metrics
        int recInterval = 10;
        List<Metric<Straight, Double>> metrics = new LinkedList<>();
        metrics.add(new ChangesOfLane(recInterval));

        //Build the roads
        Road firstStraight = new Straight(5, 50, 0.5, 5, 0.1, 0.5, 1, null, metrics);
        Road secondStraight = new Straight(2, 50, 0.5, 5, 0.1, 0.5, 1, null, metrics);
        Road prova = new Straight(1, 10, 0.5, 5, 0.1, 0.5, 1, null, metrics);
        Road firstCross = new YCross(5, secondStraight);
        Road secondCross = new YCross(2, firstStraight);
        firstStraight.setOutgoing(firstCross);
        secondStraight.setOutgoing(secondCross);
        prova.setOutgoing(secondCross);

        //Run the scenario
        var thr1 = new RoadsUpdater(firstCross, secondStraight);
        var thr2 = new RoadsUpdater(secondCross, firstStraight);
        var thr3 = new RoadsUpdater(prova);
        Scenario scenario = new Scenario(0, thr1, thr2, thr3);
        scenario.run(runs, true);
    }

    private static void changeOfLanesMetric() throws Exception {
        int recInterval = 10;
        int[] lanes = {2, 3, 4, 5};
        double[] densities = {0.1, 0.3, 0.5, 0.8};
        for (int lane: lanes) {
            for (double density: densities) {
                double average_changes = 0;
                System.out.println("Lanes: " + lane + ", Density: " + density);
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
                    System.out.println("Run "+ i +", " + "Changes of lane: " + changesLane + ", AverageChanges: " + changesLane/1000);
                }
                average_changes /= 5;
                System.out.println("Average Changes of lane configuration: " + average_changes + "\n");
            }
        }
    }

    public static void main(String[] args) {
        int runs_first_scenarios = 5;
        int runs_second_scenarios = 10;
        try {
            overtakeScenario(runs_first_scenarios);
            pingPongScenario(runs_first_scenarios);
            //randomScenario(runs_second_scenarios);
            //intersectionScenario(runs_second_scenarios);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
