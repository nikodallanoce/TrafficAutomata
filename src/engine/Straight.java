package engine;

import engine.roads.Road;
import engine.rules.RulesOvertake;
import engine.rules.RulesStraight;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Straight extends Road {
    private final int lanes;
    private final int length;
    private final double density;
    private final int maxSpeed;
    private boolean[][] road;
    private Map<Position, Vehicle> vehiclePositions;
    private double flow = 0;

    public Straight(int lanes, int length, double density, int maxSpeed, RulesStraight rules, Road outgoing) {
        super(outgoing, rules);
        this.lanes = lanes;
        this.length = length;
        this.density = density;
        this.maxSpeed = maxSpeed;
        buildRoad();
    }

    public Straight(int lanes, int length, double density, int maxSpeed, double pDecreaseSpeed, double pChangeLane, int direction, Road outgoing) {
        this(lanes, length, density, maxSpeed, new RulesOvertake(pDecreaseSpeed, direction, pChangeLane), outgoing);
    }

    public Straight(int lanes, int length, Road outgoing) {
        this(lanes, length, 0.5, 5, 0.1, 0.3, 1, outgoing);
    }

    private void buildRoad() {
        //Set each cell to empty
        road = new boolean[lanes][length];
        vehiclePositions = new ConcurrentHashMap<>();

        //Fill an array with cell indexes
        ArrayList<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < lanes * length; i++) {
            indexes.add(i);
        }

        //Set up the cars randomly on the road and assign them a starting speed
        Collections.shuffle(indexes);
        Random rand = new Random();
        for (int i = 0; i < density * lanes * length; i++) {
            int index = indexes.get(i);
            int lane_index = index / length;
            int cell_index = index % length;
            int vehicle_v = rand.nextInt(maxSpeed + 1);
            Vehicle vehicle = new Car(vehicle_v);
            vehiclePositions.put(new Position(lane_index, cell_index), vehicle);
            road[lane_index][cell_index] = true;
        }
    }

    @Override
    public boolean acceptVehicle(Vehicle vehicle) {
        List<Integer> freeLanes = new ArrayList<>();
        for (int i=0;i<lanes;i++) {
            if (!road[i][0]) {
                freeLanes.add(i);
            }
        }
        if (freeLanes.size()>0) {
            Collections.shuffle(freeLanes);
            int chosenLane = freeLanes.get(0);
            road[chosenLane][0]=true;
            vehiclePositions.put(new Position(chosenLane, 0), vehicle);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        String output= "";
        for (int i = 0; i < lanes; i++) {
            for (int j = 0; j < length; j++) {
                if (!road[i][j]) {
                    output = output.concat("_");
                } else {
                    Position vehicleKey = new Position(0,0);
                    for (var key : vehiclePositions.keySet()) {
                        if (key.lane() == i && key.laneCell() == j) {
                            vehicleKey = key;
                        }
                    }
                    Vehicle vehicle = vehiclePositions.get(vehicleKey);
                    output = output.concat(vehicle.toString());
                }
            }
            if (i != lanes - 1) {
                output = output.concat("\n");
            }
        }
        return output;
    }

    @Override
    public Map<Position, Vehicle> vehicles() {
        return vehiclePositions;
    }

    @Override
    public int maxSpeed() {
        return maxSpeed;
    }

    @Override
    public int nLanes() {
        return lanes;
    }

    @Override
    public int lanesLength() {
        return length;
    }

    @Override
    public boolean[][] roadStatus() {
        return road;
    }

    public double flow() {
        return flow;
    }
}
