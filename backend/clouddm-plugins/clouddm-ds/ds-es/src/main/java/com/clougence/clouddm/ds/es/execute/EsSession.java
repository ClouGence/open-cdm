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

import com.clougence.clouddm.ds.es.dsconf.EsConfig;
import com.clougence.clouddm.ds.es.i18n.EsDsI18nKeys;
import com.clougence.clouddm.sdk.execute.meta.DsMetaService;
import com.clougence.clouddm.sdk.execute.session.*;
import com.clougence.clouddm.sdk.execute.session.rdb.RdbIsolation;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.drivers.DsObject;
import com.clougence.sql.es.EsServerVersion;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EsSession implements Session {
    @Getter
    private final String                     sessionId;
    @Getter
    private final EsConfig                   dsConfig;
    @Getter
    private final DsMetaService              metaService;
    private final DsObject<EsClient>         resource;
    private final EsClient                   client;
    private final Object                     stateLock      = new Object();
    private final List<SessionCloseListener> closeListeners = new ArrayList<>();
    private final AtomicInteger              executing      = new AtomicInteger();
    @Getter
    private volatile long                    lastQueryTime  = System.currentTimeMillis();
    @Getter
    private volatile boolean                 readOnly;
    private boolean                          closed;

    public EsSession(SessionContextDTO context, EsConfig config, DsObject<EsClient> resource){
        sessionId = context.getSessionId();
        dsConfig = config;
        readOnly = context.isRdbReadOnly() || Boolean.TRUE.equals(config.getReadOnly());
        this.resource = resource;
        client = resource.getTarget();
        metaService = new EsMetaService(this);
        resource.addCloseListener(this::onResourceClosed);
    }

    EsServerVersion readVersion() {
        synchronized (stateLock) {
            checkOpen();
            executing.incrementAndGet();
            lastQueryTime = System.currentTimeMillis();
        }
        try {
            EsServerVersion version = client.probe();
            dsConfig.setVersion(version.number());
            return version;
        } finally {
            executing.decrementAndGet();
        }
    }

    @Override
    public void cancel() {
        client.cancel();
    }

    @Override
    public boolean isExecuting() { return executing.get() > 0; }

    @Override
    public void close() {
        resource.close();
    }

    private void onResourceClosed() {
        List<SessionCloseListener> listeners;
        synchronized (stateLock) {
            closed = true;
            listeners = List.copyOf(closeListeners);
            closeListeners.clear();
        }

        for (SessionCloseListener listener : listeners) {
            try {
                listener.onClose(sessionId);
            } catch (Exception e) {
                String msg = "ES session close listener failed";
                log.error(msg, e);
            }
        }
    }

    @Override
    public void addCloseListener(SessionCloseListener listener) {
        synchronized (stateLock) {
            checkOpen();
            closeListeners.add(listener);
        }
    }

    private void checkOpen() {
        if (closed || resource.isClose()) {
            throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_CLIENT_CLOSED);
        }
    }

    @Override
    public void executeQuery(QueryRequest query, ResultBuilder builder) {
        throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_OPERATION_UNSUPPORTED);
    }

    @Override
    public <V> V executeQuery(SessionCallback<V> callback) {
        throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_OPERATION_UNSUPPORTED);
    }

    @Override
    public String getCurrentQueryId() { return null; }

    @Override
    public void commit() {
        throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_OPERATION_UNSUPPORTED);
    }

    @Override
    public void rollback() {
        throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_OPERATION_UNSUPPORTED);
    }

    @Override
    public boolean isAutoCommit() { return true; }

    @Override
    public boolean hasUnCommitted() {
        return false;
    }

    @Override
    public RdbIsolation getIsolation() { return RdbIsolation.DEFAULT; }

    @Override
    public void setAutoCommit(boolean autoCommit) {
        if (!autoCommit) {
            throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_OPERATION_UNSUPPORTED);
        }
    }

    @Override
    public void setIsolation(RdbIsolation isolation) {
        if (isolation != RdbIsolation.DEFAULT) {
            throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_OPERATION_UNSUPPORTED);
        }
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        synchronized (stateLock) {
            checkOpen();
            this.readOnly = readOnly || Boolean.TRUE.equals(dsConfig.getReadOnly());
        }
    }

    @Override
    public void setCurrentCatalog(String catalog) {
        if (catalog != null && !catalog.isBlank()) {
            throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_OPERATION_UNSUPPORTED);
        }
    }

    @Override
    public void setCurrentSchema(String schema) {
        if (schema != null && !schema.isBlank()) {
            throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_OPERATION_UNSUPPORTED);
        }
    }
}
