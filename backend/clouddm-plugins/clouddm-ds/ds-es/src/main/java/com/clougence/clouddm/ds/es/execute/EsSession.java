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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import com.clougence.clouddm.ds.es.dsconf.EsConfig;
import com.clougence.clouddm.ds.es.i18n.EsDsI18nKeys;
import com.clougence.clouddm.sdk.execute.meta.DsMetaService;
import com.clougence.clouddm.sdk.execute.session.*;
import com.clougence.clouddm.sdk.execute.session.rdb.RdbIsolation;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.sdk.sql.SqlParserParameters;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.drivers.DsObject;
import com.clougence.sql.es.*;
import com.clougence.utils.i18n.I18nUtils;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EsSession implements Session {
    private static final I18nUtils           I18N           = I18nUtils.initI18n(EsDsI18nKeys.class);
    private final ReentrantLock              executionLock  = new ReentrantLock();
    private EsExecutionContext               activeExecution;
    @Getter
    private volatile String                  currentQueryId;
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

    List<String> readIndices() {
        synchronized (stateLock) {
            checkOpen();
            executing.incrementAndGet();
            lastQueryTime = System.currentTimeMillis();
        }
        try {
            return client.listIndices();
        } finally {
            executing.decrementAndGet();
        }
    }

    JsonNode readIndexDefinition(String index) {
        synchronized (stateLock) {
            checkOpen();
            executing.incrementAndGet();
            lastQueryTime = System.currentTimeMillis();
        }
        try {
            return client.readIndexDefinition(index, new EsExecutionContext(dsConfig.getRequestTimeoutMs()));
        } finally {
            executing.decrementAndGet();
        }
    }

    @Override
    public void cancel() {
        synchronized (stateLock) {
            if (activeExecution != null) {
                activeExecution.cancel();
            }
            client.cancel();
        }
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
            if (activeExecution != null) {
                activeExecution.cancel();
            }
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
        long start = System.currentTimeMillis();
        boolean acquired = executionLock.tryLock();
        boolean started = false;
        try {
            if (!acquired) {
                throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_SESSION_BUSY);
            }
            long timeout = dsConfig.getRequestTimeoutMs();
            if (query.getResultConf().getQueryTimeoutSec() > 0) {
                timeout = Math.min(timeout, query.getResultConf().getQueryTimeoutSec() * 1000L);
            }
            EsExecutionContext execution = new EsExecutionContext(timeout);
            synchronized (stateLock) {
                checkOpen();
                activeExecution = execution;
                currentQueryId = query.getQueryId();
                executing.incrementAndGet();
                lastQueryTime = start;
                started = true;
            }
            executeCommand(query, builder, execution, start);
        } catch (Exception e) {
            ThirdPartyApiException failure;
            if (e instanceof ThirdPartyApiException apiException) {
                failure = apiException;
            } else {
                IOException safe = new IOException("ES execution failed (" + e.getClass().getSimpleName() + ")");
                safe.setStackTrace(e.getStackTrace());
                failure = ThirdPartyApiException.as().with(safe, EsDsI18nKeys.ES_QUERY_FAILED);
            }
            String msg = "Execute ES query failed, queryId=" + query.getQueryId();
            log.error(msg, failure);
            String detail = I18N.getMessage(failure.getMessageKey(), failure.getMessageArgs());
            try {
                var message = builder.newMessage(query);
                // SessionAgent uses notify=true errors to finish the prepared audit as FAILURE.
                message.receiveMessage(MessageLevel.Error, detail, true);
                message.collectCost(System.currentTimeMillis() - start);
                message.finishRecord(false, detail, failure);
            } catch (Exception reportingFailure) {
                failure.addSuppressed(reportingFailure);
            }
            throw failure;
        } finally {
            if (started) {
                synchronized (stateLock) {
                    activeExecution = null;
                    currentQueryId = null;
                    executing.decrementAndGet();
                }
            }
            if (acquired) {
                executionLock.unlock();
            }
        }
    }

    private void executeCommand(QueryRequest query, ResultBuilder builder, EsExecutionContext execution, long start) throws Exception {
        if ((query.getQueryArgs() != null && !query.getQueryArgs().isEmpty()) || query.isUseCallable() || query.isUseExplain() || query.isUseCompile()) {
            throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_OPERATION_UNSUPPORTED);
        }

        EsServerVersion version = client.getServerVersion();
        if (version == null) {
            version = client.probe(execution);
            dsConfig.setVersion(version.number());
        }

        SqlParserParameters parameters = new SqlParserParameters(Map.of(SqlParserParameters.VERSION, version.number()));
        EsRequest request = new EsRequestParser(parameters).parse(query.getQueryBody());
        EsRequestAnalysis analysis = EsRequestPolicy.analyze(request);
        analysis.checkResolvedResources(query);
        boolean write = analysis.operation().getAction() != BehaviorAction.READ;
        if (readOnly && write) {
            throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_READ_ONLY);
        }

        for (String name : analysis.indices()) {
            execution.checkActive();
            if (analysis.operation() == EsOperation.CREATE_INDEX && name.equals(analysis.index().name())) {
                continue;
            }
            if (analysis.operation() == EsOperation.READ_INDEX) {
                client.checkConcreteIndex(name, execution);
            } else {
                client.checkIndex(name, write && name.equals(analysis.index().name()), execution);
            }
        }
        if (analysis.operation() == EsOperation.SEARCH) {
            long limit = EsSearchLimit.resolve(request).size();
            long maximum = query.getResultConf().getFetchRecordCountLimit();
            if (maximum > 0) {
                limit = Math.min(limit, maximum);
            }
            if (version.capabilities().pitShardDoc() && limit > EsSearchPagination.PAGE_SIZE && EsSearchPagination.supports(request)) {
                new EsResultWriter(query, builder, execution, start).writeContinuous(request, client, limit);
                return;
            }
        }
        EsResponse response = client.execute(request, analysis.operation(), query.getResultConf().getFetchResultSetBytesLimit(), execution);
        new EsResultWriter(query, builder, execution, start).write(request, analysis.operation(), response);
    }

    @Override
    public <V> V executeQuery(SessionCallback<V> callback) {
        throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_OPERATION_UNSUPPORTED);
    }

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
