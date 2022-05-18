package pkg;

import java.util.Objects;

public class Straight implements Road {

    private int lanes;
    private int length;
    private Road incoming;
    private Road outgoing;
    private int[][] road;

    private static int seq = 0;
    private final int ID;

    public int getXStartingPoint(){
        return road[0].length-1;
    }

    public int getYStartingPoint(){
        return road.length-1;
    }

    public Straight(int lanes, int length, Road outgoing) {
        this.lanes = lanes;
        this.length = length;
        //this.ingoing = ingoing;
        this.outgoing = outgoing;
        this.ID = seq;
        seq = seq + 1;
        buildRoad();
    }

    public Straight(int lanes, int length){
        this(lanes, length, null);
    }

    public Request handleRequest(Request request){
        Vehicle vehicle = request.getVehicle();
        Position newPos = request.getNewPosition();
        int newX = newPos.getX();
        int newY = newPos.getY() - vehicle.getSpeed();
        if(newY<road.length && newX<road[0].length && newY>=0 && newX>=0) {
            if (road[newY][newX] == 0) {
                road[vehicle.getPosition().getY()][vehicle.getPosition().getX()] = 0;
                road[newY][newX] = 1;
                newPos.setX(newX);
                newPos.setY(newY);
                vehicle.updatePosition(newPos);
            }
        } else if (newY >= road.length || newY<0) {
            var nextRoad = newPos.getRoad().getNextRoad();
            newPos = new Position(nextRoad, nextRoad.getXStartingPoint(), nextRoad.getYStartingPoint());
        }
        return new Request(vehicle, newPos);
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
        Straight straight = (Straight) o;
        return lanes == straight.lanes && length == straight.length && ID == straight.ID && Objects.equals(incoming, straight.incoming) && Objects.equals(outgoing, straight.outgoing);
    }

    @Override
    public String toString() {
        return "Straight{" +
                "ID=" + ID +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(lanes, length, incoming, outgoing, ID);
    }
}
