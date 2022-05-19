import pkg.*;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        //Scenario scenario = new Scenario();
        Straight road = new Straight(2, 50, 0.3, 5, 0.05, 0.3, 1, null);
        System.out.println(road);
        for (int i=0;i<5;i++) {
            List<Vehicle> vehicles = road.run_step();
            System.out.println(road);
        }
    }
}
