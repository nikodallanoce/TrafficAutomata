package engine.roads;

import engine.Position;
import engine.vehicles.Vehicle;

import java.util.Map;
import java.util.Optional;

public class DeadEndRoad extends Road {
    public DeadEndRoad() {
        super(null, null);
    }

    @Override
    public void runStep() {
    }

    @Override
    public void insertVehicle(Vehicle vehicle, int lane, int cell) throws Exception {
        throw new Exception("No car can be put inside a dead end road");
    }

    @Override
    public boolean acceptVehicle(Vehicle vehicle) {
        return true;
    }

    @Override
    public void setOutgoing(Road outgoing) throws Exception {
        throw new Exception("No road can be put after a dead end road");
    }

    @Override
    public Road nextRoad() {
        return null;
    }

    @Override
    public Map<Vehicle, Position> vehicles() {
        return null;
    }

    @Override
    public int maxSpeed() {
        return 0;
    }

    @Override
    public int nLanes() {
        return 0;
    }

    @Override
    public int lanesLength() {
        return 0;
    }

    @Override
    public boolean[][] roadStatus() {
        return null;
    }

    @Override
    public void computeMetrics(int step) {

    }


    @Override
    public void updateChangesOfLane(int changes) {
    }

    @Override
    public Optional<String> metricsToString() {
        return Optional.empty();
    }
}
