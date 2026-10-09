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
package com.clougence.clouddm.ds.kafka.execute;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import com.clougence.clouddm.ds.kafka.dsconf.KafkaConfig;
import com.clougence.clouddm.ds.kafka.i18n.KafkaDsI18nKeys;
import com.clougence.clouddm.sdk.execute.meta.DsMetaService;
import com.clougence.clouddm.sdk.execute.session.MessageLevel;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.ResultBuilder.ResultMessageBuild;
import com.clougence.clouddm.sdk.execute.session.ResultBuilder;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.session.SessionCallback;
import com.clougence.clouddm.sdk.execute.session.SessionCloseListener;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.clouddm.sdk.execute.session.rdb.RdbIsolation;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.drivers.DsObject;
import com.clougence.sql.kafka.KafkaCommand;
import com.clougence.sql.kafka.KafkaCommandParser;
import com.clougence.sql.kafka.KafkaSqlI18nKeys;
import com.clougence.utils.i18n.I18nUtils;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.DescribeClusterOptions;
import org.apache.kafka.common.KafkaFuture;

@Slf4j
public class KafkaSession implements Session {
    private final I18nUtils                  i18n           = I18nUtils.initI18n(KafkaDsI18nKeys.class, KafkaSqlI18nKeys.class);
    @Getter
    private final String                     sessionId;
    @Getter
    private final KafkaConfig                dsConfig;
    @Getter
    private final DsMetaService              metaService;
    private final DsObject<KafkaClients>     resource;
    private final KafkaClients               clients;
    private final ReentrantLock              executionLock  = new ReentrantLock();
    private final Object                     stateLock      = new Object();
    private final List<SessionCloseListener> closeListeners = new CopyOnWriteArrayList<>();
    @Getter
    private volatile long                    lastQueryTime  = System.currentTimeMillis();
    @Getter
    private volatile String                  currentQueryId;
    @Getter
    private volatile boolean                 readOnly;
    private volatile boolean                 closed;
    @Getter
    private volatile boolean                 executing;
    private boolean                          cancelled;
    private KafkaFuture<?>                   pendingRequest;

    public KafkaSession(SessionContextDTO context, KafkaConfig config, DsObject<KafkaClients> resource){
        this.sessionId = context.getSessionId();
        this.dsConfig = config;
        this.readOnly = context.isRdbReadOnly() || Boolean.TRUE.equals(config.getReadOnly());
        this.resource = resource;
        this.clients = resource.getTarget();
        this.metaService = new KafkaMetaService(this);
        resource.addCloseListener(this::onResourceClosed);
    }

    @Override
    public void executeQuery(QueryRequest query, ResultBuilder builder) {
        long start = System.currentTimeMillis();
        boolean acquired = executionLock.tryLock();
        try {
            if (!acquired) {
                throw ThirdPartyApiException.as().with(KafkaDsI18nKeys.KAFKA_SESSION_BUSY);
            }
            synchronized (stateLock) {
                checkOpen();
                cancelled = false;
                currentQueryId = query.getQueryId();
                executing = true;
                lastQueryTime = start;
            }
            executeCommand(query, builder, start);
        } catch (Exception e) {
            ThirdPartyApiException failure;
            if (e instanceof ThirdPartyApiException apiException) {
                failure = apiException;
            } else {
                failure = ThirdPartyApiException.as().with(e);
            }
            String msg = "Execute Kafka command failed, queryId=" + query.getQueryId();
            log.error(msg, e);
            String detail = i18n.getMessage(failure.getMessageKey(), failure.getMessageArgs());
            ResultMessageBuild message = builder.newMessage(query);
            // SessionAgent uses notify=true errors to finish the prepared audit as FAILURE.
            message.receiveMessage(MessageLevel.Error, detail, true);
            message.collectCost(System.currentTimeMillis() - start);
            message.finishRecord(false, detail, failure);
            throw failure;
        } finally {
            if (acquired) {
                synchronized (stateLock) {
                    currentQueryId = null;
                    executing = false;
                    cancelled = false;
                }
                executionLock.unlock();
            }
        }
    }

