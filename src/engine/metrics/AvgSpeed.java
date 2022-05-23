package engine.metrics;

import engine.roads.Straight;

import java.util.Optional;

public class AvgSpeed extends Metric<Straight, Double> {

    public AvgSpeed(int intervalOfRecording) {
        super(intervalOfRecording);
    }

    @Override
    public Double getRecord(Straight road) {
        return road.averageSpeed();
    }


}
