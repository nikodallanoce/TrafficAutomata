package engine;

public class Request {

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Position getNextPosition() {
        return nextPosition;
    }

    public void setNextPosition(Position nextPosition) {
        this.nextPosition = nextPosition;
    }
    private Vehicle vehicle;
    private Position nextPosition;

    public Request(Vehicle vehicle, Position nextPosition) {
        this.vehicle = vehicle;
        this.nextPosition = nextPosition;
    }
}
