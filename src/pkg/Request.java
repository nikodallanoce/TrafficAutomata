package pkg;

public class Request {

    private Vehicle vehicle;

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Position getNewPosition() {
        return newPosition;
    }

    public void setNewPosition(Position newPosition) {
        this.newPosition = newPosition;
    }

    private Position newPosition;

    public Request(Vehicle vehicle, Position newPosition){
        this.vehicle=vehicle;
        this.newPosition=newPosition;
    }

}
