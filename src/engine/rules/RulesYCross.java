package engine.rules;

import engine.roads.Road;
import engine.Vehicle;
import engine.roads.YCross;

import java.util.concurrent.BlockingDeque;

public class RulesYCross extends RulesCross {
    @Override
    public void apply(Road road) {
        Road outgoing = road.nextRoad();
        YCross roadCross = (YCross) road;
        BlockingDeque<Vehicle> queue = roadCross.vehiclesQueue();
        var accepted = true;
        while (accepted && queue.size()>0){
            accepted = outgoing.acceptVehicle(queue.element());
            if (accepted) queue.removeFirst();
        }
    }
}
