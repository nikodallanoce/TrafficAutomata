package engine.vehicles;

import engine.vehicles.Vehicle;

public class Car extends Vehicle {

    public Car() {
        super(1,1);
    }

    public Car(int speed) {
        super(speed,1);
    }
}
