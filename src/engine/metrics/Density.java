package engine.metrics;

import engine.roads.Straight;

public class Density extends Metric<Straight, Double> {
    public Density(int intervalOfRecording) {
        super(intervalOfRecording);
    }
    @Override
    public Double getRecord(Straight road) {
        return road.density();
    }
}
