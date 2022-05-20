package engine;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Scenario {

    private List<Thread> threadUpdater;
    private CyclicBarrier barrier;
    private List<RoadsUpdater> roadsUpdaters;
    private Road start;

    public Scenario(Road start, RoadsUpdater... updaters) {
        this.start = start;
        setup(updaters);
    }

    public void run(int steps) {
        threadUpdater.forEach(Thread::start);
        for (int i = 0; i < steps - 1; i++) {
            try {
                printStatus();
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
        }
        stopThreads();
    }

    public void printStatus() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        StringBuilder sb = new StringBuilder();
        var currRoad = start;
        while (!(currRoad instanceof DeadRoad)) {
            sb.append(currRoad.toString());
            currRoad = currRoad.getNextRoad();
        }
        System.out.println(sb.toString());
    }

    private void setup(RoadsUpdater... updaters) {
        int nUpdaters = updaters.length;
        this.roadsUpdaters = List.of(updaters);
        this.barrier = new CyclicBarrier(nUpdaters + 1);
        this.threadUpdater = new ArrayList<>(nUpdaters);
        for (var upd : updaters) {
            upd.setBarrier(barrier);
            Thread thr = new Thread(upd);
            threadUpdater.add(thr);
        }
    }

    private void stopThreads() {
        roadsUpdaters.forEach(roadsUpdater -> roadsUpdater.setFinished(true));
        try {
            barrier.await();
            printStatus();
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
