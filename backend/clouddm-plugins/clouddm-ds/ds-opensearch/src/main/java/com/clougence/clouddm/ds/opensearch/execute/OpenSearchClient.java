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
import java.net.URI;
import java.util.*;
import java.util.concurrent.*;

import javax.net.ssl.SSLException;

import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.core5.http.ConnectionRequestTimeoutException;
import org.apache.hc.core5.http.ContentTooLongException;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.nio.support.AsyncRequestBuilder;
import org.apache.hc.core5.io.CloseMode;

import com.clougence.clouddm.ds.opensearch.definition.ui.OpenSearchCommandTemplates;
import com.clougence.clouddm.ds.opensearch.dsconf.OpenSearchConfig;
import com.clougence.clouddm.ds.opensearch.i18n.OpenSearchDsI18nKeys;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.sql.es.EsRequest;
import com.clougence.sql.es.EsSqlI18nKeys;
import com.clougence.sql.es.RestApiProfile;
import com.clougence.sql.es.RestProduct;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.JsonNodeFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class OpenSearchClient implements AutoCloseable {
    private final CloseableHttpAsyncClient         http;
    private final ScheduledExecutorService         deadlines;
    private final List<URI>                        endpoints;
    private final int                              queueTimeoutMs;
    private final int                              requestTimeoutMs;
    private final int                              maxResponseBytes;
    private final ObjectMapper                     mapper    = JsonMapper.builder()
        .enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, DeserializationFeature.FAIL_ON_TRAILING_TOKENS)
        .disable(JsonNodeFeature.STRIP_TRAILING_BIGDECIMAL_ZEROES)
        .build();
    private final Object                           stateLock = new Object();
    private final Map<OpenSearchExchange, Boolean> pending   = new HashMap<>();
    private int                                    cursors;
    private boolean                                transportClosed;
    private boolean                                closed;
    private long                                   cancellationSequence;
    private int                                    nextEndpoint;
    @Getter
    private volatile String                        serverVersion;

    public OpenSearchClient(CloseableHttpAsyncClient http, ScheduledExecutorService deadlines, List<URI> endpoints, OpenSearchConfig config){
        this.http = http;
        this.deadlines = deadlines;
        this.endpoints = List.copyOf(endpoints);
        queueTimeoutMs = config.getConnectionRequestTimeoutMs();
        requestTimeoutMs = config.getRequestTimeoutMs();
        maxResponseBytes = config.getMaxResponseBytes();
    }

    public String probe() {
        return probe(cancellationSequence());
    }

    long cancellationSequence() {
        synchronized (stateLock) {
            checkActive(cancellationSequence);
            return cancellationSequence;
        }
    }

    String probe(long sequence) {
        int start;
        synchronized (stateLock) {
            checkActive(sequence);
            start = nextEndpoint;
            nextEndpoint = (nextEndpoint + 1) % endpoints.size();
        }
        for (int attempt = 0; attempt < endpoints.size(); attempt++) {
            try {
                OpenSearchResponse response = request(AsyncRequestBuilder.get(endpoints.get((start + attempt) % endpoints.size())), sequence, maxResponseBytes, null, false, false);
                if (response.status() != 200) {
                    throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_HTTP_ERROR, response.status());
                }
                String version;
                try {
                    JsonNode root = mapper.readTree(response.body());
                    if (root == null || !"opensearch".equals(root.path("version").path("distribution").asText())) {
                        throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_VERSION_INVALID);
                    }
                    version = root.path("version").path("number").asText();
                    RestApiProfile.from(RestProduct.OPENSEARCH.parameters(version));
                    if (version.isBlank()) {
                        throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_VERSION_INVALID);
                    }
                } catch (IOException e) {
                    throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_VERSION_INVALID);
                }
                synchronized (stateLock) {
                    checkActive(sequence);
                    serverVersion = version;
                }
                return version;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_REQUEST_CANCELLED);
            } catch (CancellationException e) {
                throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_REQUEST_CANCELLED);
            } catch (ExecutionException e) {
                Throwable cause = e.getCause();
                if (cause instanceof CancellationException) {
                    throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_REQUEST_CANCELLED);
                }
                if (cause instanceof SSLException) {
                    throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_TLS_INVALID);
                }
                if (cause instanceof ContentTooLongException) {
                    throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_RESPONSE_SIZE_LIMIT, maxResponseBytes);
                }
                // Only the read-only product probe can try another node. The HTTP transport itself never replays requests.
                boolean transportFailure = cause instanceof IOException || cause instanceof TimeoutException;
                if (transportFailure && attempt + 1 < endpoints.size()) {
                    continue;
                }
                if (cause instanceof TimeoutException || cause instanceof java.net.SocketTimeoutException || cause instanceof ConnectionRequestTimeoutException) {
                    throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_REQUEST_TIMEOUT);
                }
                // Remote bodies and native exception messages may contain credentials or server data.
                IOException safe = new IOException("OpenSearch connection failed (" + cause.getClass().getSimpleName() + ")");
                safe.setStackTrace(cause.getStackTrace());
                log.error("OpenSearch connection failed", safe);
                throw ThirdPartyApiException.as().with(safe, OpenSearchDsI18nKeys.OPENSEARCH_CONNECT_FAILED);
            }
        }
        throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_CONNECT_FAILED);
    }

    private OpenSearchResponse request(AsyncRequestBuilder request, long sequence, int responseLimit, OpenSearchExecutionContext execution, boolean retainCursorResponse,
                                       boolean cleanup) throws InterruptedException, ExecutionException {
        OpenSearchExchange exchange = new OpenSearchExchange(deadlines, requestTimeoutMs);
        synchronized (stateLock) {
            checkRequest(sequence, cleanup);
            pending.put(exchange, cleanup);
            exchange.startQueue(queueTimeoutMs);
        }
        ScheduledFuture<?> totalDeadline = null;
        try {
            if (execution != null) {
                totalDeadline = deadlines
                    .schedule(() -> exchange.failed(new TimeoutException("OpenSearch execution timed out")), execution.remainingMillis(), TimeUnit.MILLISECONDS);
            }
            HttpClientContext context = HttpClientContext.create();
            context.setUserToken(exchange);
            context.setAttribute(OpenSearchExchange.CONTEXT_ATTRIBUTE, exchange);
            try {
                var future = http.execute(request.build(), new OpenSearchResponseConsumer(responseLimit, exchange), context, exchange);
                exchange.setDependency(() -> future.cancel(true));
            } catch (RuntimeException e) {
                exchange.failed(e);
            }
            OpenSearchResponse response = exchange.await();
            if (!retainCursorResponse) {
                if (execution != null) {
                    execution.checkActive();
                }
                synchronized (stateLock) {
                    checkRequest(sequence, cleanup);
                }
            }
            return response;
        } catch (InterruptedException e) {
            exchange.cancel();
            throw e;
        } finally {
            if (totalDeadline != null) {
                totalDeadline.cancel(false);
            }
            exchange.cancel();
            synchronized (stateLock) {
                pending.remove(exchange);
            }
        }
    }

    List<String> listIndices(OpenSearchExecutionContext context) {
        JsonNode body = metadata("/_resolve/index/*", Map.of("expand_wildcards", "all"), context);
        return concreteIndices(body);
    }

    private List<String> concreteIndices(JsonNode body) {
        if (body == null || !body.path("indices").isArray()) {
            throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_METADATA_INVALID);
        }
        List<String> names = new ArrayList<>();
        for (JsonNode index : body.path("indices")) {
            if (!index.path("name").isTextual()) {
                throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_METADATA_INVALID);
            }
            if (!index.hasNonNull("data_stream")) {
                names.add(index.path("name").asText());
            }
        }
        return names;
    }

    String checkConcreteIndex(String index, boolean allowMissing, OpenSearchExecutionContext context) {
        String path = OpenSearchCommandTemplates.indexPath(index);
        var request = newRequest("GET", "/_resolve/index" + path, context).addParameter("expand_wildcards", "all");
        OpenSearchJsonResponse response = perform(request, context, maxResponseBytes);
        if (allowMissing && response.status() == 404 && response.body() != null && response.body().path("error").path("type").asText().equals("index_not_found_exception")) {
            return path;
        }
        if (response.status() != 200) {
            throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_HTTP_ERROR, response.status());
        }
        List<String> names = concreteIndices(response.body());
        if (allowMissing && names.isEmpty() && response.body().path("indices").isEmpty() && response.body().path("aliases").isArray() && response.body().path("aliases").isEmpty()
            && response.body().path("data_streams").isArray() && response.body().path("data_streams").isEmpty()) {
            return path;
        }
        if (!names.equals(List.of(index))) {
            throw ThirdPartyApiException.as().with(EsSqlI18nKeys.ES_INDEX_NOT_CONCRETE, index);
        }
        return path;
    }

    JsonNode readIndexDefinition(String index, OpenSearchExecutionContext context) {
        String path = checkConcreteIndex(index, false, context);
        JsonNode response = metadata(path, Map.of(), context);
        if (response == null || !response.isObject() || response.size() != 1 || !response.has(index)) {
            throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_METADATA_INVALID);
        }
        JsonNode definition = response.get(index);
        for (String section : List.of("mappings", "settings", "aliases")) {
            if (!definition.path(section).isObject()) {
                throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_METADATA_INVALID);
            }
        }
        return definition;
    }

    void checkIndex(String index, boolean write, boolean search, OpenSearchExecutionContext context) {
        String path = checkConcreteIndex(index, false, context);
        JsonNode mappings = metadata(path + "/_mapping", Map.of(), context);
        // A name replaced by an alias after resolution must not expand the authorized scope.
        if (mappings == null || mappings.size() != 1 || !mappings.has(index) || !mappings.path(index).path("mappings").isObject()) {
            throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_METADATA_INVALID);
        }

        checkMapping(mappings.path(index).path("mappings"), write);
        if (!write && !search) {
            return;
        }
        JsonNode settings = metadata(path + "/_settings", Map.of(), context);
        if (settings == null || settings.size() != 1 || !settings.path(index).path("settings").path("index").isObject()) {
            throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_METADATA_INVALID);
        }
        JsonNode values = settings.path(index).path("settings").path("index");
        if (search) {
            checkSystemSearchPipeline(context);
        }
        String searchPipeline = values.path("search").path("default_pipeline").asText("");
        if (search && !searchPipeline.isEmpty() && !searchPipeline.equals("_none")) {
            throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_IMPLICIT_RESOURCE_UNSUPPORTED);
        }
        for (String name : List.of("default_pipeline", "final_pipeline")) {
            String pipeline = values.path(name).asText("");
            if (write && !pipeline.isEmpty() && !pipeline.equals("_none")) {
                throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_IMPLICIT_RESOURCE_UNSUPPORTED);
            }
        }
    }

    void checkCreateIndex(String index, OpenSearchExecutionContext execution) {
        JsonNode composable = metadata("/_index_template", Map.of(), execution);
        if (!composable.path("index_templates").isArray()) {
            throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_METADATA_INVALID);
        }
        for (JsonNode template : composable.path("index_templates")) {
            checkTemplatePatterns(index, template.path("index_template"));
        }
        // Simulation only resolves composable templates; legacy templates must also be checked.
        JsonNode legacy = metadata("/_template", Map.of(), execution);
        for (JsonNode template : legacy) {
            checkTemplatePatterns(index, template);
        }
    }

    private void checkTemplatePatterns(String index, JsonNode template) {
        JsonNode patterns = template.path("index_patterns");
        if (!patterns.isArray() || patterns.isEmpty()) {
            throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_METADATA_INVALID);
        }
        for (JsonNode pattern : patterns) {
            if (!pattern.isTextual()) {
                throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_METADATA_INVALID);
            }
            String regex = java.util.Arrays.stream(pattern.textValue().split("\\*", -1)).map(java.util.regex.Pattern::quote).collect(java.util.stream.Collectors.joining(".*"));
            if (index.matches(regex)) {
                throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_TEMPLATE_UNSUPPORTED);
            }
        }
    }

    private void checkSystemSearchPipeline(OpenSearchExecutionContext execution) {
        // This cluster feature first exists in 3.3; older versions need no cluster-settings permission.
        String[] version = serverVersion.split("\\.");
        if (version[0].equals("2") || Integer.parseInt(version[1]) < 3) {
            return;
        }
        JsonNode settings = metadata("/_cluster/settings", Map.of("include_defaults", "true", "flat_settings", "true"), execution);
        String key = "cluster.search.enabled_system_generated_factories";
        for (String scope : List.of("transient", "persistent", "defaults")) {
            if (!settings.path(scope).isObject()) {
                throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_METADATA_INVALID);
            }
            JsonNode factories = settings.path(scope).get(key);
            if (factories == null || factories.isNull()) {
                continue;
            }
            // Settings precedence is transient > persistent > node defaults, including an explicit empty list.
            if (!factories.isArray() || !factories.isEmpty()) {
                throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_IMPLICIT_RESOURCE_UNSUPPORTED);
            }
            return;
        }
    }

    private void checkMapping(JsonNode mapping, boolean write) {
        for (String namespace : List.of("properties", "derived")) {
            mapping.path(namespace).forEach(field -> checkMappingField(field, write));
        }
        for (JsonNode template : mapping.path("dynamic_templates")) {
            template.forEach(options -> {
                if (options.has("mapping")) {
                    checkMappingField(options.path("mapping"), write);
                }
                if (options.has("derived")) {
                    checkMappingField(options.path("derived"), write);
                }
            });
        }
    }

    private void checkMappingField(JsonNode field, boolean write) {
        String type = field.path("type").asText();
        if (type.equals("semantic") || field.has("model_id") || (write && (type.equals("percolator") || type.contains("{")))) {
            throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_IMPLICIT_RESOURCE_UNSUPPORTED);
        }
        for (String namespace : List.of("properties", "fields")) {
            field.path(namespace).forEach(child -> checkMappingField(child, write));
        }
    }

    private JsonNode metadata(String path, Map<String, String> parameters, OpenSearchExecutionContext execution) {
        AsyncRequestBuilder request = newRequest("GET", path, execution);
        parameters.forEach(request::addParameter);
        OpenSearchJsonResponse response = perform(request, execution, maxResponseBytes);
        if (response.status() != 200) {
            throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_HTTP_ERROR, response.status());
        }
        if (response.body() == null || !response.body().isObject()) {
            throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_METADATA_INVALID);
        }
        return response.body();
    }

    OpenSearchJsonResponse execute(EsRequest source, long byteLimit, OpenSearchExecutionContext execution) {
        if (source.rawBody() != null && source.method().equals("PUT") && (source.segments().size() == 1 || source.segments().get(1).value().equals("_mapping"))) {
            try {
                JsonNode body = mapper.readTree(source.rawBody());
                if (source.segments().size() == 1) {
                    body = body.path("mappings");
                }
                checkMapping(body, true);
            } catch (IOException e) {
                throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_RESPONSE_INVALID);
            }
        }
        String path = source.path();
        if (!source.parameters().isEmpty()) {
            java.util.StringJoiner parameters = new java.util.StringJoiner("&", "?", "");
            for (var parameter : source.parameters()) {
                parameters.add(java.net.URLEncoder.encode(parameter.name(), java.nio.charset.StandardCharsets.UTF_8) + "="
                               + java.net.URLEncoder.encode(parameter.value(), java.nio.charset.StandardCharsets.UTF_8));
            }
            path += parameters;
        }
        AsyncRequestBuilder request = newRequest(source.method(), path, execution);
        if (source.rawBody() != null) {
            request.setEntity(source.rawBody(), ContentType.APPLICATION_JSON);
        }
        int limit = maxResponseBytes;
        if (byteLimit > 0) {
            limit = (int) Math.min(limit, byteLimit);
        }
        return perform(request, execution, limit);
    }

    private AsyncRequestBuilder newRequest(String method, String path, OpenSearchExecutionContext execution) {
        return newRequest(method, path, execution, false);
    }

    private AsyncRequestBuilder newRequest(String method, String path, OpenSearchExecutionContext execution, boolean cleanup) {
        execution.checkActive();
        URI endpoint;
        synchronized (stateLock) {
            checkRequest(execution.sequence(), cleanup);
            endpoint = endpoints.get(nextEndpoint);
            nextEndpoint = (nextEndpoint + 1) % endpoints.size();
        }
        return AsyncRequestBuilder.create(method).setUri(endpoint.resolve(path)).addHeader("Accept", "application/json");
    }

    private OpenSearchJsonResponse perform(AsyncRequestBuilder request, OpenSearchExecutionContext execution, int limit) {
        return perform(request, execution, limit, false, false);
    }

    private OpenSearchJsonResponse perform(AsyncRequestBuilder request, OpenSearchExecutionContext execution, int limit, boolean retainCursorResponse, boolean cleanup) {
        try {
            OpenSearchResponse response = request(request, execution.sequence(), limit, execution, retainCursorResponse, cleanup);
            JsonNode body = null;
            if (response.body().length > 0) {
                try {
                    body = mapper.readTree(response.body());
                } catch (IOException e) {
                    if (response.status() >= 300) {
                        throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_HTTP_ERROR, response.status());
                    }
                    throw e;
                }
            }
            if (!retainCursorResponse) {
                execution.checkActive();
            }
            return new OpenSearchJsonResponse(response.status(), body);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_REQUEST_CANCELLED);
        } catch (CancellationException e) {
            throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_REQUEST_CANCELLED);
        } catch (ExecutionException | IOException e) {
            Throwable cause = e;
            if (e instanceof ExecutionException) {
                cause = e.getCause();
            }
            IOException safe = new IOException("OpenSearch request failed (" + cause.getClass().getSimpleName() + ")");
            safe.setStackTrace(cause.getStackTrace());
            log.error("OpenSearch request failed", safe);
            if (cause instanceof CancellationException) {
                throw ThirdPartyApiException.as().with(safe, OpenSearchDsI18nKeys.OPENSEARCH_REQUEST_CANCELLED);
            }
            if (cause instanceof ContentTooLongException) {
                throw ThirdPartyApiException.as().with(safe, OpenSearchDsI18nKeys.OPENSEARCH_RESPONSE_SIZE_LIMIT, limit);
            }
            if (cause instanceof TimeoutException || cause instanceof java.net.SocketTimeoutException || cause instanceof ConnectionRequestTimeoutException) {
                throw ThirdPartyApiException.as().with(safe, OpenSearchDsI18nKeys.OPENSEARCH_REQUEST_TIMEOUT);
            }
            if (cause instanceof SSLException) {
                throw ThirdPartyApiException.as().with(safe, OpenSearchDsI18nKeys.OPENSEARCH_TLS_INVALID);
            }
            throw ThirdPartyApiException.as().with(safe, OpenSearchDsI18nKeys.OPENSEARCH_QUERY_FAILED);
        }
    }

    JsonNode parseBody(EsRequest request) throws IOException {
        if (request.rawBody() == null) {
            return mapper.createObjectNode();
        }
        return mapper.readTree(request.rawBody());
    }

    OpenSearchJsonResponse executeCursor(String method, String path, Map<String, String> parameters, JsonNode body, long byteLimit, OpenSearchExecutionContext execution,
                                         boolean cleanup) {
        // Cursor POST parameters belong in the URI, including the bodyless PIT creation request.
        if (!parameters.isEmpty()) {
            StringJoiner query = new StringJoiner("&", "?", "");
            parameters.forEach((name, value) -> query
                .add(java.net.URLEncoder.encode(name, java.nio.charset.StandardCharsets.UTF_8) + "=" + java.net.URLEncoder.encode(value, java.nio.charset.StandardCharsets.UTF_8)));
            path += query;
        }
        AsyncRequestBuilder request = newRequest(method, path, execution, cleanup);
        if (body != null) {
            request.setEntity(body.toString(), ContentType.APPLICATION_JSON);
        }
        int limit = maxResponseBytes;
        if (byteLimit > 0) {
            limit = (int) Math.min(limit, byteLimit);
        }
        return perform(request, execution, limit, true, cleanup);
    }

    void retainCursor(OpenSearchExecutionContext execution) {
        synchronized (stateLock) {
            execution.checkActive();
            checkActive(execution.sequence());
            cursors++;
        }
    }

    void releaseCursor() {
        boolean finish;
        synchronized (stateLock) {
            cursors--;
            finish = closed && cursors == 0;
        }
        if (finish) {
            closeTransport();
        }
    }

    private void checkRequest(long sequence, boolean cleanup) {
        if (cleanup) {
            if (transportClosed || cursors == 0) {
                throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_CLIENT_CLOSED);
            }
            return;
        }
        checkActive(sequence);
    }

    private void checkActive(long sequence) {
        if (closed) {
            throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_CLIENT_CLOSED);
        }
        if (sequence != cancellationSequence || Thread.currentThread().isInterrupted()) {
            throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_REQUEST_CANCELLED);
        }
    }

    public void cancel() {
        List<OpenSearchExchange> exchanges;
        synchronized (stateLock) {
            cancellationSequence++;
            exchanges = pending.entrySet().stream().filter(entry -> !entry.getValue()).map(Map.Entry::getKey).toList();
        }
        exchanges.forEach(OpenSearchExchange::cancel);
    }

    @Override
    public void close() {
        List<OpenSearchExchange> exchanges;
        boolean finish;
        synchronized (stateLock) {
            if (closed) {
                return;
            }
            closed = true;
            cancellationSequence++;
            exchanges = pending.entrySet().stream().filter(entry -> !entry.getValue()).map(Map.Entry::getKey).toList();
            finish = cursors == 0;
            if (!finish) {
                // Let the execution's finally release its cursor, but bound transport lifetime if result output stalls.
                deadlines.schedule(this::closeTransport, 5, TimeUnit.SECONDS);
            }
        }
        exchanges.forEach(OpenSearchExchange::cancel);
        if (finish) {
            closeTransport();
        }
    }

    private void closeTransport() {
        List<OpenSearchExchange> exchanges;
        synchronized (stateLock) {
            if (transportClosed) {
                return;
            }
            transportClosed = true;
            exchanges = List.copyOf(pending.keySet());
        }
        try {
            exchanges.forEach(OpenSearchExchange::cancel);
        } finally {
            try {
                http.close(CloseMode.IMMEDIATE);
            } finally {
                deadlines.shutdownNow();
            }
        }
    }
}
