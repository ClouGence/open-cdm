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
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.http.ContentTooLongException;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.*;

import com.clougence.clouddm.ds.es.definition.ui.EsCommandTemplates;
import com.clougence.clouddm.ds.es.i18n.EsDsI18nKeys;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.sql.es.EsOperation;
import com.clougence.sql.es.EsRequest;
import com.clougence.sql.es.EsServerVersion;
import com.clougence.sql.es.EsSqlI18nKeys;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.JsonNodeFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class EsClient implements AutoCloseable {
    private final RestClient                           client;
    private final int                                  requestTimeoutMs;
    private final Object                               stateLock = new Object();
    private final Map<EsResponseListener, Cancellable> pending   = new HashMap<>();
    private final ObjectMapper                         mapper    = JsonMapper.builder()
        .enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, DeserializationFeature.FAIL_ON_TRAILING_TOKENS)
        .disable(JsonNodeFeature.STRIP_TRAILING_BIGDECIMAL_ZEROES)
        .build();
    private volatile EsServerVersion                   serverVersion;
    private boolean                                    closed;

    public EsClient(RestClient client, int requestTimeoutMs){
        this.client = client;
        this.requestTimeoutMs = requestTimeoutMs;
    }

    public EsServerVersion getServerVersion() {
        synchronized (stateLock) {
            checkOpen();
            return serverVersion;
        }
    }

    public EsServerVersion probe() {
        return probe(null);
    }

    EsServerVersion probe(EsExecutionContext context) {
        Request request = new Request("GET", "/");
        JsonNode body = perform(request, "Probe ES server failed", true, context, 1024 * 1024, false).body();
        EsServerVersion version = EsServerVersion.parse(body.path("version").path("number").asText());
        synchronized (stateLock) {
            checkOpen();
            serverVersion = version;
        }
        return version;
    }

    private EsResponse perform(Request request, String msg, boolean probe, EsExecutionContext context, int responseLimit, boolean retainCursorResponse) {
        // Native JSON works across ES 7/8/9; compatibility headers only bridge one major.
        request.setOptions(RequestOptions.DEFAULT.toBuilder()
            .addHeader("Accept", "application/json")
            .setHttpAsyncResponseConsumerFactory(() -> new EsResponseConsumer(responseLimit)));
        EsResponseListener result = new EsResponseListener();
        Cancellable operation;
        synchronized (stateLock) {
            checkOpen();
            if (context != null) {
                context.checkActive();
            }
            operation = client.performRequestAsync(request, result);
            pending.put(result, operation);
        }

        try {
            long timeout = requestTimeoutMs;
            if (context != null) {
                timeout = Math.min(timeout, context.remainingMillis());
            }

            Response response = result.get(timeout, TimeUnit.MILLISECONDS);
            JsonNode body = null;
            if (response.getEntity() != null) {
                body = mapper.readTree(EntityUtils.toByteArray(response.getEntity()));
            }

            if (context != null && !retainCursorResponse) {
                context.checkActive();
            }

            if (probe) {
                String product = response.getHeader("X-Elastic-Product");
                if (body == null || !body.isObject() || body.path("version").has("distribution") || (product != null && !"Elasticsearch".equals(product))
                    || (product == null && !"You Know, for Search".equals(body.path("tagline").asText()))) {
                    throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_VERSION_INVALID);
                }
            }
            return new EsResponse(response.getStatusLine().getStatusCode(), body);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_REQUEST_CANCELLED);
        } catch (TimeoutException e) {
            throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_REQUEST_TIMEOUT);
        } catch (CancellationException e) {
            throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_REQUEST_CANCELLED);
        } catch (ExecutionException | IOException e) {
            Throwable cause = e;
            if (e instanceof ExecutionException && e.getCause() != null) {
                cause = e.getCause();
            }
            // ResponseException and JSON errors can echo credentials or server data; retain only the stack.
            IOException safe = new IOException(msg + " (" + cause.getClass().getSimpleName() + ")");
            safe.setStackTrace(cause.getStackTrace());
            log.error(msg, safe);
            if (cause instanceof CancellationException) {
                throw ThirdPartyApiException.as().with(safe, EsDsI18nKeys.ES_REQUEST_CANCELLED);
            }
            if (cause instanceof ContentTooLongException) {
                throw ThirdPartyApiException.as().with(safe, EsDsI18nKeys.ES_RESPONSE_SIZE_LIMIT, responseLimit);
            }
            if (cause instanceof SocketTimeoutException || cause instanceof ConnectTimeoutException || cause instanceof TimeoutException) {
                throw ThirdPartyApiException.as().with(safe, EsDsI18nKeys.ES_REQUEST_TIMEOUT);
            }
            if (cause instanceof ResponseException responseException) {
                throw ThirdPartyApiException.as().with(safe, EsDsI18nKeys.ES_HTTP_ERROR, responseException.getResponse().getStatusLine().getStatusCode());
            }
            if (probe) {
                throw ThirdPartyApiException.as().with(safe, EsDsI18nKeys.ES_CONNECT_FAILED);
            }
            if (context != null) {
                throw ThirdPartyApiException.as().with(safe, EsDsI18nKeys.ES_QUERY_FAILED);
            }
            throw ThirdPartyApiException.as().with(safe, EsDsI18nKeys.ES_METADATA_FAILED);
        } finally {
            operation.cancel();
            synchronized (stateLock) {
                pending.remove(result);
            }
        }
    }

    List<String> listIndices() {
        JsonNode body = metadata("/_resolve/index/*", Map.of("expand_wildcards", "all"), null);
        return concreteIndices(body);
    }

    private List<String> concreteIndices(JsonNode body) {
        if (body == null || !body.path("indices").isArray()) {
            throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_METADATA_INVALID);
        }
        List<String> names = new ArrayList<>();
        for (JsonNode index : body.path("indices")) {
            if (!index.path("name").isTextual()) {
                throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_METADATA_INVALID);
            }
            if (!index.hasNonNull("data_stream")) {
                names.add(index.path("name").asText());
            }
        }
        return names;
    }

    String checkConcreteIndex(String index, EsExecutionContext context) {
        String path = EsCommandTemplates.indexPath(index);
        if (!concreteIndices(metadata("/_resolve/index" + path, Map.of("expand_wildcards", "all"), context)).contains(index)) {
            throw ThirdPartyApiException.as().with(EsSqlI18nKeys.ES_INDEX_NOT_CONCRETE, index);
        }

        return path;
    }

    JsonNode readIndexDefinition(String index, EsExecutionContext context) {
        String path = checkConcreteIndex(index, context);
        JsonNode response = metadata(path, Map.of(), context);
        if (response == null || !response.isObject() || response.size() != 1 || !response.has(index)) {
            throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_METADATA_INVALID);
        }
        JsonNode definition = response.get(index);
        for (String section : List.of("mappings", "settings", "aliases")) {
            if (!definition.path(section).isObject()) {
                throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_METADATA_INVALID);
            }
        }
        return definition;
    }

    void checkIndex(String index, boolean write, EsExecutionContext context) {
        String path = checkConcreteIndex(index, context);
        JsonNode mappings = metadata(path + "/_mapping", Map.of(), context);
        // A name replaced by an alias after resolution must not expand the authorized scope.
        if (mappings == null || mappings.size() != 1 || !mappings.has(index) || !mappings.path(index).path("mappings").isObject()) {
            throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_METADATA_INVALID);
        }

        checkMapping(mappings.path(index).path("mappings"), write);
        if (write) {
            JsonNode settings = metadata(path + "/_settings", Map.of(), context);
            if (settings == null || settings.size() != 1 || !settings.path(index).path("settings").path("index").isObject()) {
                throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_METADATA_INVALID);
            }
            JsonNode values = settings.path(index).path("settings").path("index");
            for (String name : List.of("default_pipeline", "final_pipeline")) {
                String pipeline = values.path(name).asText("");
                if (!pipeline.isEmpty() && !pipeline.equals("_none")) {
                    throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_IMPLICIT_RESOURCE_UNSUPPORTED);
                }
            }
        }
    }

    private void checkMapping(JsonNode mapping, boolean write) {
        for (String namespace : List.of("properties", "runtime")) {
            mapping.path(namespace).forEach(field -> checkMappingField(field, write));
        }
        for (JsonNode template : mapping.path("dynamic_templates")) {
            template.forEach(options -> {
                if (options.has("mapping")) {
                    checkMappingField(options.path("mapping"), write);
                }
                if (options.has("runtime")) {
                    checkMappingField(options.path("runtime"), write);
                }
            });
        }
        // ES 7 responses may retain the mapping type wrapper.
        mapping.properties().forEach(entry -> {
            if (!List.of("properties", "runtime", "dynamic_templates", "_meta").contains(entry.getKey()) && entry.getValue().has("properties")) {
                checkMapping(entry.getValue(), write);
            }
        });
    }

    private void checkMappingField(JsonNode field, boolean write) {
        String type = field.path("type").asText();
        if (type.equals("lookup") || type.equals("semantic_text") || (write && (type.equals("percolator") || type.contains("{")))) {
            throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_IMPLICIT_RESOURCE_UNSUPPORTED);
        }
        for (String namespace : List.of("properties", "fields")) {
            field.path(namespace).forEach(child -> checkMappingField(child, write));
        }
    }

    private JsonNode metadata(String path, Map<String, String> parameters, EsExecutionContext context) {
        Request request = new Request("GET", path);
        parameters.forEach(request::addParameter);
        return perform(request, "Read ES metadata failed", false, context, 16 * 1024 * 1024, false).body();
    }

    EsResponse execute(EsRequest source, EsOperation operation, long byteLimit, EsExecutionContext context) {
        Request request = new Request(source.method(), source.path());
        for (var parameter : source.parameters()) {
            request.addParameter(parameter.name(), parameter.value());
        }

        if (source.rawBody() != null) {
            request.setJsonEntity(source.rawBody());
        }

        // A missing document is a normal API result; missing indices remain errors.
        if (operation == EsOperation.READ_DOCUMENT || operation == EsOperation.DELETE_DOCUMENT) {
            request.addParameter("ignore", "404");
        }

        int responseLimit = 64 * 1024 * 1024;
        if (byteLimit > 0) {
            responseLimit = (int) Math.min(responseLimit, byteLimit);
        }

        return perform(request, "Execute ES request failed", false, context, responseLimit, false);
    }

    JsonNode parseBody(EsRequest request) throws IOException {
        if (request.rawBody() == null) {
            return mapper.createObjectNode();
        }
        return mapper.readTree(request.rawBody());
    }

    EsResponse executeInternal(String method, String path, Map<String, String> parameters, JsonNode body, long byteLimit, EsExecutionContext context) {
        Request request = new Request(method, path);
        parameters.forEach(request::addParameter);
        if (body != null) {
            request.setJsonEntity(body.toString());
        }
        int limit = 64 * 1024 * 1024;
        if (byteLimit > 0) {
            limit = (int) Math.min(limit, byteLimit);
        }
        return perform(request, "Execute ES cursor request failed", false, context, limit, true);
    }

    public void cancel() {
        synchronized (stateLock) {
            pending.forEach((future, operation) -> {
                future.cancel(false);
                operation.cancel();
            });
        }
    }

    private void checkOpen() {
        if (closed) {
            throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_CLIENT_CLOSED);
        }
    }

    @Override
    public void close() {
        synchronized (stateLock) {
            if (closed) {
                return;
            }
            closed = true;
            cancel();
        }
        try {
            client.close();
        } catch (IOException e) {
            String msg = "Close ES client failed";
            log.error(msg, e);
            throw ThirdPartyApiException.as().with(e, EsDsI18nKeys.ES_CLOSE_FAILED);
        }
    }
}
