/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
