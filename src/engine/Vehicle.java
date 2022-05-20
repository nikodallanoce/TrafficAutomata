package engine;

public abstract class Vehicle {

    private final int ID;
    private static int seq;
    private int speed;
    private int size;

    public Vehicle(int speed, int size) {
        this.ID = seq++;
        this.speed = speed;
        this.size = size;
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

    public void setSize(int size) {
        this.size = size;
    }

    public Vehicle(){
        this(1, 1);
    }
}
