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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.clougence.clouddm.ds.opensearch.dsconf.OpenSearchConfig;
import com.clougence.clouddm.ds.opensearch.i18n.OpenSearchDsI18nKeys;
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
public class OpenSearchSession implements Session {
    private static final I18nUtils           I18N           = I18nUtils.initI18n(OpenSearchDsI18nKeys.class);
    private OpenSearchExecutionContext       activeExecution;
    @Getter
    private volatile String                  currentQueryId;
    @Getter
    private final String                     sessionId;
    @Getter
    private final OpenSearchConfig           dsConfig;
    @Getter
    private final DsMetaService              metaService;
    private final DsObject<OpenSearchClient> resource;
    private final OpenSearchClient           client;
    private final Object                     stateLock      = new Object();
    private final List<SessionCloseListener> closeListeners = new ArrayList<>();
    private final AtomicInteger              executing      = new AtomicInteger();
    @Getter
    private volatile long                    lastQueryTime  = System.currentTimeMillis();
    @Getter
    private volatile boolean                 readOnly;
    private boolean                          closed;

    public OpenSearchSession(SessionContextDTO context, OpenSearchConfig config, DsObject<OpenSearchClient> resource){
        sessionId = context.getSessionId();
        dsConfig = config;
        readOnly = context.isRdbReadOnly() || Boolean.TRUE.equals(config.getReadOnly());
        this.resource = resource;
        client = resource.getTarget();
        metaService = new OpenSearchMetaService(this);
        resource.addCloseListener(this::onResourceClosed);
    }

