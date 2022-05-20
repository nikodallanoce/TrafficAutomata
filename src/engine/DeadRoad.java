package engine;

public class DeadRoad extends Road{
    public DeadRoad() {
        super(null);
    }

    @Override
    public void runStep() {
        return;
    }

    @Override
    public boolean acceptVehicle(Vehicle vehicle) {
        return true;
    }
}
