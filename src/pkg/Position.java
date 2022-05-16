package pkg;

public class Position {

    private int x;
    private int y;
    private Road road;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setRoad(Road road) {
        this.road = road;
    }

    public Road getRoad() {
        return road;
    }

    public Position(Road road, int x, int y) {
        this.x = x;
        this.y = y;
        this.road = road;
    }

    @Override
    public String toString() {
        return "Position{" +
                "x=" + x +
                ", y=" + y +
                ", road=" + road +
                '}';
    }
}
