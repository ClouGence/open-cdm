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
package com.clougence.clouddm.ds.kafka.execute.jdbc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.admin.DescribeTopicsResult;
import org.apache.kafka.clients.admin.TopicDescription;
import org.apache.kafka.common.KafkaFuture;

/**
 * Bridge DescribeTopicsResult across kafka-clients 2.x / 3.x / 4.x:
 * 2.x exposes {@code all()}, 4.x keeps only {@code allTopicNames()}.
 */
public final class KafkaAdminCompat {

    private KafkaAdminCompat(){
    }

    @SuppressWarnings("unchecked")
    public static Map<String, TopicDescription> awaitTopicDescriptions(DescribeTopicsResult result, long timeoutMs) throws Exception {
        KafkaFuture<Map<String, TopicDescription>> future = invokeTopicFuture(result);
        return future.get(timeoutMs, TimeUnit.MILLISECONDS);
    }

    @SuppressWarnings("unchecked")
    private static KafkaFuture<Map<String, TopicDescription>> invokeTopicFuture(DescribeTopicsResult result) throws Exception {
        try {
            Method allTopicNames = result.getClass().getMethod("allTopicNames");
            return (KafkaFuture<Map<String, TopicDescription>>) invoke(allTopicNames, result);
        } catch (NoSuchMethodException ignored) {
            Method all = result.getClass().getMethod("all");
            return (KafkaFuture<Map<String, TopicDescription>>) invoke(all, result);
        }
    }

    private static Object invoke(Method method, Object target) throws Exception {
        try {
            return method.invoke(target);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause() == null ? e : e.getCause();
            if (cause instanceof Exception) {
                throw (Exception) cause;
            }
            if (cause instanceof Error) {
                throw (Error) cause;
            }
            throw e;
        }
    }
}
