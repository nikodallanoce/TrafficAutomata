package engine;

public abstract class Road {
    private Policy policy;
    protected Road outgoing;

    public Road(Road outgoing){
        this.outgoing = outgoing;
    }

    public abstract void runStep();

    public abstract boolean acceptVehicle(Vehicle vehicle);

    public Road getNextRoad() {
        return outgoing;
    }
    public Request handleRequest(Request req){
        return policy.apply(req);
    }

}
