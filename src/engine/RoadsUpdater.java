package engine;

import engine.roads.Road;

import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class RoadsUpdater implements Runnable{
    private final List<Road> roads;
    private static int seq;
    private final int ID;

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    private boolean finished;

    public void setBarrier(CyclicBarrier barrier) {
        this.barrier = barrier;
    }

    private CyclicBarrier barrier;

    public RoadsUpdater(Road... roads){
        this.ID= seq++;
        this.roads = List.of(roads);
    }

    public List<Road> getRoads() {
        return roads;
    }

    @Override
    public void run() {
        while (!finished) {
            for (var road : roads) {
                road.runStep();
            }
            try {
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
