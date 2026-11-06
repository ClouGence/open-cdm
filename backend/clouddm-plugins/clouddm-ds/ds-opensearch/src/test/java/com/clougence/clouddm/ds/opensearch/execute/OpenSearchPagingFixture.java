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
package com.clougence.clouddm.ds.opensearch.execute;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sun.net.httpserver.HttpExchange;

/** Deterministic protocol peer, not a substitute for multi-shard OpenSearch integration. */
final class OpenSearchPagingFixture implements Function<HttpExchange, OpenSearchResponse> {
    final ObjectMapper   mapper        = new ObjectMapper();
    final List<JsonNode> searchBodies  = new ArrayList<>();
    final List<String>   cleared       = new ArrayList<>();
    int                  liveDocuments = 2505;
    int                  snapshotDocuments;
    int                  page;
    int                  offset;
    int                  scrollSize;
    int                  failPage;
    int                  emptyPage;
    int                  repeatSortAt  = -1;
    int                  blockPage;
    boolean              pit;
    boolean              sorted;
    boolean              routedDuplicateIds;
    boolean              closeFailure;
    boolean              blockClose;
    boolean              partialOpen;
    String               payload       = "";
    String               latestId;
    final CountDownLatch arrived       = new CountDownLatch(1);
    final CountDownLatch release       = new CountDownLatch(1);
    final CountDownLatch closing       = new CountDownLatch(1);
    final CountDownLatch releaseClose  = new CountDownLatch(1);

    @Override
    public OpenSearchResponse apply(HttpExchange exchange) {
        OpenSearchResponse metadata = OpenSearchHttpStub.metadata(exchange);
        if (metadata != null) {
            return metadata;
        }
        try {
            String path = exchange.getRequestURI().getPath();
            String method = exchange.getRequestMethod();
            JsonNode body = mapper.readTree((String) exchange.getAttribute("requestBody"));
            if (method.equals("DELETE")) {
                closing.countDown();
                if (blockClose) {
                    releaseClose.await(5, TimeUnit.SECONDS);
                }
                String field = "scroll_id";
                if (path.equals("/_search/point_in_time")) {
                    field = "pit_id";
                }
                String id = body.path(field).get(0).asText();
                cleared.add(id);
                if (pit) {
                    return OpenSearchHttpStub.json(200, "{\"pits\":[{\"pit_id\":\"" + id + "\",\"successful\":" + !closeFailure + "}]}");
                }
                return OpenSearchHttpStub.json(200, "{\"succeeded\":" + !closeFailure + ",\"num_freed\":2}");
            }
            if (path.equals("/books/_search/point_in_time")) {
                pit = true;
                snapshotDocuments = liveDocuments;
                latestId = "pit-open";
                int failures = 0;
                if (partialOpen) {
                    failures = 1;
                }
                return OpenSearchHttpStub.json(200, "{\"pit_id\":\"pit-open\",\"_shards\":{\"total\":2,\"successful\":2,\"failed\":" + failures + "}}");
            }
            if (!pit && page == 0) {
                snapshotDocuments = liveDocuments;
                scrollSize = body.path("size").asInt();
            }
            searchBodies.add(body);
            page++;
            if (page == blockPage) {
                arrived.countDown();
                release.await(5, TimeUnit.SECONDS);
            }
            if (page == failPage) {
                return OpenSearchHttpStub.json(500, "{\"error\":{\"type\":\"search_phase_execution_exception\"}}");
            }
            latestId = "scroll-" + page;
            int size = scrollSize;
            if (pit) {
                latestId = "pit-" + page;
                size = body.path("size").asInt();
            }
            int from = offset;
            int to = Math.min(snapshotDocuments, from + size);
            if (page == emptyPage) {
                to = from;
            }
            ObjectNode result = mapper.createObjectNode();
            if (pit) {
                result.put("pit_id", latestId);
            } else {
                result.put("_scroll_id", latestId);
            }
            result.putObject("_shards").put("total", 2).put("successful", 2).put("failed", 0);
            ObjectNode hits = result.putObject("hits");
            hits.putObject("total").put("value", snapshotDocuments).put("relation", "eq");
            var values = hits.putArray("hits");
            for (int i = from; i < to; i++) {
                var hit = values.addObject().put("_index", "books").put("_id", "doc-" + i).put("_score", 1.0);
                if (routedDuplicateIds) {
                    // Two shard-local documents share an ID, including the pair at the page boundary.
                    hit.put("_id", "doc-" + ((i + 1) / 2));
                    hit.put("_routing", "route-" + (i % 2));
                }
                hit.putObject("_source").put("id", i).put("value", "中文 😀").put("payload", payload).put("large", "18446744073709551615");
                if (pit) {
                    // Equal primary keys across simulated shards; tie values also exercise 64-bit JSON transport.
                    var sort = hit.putArray("sort");
                    if (sorted) {
                        sort.add(i / 7);
                    } else {
                        sort.add(1.0);
                    }
                    long shardDoc = 9007199254740993L + i;
                    if (i == repeatSortAt) {
                        shardDoc--;
                    }
                    sort.add(shardDoc);
                } else if (sorted) {
                    hit.putArray("sort").add(i / 7);
                }
            }
            offset = to;
            if (page == 1) {
                liveDocuments += 5;
            } // Writes after opening cannot enter the snapshot.
            return OpenSearchHttpStub.json(200, result.toString());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new AssertionError(e);
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }
}
