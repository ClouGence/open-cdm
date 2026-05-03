/*
 * Copyright 2015-2022 the original author or authors.
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
 * 生产者和消费者之间的 数据传输通道
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

    /** 拿一批数据，如果没有数据可拿返回 null */
    public List<BoundQuery> tryPoll() {
        return this.dataSet.poll();
    }

    /** 放入数据，如果放入失败返回 false 否则返回 true */
    public boolean tryOffer(List<BoundQuery> queries) {
        return this.dataSet.offer(queries);
    }

    /** 传输通道上目前数据多少 */
    public int getQueueSize() { return this.dataSet.size(); }

    public void clear() {
        this.dataSet.clear();
    }
}
