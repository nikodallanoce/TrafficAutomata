package pkg;

import java.util.*;

public class Straight implements Road {
    private final int lanes;
    private final int length;
    private final double density;
    private final int max_v;
    private final double p_decrease_v;
    private final double p_change_lane;
    private boolean[][] road;
    private final int direction;
    private Map<int[], Vehicle> vehiclePositions;
    private double flow = 0;
    private Road outgoing;
    private static int seq = 0;
    private final int road_id;

    public Straight(int lanes, int length, double density, int max_v, double p_decrease_v, double p_change_lane, int direction, Road outgoing) {
        this.lanes = lanes;
        this.length = length;
        this.density = density;
        this.max_v = max_v;
        this.p_decrease_v = p_decrease_v;
        this.p_change_lane = p_change_lane;
        this.direction = direction;
        this.outgoing = outgoing;
        this.road_id = seq;
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

        //Set up the cars randomly on the road and assign a starting speed
        Collections.shuffle(indexes);
        Random rand = new Random();
        for (int i=0;i<density*lanes*length;i++) {
            int index = indexes.get(i);
            int lane_index = index/length;
            int cell_index = index%length;
            int vehicle_v = rand.nextInt(max_v+1);
            Vehicle vehicle = new Car(vehicle_v);
            vehiclePositions.put(new int[]{lane_index, cell_index}, vehicle);
            road[lane_index][cell_index] = true;
        }
    }

    public void run_step() {
        //Change lanes if possible

        //Update the cars' speeds
        for (var vehiclePosition: vehiclePositions.entrySet()) {
            int vehicle_lane = vehiclePosition.getKey()[0];
            int vehicle_cell = vehiclePosition.getKey()[1];
            Vehicle vehicle = vehiclePosition.getValue();
            int distance = 0;

            //Check how much the car can move forward
            while (vehicle_cell + distance + 1 < length && !road[vehicle_lane][(vehicle_cell + distance + 1)]) {
                distance += 1;
            }
            //If the car can move into another road then increase its allowed distance
            if (vehicle_cell + distance + 1 == length) {
                distance++;
            }

            //Increase the car speed if the distance to cover is bigger than its actual speed, and it has not reached the maximum
            if (distance > vehicle.getSpeed() && vehicle.getSpeed() < max_v) {
                int current_speed = vehicle.getSpeed();
                vehicle.setSpeed(current_speed + 1);
            }

            //Set the car speed to the distance if it could not move to his previous velocity
            if (distance < vehicle.getSpeed()) {
                vehicle.setSpeed(distance);
            }

            //Decrease the car speed with a random probability
            Random rand = new Random();
            if (vehicle.getSpeed() > 0 && rand.nextDouble() <= p_decrease_v) {
                int current_speed = vehicle.getSpeed();
                vehicle.setSpeed(current_speed - 1);
            }
        }

        //Update the road state
        Map<int[], Vehicle> vehiclesPreviousPositions = new HashMap<>(vehiclePositions);
        for (var vehiclePosition: vehiclesPreviousPositions.entrySet()) {
            int vehicle_lane = vehiclePosition.getKey()[0];
            int vehicle_cell = vehiclePosition.getKey()[1];
            Vehicle vehicle = vehiclePosition.getValue();
            int vehicle_steps = vehicle.getSpeed();
            if (vehicle_steps != 0) {
                //Remove the car from its current position
                vehiclePositions.remove(vehiclePosition.getKey());
                road[vehicle_lane][vehicle_cell] = false;

                //Check if the car can move into another road, if so then move on
                if (vehicle_cell + vehicle_steps == length) {
                    //Put car inside new road, Niko
                    continue;
                }

                //Move the car into its new position inside the road
                road[vehicle_lane][vehicle_cell + vehicle_steps] = true;
                vehiclePositions.put(new int[]{vehicle_lane, vehicle_cell + vehicle_steps}, vehicle);
            }
        }
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
        return lanes == straight.lanes && length == straight.length && road_id == straight.road_id && Objects.equals(outgoing, straight.outgoing);
    }

    @Override
    public String toString() {
        String output = "";
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
        return output;
    }

    @Override
    public int hashCode() {
        return Objects.hash(lanes, length, outgoing, road_id);
    }

    @Override
    public Request handleRequest(Request request) {
        return null;
    }
}
