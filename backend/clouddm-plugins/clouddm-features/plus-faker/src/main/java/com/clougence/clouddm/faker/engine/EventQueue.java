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

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.clougence.clouddm.faker.generator.BoundQuery;

import lombok.Getter;

/**
 * Data transmission channels between producers and consumers
 * @version : 2022-07-25
 * @author 赵永春 (zyc@hasor.net)
 */
public class EventQueue {

    @Getter
    private final int                             capacity;
    private final BlockingQueue<List<BoundQuery>> dataSet;

    public EventQueue(int capacity){
        this.capacity = capacity;
        this.dataSet = new LinkedBlockingQueue<>(capacity);
    }

    /** Get a load of data, if you don't have it, you can get it back. */
    public List<BoundQuery> tryPoll() {
        return this.dataSet.poll();
    }

    /** Put the data in, return false if it failed or return true */
    public boolean tryOffer(List<BoundQuery> queries) {
        return this.dataSet.offer(queries);
    }

    /** What's the current data on the transmission channel? */
    public int getQueueSize() { return this.dataSet.size(); }

    public void clear() {
        this.dataSet.clear();
    }
}
