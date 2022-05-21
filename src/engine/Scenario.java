package engine;

import engine.roads.Road;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Scenario {
    private List<Thread> threadUpdater;
    private CyclicBarrier barrier;
    private List<RoadsUpdater> roadsUpdaters;
    private int step;
    private List<Road> roads;

    public Scenario(RoadsUpdater... updaters) {
        this.roads = new LinkedList<>();
        setup(updaters);
    }

    public void run(int steps) {
        printStatus();
        threadUpdater.forEach(Thread::start);
        for (step = 1; step < steps - 1; step++) {
            try {
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
        }
        step++;
        stopThreads();
    }

    public void printStatus() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        StringBuilder sb = new StringBuilder();
        if (step != 0) {
            sb.append("STEP ").append(step);
        } else {
            sb.append("Starting STEP ");
        }
        sb.append("\n");
        roads.forEach(road -> sb.append(road.toString()).append("\n"));
        sb.append("_____________________________________________");
        System.out.println(sb);
    }

    private void setup(RoadsUpdater... updaters) {
        int nUpdaters = updaters.length;
        this.roadsUpdaters = List.of(updaters);
        this.barrier = new CyclicBarrier(nUpdaters + 1, this::printStatus);
        this.threadUpdater = new ArrayList<>(nUpdaters);
        for (var upd : updaters) {
            roads.addAll(upd.getRoads());
            upd.setBarrier(barrier);
            Thread thr = new Thread(upd);
            threadUpdater.add(thr);
        }
    }

    private void stopThreads() {
        roadsUpdaters.forEach(roadsUpdater -> roadsUpdater.setFinished(true));
        try {
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            throw new RuntimeException(e);
        }
        threadUpdater.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

    }


}
