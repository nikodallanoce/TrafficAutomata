package engine.roads;

import engine.Position;
import engine.Vehicle;
import engine.roads.Road;
import engine.rules.RulesSet;
import engine.rules.RulesYCross;

import java.util.Map;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class YCross extends Road {

    private BlockingDeque<Vehicle> queue;
    private final int capacity;

    public YCross(int capacity, Road outgoing, RulesSet<YCross> rules) {
        super(outgoing, rules);
        this.capacity = capacity;
        this.queue = new LinkedBlockingDeque<>(capacity);
    }

    public YCross(int capacity, Road outgoing) {
        this(capacity, outgoing, new RulesYCross());
    }

    public YCross(){
        this(Integer.MAX_VALUE, null, new RulesYCross());
    }

    public void setOutgoing(Road outgoing){
        this.outgoing = outgoing;
    }

    public boolean acceptVehicle(Vehicle vehicle) {
        boolean acceptedVehicle = queue.offer(vehicle);
        if (acceptedVehicle) {
            vehicle.setSpeed(1);
        }
        return acceptedVehicle;
    }

    @Override
    public void runStep() {
        var accepted = true;
        while (accepted && queue.size()>0){
            accepted = outgoing.acceptVehicle(queue.element());
            if (accepted) queue.removeFirst();
        }
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        sb.append(getClass().getSimpleName()).append(" -> ");
        if (queue.isEmpty()){
            sb.append("empty");
        }
        else {
            queue.forEach(sb::append);
        }
        return sb.toString();
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
        return new boolean[0][];
    }

    public BlockingDeque<Vehicle> vehiclesQueue() {
        return queue;
    }

    public int capacity() {
        return capacity;
    }
}
