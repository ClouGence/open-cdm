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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeoutException;

import javax.net.ssl.SSLException;

import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.core5.http.ConnectionRequestTimeoutException;
import org.apache.hc.core5.http.ContentTooLongException;
import org.apache.hc.core5.http.nio.support.AsyncRequestBuilder;
import org.apache.hc.core5.io.CloseMode;

import com.clougence.clouddm.ds.opensearch.dsconf.OpenSearchConfig;
import com.clougence.clouddm.ds.opensearch.i18n.OpenSearchDsI18nKeys;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.sql.es.RestApiProfile;
import com.clougence.sql.es.RestProduct;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class OpenSearchClient implements AutoCloseable {
    private final CloseableHttpAsyncClient http;
    private final ScheduledExecutorService deadlines;
    private final List<URI>                endpoints;
    private final int                      queueTimeoutMs;
    private final int                      requestTimeoutMs;
    private final int                      maxResponseBytes;
    private final ObjectMapper             mapper    = new ObjectMapper();
    private final Object                   stateLock = new Object();
    private final Set<OpenSearchExchange>  pending   = new HashSet<>();
    private boolean                        closed;
    private long                           cancellationSequence;
    private int                            nextEndpoint;
    @Getter
    private volatile String                serverVersion;

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
                OpenSearchResponse response = request(endpoints.get((start + attempt) % endpoints.size()), sequence);
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

    private OpenSearchResponse request(URI endpoint, long sequence) throws InterruptedException, ExecutionException {
        OpenSearchExchange exchange = new OpenSearchExchange(deadlines, requestTimeoutMs);
        synchronized (stateLock) {
            checkActive(sequence);
            pending.add(exchange);
            exchange.startQueue(queueTimeoutMs);
        }
        try {
            HttpClientContext context = HttpClientContext.create();
            context.setUserToken(exchange);
            context.setAttribute(OpenSearchExchange.CONTEXT_ATTRIBUTE, exchange);
            try {
                var future = http.execute(AsyncRequestBuilder.get(endpoint).build(), new OpenSearchResponseConsumer(maxResponseBytes, exchange), context, exchange);
                exchange.setDependency(() -> future.cancel(true));
            } catch (RuntimeException e) {
                exchange.failed(e);
            }
            return exchange.await();
        } catch (InterruptedException e) {
            exchange.cancel();
            throw e;
        } finally {
            synchronized (stateLock) {
                pending.remove(exchange);
            }
        }
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
            exchanges = List.copyOf(pending);
        }
        exchanges.forEach(OpenSearchExchange::cancel);
    }

    @Override
    public void close() {
        List<OpenSearchExchange> exchanges;
        synchronized (stateLock) {
            if (closed) {
                return;
            }
            closed = true;
            cancellationSequence++;
            exchanges = List.copyOf(pending);
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
