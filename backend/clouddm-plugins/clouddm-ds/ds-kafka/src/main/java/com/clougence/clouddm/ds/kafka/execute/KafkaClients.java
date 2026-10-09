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

import java.time.Duration;
import java.util.Properties;

import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;

import com.clougence.clouddm.ds.kafka.i18n.KafkaConfigI18nKeys;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;

import lombok.extern.slf4j.Slf4j;

/** Owns the session Admin; each created Consumer is owned and closed by its query execution thread. */
@Slf4j
public class KafkaClients implements AutoCloseable {

    private static final Duration CLOSE_TIMEOUT = Duration.ofSeconds(5);
    private final Properties      properties;
    private final Admin           admin;
    private boolean               closed;

    public KafkaClients(Properties properties){
        this.properties = new Properties();
        this.properties.putAll(properties);
        this.admin = Admin.create(this.properties);
    }

    public synchronized Admin getAdmin() {
        if (this.closed) {
            throw ThirdPartyApiException.as().with(KafkaConfigI18nKeys.CONFIG_KAFKA_CLIENTS_CLOSED);
        }
        return this.admin;
    }

    public synchronized Consumer<byte[], byte[]> createConsumer() {
        if (this.closed) {
            throw ThirdPartyApiException.as().with(KafkaConfigI18nKeys.CONFIG_KAFKA_CLIENTS_CLOSED);
        }
        Properties consumerProperties = new Properties();
        consumerProperties.putAll(this.properties);
        consumerProperties.remove(ConsumerConfig.GROUP_ID_CONFIG);
        consumerProperties.remove(ConsumerConfig.GROUP_INSTANCE_ID_CONFIG);
        consumerProperties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        consumerProperties.setProperty(ConsumerConfig.ALLOW_AUTO_CREATE_TOPICS_CONFIG, "false");
        consumerProperties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "none");
        try {
            return new KafkaConsumer<>(consumerProperties, new ByteArrayDeserializer(), new ByteArrayDeserializer());
        } catch (KafkaException e) {
            String msg = "Create Kafka consumer failed";
            log.error(msg, e);
            throw ThirdPartyApiException.as().with(e);
        }
    }

    @Override
    public synchronized void close() {
        if (this.closed) {
            return;
        }
        this.closed = true;
        try {
            this.admin.close(CLOSE_TIMEOUT);
        } catch (RuntimeException e) {
            String msg = "Close Kafka admin failed";
            log.error(msg, e);
            throw ThirdPartyApiException.as().with(e);
        } finally {
            this.properties.clear();
        }
    }
}
