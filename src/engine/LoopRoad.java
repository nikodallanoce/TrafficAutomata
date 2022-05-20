package engine;

public class LoopRoad extends YCross{
    private Road incoming;
    public LoopRoad(int capacity, Road outgoing, Road incoming) {
        super(capacity, outgoing);
        this.incoming = incoming;
    }
}
