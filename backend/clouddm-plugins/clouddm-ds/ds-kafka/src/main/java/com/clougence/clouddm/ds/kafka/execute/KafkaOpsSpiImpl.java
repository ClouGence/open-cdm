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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.sdk.execute.kafka.KafkaOpsSpi;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.utils.ExceptionUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * Plugin-CL SPI proxy. Real AdminClient work runs in {@code KafkaOpsAdmin} on the driver ClassLoader.
 */
@Slf4j
public class KafkaOpsSpiImpl implements KafkaOpsSpi {

    private static final String OPS_ADMIN = "com.clougence.clouddm.ds.kafka.execute.jdbc.KafkaOpsAdmin";

    @Override
    public void createTopic(Connection connection, String topicName, int partitions, short replicationFactor, Map<String, String> configs) {
        invoke(connection, "createTopic", new Class<?>[] { Connection.class, String.class, int.class, short.class, Map.class }, //
               connection, topicName, partitions, replicationFactor, configs);
    }

    @Override
    public void deleteTopic(Connection connection, String topicName) {
        invoke(connection, "deleteTopic", new Class<?>[] { Connection.class, String.class }, connection, topicName);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, String> describeTopicConfigs(Connection connection, String topicName) {
        return (Map<String, String>) invoke(connection, "describeTopicConfigs", new Class<?>[] { Connection.class, String.class }, connection, topicName);
    }

    @Override
    public void alterTopicConfigs(Connection connection, String topicName, Map<String, String> configs) {
        invoke(connection, "alterTopicConfigs", new Class<?>[] { Connection.class, String.class, Map.class }, connection, topicName, configs);
    }

    @Override
    public void alterTopicLayout(Connection connection, String topicName, int partitions, short replicationFactor) {
        invoke(connection, "alterTopicLayout", new Class<?>[] { Connection.class, String.class, int.class, short.class }, //
               connection, topicName, partitions, replicationFactor);
    }

    @Override
    public void deleteConsumerGroup(Connection connection, String groupId) {
        invoke(connection, "deleteConsumerGroup", new Class<?>[] { Connection.class, String.class }, connection, groupId);
    }

    @Override
    public void resetConsumerGroupOffsets(Connection connection, String groupId, String mode, List<String> topics, Long timestampMs, Long offset) {
        invoke(connection, "resetConsumerGroupOffsets", //
               new Class<?>[] { Connection.class, String.class, String.class, List.class, Long.class, Long.class }, //
               connection, groupId, mode, topics, timestampMs, offset);
    }

    private static Object invoke(Connection connection, String methodName, Class<?>[] parameterTypes, Object... args) {
        try {
            ClassLoader driverCl = resolveDriverClassLoader(connection);
            Class<?> opsType = Class.forName(OPS_ADMIN, true, driverCl);
            Method method = opsType.getMethod(methodName, parameterTypes);
            return method.invoke(null, args);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause() == null ? e : e.getCause();
            throw wrap(methodName + " failed", cause);
        } catch (Exception e) {
            throw wrap(methodName + " failed", e);
        }
    }

    /**
     * DsFactory returns {@code JdbcConnection} (app CL). The real Kafka adapter lives inside and is loaded by the driver CL.
     */
    private static ClassLoader resolveDriverClassLoader(Connection connection) {
        try {
            Method adapterConnection = connection.getClass().getDeclaredMethod("adapterConnection");
            adapterConnection.setAccessible(true);
            Object adapter = adapterConnection.invoke(connection);
            if (adapter != null && adapter.getClass().getClassLoader() != null) {
                return adapter.getClass().getClassLoader();
            }
        } catch (ReflectiveOperationException ignored) {
            // fall through
        }
        ClassLoader cl = connection.getClass().getClassLoader();
        if (cl == null) {
            cl = Thread.currentThread().getContextClassLoader();
        }
        return cl;
    }

    private static RuntimeException wrap(String message, Throwable e) {
        String msg = message + ", " + ExceptionUtils.getRootCauseMessage(e);
        log.error(msg, e);
        if (e instanceof Exception) {
            return ThirdPartyApiException.as().with((Exception) e, msg);
        }
        return ThirdPartyApiException.as().with(new Exception(e), msg);
    }
}
