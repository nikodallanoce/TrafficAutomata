package pkg;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

public class RoadStatusUpdater implements Runnable {
    private List<Road> roadsOfCompetence;
    private BlockingQueue<Request> requests;

    public Scenario getScenario() {
        return scenario;
    }

    public void setScenario(Scenario scenario) {
        this.scenario = scenario;
    }

    private Scenario scenario;
    private static int seq = 0;
    private final int ID;

    public Iterator<Road> roadListIterator() {
        return roadsOfCompetence.listIterator();
    }

    public RoadStatusUpdater(Road... roadsOfCompetence) {
        this.roadsOfCompetence = List.of(roadsOfCompetence);
        this.requests = new LinkedBlockingQueue<>();
        this.ID = seq;
        seq = seq + 1;
    }

    public boolean insertRequest(Request req) {
        return requests.add(req);
    }

    @Override
    public void run() {
        try {
            while (!scenario.isFinished()) {
                if (requests.size() > 0) {
                    Request req = requests.take();
                    Road road = req.getNewPosition().getRoad();
                    Request nextReq = road.handleRequest(req);

                    if (!nextReq.getNewPosition().getRoad().getClass().equals(RoadFinish.class)) {
                        scenario.getUpdaterByRoad(nextReq.getNewPosition().getRoad()).insertRequest(nextReq);
                    }
                    else {
                        var pos = req.getVehicle().getPosition();
                        pos.getRoad().setFreeCells(pos.getX(), pos.getY());
                    }
                    System.out.println(nextReq.getVehicle() + " ->" + nextReq.getNewPosition());
                    Thread.sleep(2000);
                    System.out.println();
                }
                scenario.awaitBarrier();

            }
        } catch (BrokenBarrierException | InterruptedException e) {
            e.printStackTrace();
        }

    }
}
