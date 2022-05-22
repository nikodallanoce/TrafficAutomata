package engine.rules;

import engine.Position;
import engine.roads.Straight;
import engine.roads.Road;
import engine.Vehicle;

import java.util.*;

public class RulesOvertake extends RulesStraight {
    private final double pChangeLane;

    public RulesOvertake(double pDecreaseSpeed, int direction, double pChangeLane) {
        super(pDecreaseSpeed, direction);
        this.pChangeLane = pChangeLane;
    }

    private boolean isPositionOccupied(int lane, int cell, Map<Vehicle, Position> positions) {
        for (var position: positions.values()) {
            int positionLane = position.lane();
            int positionCell = position.laneCell();
            if (lane == positionLane && cell == positionCell) {
                return true;
            }
        }
        return false;
    }

    private void overtake(Map.Entry<Vehicle, Position> vehiclePosition, Road straight, List<Integer> adjacentLanes, Map<Vehicle, Position> vehiclesNewLane) {
        int length = straight.lanesLength();
        boolean[][] road = straight.roadStatus();
        int vehicleLane = vehiclePosition.getValue().lane();
        int vehicleCell = vehiclePosition.getValue().laneCell();
        Vehicle vehicle = vehiclePosition.getKey();
        for (int otherLane: adjacentLanes) {
            int distanceAhead = 0;
            int distanceAheadOtherLane = 0;
            int distanceBehindOtherLane = 0;

            //How far ahead the car can move
            while (vehicleCell + distanceAhead + 1 < length && !road[vehicleLane][(vehicleCell + distanceAhead + 1)]) {
                distanceAhead += 1;
            }

            //If the adjacent cell in the other lane is free
            if (!road[otherLane][vehicleCell] && !isPositionOccupied(otherLane, vehicleCell, vehiclesNewLane)) {
                //How far ahead in other lane the car can move forward
                while (vehicleCell + distanceAheadOtherLane + 1 < length && !road[otherLane][vehicleCell + distanceAheadOtherLane + 1]) {
                    distanceAheadOtherLane += 1;
                }

                //How free is the other lane behind the car
                if (vehicleCell - distanceBehindOtherLane == 0) {
                    distanceBehindOtherLane = straight.maxSpeed();
                } else {
                    while (vehicleCell - distanceBehindOtherLane - 1 >= 0 && !road[otherLane][vehicleCell - distanceBehindOtherLane - 1]) {
                        if (vehicleCell - distanceBehindOtherLane - 1 == 0) {
                            distanceBehindOtherLane = straight.maxSpeed();
                            break;
                        } else {
                            distanceBehindOtherLane += 1;
                        }
                    }
                }

                //The car could change lane only if it is convenient in terms of distance
                if (distanceAhead < distanceAheadOtherLane && distanceBehindOtherLane >= straight.maxSpeed()) {
                    Random rand = new Random();

                    //The car changes lane with pChangeLane probability
                    if (rand.nextDouble() <= pChangeLane) {
                        vehiclesNewLane.put(vehicle, new Position(otherLane, vehicleCell));
                        break;
                    }
                }
            }
        }
    }

    private void changeLane(Road straight) {
        Map<Vehicle, Position> vehiclePositions = straight.vehicles();
        Map<Vehicle, Position> vehiclesPreviousPositions = new HashMap<>(vehiclePositions);
        boolean[][] road = straight.roadStatus();
        Map<Vehicle, Position> vehiclesNewLane = new HashMap<>();
        for (var vehiclePosition : vehiclesPreviousPositions.entrySet()) {
            int vehicleLane = vehiclePosition.getValue().lane();

            //Retrieve the lanes in which the car can move
            List<Integer> adjacentLanes = new ArrayList<>();
            if (vehicleLane + 1 < straight.nLanes()) {
                adjacentLanes.add(vehicleLane+1);
            }
            if (vehicleLane - 1 >= 0) {
                adjacentLanes.add(vehicleLane-1);
            }

            //Try changing the lane
            overtake(vehiclePosition, straight, adjacentLanes, vehiclesNewLane);
        }
        for (var vehicleChangedLane: vehiclesNewLane.entrySet()) {
            Vehicle vehicle = vehicleChangedLane.getKey();

            //Previous position
            var previousVehiclePosition  = vehiclePositions.get(vehicle);
            int previousLane = previousVehiclePosition.lane();
            int previousCell = previousVehiclePosition.laneCell();

            //New position
            int newLane = vehicleChangedLane.getValue().lane();
            int newCell = vehicleChangedLane.getValue().laneCell();

            //Remove car from previous lane
            road[previousLane][previousCell] = false;
            vehiclePositions.remove(vehicleChangedLane.getKey());

            //Move the car into its new lane
            road[newLane][newCell] = true;
            vehiclePositions.put(vehicle, new Position(newLane, newCell));
        }
    }

    @Override
    public void apply(Straight road) {
        //Change lanes if possible
        if (road.nLanes()>1) {
            changeLane(road);
        }
        //Update the cars' speeds and the road state
        super.apply(road);
    }
}
