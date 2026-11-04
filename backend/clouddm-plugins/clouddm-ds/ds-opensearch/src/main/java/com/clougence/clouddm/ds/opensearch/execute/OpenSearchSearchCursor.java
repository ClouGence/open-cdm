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

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import com.clougence.clouddm.ds.opensearch.definition.ui.OpenSearchCommandTemplates;
import com.clougence.clouddm.ds.opensearch.i18n.OpenSearchDsI18nKeys;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.sql.es.EsJsonLiteral;
import com.clougence.sql.es.EsRequest;
import com.clougence.sql.es.EsSearchPagination;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/** One snapshot per authorized execution. Never retry a page or restart a failed cursor. */
final class OpenSearchSearchCursor implements AutoCloseable {
    private final OpenSearchClient           client;
    private final OpenSearchExecutionContext execution;
    private final String                     index;
    private final ObjectNode                 body;
    private final boolean                    pit;
    private final long                       byteLimit;
    private final Map<String, String>        searchParameters = new LinkedHashMap<>();
    private final Map<String, String>        openParameters   = new LinkedHashMap<>();
    private final int                        visibleSortCount;
    private final int                        pageSortCount;
    private String                           cursorId;
    private JsonNode                         after;
    private int                              scrollSize;
    private boolean                          closed;

    static boolean supports(EsRequest request, boolean pit) {
        // Use the shared conservative shape check: no aggregates, offsets, scripts, relative time or lookups.
        if (!EsSearchPagination.supports(request)) {
            return false;
        }
        if (!pit) {
            // Scroll requires accurate total-hit tracking; preserve requests that explicitly disable/limit it.
            for (var parameter : request.parameters()) {
                if (parameter.name().equals("track_total_hits") && !parameter.value().equals("true")) {
                    return false;
                }
            }
            if (request.body() != null) {
                for (var member : request.body().members()) {
                    if (member.name().value().equals("track_total_hits") && (!(member.value() instanceof EsJsonLiteral value) || !value.rawText().equals("true"))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    OpenSearchSearchCursor(OpenSearchClient client, EsRequest request, OpenSearchExecutionContext execution, long byteLimit, boolean pit) throws IOException{
        this.client = client;
        this.execution = execution;
        this.byteLimit = byteLimit;
        this.pit = pit;
        index = request.segments().get(0).value();
        body = (ObjectNode) client.parseBody(request);
        for (var parameter : request.parameters()) {
            String name = parameter.name();
            if (pit && (name.equals("routing") || name.equals("preference"))) {
                openParameters.put(name, parameter.value());
            } else if (!name.equals("size")) {
                searchParameters.put(name, parameter.value());
            }
        }
        searchParameters.put("allow_partial_search_results", "false");
        ArrayNode requested = JsonNodeFactory.instance.arrayNode();
        JsonNode requestedSort = body.get("sort");
        if (requestedSort != null) {
            if (requestedSort.isArray()) {
                requested.addAll((ArrayNode) requestedSort);
            } else {
                requested.add(requestedSort);
            }
        }
        ArrayNode sort = JsonNodeFactory.instance.arrayNode();
        for (JsonNode field : requested) {
            if (field.isObject()) {
                field.properties().forEach(entry -> sort.addObject().set(entry.getKey(), entry.getValue()));
            } else {
                sort.add(field);
            }
        }
        visibleSortCount = sort.size();
        if (pit) {
            openParameters.put("keep_alive", "1m");
            openParameters.put("allow_partial_pit_creation", "false");
            if (sort.isEmpty()) {
                // Preserve native relevance ordering; _shard_doc alone would select different top hits.
                sort.addObject().put("_score", "desc");
            }
            boolean stable = false;
            for (JsonNode field : sort) {
                if ((field.isTextual() && field.asText().equals("_shard_doc")) || field.has("_shard_doc")) {
                    stable = true;
                }
            }
            if (!stable) {
                sort.addObject().put("_shard_doc", "asc");
            }
            body.set("sort", sort);
        } else {
            searchParameters.put("scroll", "1m");
        }
        pageSortCount = sort.size();
        client.retainCursor(execution);
    }

    boolean hasVisibleSort() {
        return visibleSortCount > 0;
    }

    JsonNode nextPage(long remaining) {
        execution.checkActive();
        if (closed) {
            throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_CLIENT_CLOSED);
        }
        if (pit && cursorId == null) {
            OpenSearchJsonResponse opened = client
                .executeCursor("POST", OpenSearchCommandTemplates.indexPath(index) + "/_search/point_in_time", openParameters, null, 1024 * 1024, execution, false);
            captureId(opened.body(), "pit_id", true);
            checkPage(opened);
        }
        OpenSearchJsonResponse response;
        if (pit) {
            body.put("size", (int) Math.min(EsSearchPagination.PAGE_SIZE, remaining));
            body.putObject("pit").put("id", cursorId).put("keep_alive", "1m");
            if (after != null) {
                body.set("search_after", after);
            }
            response = client.executeCursor("POST", "/_search", searchParameters, body, byteLimit, execution, false);
            captureId(response.body(), "pit_id", false);
        } else if (cursorId == null) {
            scrollSize = (int) Math.min(EsSearchPagination.PAGE_SIZE, remaining);
            body.put("size", scrollSize);
            response = client.executeCursor("POST", OpenSearchCommandTemplates.indexPath(index) + "/_search", searchParameters, body, byteLimit, execution, false);
            captureId(response.body(), "_scroll_id", true);
        } else {
            ObjectNode next = JsonNodeFactory.instance.objectNode().put("scroll_id", cursorId).put("scroll", "1m");
            response = client.executeCursor("POST", "/_search/scroll", Map.of(), next, byteLimit, execution, false);
            captureId(response.body(), "_scroll_id", true);
        }
        // A completed response can race cancellation. Retain its latest ID before checking the execution state.
        checkPage(response);
        JsonNode hits = response.body().path("hits").path("hits");
        int maximum = scrollSize;
        if (pit) {
            maximum = body.path("size").intValue();
        }
        if (!hits.isArray() || hits.size() > maximum) {
            throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_RESPONSE_INVALID);
        }
        JsonNode lastSort = after;
        for (JsonNode hit : hits) {
            if (!hit.isObject() || !index.equals(hit.path("_index").asText()) || !hit.path("_id").isTextual()) {
                throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_RESPONSE_INVALID);
            }
            if (pit) {
                JsonNode sort = hit.path("sort");
                if (!sort.isArray() || sort.size() != pageSortCount) {
                    throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_RESPONSE_INVALID);
                }
                if (sort.equals(lastSort)) {
                    throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_CURSOR_NOT_ADVANCING);
                }
                lastSort = sort;
                ObjectNode document = (ObjectNode) hit;
                if (visibleSortCount == 0) {
                    document.remove("sort");
                } else {
                    ArrayNode visible = JsonNodeFactory.instance.arrayNode();
                    for (int i = 0; i < visibleSortCount; i++) {
                        visible.add(sort.get(i));
                    }
                    document.set("sort", visible);
                }
            }
        }
        after = lastSort;
        return response.body();
    }

