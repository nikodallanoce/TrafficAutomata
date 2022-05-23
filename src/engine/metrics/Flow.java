package engine.metrics;

import engine.roads.Straight;

import java.util.List;
import java.util.Optional;

public class Flow extends Metric<Straight, Double>{

    public Flow(int intervalOfRecording) {
        super(intervalOfRecording);
    }

    @Override
    public Double getRecord(Straight road) {
        return road.newFlow();
    }
}
