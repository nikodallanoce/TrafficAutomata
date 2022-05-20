import engine.Road;
import engine.Straight;
import engine.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        //Scenario scenario = new Scenario();
        Road road = new Straight(2, 50, 0.3, 5, 0.05, 0.3, 1, null);
        System.out.println(road);
        for (int i=0;i<5;i++) {
            road.runStep();
            System.out.println(road);
        }
    }
}
