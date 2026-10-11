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

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import com.clougence.clouddm.ds.es.definition.ui.EsCommandTemplates;
import com.clougence.clouddm.ds.es.i18n.EsDsI18nKeys;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.sql.es.EsRequest;
import com.clougence.sql.es.EsSearchPagination;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/** One authorized index and one PIT per execution. A failed page is never retried or restarted. */
final class EsSearchCursor implements AutoCloseable {
    private final EsClient            client;
    private final EsExecutionContext  execution;
    private final String              index;
    private final ObjectNode          body;
    private final Map<String, String> searchParameters = new LinkedHashMap<>();
    private final Map<String, String> openParameters   = new LinkedHashMap<>();
    private final long                byteLimit;
    private final int                 visibleSortCount;
    private String                    pit;
    private JsonNode                  after;

    EsSearchCursor(EsClient client, EsRequest request, EsExecutionContext execution, long byteLimit) throws IOException{
        this.client = client;
        this.execution = execution;
        this.byteLimit = byteLimit;
        index = request.segments().get(0).value();
        body = (ObjectNode) client.parseBody(request);
        for (var parameter : request.parameters()) {
            String name = parameter.name();
            if (name.equals("routing") || name.equals("preference")) {
                openParameters.put(name, parameter.value());
            } else if (!name.equals("size")) {
                searchParameters.put(name, parameter.value());
            }
        }
        openParameters.put("keep_alive", "1m");
        searchParameters.put("allow_partial_search_results", "false");
        ArrayNode requestedFields = JsonNodeFactory.instance.arrayNode();
        JsonNode requestedSort = body.get("sort");
        if (requestedSort != null) {
            if (requestedSort.isArray()) {
                requestedFields.addAll((ArrayNode) requestedSort);
            } else {
                requestedFields.add(requestedSort);
            }
        }
        ArrayNode sort = JsonNodeFactory.instance.arrayNode();
        for (JsonNode field : requestedFields) {
            if (field.isObject()) {
                // ES accepts multiple sort fields in one object; each produces its own sort value.
                for (var entry : field.properties()) {
                    sort.addObject().set(entry.getKey(), entry.getValue());
                }
            } else {
                sort.add(field);
            }
        }
        visibleSortCount = sort.size();
        if (sort.isEmpty()) {
            // Native searches default to score descending; _shard_doc alone would change the requested top hits.
            sort.addObject().put("_score", "desc");
        }
        boolean hasTieBreaker = false;
        for (JsonNode field : sort) {
            if ((field.isTextual() && field.asText().equals("_shard_doc")) || field.has("_shard_doc")) {
                hasTieBreaker = true;
            }
        }
        if (!hasTieBreaker) {
            sort.addObject().put("_shard_doc", "asc");
        }
        body.set("sort", sort);
    }

    JsonNode nextPage(long remaining) {
        execution.checkActive();
        if (pit == null) {
            EsResponse opened = client.executeInternal("POST", EsCommandTemplates.indexPath(index) + "/_pit", openParameters, null, 1024 * 1024, execution);
            if (opened.body() == null || !opened.body().isObject()) {
                throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_RESPONSE_INVALID);
            }
            JsonNode id = opened.body().path("id");
            if (!id.isTextual() || id.asText().isEmpty()) {
                throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_RESPONSE_INVALID);
            }
            pit = id.asText();
            EsResultWriter.checkStatus(opened.body(), opened.status());
        }
        body.put("size", (int) Math.min(EsSearchPagination.PAGE_SIZE, remaining));
        body.putObject("pit").put("id", pit).put("keep_alive", "1m");
        if (after != null) {
            body.set("search_after", after);
        }
        EsResponse response = client.executeInternal("POST", "/_search", searchParameters, body, byteLimit, execution);
        JsonNode result = response.body();
        if (result == null || !result.isObject()) {
            throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_RESPONSE_INVALID);
        }
        if (result.has("pit_id")) {
            if (!result.get("pit_id").isTextual() || result.get("pit_id").asText().isEmpty()) {
                throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_RESPONSE_INVALID);
            }
            pit = result.get("pit_id").asText();
        }
        // Capture the latest PIT before checking cancellation, so finally can release it.
        execution.checkActive();
        EsResultWriter.checkStatus(result, response.status());
        JsonNode hits = result.path("hits").path("hits");
        if (!hits.isArray() || hits.size() > body.path("size").asInt()) {
            throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_RESPONSE_INVALID);
        }
        for (JsonNode hit : hits) {
            if (!hit.path("_index").asText().equals(index) || !hit.path("sort").isArray() || hit.path("sort").size() != body.path("sort").size()) {
                throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_RESPONSE_INVALID);
            }
        }
        if (!hits.isEmpty()) {
            JsonNode next = hits.get(hits.size() - 1).get("sort");
            if (next.equals(after)) {
                throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_CURSOR_NOT_ADVANCING);
            }
            after = next;
        }
        // Internal pagination sort values are not part of the user's requested output.
        for (JsonNode hit : hits) {
            ObjectNode document = (ObjectNode) hit;
            if (visibleSortCount == 0) {
                document.remove("sort");
            } else {
                ArrayNode sort = JsonNodeFactory.instance.arrayNode();
                for (int i = 0; i < visibleSortCount; i++) {
                    sort.add(hit.path("sort").get(i));
                }
                document.set("sort", sort);
            }
        }
        return result;
    }

    @Override
    public void close() {
        if (pit == null) {
            return;
        }
        String id = pit;
        pit = null;
        // Cleanup has its own short deadline, including after cancellation or the query deadline.
        boolean interrupted = Thread.interrupted();
        try {
            ObjectNode close = JsonNodeFactory.instance.objectNode().put("id", id);
            EsResponse response = client.executeInternal("DELETE", "/_pit", Map.of(), close, 1024 * 1024, new EsExecutionContext(5000));
            if (response.body() == null || !response.body().path("succeeded").asBoolean()) {
                throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_PIT_CLOSE_FAILED);
            }
        } finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
