package pkg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Scenario {

    private Map<Road,RoadStatusUpdater> roadThreadMap;
    private List<Thread> threads;
    private CyclicBarrier barrier;

    public boolean isFinished() {
        return finished;
    }

    private boolean finished;

    public Scenario(){
        this.roadThreadMap = new HashMap<>();
        this.threads= new ArrayList<>();
    }

    public int awaitBarrier() throws BrokenBarrierException, InterruptedException {
        return barrier.await();
    }

    public RoadStatusUpdater getUpdaterByRoad(Road road){
        return roadThreadMap.get(road);
    }

    public void setFinished(boolean value){
        finished = value;
    }

    public void placeVehicles(Vehicle... vehicles){
        var thrs = roadThreadMap.values();
        thrs.forEach(roadStatusUpdater -> {
            threads.add(new Thread(roadStatusUpdater));
            roadStatusUpdater.setScenario(this);
        });
        for (var veh:vehicles){
            var pos = veh.getPosition();
            pos.getRoad().setOccupiedCells(pos.getX(), pos.getY());
            var thr = roadThreadMap.get(pos.getRoad());
            thr.insertRequest(new Request(veh, veh.getPosition()));
        }
    }

    public void start(){
        this.barrier = new CyclicBarrier(threads.size());
        threads.forEach(Thread::start);
    }

    public void addEntry(RoadStatusUpdater thr){
        thr.roadListIterator().forEachRemaining(road -> roadThreadMap.put(road, thr));
    }

}
