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
package com.clougence.clouddm.faker.generator.loader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.clougence.clouddm.faker.config.FakerEngineConfig;
import com.clougence.clouddm.faker.generator.SqlArg;

/**
 * 反查数据加载器
 * @version : 2022-07-25
 * @author 赵永春 (zyc@hasor.net)
 */
public class PrecociousDataLoaderFactory implements DataLoaderFactory {

    @Override
    public DataLoader createDataLoader(FakerEngineConfig fakerConfig) {
        final DataLoader defaultDataLoader = new DefaultDataLoaderFactory().createDataLoader(fakerConfig);
        final BlockingQueue<Map<String, SqlArg>> precociousDataSet = new LinkedBlockingQueue<>();
        final int precociousSize = 4096;

        return (session, useFor, fakerTable, batchSize) -> {
            if (precociousSize <= 1) {
                return defaultDataLoader.loadSomeData(session, useFor, fakerTable, batchSize);
            }

            List<Map<String, SqlArg>> result = new ArrayList<>();
            for (int i = 0; i < batchSize; i++) {
                if (precociousDataSet.size() < batchSize) {
                    synchronized (this) {
                        if (precociousDataSet.size() < batchSize) {
                            List<Map<String, SqlArg>> someData = defaultDataLoader.loadSomeData(session, useFor, fakerTable, Math.max(precociousSize, batchSize));
                            precociousDataSet.addAll(someData);
                        }
                    }
                }

                Map<String, SqlArg> poll = precociousDataSet.poll();
                if (poll != null && !poll.isEmpty()) {
                    result.add(poll);
                }

            }
            return result;
        };
    }
}