    private void executeCommand(QueryRequest query, ResultBuilder builder, long start) {
        if (query.getQueryArgs() != null && !query.getQueryArgs().isEmpty()) {
            throw ThirdPartyApiException.as().with(KafkaSqlI18nKeys.KAFKA_ARGS_UNSUPPORTED);
        }
        if (query.isUseCallable() || query.isUseExplain() || query.isUseCompile()) {
            throw ThirdPartyApiException.as().with(KafkaDsI18nKeys.KAFKA_SESSION_UNSUPPORTED);
        }
        KafkaCommand command = new KafkaCommandParser().parse(query.getQueryBody());
        if (!command.has("--help") && readOnly && (command.has("--create") || command.has("--delete"))) {
            throw ThirdPartyApiException.as().with(KafkaDsI18nKeys.KAFKA_SESSION_READ_ONLY);
        }
        synchronized (stateLock) {
            if (cancelled) {
                throw ThirdPartyApiException.as().with(KafkaDsI18nKeys.KAFKA_QUERY_CANCELLED);
            }
        }
        if (!command.has("--help")) {
            throw ThirdPartyApiException.as().with(KafkaDsI18nKeys.KAFKA_COMMAND_UNSUPPORTED);
        }
        ResultMessageBuild message = builder.newMessage(query);
        String help = i18n.getMessage("KAFKA_HELP_" + command.getType().name()) + "\n" + i18n.getMessage(KafkaSqlI18nKeys.KAFKA_HELP_COMMON);
        message.receiveMessage(MessageLevel.Info, help, false);
        message.collectCost(System.currentTimeMillis() - start);
        message.finishRecord(true);
    }

    void testConnect() {
        if (!executionLock.tryLock()) {
            throw ThirdPartyApiException.as().with(KafkaDsI18nKeys.KAFKA_SESSION_BUSY);
        }
        try {
            KafkaFuture<?> request;
            synchronized (stateLock) {
                checkOpen();
                lastQueryTime = System.currentTimeMillis();
                request = clients.getAdmin().describeCluster(new DescribeClusterOptions().timeoutMs(dsConfig.getApiTimeoutMs())).nodes();
                pendingRequest = request;
                executing = true;
            }
            request.get(dsConfig.getApiTimeoutMs(), TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            String msg = "Test Kafka connection failed";
            log.error(msg, e);
            throw ThirdPartyApiException.as().with(e);
        } finally {
            synchronized (stateLock) {
                if (pendingRequest != null && !pendingRequest.isDone()) {
                    pendingRequest.cancel(false);
                }
                pendingRequest = null;
                executing = false;
            }
            executionLock.unlock();
        }
    }

    @Override
    public void cancel() {
        synchronized (stateLock) {
            if (executing) {
                cancelled = true;
            }
            if (pendingRequest != null) {
                pendingRequest.cancel(false);
            }
        }
    }

    @Override
    public void close() {
        synchronized (stateLock) {
            closed = true;
            cancel();
        }
        executionLock.lock();
        try {
            resource.close();
        } finally {
            executionLock.unlock();
        }
    }

    private void onResourceClosed() {
        synchronized (stateLock) {
            closed = true;
            cancel();
        }
        for (SessionCloseListener listener : closeListeners) {
            try {
                listener.onClose(sessionId);
            } catch (Exception e) {
                String msg = "Kafka session close listener failed";
                log.error(msg, e);
            }
        }
        closeListeners.clear();
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
            throw ThirdPartyApiException.as().with(KafkaDsI18nKeys.KAFKA_SESSION_CLOSED);
        }
    }

    @Override
    public <V> V executeQuery(SessionCallback<V> callback) {
        throw ThirdPartyApiException.as().with(KafkaDsI18nKeys.KAFKA_SESSION_UNSUPPORTED);
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
    public void setReadOnly(boolean readOnly) {
        if (!executionLock.tryLock()) {
            throw ThirdPartyApiException.as().with(KafkaDsI18nKeys.KAFKA_SESSION_BUSY);
        }
        try {
            checkOpen();
            this.readOnly = readOnly || Boolean.TRUE.equals(dsConfig.getReadOnly());
        } finally {
            executionLock.unlock();
        }
    }

    @Override
    public void commit() {
        throw ThirdPartyApiException.as().with(KafkaDsI18nKeys.KAFKA_SESSION_UNSUPPORTED);
    }

    @Override
    public void rollback() {
        throw ThirdPartyApiException.as().with(KafkaDsI18nKeys.KAFKA_SESSION_UNSUPPORTED);
    }

    @Override
    public void setAutoCommit(boolean autoCommit) {
        if (!autoCommit) {
            throw ThirdPartyApiException.as().with(KafkaDsI18nKeys.KAFKA_SESSION_UNSUPPORTED);
        }
    }

    @Override
    public void setIsolation(RdbIsolation isolation) {
        if (isolation != RdbIsolation.DEFAULT) {
            throw ThirdPartyApiException.as().with(KafkaDsI18nKeys.KAFKA_SESSION_UNSUPPORTED);
        }
    }

    @Override
    public void setCurrentCatalog(String catalog) {
        if (catalog != null && !catalog.isBlank()) {
            throw ThirdPartyApiException.as().with(KafkaDsI18nKeys.KAFKA_SESSION_UNSUPPORTED);
        }
    }

    @Override
    public void setCurrentSchema(String schema) {
        if (schema != null && !schema.isBlank()) {
            throw ThirdPartyApiException.as().with(KafkaDsI18nKeys.KAFKA_SESSION_UNSUPPORTED);
        }
    }
}
