package pkg;

import java.util.*;

public class Straight implements Road {
    private final int lanes;
    private final int length;
    private final double density;
    private final int maxSpeed;
    private final double pDecreaseSpeed;
    private final double pChangeLane;
    private boolean[][] road;
    private final int direction;
    private Map<int[], Vehicle> vehiclePositions;
    private double flow = 0;
    private Road outgoing;
    private static int seq = 0;
    private final int roadId;

    public Straight(int lanes, int length, double density, int maxSpeed, double pDecreaseSpeed, double pChangeLane, int direction, Road outgoing) {
        this.lanes = lanes;
        this.length = length;
        this.density = density;
        this.maxSpeed = maxSpeed;
        this.pDecreaseSpeed = pDecreaseSpeed;
        this.pChangeLane = pChangeLane;
        this.direction = direction;
        this.outgoing = outgoing;
        this.roadId = seq;
        seq++;
        buildRoad();
    }

    public Straight(int lanes, int length) {
        this(lanes, length, 0.5, 5, 0.1, 0.3, 1, null);
    }

    private void buildRoad() {
        //Set each cell to empty
        road = new boolean[lanes][length];
        vehiclePositions = new HashMap<>();

        //Fill an array with cell indexes
        ArrayList<Integer> indexes= new ArrayList<>();
        for (int i=0;i<lanes*length;i++) {
            indexes.add(i);
        }

        //Set up the cars randomly on the road and assign them a starting speed
        Collections.shuffle(indexes);
        Random rand = new Random();
        for (int i=0;i<density*lanes*length;i++) {
            int index = indexes.get(i);
            int lane_index = index/length;
            int cell_index = index%length;
            int vehicle_v = rand.nextInt(maxSpeed +1);
            Vehicle vehicle = new Car(vehicle_v);
            vehiclePositions.put(new int[]{lane_index, cell_index}, vehicle);
            road[lane_index][cell_index] = true;
        }
    }

    private void changeLane() {
        Map<int[], Vehicle> vehiclesPreviousPositions = new HashMap<>(vehiclePositions);
        for (var vehiclePosition: vehiclesPreviousPositions.entrySet()) {
            int vehicleLane = vehiclePosition.getKey()[0];
            int vehicleCell = vehiclePosition.getKey()[1];
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
                    if (rand.nextDouble()<= pChangeLane) {
                        //Remove car from previous lane
                        vehiclePositions.remove(vehiclePosition.getKey());
                        road[vehicleLane][vehicleCell] = false;

                        //Move the car into its new lane
                        road[otherLane][vehicleCell] = true;
                        vehiclePositions.put(new int[]{otherLane, vehicleCell}, vehicle);
                    }
                }
            }
        }
    }

    private void updateSpeeds() {
        for (var vehiclePosition: vehiclePositions.entrySet()) {
            int vehicleLane = vehiclePosition.getKey()[0];
            int vehicleCell = vehiclePosition.getKey()[1];
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

    public List<Vehicle> run_step() {
        List<Vehicle> vehiclesOut = new ArrayList<>();

        //Change lanes if possible
        changeLane();

        //Update the cars' speeds
        updateSpeeds();

        //Update the road state
        Map<int[], Vehicle> vehiclesPreviousPositions = new HashMap<>(vehiclePositions);
        for (var vehiclePosition: vehiclesPreviousPositions.entrySet()) {
            int vehicleLane = vehiclePosition.getKey()[0];
            int vehicleCell = vehiclePosition.getKey()[1];
            Vehicle vehicle = vehiclePosition.getValue();
            int vehicleSteps = vehicle.getSpeed();
            if (vehicleSteps != 0) {
                //Remove the car from its current position
                vehiclePositions.remove(vehiclePosition.getKey());
                road[vehicleLane][vehicleCell] = false;

                //Check if the car can move into another road, if so then move on
                if (vehicleCell + vehicleSteps == length) {
                    //Put car inside new road, Niko
                    vehiclesOut.add(vehicle);
                    continue;
                }

                //Move the car into its new position inside the road
                road[vehicleLane][vehicleCell + vehicleSteps] = true;
                vehiclePositions.put(new int[]{vehicleLane, vehicleCell + vehicleSteps}, vehicle);
            }
        }
        return vehiclesOut;
    }

    /*public Request handleRequest(Request request) {
        Vehicle vehicle = request.getVehicle();
        Position newPos = request.getNewPosition();
        int newX = newPos.getX();
        int newY = newPos.getY() - vehicle.getSpeed();
        if(newY<road.length && newX<road[0].length && newY>=0 && newX>=0) {
            if (road[newY][newX] == 0) {
                road[vehicle.getPosition().getY()][vehicle.getPosition().getX()] = 0;
                road[newY][newX] = 1;
                newPos.setX(newX);
                newPos.setY(newY);
                vehicle.updatePosition(newPos);
            }
        } else if (newY >= road.length || newY<0) {
            var nextRoad = newPos.getRoad().getNextRoad();
            newPos = new Position(nextRoad, nextRoad.getXStartingPoint(), nextRoad.getYStartingPoint());
        }
        return new Request(vehicle, newPos);
    }*/

    @Override
    public void setOccupiedCells(int x, int y) {
        road[y][x] = true;
    }

    @Override
    public void setFreeCells(int x, int y) {
        road[y][x] = false;
    }

    @Override
    public Road getNextRoad() {
        return outgoing;
    }

    public int getXStartingPoint() {
        return road[0].length-1;
    }

    public int getYStartingPoint() {
        return road.length-1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Straight straight = (Straight) o;
        return lanes == straight.lanes && length == straight.length && roadId == straight.roadId && Objects.equals(outgoing, straight.outgoing);
    }

    @Override
    public String toString() {
        String output = "Start Road\n";
        for (int i=0;i<lanes;i++) {
            for (int j=0;j<length;j++) {
                if (!road[i][j]) {
                    output = output.concat("_");
                } else {
                    int[] vehicleKey = new int[2];
                    for (var key: vehiclePositions.keySet()) {
                        if (key[0]==i && key[1]==j) {
                            vehicleKey = key;
                        }
                    }
                    Vehicle vehicle = vehiclePositions.get(vehicleKey);
                    output = output.concat(String.valueOf(vehicle.getSpeed()));
                }
            }
            if (i!=lanes-1) {
                output = output.concat("\n");
            }
        }
        output = output.concat("End Road\n");
        return output;
    }

    @Override
    public int hashCode() {
        return Objects.hash(lanes, length, outgoing, roadId);
    }

    @Override
    public Request handleRequest(Request request) {
        return null;
    }
}
