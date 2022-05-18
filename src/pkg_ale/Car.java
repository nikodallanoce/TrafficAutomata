package pkg_ale;

public class Car {
    private static int id=0;
    private int car_id;
    private int x;
    private int y;
    private int v;

    public Car(int x, int y, int v) {
        this.car_id = id + 1;
        id++;
        this.x = x;
        this.y = y;
        this.v = v;
    }

    public void change_speed(int v) {
        this.v = v;
    }

    public int[] position() {
        int[] position = new int[2];
        position[0] = x;
        position[1] = y;
        return position;
    }

    public void change_position(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
