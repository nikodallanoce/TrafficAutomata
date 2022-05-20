package engine;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class YCross extends Road{

    private BlockingDeque<Vehicle> queue;
    private final int capacity;

    public YCross(int capacity, Road outgoing) {
        super(outgoing);
        this.capacity = capacity;
        this.queue = new LinkedBlockingDeque<>(capacity);
    }

    public YCross(){
        this(Integer.MAX_VALUE, null);
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
        sb.append(getClass().getSimpleName()+" -> ");
        if (queue.isEmpty()){
            sb.append("empty");
        }
        else {
            queue.forEach(sb::append);
        }
        return sb.toString();
    }
}
