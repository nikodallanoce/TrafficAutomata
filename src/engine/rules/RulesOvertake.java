package engine.rules;

import engine.Position;
import engine.Road;
import engine.Vehicle;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RulesOvertake extends RulesStraight {
    private final double pChangeLane;

    public RulesOvertake(double pDecreaseSpeed, int direction, double pChangeLane) {
        super(pDecreaseSpeed, direction);
        this.pChangeLane = pChangeLane;
    }

    private void changeLane(Road straight) {
        Map<Position, Vehicle> vehiclePositions = straight.vehicles();
        boolean[][] road = straight.roadStatus();
        int lanes = straight.nLanes();
        int length = straight.lanesLength();
        int maxSpeed = straight.maxSpeed();
        Map<Position, Vehicle> vehiclesPreviousPositions = new HashMap<>(vehiclePositions);
        for (var vehiclePosition : vehiclesPreviousPositions.entrySet()) {
            int vehicleLane = vehiclePosition.getKey().lane();
            int vehicleCell = vehiclePosition.getKey().laneCell();
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

    @Override
    public void apply(Road road) {
        //Change lanes if possible
        changeLane(road);

        //Update the cars' speeds and the road state
        super.apply(road);
    }
}
