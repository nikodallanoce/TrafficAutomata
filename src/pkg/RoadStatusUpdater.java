package pkg;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

public class RoadStatusUpdater implements Runnable {
    private List<Road> roadsOfCompetence;
    private BlockingQueue<Request> requests;
    private ReentrantLock mutex;

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
        this.mutex = new ReentrantLock();
        seq = seq + 1;
    }

    public boolean insertRequest(Request req) {
        return requests.add(req);
        /*boolean success = false;
        try {
            mutex.lock();
            success = requests.add(vehicle);
        } finally {
            mutex.unlock();
        }
        return success;*/
    }

    @Override
    public void run() {
        while (!scenario.isFinished()) {
            try {
                Request req = requests.take();
                if(ID==1&&false){
                    System.out.println();
                }
                Road road = req.getNewPosition().getRoad();
                Request nextReq = road.handleRequest(req);
                if (nextReq.getNewPosition().getRoad().getClass().equals(RoadFinish.class)) {
                    scenario.setFinished(true);
                } else {
                    scenario.getUpdaterByRoad(nextReq.getNewPosition().getRoad()).insertRequest(nextReq);
                }
                System.out.println(nextReq.getVehicle().getPosition() + ""+ nextReq.getNewPosition());
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