    private void captureId(JsonNode result, String field, boolean required) {
        if (result == null || !result.isObject()) {
            throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_RESPONSE_INVALID);
        }
        JsonNode id = result.get(field);
        if (id != null && id.isTextual() && !id.asText().isBlank()) {
            cursorId = id.asText();
        } else if (id != null || (required && !result.has("error"))) {
            throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_RESPONSE_INVALID);
        }
    }

    private void checkPage(OpenSearchJsonResponse response) {
        execution.checkActive();
        if (response.status() < 200 || response.status() >= 300) {
            throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_HTTP_ERROR, response.status());
        }
        OpenSearchResultWriter.checkStatus(response.body(), response.status());
    }

    @Override
    public void close() {
        if (closed) {
            return;
        }
        closed = true;
        boolean interrupted = Thread.interrupted();
        try {
            if (cursorId == null) {
                return;
            }
            ObjectNode close = JsonNodeFactory.instance.objectNode();
            String path = "/_search/scroll";
            if (pit) {
                path = "/_search/point_in_time";
                close.putArray("pit_id").add(cursorId);
            } else {
                close.putArray("scroll_id").add(cursorId);
            }
            OpenSearchJsonResponse response = client.executeCursor("DELETE", path, Map.of(), close, 1024 * 1024, new OpenSearchExecutionContext(5000, 0), true);
            boolean success = response.status() == 200 && response.body() != null;
            if (pit) {
                JsonNode pits = response.body();
                if (pits != null) {
                    pits = pits.path("pits");
                    success = success && pits.isArray() && pits.size() == 1 && pits.get(0).path("successful").asBoolean() && cursorId.equals(pits.get(0).path("pit_id").asText());
                }
            } else {
                success = success && response.body().path("succeeded").asBoolean();
            }
            if (!success) {
                throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_CURSOR_CLOSE_FAILED);
            }
        } catch (ThirdPartyApiException e) {
            if (OpenSearchDsI18nKeys.OPENSEARCH_CURSOR_CLOSE_FAILED.equals(e.getMessageKey())) {
                throw e;
            }
            throw ThirdPartyApiException.as().with(e, OpenSearchDsI18nKeys.OPENSEARCH_CURSOR_CLOSE_FAILED);
        } finally {
            client.releaseCursor();
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
