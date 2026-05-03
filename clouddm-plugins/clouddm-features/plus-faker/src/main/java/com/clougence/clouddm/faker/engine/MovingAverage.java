package com.clougence.clouddm.faker.engine;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import lombok.Getter;

public class MovingAverage {

    private final Queue<Double> windowData;
    private final int           windowSize;
    private double              sum;
    @Getter
    private double              avg;

    public MovingAverage(int windowSize){
        this.windowSize = windowSize;
        this.windowData = new ConcurrentLinkedQueue<>();
        this.sum = 0;
    }

    public double next(double val) {
        this.sum += val;
        this.windowData.add(val);

        if (this.windowData.size() > this.windowSize) {
            Double remove = this.windowData.remove();
            if (remove != null && this.sum > remove) {
                this.sum -= remove;
            }
        }

        this.avg = this.sum / this.windowData.size();
        return this.avg;
    }

}
