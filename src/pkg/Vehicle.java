package pkg;

public interface Vehicle {

    public Position getPosition();
    public int getSpeed();

    public void updatePosition(Position newPosition);
    public void setSpeed(int amount);
}
