package engine;

public abstract class Vehicle {
    private final int ID;
    private static int seq;
    private int speed;
    private final int size;

    public Vehicle(int speed, int size) {
        this.ID = seq++;
        this.speed = speed;
        this.size = size;
    }

    public Vehicle() {
        this(1, 1);
    }

    @Override
    public String toString() {
        return String.format("{Id:%2d,V:%1d}", ID, speed);
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getSize() {
        return size;
    }
}
