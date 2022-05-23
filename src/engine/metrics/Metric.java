package engine.metrics;

import engine.roads.Road;
import engine.roads.Straight;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public abstract class Metric<T extends Road, K extends Number> {

    //protected List<K> records;
    protected int intervalOfRecording;

    protected Metric(int intervalOfRecording){
        this.intervalOfRecording = intervalOfRecording;
    }

    public Optional<K> compute(T road, int step){
        Optional<K> metric = Optional.empty();
        if (step % intervalOfRecording == 0){
            metric = Optional.of(getRecord(road));
            //records.add(metric.get());
        }
        return metric;
    }

    public abstract K getRecord(T road);
}
