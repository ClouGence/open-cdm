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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.*;

import com.clougence.clouddm.ds.es.i18n.EsDsI18nKeys;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.sql.es.EsServerVersion;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class EsClient implements AutoCloseable {
    private final RestClient                           client;
    private final int                                  requestTimeoutMs;
    private final Object                               stateLock = new Object();
    private final Map<EsResponseListener, Cancellable> pending   = new HashMap<>();
    private final ObjectMapper                         mapper    = new ObjectMapper();
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
        Request request = new Request("GET", "/");
        // Native JSON works on all supported majors; compatibility headers only bridge one major.
        request.setOptions(RequestOptions.DEFAULT.toBuilder()
            .addHeader("Accept", "application/json")
            .setHttpAsyncResponseConsumerFactory(new HttpAsyncResponseConsumerFactory.HeapBufferedResponseConsumerFactory(1024 * 1024)));
        EsResponseListener result = new EsResponseListener();
        Cancellable operation;
        synchronized (stateLock) {
            checkOpen();
            operation = client.performRequestAsync(request, result);
            pending.put(result, operation);
        }
        try {
            Response response = result.get(requestTimeoutMs, TimeUnit.MILLISECONDS);
            var body = mapper.readTree(EntityUtils.toString(response.getEntity()));
            var product = response.getHeader("X-Elastic-Product");
            if (body == null || !body.isObject() || body.path("version").has("distribution") || (product != null && !"Elasticsearch".equals(product))
                || (product == null && !"You Know, for Search".equals(body.path("tagline").asText()))) {
                throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_VERSION_INVALID);
            }
            EsServerVersion version = EsServerVersion.parse(body.path("version").path("number").asText());
            synchronized (stateLock) {
                checkOpen();
                serverVersion = version;
            }
            return version;
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
            String msg = "Probe ES server failed";
            // ResponseException and JSON errors can echo credentials or server data; retain only the stack.
            IOException safe = new IOException(msg + " (" + cause.getClass().getSimpleName() + ")");
            safe.setStackTrace(cause.getStackTrace());
            log.error(msg, safe);
            if (cause instanceof SocketTimeoutException || cause instanceof ConnectTimeoutException || cause instanceof TimeoutException) {
                throw ThirdPartyApiException.as().with(safe, EsDsI18nKeys.ES_REQUEST_TIMEOUT);
            }
            if (cause instanceof ResponseException responseException) {
                throw ThirdPartyApiException.as().with(safe, EsDsI18nKeys.ES_HTTP_ERROR, responseException.getResponse().getStatusLine().getStatusCode());
            }
            throw ThirdPartyApiException.as().with(safe, EsDsI18nKeys.ES_CONNECT_FAILED);
        } finally {
            operation.cancel();
            synchronized (stateLock) {
                pending.remove(result);
            }
        }
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
