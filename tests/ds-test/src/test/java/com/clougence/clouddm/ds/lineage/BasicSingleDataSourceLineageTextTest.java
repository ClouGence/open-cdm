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
package com.clougence.clouddm.ds.lineage;

import java.util.List;
import java.util.function.Predicate;

import com.clougence.clouddm.ds.TextResourceShard;

public abstract class BasicSingleDataSourceLineageTextTest extends BasicLineageTextTest {

    private final String            datasource;
    private final TextResourceShard fixtureShard;

    protected BasicSingleDataSourceLineageTextTest(String resourceDirectory, int shardCount, int shardId){
        String prefix = "lineage/";
        if (!resourceDirectory.startsWith(prefix)) {
            throw new IllegalArgumentException("Lineage resource parent must start with " + prefix + ": " + resourceDirectory);
        }
        String relative = resourceDirectory.substring(prefix.length());
        int separator = relative.indexOf('/');
        this.datasource = separator < 0 ? relative : relative.substring(0, separator);
        this.fixtureShard = new TextResourceShard(resourceDirectory, shardCount, shardId);
    }

    @Override
    protected final String datasource() {
        return datasource;
    }

    @Override
    protected List<String> fixtureResources() {
        return fixtureShard.resourceFiles();
    }

    protected final List<String> fixtureResources(Predicate<String> filter) {
        return fixtureShard.resourceFiles(filter);
    }
}
