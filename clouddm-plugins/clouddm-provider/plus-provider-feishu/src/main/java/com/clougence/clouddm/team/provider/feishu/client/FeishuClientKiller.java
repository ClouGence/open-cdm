package com.clougence.clouddm.team.provider.feishu.client;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Deque;

import org.slf4j.Logger;

import com.clougence.utils.io.IOUtils;
import com.lark.oapi.okhttp.ConnectionPool;
import com.lark.oapi.okhttp.OkHttpClient;
import com.lark.oapi.okhttp.internal.connection.RealConnection;
import com.lark.oapi.okhttp.internal.connection.RealConnectionPool;

public class FeishuClientKiller {

    private static final Method WS_DISCONNECT_METHOD;
    private static final Field  WS_CACHE_FIELD;
    private static final Field  WS_AUTO_RECONNECT_FIELD;
    private static final Field  WS_HTTP_CLIENT_FIELD;
    private static final Field  WS_HTTP_CLIENT_CONNECTION_POOL_FIELD;
    private static final Field  WS_HTTP_CLIENT_CONNECTION_POOL_DELEGATE_FIELD;
    private static final Field  WS_HTTP_CLIENT_CONNECTION_POOL_DELEGATE_EXECUTOR_FIELD;
    private static final Field  WS_HTTP_CLIENT_CONNECTION_POOL_CONNECTIONS_FIELD;
    private static final Field  WS_EXECUTOR_FIELD;

    private static Method findDeclaredMethod(Class<?> type, String name) throws NoSuchMethodException {
        Method method = type.getDeclaredMethod(name);
        method.setAccessible(true);
        return method;
    }

    private static Field findDeclaredField(Class<?> type, String name) throws NoSuchFieldException {
        Field field = type.getDeclaredField(name);
        field.setAccessible(true);
        return field;
    }

    static {
        Method disconnectMethod;
        Field cacheField;
        Field autoReconnectField;
        Field httpClientField;
        Field httpClientConnectionPoolField;
        Field httpClientConnectionPoolDelegateField;
        Field httpClientConnectionPoolDelegateExecutorField;
        Field httpClientConnectionPoolConnectionsField;
        Field executorField;

        try {
            disconnectMethod = findDeclaredMethod(com.lark.oapi.ws.Client.class, "disconnect");
            cacheField = findDeclaredField(com.lark.oapi.ws.Client.class, "cache");
            autoReconnectField = findDeclaredField(com.lark.oapi.ws.Client.class, "autoReconnect");
            httpClientField = findDeclaredField(com.lark.oapi.ws.Client.class, "httpClient");
            httpClientConnectionPoolField = findDeclaredField(OkHttpClient.class, "connectionPool");
            httpClientConnectionPoolDelegateField = findDeclaredField(ConnectionPool.class, "delegate");
            httpClientConnectionPoolDelegateExecutorField = findDeclaredField(RealConnectionPool.class, "executor");
            httpClientConnectionPoolConnectionsField = findDeclaredField(RealConnectionPool.class, "connections");
            executorField = findDeclaredField(com.lark.oapi.ws.Client.class, "executor");
        } catch (Throwable t) {
            disconnectMethod = null;
            cacheField = null;
            autoReconnectField = null;
            httpClientField = null;
            httpClientConnectionPoolField = null;
            httpClientConnectionPoolDelegateField = null;
            httpClientConnectionPoolDelegateExecutorField = null;
            httpClientConnectionPoolConnectionsField = null;
            executorField = null;
        }

        WS_DISCONNECT_METHOD = disconnectMethod;
        WS_CACHE_FIELD = cacheField;
        WS_AUTO_RECONNECT_FIELD = autoReconnectField;
        WS_HTTP_CLIENT_FIELD = httpClientField;
        WS_HTTP_CLIENT_CONNECTION_POOL_FIELD = httpClientConnectionPoolField;
        WS_HTTP_CLIENT_CONNECTION_POOL_DELEGATE_FIELD = httpClientConnectionPoolDelegateField;
        WS_HTTP_CLIENT_CONNECTION_POOL_DELEGATE_EXECUTOR_FIELD = httpClientConnectionPoolDelegateExecutorField;
        WS_HTTP_CLIENT_CONNECTION_POOL_CONNECTIONS_FIELD = httpClientConnectionPoolConnectionsField;
        WS_EXECUTOR_FIELD = executorField;
    }

    public static void close(Logger log, WsClient wsClient) {
        if (wsClient == null) {
            return;
        }

        // close reconnect
        wsClient.autoReconnect = false;
        wsClient.inited.set(false);
        wsClient.disconnect();
        wsClient.executor.shutdownNow();

        // close okhttp3
        wsClient.httpClient.dispatcher().cancelAll();
        wsClient.httpClient.dispatcher().executorService().shutdownNow();
        IOUtils.closeQuietly(wsClient.httpClient.cache());
        wsClient.httpClient.connectionPool().evictAll();
        if (WS_HTTP_CLIENT_CONNECTION_POOL_DELEGATE_FIELD != null) {
            try {
                Object poolValue = WS_HTTP_CLIENT_CONNECTION_POOL_DELEGATE_FIELD.get(wsClient.httpClient.connectionPool());
                if (!(poolValue instanceof RealConnectionPool)) {
                    return;
                }

                RealConnectionPool pool = (RealConnectionPool) poolValue;
                Object connectionsValue = WS_HTTP_CLIENT_CONNECTION_POOL_CONNECTIONS_FIELD.get(pool);
                if (connectionsValue instanceof Deque<?>) {
                    for (Object connection : (Deque<?>) connectionsValue) {
                        if (connection instanceof RealConnection) {
                            RealConnection realConnection = (RealConnection) connection;
                            realConnection.cancel();
                            IOUtils.closeQuietly(realConnection.socket());
                        }
                    }
                }

                pool.evictAll();
            } catch (Exception ex) {
                log.warn("free wsClient at httpClient Field " + ex.getMessage());
            }
        }

        log.info("free wsClient finish.");
    }

    public static void close(Logger log, com.lark.oapi.Client apiClient) {
        //
    }
}
