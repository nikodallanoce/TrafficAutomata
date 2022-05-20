package engine;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Scenario {

    private List<Thread> threadUpdater;
    private CyclicBarrier barrier;
    private List<RoadsUpdater> roadsUpdaters;

    public Scenario(RoadsUpdater... updaters){
        setup(updaters);
    }

    public void run(int steps){
        threadUpdater.forEach(Thread::start);
        for (int i = 0; i < steps; i++) {
            try {
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
        }
        stopThreads();
    }

    private void setup(RoadsUpdater... updaters){
        int nUpdaters = updaters.length;
        this.roadsUpdaters = List.of(updaters);
        this.barrier = new CyclicBarrier(nUpdaters + 1);
        this.threadUpdater = new ArrayList<>(nUpdaters);
        for (var upd : updaters){
            upd.setBarrier(barrier);
            Thread thr = new Thread(upd);
            threadUpdater.add(thr);
        }
    }

    private void stopThreads(){
        roadsUpdaters.forEach(roadsUpdater -> roadsUpdater.setFinished(true));
        threadUpdater.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

    }


}
