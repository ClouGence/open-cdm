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
package com.clougence.clouddm.ds.es.execute;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.elasticsearch.client.Node;
import org.elasticsearch.client.NodeSelector;

/** Distribute requests across available nodes without replaying a failed write on another node. */
public final class EsSingleAttemptNodeSelector implements NodeSelector {
    private final AtomicInteger next = new AtomicInteger();

    @Override
    public void select(Iterable<Node> nodes) {
        List<Node> available = new ArrayList<>();
        nodes.forEach(available::add);
        if (available.size() <= 1) {
            return;
        }
        Node selected = available.get(Math.floorMod(next.getAndIncrement(), available.size()));
        var iterator = nodes.iterator();
        while (iterator.hasNext()) {
            if (iterator.next() != selected) {
                iterator.remove();
            }
        }
    }
}
