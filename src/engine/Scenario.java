package engine;

import engine.roads.Road;
import engine.roads.Straight;

import java.util.*;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Scenario {
    private List<Thread> threadUpdater;
    private CyclicBarrier barrier;
    private List<RoadsUpdater> roadsUpdaters;
    private int step;
    private List<Road> roads;
    private final Road start;
    private final int sleep;
    private Map<Straight, List<Double>> flows;
    private Map<Straight, List<Double>> averageSpeeds;
    private Map<Straight, List<Double>> densities;

    public Scenario(Road start, int numOfWorkers, int sleep) {
        this.roads = new LinkedList<>();
        this.start = start;
        this.sleep = sleep;
        setup(numOfWorkers);
    }


    public void run(int steps) {
        printStatus();
        threadUpdater.forEach(Thread::start);
        for (step = 1; step < steps; step++) {
            try {
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
        }
        stopThreads();
    }
    /*
    public List<Map<Straight, List<Double>>> run(int steps) {
        this.flows = new HashMap<>();
        this.averageSpeeds = new HashMap<>();
        this.densities = new HashMap<>();
        Map<Straight, List<Double>> changeOfLanes = new HashMap<>();
        for (var road: roads) {
            if (road instanceof Straight) {
                flows.put((Straight) road, new ArrayList<>());
                averageSpeeds.put((Straight) road, new ArrayList<>());
                densities.put((Straight) road, new ArrayList<>());
                changeOfLanes.put((Straight) road, new ArrayList<>());
            }
        }
        printStatus();
        threadUpdater.forEach(Thread::start);
        for (step = 1; step < steps; step++) {
            try {
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
        }
        stopThreads();
        for (var road: flows.keySet()) {
            var lanesChanged = changeOfLanes.get(road);
            lanesChanged.add((double) road.changesOfLane()/steps);
        }
        List<Map<Straight, List<Double>>> output = new ArrayList<>();
        output.add(flows);
        output.add(averageSpeeds);
        output.add(densities);
        output.add(changeOfLanes);
        return output;
    }*/

    public void printStatus() {
        try {
            Thread.sleep(sleep);
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
        Road next = this.start;
        sb.append(next.toString()).append("\n").append("\n");
        next = next.nextRoad();
        while (!Objects.isNull(next) && !next.equals(this.start)) {
            sb.append(next).append("\n").append("\n");
            next = next.nextRoad();
        }
        sb.deleteCharAt(sb.lastIndexOf("\n"));
        sb.append("_____________________________________________").append("\n");
        System.out.println(sb);
        if(step!=0) computeMetrics();
    }

    private void computeMetrics(){
        for (var road: roads) {
            road.computeMetrics(step);
        }
    }

    private void setup(int numOfWorkers) {
        this.roadsUpdaters = setupThreadsWorkload(numOfWorkers);
        this.barrier = new CyclicBarrier(numOfWorkers + 1, this::printStatus);
        this.threadUpdater = new ArrayList<>(numOfWorkers);
        for (var upd : roadsUpdaters) {
            roads.addAll(upd.getRoads());
            upd.setBarrier(barrier);
            Thread thr = new Thread(upd);
            threadUpdater.add(thr);
        }
    }

    private List<RoadsUpdater> setupThreadsWorkload(int numOfWorkers) {
        List<List<Road>> roadsPerThread = new ArrayList<>(numOfWorkers);
        Road next = start.nextRoad();
        int i = 0;
        while (!Objects.isNull(next) && !next.equals(start)) {
            if (roadsPerThread.size() <= i) {
                roadsPerThread.add(new LinkedList<>());
            }
            roadsPerThread.get(i).add(next);
            if (next instanceof Straight) {
                i++;
            }
            next = next.nextRoad();
            i = i % numOfWorkers;
        }
        roadsPerThread.get(roadsPerThread.size() - 1).add(start);
        List<RoadsUpdater> threads = new LinkedList<>();
        roadsPerThread.forEach(lstRoads -> threads.add(new RoadsUpdater(lstRoads)));
        return threads;
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
