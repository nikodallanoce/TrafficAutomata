package engine.rules;

import engine.Position;
import engine.roads.Straight;
import engine.roads.Road;
import engine.Vehicle;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public abstract class RulesStraight implements RulesSet<Straight> {
    private final double pDecreaseSpeed;
    private final int direction;

    public RulesStraight(double pDecreaseSpeed, int direction) {
        this.pDecreaseSpeed = pDecreaseSpeed;
        this.direction = direction;
    }

    private void updateSpeeds(Road straight) {
        Map<Position, Vehicle> vehiclePositions = straight.vehicles();
        boolean[][] road = straight.roadStatus();
        int length = straight.lanesLength();
        int maxSpeed = straight.maxSpeed();
        for (var vehiclePosition : vehiclePositions.entrySet()) {
            int vehicleLane = vehiclePosition.getKey().lane();
            int vehicleCell = vehiclePosition.getKey().laneCell();
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

    private void updateRoad(Road straight) {
        Map<Position, Vehicle> vehiclePositions = straight.vehicles();
        boolean[][] road = straight.roadStatus();
        int length = straight.lanesLength();
        Road outgoing = straight.nextRoad();
        Map<Position, Vehicle> vehiclesPreviousPositions = new HashMap<>(vehiclePositions);
        for (var vehiclePosition : vehiclesPreviousPositions.entrySet()) {
            int vehicleLane = vehiclePosition.getKey().lane();
            int vehicleCell = vehiclePosition.getKey().laneCell();
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
    public void apply(Straight road) {
        //Update the cars' speeds
        updateSpeeds(road);
        //Update the road state
        updateRoad(road);
    }
}
