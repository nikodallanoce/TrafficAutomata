package pkg;

public class Car implements Vehicle{

    private String name;
    private int length;

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
        this.name = name;
        this.length = length;
        this.position = position;
    }

    @Override
    public String toString() {
        return "Car{" +
                "name='" + name + '\'' +
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
