package engine.metrics;

import engine.roads.Straight;

public class ChangesOfLane extends Metric<Straight, Double> {
    public ChangesOfLane(int intervalOfRecording) {
        super(intervalOfRecording);
    }

    @Override
    public Double getRecord(Straight road) {
        return (double) road.changesOfLane();
    }
}
