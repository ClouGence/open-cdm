package com.clougence.clouddm.team.provider.oidc.client;

import java.lang.reflect.Field;
import java.util.Deque;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;

import com.clougence.utils.io.IOUtils;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.internal.connection.RealConnection;
import okhttp3.internal.connection.RealConnectionPool;

public class OidcClientKiller {

    private static final Field WS_HTTP_CLIENT_CONNECTION_POOL_DELEGATE_FIELD;
    private static final Field WS_HTTP_CLIENT_CONNECTION_POOL_DELEGATE_EXECUTOR_FIELD;
    private static final Field WS_HTTP_CLIENT_CONNECTION_POOL_CONNECTIONS_FIELD;

    private static Field findDeclaredField(Class<?> type, String name) throws NoSuchFieldException {
        Field field = type.getDeclaredField(name);
        field.setAccessible(true);
        return field;
    }

    static {
        Field httpClientConnectionPoolDelegateField;
        Field httpClientConnectionPoolDelegateExecutorField;
        Field httpClientConnectionPoolConnectionsField;

        try {
            httpClientConnectionPoolDelegateField = findDeclaredField(ConnectionPool.class, "delegate");
            httpClientConnectionPoolDelegateExecutorField = findDeclaredField(RealConnectionPool.class, "executor");
            httpClientConnectionPoolConnectionsField = findDeclaredField(RealConnectionPool.class, "connections");
        } catch (Throwable t) {
            httpClientConnectionPoolDelegateField = null;
            httpClientConnectionPoolDelegateExecutorField = null;
            httpClientConnectionPoolConnectionsField = null;
        }

        WS_HTTP_CLIENT_CONNECTION_POOL_DELEGATE_FIELD = httpClientConnectionPoolDelegateField;
        WS_HTTP_CLIENT_CONNECTION_POOL_DELEGATE_EXECUTOR_FIELD = httpClientConnectionPoolDelegateExecutorField;
        WS_HTTP_CLIENT_CONNECTION_POOL_CONNECTIONS_FIELD = httpClientConnectionPoolConnectionsField;
    }

    public static void close(Logger log, OkHttpClient client) {
        // close okhttp3
        if (client == null) {
            return;
        }

        IOUtils.closeQuietly(client.cache());
        client.connectionPool().evictAll();
        if (WS_HTTP_CLIENT_CONNECTION_POOL_DELEGATE_FIELD != null) {
            try {
                Object poolValue = WS_HTTP_CLIENT_CONNECTION_POOL_DELEGATE_FIELD.get(client.connectionPool());
                if (!(poolValue instanceof RealConnectionPool)) {
                    return;
                }

                RealConnectionPool pool = (RealConnectionPool) poolValue;
                Object poolExecutorValue = WS_HTTP_CLIENT_CONNECTION_POOL_DELEGATE_EXECUTOR_FIELD.get(pool);
                if (poolExecutorValue instanceof ThreadPoolExecutor) {
                    ((ThreadPoolExecutor) poolExecutorValue).shutdownNow();
                }

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
                log.warn("free httpClient at httpClient Field " + ex.getMessage());
            }
        }
    }
}
