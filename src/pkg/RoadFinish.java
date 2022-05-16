package pkg;

import jdk.jshell.spi.ExecutionControl;

public class RoadFinish implements Road{

    public RoadFinish(){}

    @Override
    public Request handleRequest(Request request) {
        return new Request(request.getVehicle(), null);
    }

    @Override
    public int getXStartingPoint() {
        return 0;
    }

    @Override
    public int getYStartingPoint() {
        return 0;
    }

    @Override
    public void setOccupiedCells(int x, int y) {
        throw new Error("END ROAD");
    }

    @Override
    public Road getNextRoad() {
        throw new Error("END ROAD");
    }

    @Override
    public String toString() {
        return "END";
    }
}
