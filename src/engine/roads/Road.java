package engine.roads;

import engine.Position;
import engine.Vehicle;
import engine.rules.RulesSet;

import java.util.Map;

public abstract class Road {
    private final RulesSet<Road> rules;
    protected Road outgoing;
    private static int seq = 0;
    private final int roadId;

    public Road(Road outgoing, RulesSet rules) {
        this.outgoing = outgoing;
        this.rules = rules;
        this.roadId = seq;
        seq++;
    }

    public abstract void insertVehicle(Vehicle vehicle, int lane, int cell) throws Exception;

    public void runStep() {
        rules.apply(this);
    }

    public abstract boolean acceptVehicle(Vehicle vehicle);

    public Road nextRoad() {
        return outgoing;
    }

    public abstract Map<Vehicle, Position> vehicles();

    public abstract int maxSpeed();

    public abstract int nLanes();

    public abstract int lanesLength();

    public abstract boolean[][] roadStatus();
}
