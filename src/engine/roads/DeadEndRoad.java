package engine.roads;

import engine.Position;
import engine.Vehicle;

import java.util.Map;

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
}