    String readVersion() {
        long sequence;
        synchronized (stateLock) {
            checkOpen();
            if (executing.get() > 0) {
                throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_SESSION_BUSY);
            }
            sequence = client.cancellationSequence();
            executing.incrementAndGet();
            lastQueryTime = System.currentTimeMillis();
        }
        try {
            String version = client.probe(sequence);
            dsConfig.setVersion(version);
            return version;
        } finally {
            executing.decrementAndGet();
        }
    }

    private OpenSearchExecutionContext beginExecution(long timeout, String queryId) {
        synchronized (stateLock) {
            checkOpen();
            if (executing.get() > 0) {
                throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_SESSION_BUSY);
            }
            OpenSearchExecutionContext execution = new OpenSearchExecutionContext(timeout, client.cancellationSequence());
            activeExecution = execution;
            currentQueryId = queryId;
            executing.incrementAndGet();
            lastQueryTime = System.currentTimeMillis();
            return execution;
        }
    }

    private void endExecution() {
        synchronized (stateLock) {
            activeExecution = null;
            currentQueryId = null;
            executing.decrementAndGet();
        }
    }

    List<String> readIndices() {
        OpenSearchExecutionContext execution = beginExecution(dsConfig.getRequestTimeoutMs(), null);
        try {
            return client.listIndices(execution);
        } finally {
            endExecution();
        }
    }

    JsonNode readIndexDefinition(String index) {
        OpenSearchExecutionContext execution = beginExecution(dsConfig.getRequestTimeoutMs(), null);
        try {
            return client.readIndexDefinition(index, execution);
        } finally {
            endExecution();
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
                String msg = "OpenSearch session close listener failed";
                log.error(msg, e);
            }
        }
    }

    @Override
    public void addCloseListener(SessionCloseListener listener) {
        synchronized (stateLock) {
            if (!closed && !resource.isClose()) {
                closeListeners.add(listener);
                return;
            }
        }
        listener.onClose(sessionId);
    }

    private void checkOpen() {
        if (closed || resource.isClose()) {
            throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_CLIENT_CLOSED);
        }
    }

    @Override
    public void executeQuery(QueryRequest query, ResultBuilder builder) {
        long start = System.currentTimeMillis();
        boolean started = false;
        try {
            long timeout = dsConfig.getRequestTimeoutMs();
            if (query.getResultConf().getQueryTimeoutSec() > 0) {
                timeout = Math.min(timeout, query.getResultConf().getQueryTimeoutSec() * 1000L);
            }
            OpenSearchExecutionContext execution = beginExecution(timeout, query.getQueryId());
            started = true;
            executeCommand(query, builder, execution, start);
        } catch (Exception e) {
            ThirdPartyApiException failure;
            if (e instanceof ThirdPartyApiException apiException) {
                failure = apiException;
            } else {
                IOException safe = new IOException("OpenSearch execution failed (" + e.getClass().getSimpleName() + ")");
                safe.setStackTrace(e.getStackTrace());
                failure = ThirdPartyApiException.as().with(safe, OpenSearchDsI18nKeys.OPENSEARCH_QUERY_FAILED);
            }
            String msg = "Execute OpenSearch query failed, queryId=" + query.getQueryId();
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
                endExecution();
            }
        }
    }

    private void executeCommand(QueryRequest query, ResultBuilder builder, OpenSearchExecutionContext execution, long start) throws Exception {
        if ((query.getQueryArgs() != null && !query.getQueryArgs().isEmpty()) || query.isUseCallable() || query.isUseExplain() || query.isUseCompile()) {
            throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_OPERATION_UNSUPPORTED);
        }

        SqlParserParameters parameters = RestProduct.OPENSEARCH.parameters(client.getServerVersion());
        EsRequest request = new EsRequestParser(parameters).parse(query.getQueryBody());
        EsRequestAnalysis analysis = EsRequestPolicy.analyze(request, parameters);
        analysis.checkResolvedResources(query);
        boolean write = analysis.operation().getAction() != BehaviorAction.READ;
        if (readOnly && write) {
            throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_READ_ONLY);
        }

        OpenSearchRequestSecurity.checkMasking(request, analysis.operation(), query.isUsingValueProcess());
        if (analysis.operation() == EsOperation.CREATE_INDEX) {
            client.checkCreateIndex(analysis.index().name(), execution);
        }
        for (String name : analysis.indices()) {
            execution.checkActive();
            if (analysis.operation() == EsOperation.CREATE_INDEX && name.equals(analysis.index().name())) {
                continue;
            }
            if (request.method().equals("HEAD")
                || List.of(EsOperation.READ_INDEX, EsOperation.DROP_INDEX, EsOperation.PUT_MAPPING, EsOperation.PUT_SETTINGS).contains(analysis.operation())) {
                client.checkConcreteIndex(name, request.method().equals("HEAD"), execution);
            } else {
                boolean search = List.of(EsOperation.SEARCH, EsOperation.COUNT, EsOperation.UPDATE_BY_QUERY, EsOperation.DELETE_BY_QUERY).contains(analysis.operation());
                client.checkIndex(name, write && name.equals(analysis.index().name()), search && name.equals(analysis.index().name()), execution);
            }
        }
        if (analysis.operation() == EsOperation.SEARCH) {
            long limit = EsSearchLimit.resolve(request).size();
            long maximum = query.getResultConf().getFetchRecordCountLimit();
            if (maximum > 0) {
                limit = Math.min(limit, maximum);
            }
            boolean pit = RestApiProfile.from(parameters).capabilities().pitShardDoc();
            if (limit > EsSearchPagination.PAGE_SIZE && OpenSearchSearchCursor.supports(request, pit)) {
                new OpenSearchResultWriter(query, builder, execution, start).writeContinuous(request, client, limit, pit);
                return;
            }
        }
        OpenSearchJsonResponse response = client.execute(request, query.getResultConf().getFetchResultSetBytesLimit(), execution);
        new OpenSearchResultWriter(query, builder, execution, start).write(request, analysis.operation(), response);
    }

    @Override
    public <V> V executeQuery(SessionCallback<V> callback) {
        throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_OPERATION_UNSUPPORTED);
    }

    @Override
    public void commit() {
        throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_OPERATION_UNSUPPORTED);
    }

    @Override
    public void rollback() {
        throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_OPERATION_UNSUPPORTED);
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
            throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_OPERATION_UNSUPPORTED);
        }
    }

    @Override
    public void setIsolation(RdbIsolation isolation) {
        if (isolation != RdbIsolation.DEFAULT) {
            throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_OPERATION_UNSUPPORTED);
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
            throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_OPERATION_UNSUPPORTED);
        }
    }

    @Override
    public void setCurrentSchema(String schema) {
        if (schema != null && !schema.isBlank()) {
            throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_OPERATION_UNSUPPORTED);
        }
    }
}
