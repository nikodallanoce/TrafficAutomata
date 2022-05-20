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

    public boolean acceptVehicle(Vehicle vehicle) {
        boolean acceptedVehicle = queue.offer(vehicle);
        if (acceptedVehicle) {
            vehicle.setSpeed(1);
        }
        return acceptedVehicle;
    }

    @Override
    public void runStep() {
        if(queue.size()>0){
            var accepted = outgoing.acceptVehicle(queue.element());
            if (accepted) queue.removeFirst();
        }
    }
}
