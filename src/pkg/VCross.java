package pkg;

import java.util.Objects;

public class VCross implements Road {

    private Road incoming1;
    private Road incoming2;
    private static int seq = 0;
    private final int ID;
    private int lanes;
    private int length;
    private int[][] road;
    private Road outgoing;

    public VCross(int lanes, int length, Road outgoing) {
        this.incoming1 = incoming1;
        this.incoming2 = incoming2;
        this.outgoing = outgoing;
        this.lanes = lanes;
        this.length = length;
        buildRoad();
        ID = seq;
        seq = seq + 1;
    }

    @Override
    public Request handleRequest(Request request) {
        Vehicle vehicle = request.getVehicle();
        Position newPos = request.getNewPosition();
        int newX = newPos.getX();
        int newY = newPos.getY() - vehicle.getSpeed();
        if (newY < road.length && newX < road[0].length && newY >= 0 && newX >= 0) {
            if (road[newY][newX] == 0) {
                road[vehicle.getPosition().getY()][vehicle.getPosition().getX()] = 0;
                road[newY][newX] = 1;
                newPos.setX(newX);
                newPos.setY(newY);
                vehicle.updatePosition(newPos);
            }
        } else if (newY >= road.length || newY < 0) {
            var nextRoad = newPos.getRoad().getNextRoad();
            newPos = new Position(nextRoad, nextRoad.getXStartingPoint(), nextRoad.getYStartingPoint());
        }
        return new Request(vehicle, newPos);
    }

    public int getXStartingPoint(){
        return road[0].length-1;
    }

    public int getYStartingPoint(){
        return road.length-1;
    }

    @Override
    public void setOccupiedCells(int x, int y) {
        road[y][x] = 1;
    }

    @Override
    public void setFreeCells(int x, int y) {
        road[y][x] = 0;
    }

    @Override
    public Road getNextRoad() {
        return outgoing;
    }

    private void buildRoad() {
        this.road = new int[length][lanes];
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VCross vCross = (VCross) o;
        return ID == vCross.ID && lanes == vCross.lanes && length == vCross.length && Objects.equals(incoming1, vCross.incoming1) && Objects.equals(incoming2, vCross.incoming2) && Objects.equals(outgoing, vCross.outgoing);
    }

    @Override
    public String toString() {
        return "VCross{" +
                "ID=" + ID +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(lanes, length, incoming1, incoming2, outgoing, ID);
    }
}
