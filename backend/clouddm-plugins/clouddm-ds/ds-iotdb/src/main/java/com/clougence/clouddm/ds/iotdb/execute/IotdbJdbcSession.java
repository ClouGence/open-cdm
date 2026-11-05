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
package com.clougence.clouddm.ds.iotdb.execute;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicReference;

import com.clougence.clouddm.ds.iotdb.dsconf.IotdbSqlDialect;
import com.clougence.clouddm.ds.iotdb.i18n.IotdbI18nKeys;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.utils.i18n.I18nUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
final class IotdbJdbcSession implements InvocationHandler {

    private static final I18nUtils        I18N             = I18nUtils.initI18n(IotdbI18nKeys.class);

    private final Connection              connection;
    private final Object                  client;
    private final Class<?>                thriftException;
    private final Method                  getSqlDialect;
    private final AtomicReference<String> disconnectReason = new AtomicReference<>();

    IotdbJdbcSession(Connection connection) throws ReflectiveOperationException{
        this.connection = connection;
        Class<?> type = connection.getClass();
        Method getClient = type.getMethod("getClient");
        this.client = getClient.invoke(connection);
        this.thriftException = type.getClassLoader().loadClass("org.apache.thrift.TException");
        Method dialectMethod;
        try {
            dialectMethod = type.getMethod("getSqlDialect");
        } catch (NoSuchMethodException e) {
            // IoTDB 1.3 only opens Tree sessions.
            dialectMethod = null;
        }
        this.getSqlDialect = dialectMethod;

        Class<?> clientType = getClient.getReturnType();
        Object guardedClient = Proxy.newProxyInstance(clientType.getClassLoader(), new Class<?>[] { clientType }, this);
        type.getMethod("setClient", clientType).invoke(connection, guardedClient);
    }

    boolean supportsTableDialect() {
        return getSqlDialect != null;
    }

    IotdbSqlDialect dialect() {
        if (getSqlDialect == null) {
            return IotdbSqlDialect.TREE;
        }
        try {
            return IotdbSqlDialect.parse((String) getSqlDialect.invoke(connection));
        } catch (ReflectiveOperationException e) {
            throw ThirdPartyApiException.as().with(e);
        }
    }

    void disconnect(String reason) {
        if (disconnectReason.compareAndSet(null, reason)) {
            try {
                connection.close();
            } catch (SQLException e) {
                log.error("Force-close IoTDB connection failed", e);
            }
        }
    }

    boolean isDisconnected() { return disconnectReason.get() != null; }

    void checkConnected() {
        if (isDisconnected()) {
            throw ThirdPartyApiException.as().with(IotdbI18nKeys.IOTDB_SESSION_DISCONNECTED);
        }
    }

    void checkRequest() {
        String reason = disconnectReason.get();
        if (reason != null) {
            throw ThirdPartyApiException.as().with(reason);
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        boolean closingSession = method.getName().equals("closeSession");
        if (isDisconnected()) {
            // Skip the RPC on the busy client so JDBC close() can release the local transport immediately.
            if (closingSession) {
                return null;
            }
            checkRequest();
        }
        try {
            return method.invoke(client, args);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (!closingSession && thriftException.isInstance(cause)) {
                // Both JDBC versions can reconnect and replay SQL when a Thrift exception escapes.
                disconnect(IotdbI18nKeys.IOTDB_STATEMENT_RESULT_UNKNOWN);
                throw ThirdPartyApiException.as().with(cause, disconnectReason.get());
            }
            throw cause;
        }
    }

    static ThirdPartyApiException localized(ThirdPartyApiException error) {
        return new ThirdPartyApiException(error,
            I18N.getMessage(error.getMessageKey(), error.getMessageArgs()),
            error.getErrorType(),
            error.getMessageKey(),
            error.getMessageArgs());
    }
}
