package pkg;

public class Car implements Vehicle {
    private int length;
    private static int seq = 0;
    private int car_id;
    private int lane;
    private int cell;

    public int getSpeed() {
        return speed;
    }

    @Override
    public void updatePosition(Position newPosition) {
        this.position = newPosition;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    private int speed;

    private Position position;

    public Car(String name, int length, Position position) {
        this.car_id = seq;
        seq++;
        this.length = length;
        this.position = position;
    }

    public Car(int speed) {
        this.car_id = seq;
        seq++;
        this.speed = speed;
    }

    @Override
    public String toString() {
        return "Car{" +
                "name='" + car_id + '\'' +
                ", position=" + position +
                '}';
    }

    public Car(String name, int length) {
        this(name, length, new Position(null,0, 0));
    }

    public boolean moveForward() {
        return true;
    }

    @Override
    public Position getPosition() {
        return position;
    }
}
