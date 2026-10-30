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
package com.clougence.clouddm.ds.etcd.execute;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;

import com.clougence.clouddm.ds.etcd.command.EtcdCommand;
import com.clougence.clouddm.ds.etcd.command.EtcdCommandKind;
import com.clougence.clouddm.ds.etcd.command.EtcdCommandParser;
import com.clougence.clouddm.ds.etcd.dsconf.EtcdConfig;
import com.clougence.clouddm.ds.etcd.i18n.EtcdDsI18nKeys;
import com.clougence.clouddm.sdk.execute.meta.DsMetaService;
import com.clougence.clouddm.sdk.execute.session.MessageLevel;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.ResultBuilder;
import com.clougence.clouddm.sdk.execute.session.ResultBuilder.ResultMessageBuild;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.session.SessionCallback;
import com.clougence.clouddm.sdk.execute.session.SessionCloseListener;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.clouddm.sdk.execute.session.rdb.RdbIsolation;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.drivers.DsObject;
import com.clougence.utils.i18n.I18nUtils;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EtcdSession implements Session {

    private final I18nUtils                  i18n           = I18nUtils.initI18n(EtcdDsI18nKeys.class);
    @Getter
    private final String                     sessionId;
    @Getter
    private final EtcdConfig                 dsConfig;
    @Getter
    private final DsMetaService              metaService;
    private final DsObject<EtcdClients>      resource;
    private final EtcdClients                clients;
    private final ReentrantLock              executionLock  = new ReentrantLock();
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

    public EtcdSession(SessionContextDTO context, EtcdConfig config, DsObject<EtcdClients> resource){
        this.sessionId = context.getSessionId();
        this.dsConfig = config;
        this.readOnly = context.isRdbReadOnly() || Boolean.TRUE.equals(config.getReadOnly());
        this.resource = resource;
        this.clients = resource.getTarget();
        this.metaService = new EtcdMetaService(this);
        resource.addCloseListener(this::onResourceClosed);
    }

    EtcdClients clients() {
        return this.clients;
    }

    void testConnect() {
        call(EtcdClients::listMembers);
    }

    <T> T call(EtcdCall<T> operation) {
        if (!this.executionLock.tryLock()) {
            throw ThirdPartyApiException.as().with(EtcdDsI18nKeys.ETCD_SESSION_BUSY);
        }
        try {
            checkOpen();
            this.executing = true;
            this.lastQueryTime = System.currentTimeMillis();
            return operation.run(this.clients);
        } catch (Exception e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            String msg = "etcd request failed";
            log.error(msg, e);
            if (e instanceof ThirdPartyApiException failure) {
                throw failure;
            }
            if (e instanceof java.util.concurrent.TimeoutException) {
                throw ThirdPartyApiException.as().with(e, EtcdDsI18nKeys.ETCD_QUERY_TIMEOUT);
            }
            throw ThirdPartyApiException.as().with(e);
        } finally {
            this.executing = false;
            this.executionLock.unlock();
        }
    }

    @Override
    public void executeQuery(QueryRequest query, ResultBuilder builder) {
        long start = System.currentTimeMillis();
        try {
            executeCommand(query, builder);
        } catch (Exception e) {
            ThirdPartyApiException failure;
            if (e instanceof ThirdPartyApiException apiException) {
                failure = apiException;
            } else {
                failure = ThirdPartyApiException.as().with(e);
            }
            String msg = "Execute etcd command failed, queryId=" + query.getQueryId();
            log.error(msg, e);
            String detail = i18n.getMessage(failure.getMessageKey(), failure.getMessageArgs());
            ResultMessageBuild message = builder.newMessage(query);
            // SessionAgent uses notify=true errors to finish the prepared audit as FAILURE.
            message.receiveMessage(MessageLevel.Error, detail, true);
            message.collectCost(System.currentTimeMillis() - start);
            message.finishRecord(false, detail, failure);
            throw failure;
        }
    }

    private void executeCommand(QueryRequest query, ResultBuilder builder) {
        EtcdCommand command = EtcdCommandParser.parse(query.getQueryBody());
        if (this.readOnly && command.getKind() != EtcdCommandKind.GET) {
            throw ThirdPartyApiException.as().with(EtcdDsI18nKeys.ETCD_SESSION_READ_ONLY);
        }
        List<Map<String, Object>> rows = new ArrayList<>();
        if (command.getKind() == EtcdCommandKind.GET) {
            Map<String, String> found = call(clients -> clients.getKey(command.getKey()));
            if (found != null) {
                rows.add(row(found.get("key"), found.get("value")));
            }
            write(query, builder, new String[] { "key", "value" }, rows);
            return;
        }
        if (command.getKind() == EtcdCommandKind.PUT) {
            call(clients -> {
                clients.putKey(command.getKey(), command.getValue());
                return null;
            });
            rows.add(row(command.getKey(), command.getValue()));
            write(query, builder, new String[] { "key", "value" }, rows);
            return;
        }
        long deleted = call(clients -> clients.deleteKey(command.getKey()));
        Map<String, Object> deletedRow = new LinkedHashMap<>();
        deletedRow.put("key", command.getKey());
        deletedRow.put("deleted", String.valueOf(deleted));
        rows.add(deletedRow);
        write(query, builder, new String[] { "key", "deleted" }, rows);
    }

    private static Map<String, Object> row(String key, String value) {
        Map<String, Object> row = new LinkedHashMap<>();
        row.put("key", key);
        row.put("value", value);
        return row;
    }

    private static void write(QueryRequest query, ResultBuilder builder, String[] columns, List<Map<String, Object>> rows) {
        try {
            new EtcdResultWriter(query, builder).rows(columns, rows);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw ThirdPartyApiException.as().with(e);
        }
    }

    @Override
    public void cancel() {
    }

    @Override
    public void close() {
        this.closed = true;
        this.executionLock.lock();
        try {
            this.resource.close();
        } finally {
            this.executionLock.unlock();
        }
    }

    private void onResourceClosed() {
        this.closed = true;
        for (SessionCloseListener listener : this.closeListeners) {
            try {
                listener.onClose(this.sessionId);
            } catch (Exception e) {
                String msg = "etcd session close listener failed";
                log.error(msg, e);
            }
        }
        this.closeListeners.clear();
    }

    @Override
    public void addCloseListener(SessionCloseListener listener) {
        checkOpen();
        this.closeListeners.add(listener);
    }

    private void checkOpen() {
        if (this.closed || this.resource.isClose()) {
            throw ThirdPartyApiException.as().with(EtcdDsI18nKeys.ETCD_SESSION_CLOSED);
        }
    }

    @Override
    public <V> V executeQuery(SessionCallback<V> callback) {
        throw ThirdPartyApiException.as().with(EtcdDsI18nKeys.ETCD_SESSION_UNSUPPORTED);
    }

    @Override
    public boolean isAutoCommit() {
        return true;
    }

    @Override
    public boolean hasUnCommitted() {
        return false;
    }

    @Override
    public RdbIsolation getIsolation() {
        return RdbIsolation.DEFAULT;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        if (!this.executionLock.tryLock()) {
            throw ThirdPartyApiException.as().with(EtcdDsI18nKeys.ETCD_SESSION_BUSY);
        }
        try {
            checkOpen();
            this.readOnly = readOnly || Boolean.TRUE.equals(this.dsConfig.getReadOnly());
        } finally {
            this.executionLock.unlock();
        }
    }

    @Override
    public void commit() {
        throw ThirdPartyApiException.as().with(EtcdDsI18nKeys.ETCD_SESSION_UNSUPPORTED);
    }

    @Override
    public void rollback() {
        throw ThirdPartyApiException.as().with(EtcdDsI18nKeys.ETCD_SESSION_UNSUPPORTED);
    }

    @Override
    public void setAutoCommit(boolean autoCommit) {
        if (!autoCommit) {
            throw ThirdPartyApiException.as().with(EtcdDsI18nKeys.ETCD_SESSION_UNSUPPORTED);
        }
    }

    @Override
    public void setIsolation(RdbIsolation isolation) {
        if (isolation != RdbIsolation.DEFAULT) {
            throw ThirdPartyApiException.as().with(EtcdDsI18nKeys.ETCD_SESSION_UNSUPPORTED);
        }
    }

    @Override
    public void setCurrentCatalog(String catalog) {
        if (catalog != null && !catalog.isBlank()) {
            throw ThirdPartyApiException.as().with(EtcdDsI18nKeys.ETCD_SESSION_UNSUPPORTED);
        }
    }

    @Override
    public void setCurrentSchema(String schema) {
        if (schema != null && !schema.isBlank()) {
            throw ThirdPartyApiException.as().with(EtcdDsI18nKeys.ETCD_SESSION_UNSUPPORTED);
        }
    }
}
