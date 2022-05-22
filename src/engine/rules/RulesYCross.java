package engine.rules;

import engine.roads.Road;
import engine.vehicles.Vehicle;
import engine.roads.YCross;

import java.util.concurrent.BlockingDeque;

public class RulesYCross extends RulesCross {
    @Override
    public void apply(YCross road) {
        Road outgoing = road.nextRoad();
        BlockingDeque<Vehicle> queue = road.vehiclesQueue();
        var accepted = true;
        while (accepted && queue.size()>0){
            accepted = outgoing.acceptVehicle(queue.element());
            if (accepted) queue.removeFirst();
        }
    }
}
