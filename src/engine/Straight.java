package engine;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Straight extends Road {
    private final int lanes;
    private final int length;
    private final double density;
    private final int maxSpeed;
    private final double pDecreaseSpeed;
    private final double pChangeLane;
    private boolean[][] road;
    private final int direction;
    private Map<Position, Vehicle> vehiclePositions;
    private double flow = 0;
    private static int seq = 0;
    private final int roadId;

    public Straight(int lanes, int length, double density, int maxSpeed, double pDecreaseSpeed, double pChangeLane, int direction, Road outgoing) {
        super(outgoing);
        this.lanes = lanes;
        this.length = length;
        this.density = density;
        this.maxSpeed = maxSpeed;
        this.pDecreaseSpeed = pDecreaseSpeed;
        this.pChangeLane = pChangeLane;
        this.direction = direction;
        this.roadId = seq;
        seq++;
        buildRoad();
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

    private void changeLane() {
        Map<Position, Vehicle> vehiclesPreviousPositions = new HashMap<>(vehiclePositions);
        for (var vehiclePosition : vehiclesPreviousPositions.entrySet()) {
            int vehicleLane = vehiclePosition.getKey().getLane();
            int vehicleCell = vehiclePosition.getKey().getCell();
            Vehicle vehicle = vehiclePosition.getValue();
            int distanceAhead = 0;
            int distanceAheadOtherLane = 0;
            int distanceBehindOtherLane = 0;
            int otherLane = 1 - vehicleLane;

            //How far ahead the car can move forward
            while (vehicleCell + distanceAhead + 1 < length && !road[vehicleLane][(vehicleCell + distanceAhead + 1)]) {
                distanceAhead += 1;
            }

            //If the adjacent cell in the other lane is free
            if (!road[otherLane][vehicleCell]) {
                //How far ahead in other lane the car can move forward
                while (vehicleCell + distanceAheadOtherLane + 1 < length && !road[otherLane][vehicleCell + distanceAheadOtherLane + 1]) {
                    distanceAheadOtherLane += 1;
                }

                //How free is the other lane behind the car
                while (vehicleCell - distanceBehindOtherLane - 1 >= 0 && !road[otherLane][vehicleCell - distanceBehindOtherLane - 1]) {
                    distanceBehindOtherLane += 1;
                }

                //The car could change lane only if it is convenient in terms of distance
                if (distanceAhead < distanceAheadOtherLane && distanceBehindOtherLane > maxSpeed) {
                    Random rand = new Random();

                    //The car changes lane with pChangeLane probability
                    if (rand.nextDouble() <= pChangeLane) {
                        //Remove car from previous lane
                        vehiclePositions.remove(vehiclePosition.getKey());
                        road[vehicleLane][vehicleCell] = false;

                        //Move the car into its new lane
                        road[otherLane][vehicleCell] = true;
                        vehiclePositions.put(new Position(otherLane, vehicleCell), vehicle);
                    }
                }
            }
        }
    }

    private void updateSpeeds() {
        for (var vehiclePosition : vehiclePositions.entrySet()) {
            int vehicleLane = vehiclePosition.getKey().getLane();
            int vehicleCell = vehiclePosition.getKey().getCell();
            Vehicle vehicle = vehiclePosition.getValue();
            int distance = 0;

            //Check how much the car can move forward
            while (vehicleCell + distance + 1 < length && !road[vehicleLane][(vehicleCell + distance + 1)]) {
                distance += 1;
            }
            //If the car can move into another road then increase its allowed distance
            if (vehicleCell + distance + 1 == length) {
                distance++;
            }

            //Increase the car speed if the distance to cover is bigger than its actual speed, and it has not reached the maximum
            if (distance > vehicle.getSpeed() && vehicle.getSpeed() < maxSpeed) {
                int currentSpeed = vehicle.getSpeed();
                vehicle.setSpeed(currentSpeed + 1);
            }

            //Set the car speed to the distance if it could not move to his previous velocity
            if (distance < vehicle.getSpeed()) {
                vehicle.setSpeed(distance);
            }

            //Decrease the car speed with a random probability
            Random rand = new Random();
            if (vehicle.getSpeed() > 0 && rand.nextDouble() <= pDecreaseSpeed) {
                int currentSpeed = vehicle.getSpeed();
                vehicle.setSpeed(currentSpeed - 1);
            }
        }
    }

    public void runStep() {
        //List<Vehicle> vehiclesOut = new ArrayList<>();

        //Change lanes if possible
        changeLane();

        //Update the cars' speeds
        updateSpeeds();

        //Update the road state
        Map<Position, Vehicle> vehiclesPreviousPositions = new HashMap<>(vehiclePositions);
        for (var vehiclePosition : vehiclesPreviousPositions.entrySet()) {
            int vehicleLane = vehiclePosition.getKey().getLane();
            int vehicleCell = vehiclePosition.getKey().getCell();
            Vehicle vehicle = vehiclePosition.getValue();
            int vehicleSteps = vehicle.getSpeed();
            if (vehicleSteps != 0) {
                boolean vehicleAccepted = false;
                //Check if the car can move into another road, if so then move on
                if (vehicleCell + vehicleSteps == length) {
                    vehicleAccepted = outgoing.acceptVehicle(vehicle);
                    if (!vehicleAccepted) {
                        vehicleSteps--;
                    }
                }

                //Remove the car from its current position
                vehiclePositions.remove(vehiclePosition.getKey());
                road[vehicleLane][vehicleCell] = false;
                //Move the car into its new position inside the road
                if (!vehicleAccepted) {
                    road[vehicleLane][vehicleCell + vehicleSteps] = true;
                    vehiclePositions.put(new Position(vehicleLane, vehicleCell + vehicleSteps), vehicle);
                }
            }
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
        //String output = "Start Road\n";
        String output= "";
        for (int i = 0; i < lanes; i++) {
            for (int j = 0; j < length; j++) {
                if (!road[i][j]) {
                    output = output.concat("_");
                } else {
                    Position vehicleKey = new Position(0,0);
                    for (var key : vehiclePositions.keySet()) {
                        if (key.getLane() == i && key.getCell() == j) {
                            vehicleKey = key;
                        }
                    }
                    Vehicle vehicle = vehiclePositions.get(vehicleKey);
                    //output = output.concat(String.valueOf(vehicle.getSpeed()));
                    output = output.concat(vehicle.toString());
                }
            }
            if (i != lanes - 1) {
                output = output.concat("\n");
            }
        }
        //output = output.concat("\nEnd Road\n");
        return output;
    }
}
