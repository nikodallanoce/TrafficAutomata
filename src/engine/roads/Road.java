package engine.roads;

import engine.Position;
import engine.metrics.Metric;
import engine.vehicles.Vehicle;
import engine.rules.RulesSet;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class Road {
    private final RulesSet<Road> rules;
    protected Road outgoing;
    private static int seq = 0;

    public int getRoadId() {
        return roadId;
    }

    private final int roadId;
    protected double flow = 0;
    protected int changesOfLane = 0;

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

    public void setOutgoing(Road outgoing) throws Exception {
        this.outgoing = outgoing;
    }

    public Road nextRoad() {
        return outgoing;
    }

    public abstract Map<Vehicle, Position> vehicles();

    @Override
    public String toString() {
        return "Road Id: "+roadId;
    }

    public abstract int maxSpeed();

    public abstract int nLanes();

    public abstract int lanesLength();

    public abstract boolean[][] roadStatus();

    public abstract void computeMetrics(int step);

    public void updateChangesOfLane(int changes) {
        changesOfLane += changes;
    }

    public int changesOfLane() {
        return changesOfLane;
    }

    public abstract Optional<String> metricsToString();

    public double flow() {
        return flow;
    }
}
