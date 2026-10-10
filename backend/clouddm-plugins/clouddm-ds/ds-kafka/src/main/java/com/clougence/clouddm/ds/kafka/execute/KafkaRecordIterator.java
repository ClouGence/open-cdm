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

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.InterruptException;
import org.apache.kafka.common.errors.TimeoutException;
import org.apache.kafka.common.errors.WakeupException;

import com.clougence.clouddm.ds.kafka.i18n.KafkaDsI18nKeys;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.sql.kafka.KafkaCommand;

import lombok.Getter;

/** Single-query cursor. All Consumer calls except wakeup run on the query thread. */
public class KafkaRecordIterator implements Iterator<Object[]>, AutoCloseable {
    private final KafkaSession                       session;
    private final Consumer<byte[], byte[]>           consumer;
    private final long                               timeoutNanos;
    private final Duration                           metadataTimeout;
    private Iterator<ConsumerRecord<byte[], byte[]>> batch = Collections.emptyIterator();
    @Getter
    private boolean                                  timedOut;

    public KafkaRecordIterator(KafkaSession session, Consumer<byte[], byte[]> consumer, long timeoutMs){
        this.session = session;
        this.consumer = consumer;
        this.timeoutNanos = TimeUnit.MILLISECONDS.toNanos(timeoutMs);
        this.metadataTimeout = Duration.ofMillis(Math.min(timeoutMs, session.getDsConfig().getApiTimeoutMs()));
    }

    public void assign(KafkaCommand command) {
        session.activateConsumer(consumer);
        try {
            String topic = command.value("--topic");
            List<TopicPartition> partitions = consumer.partitionsFor(topic, metadataTimeout).stream().map(info -> new TopicPartition(topic, info.partition())).toList();
            session.checkCancelled();
            if (partitions.isEmpty()) {
                throw ThirdPartyApiException.as().with(KafkaDsI18nKeys.KAFKA_TOPIC_NOT_FOUND, topic);
            }
            if (command.has("--partition")) {
                TopicPartition partition = new TopicPartition(topic, Integer.parseInt(command.value("--partition")));
                if (!partitions.contains(partition)) {
                    throw ThirdPartyApiException.as().with(KafkaDsI18nKeys.KAFKA_PARTITION_NOT_FOUND, topic, partition.partition());
                }
                partitions = List.of(partition);
            }
            consumer.assign(partitions);
            Map<TopicPartition, Long> offsets;
            if (command.has("--from-beginning")) {
                offsets = consumer.beginningOffsets(partitions, metadataTimeout);
            } else {
                // Resolve the starting end offsets now, before the first poll; never reuse a previous query's position.
                offsets = consumer.endOffsets(partitions, metadataTimeout);
            }
            session.checkCancelled();
            offsets.forEach(consumer::seek);
        } catch (WakeupException e) {
            session.checkCancelled();
            throw e;
        } catch (InterruptException e) {
            Thread.currentThread().interrupt();
            throw ThirdPartyApiException.as().with(e, KafkaDsI18nKeys.KAFKA_QUERY_CANCELLED);
        } catch (TimeoutException e) {
            throw ThirdPartyApiException.as().with(e, KafkaDsI18nKeys.KAFKA_QUERY_TIMEOUT);
        }
    }

    @Override
    public boolean hasNext() {
        session.checkCancelled();
        if (timedOut) {
            return false;
        }
        long start = System.nanoTime();
        try {
            while (!batch.hasNext()) {
                long remaining = timeoutNanos - (System.nanoTime() - start);
                if (remaining <= 0) {
                    timedOut = true;
                    return false;
                }
                batch = consumer.poll(Duration.ofNanos(Math.min(remaining, TimeUnit.SECONDS.toNanos(1)))).iterator();
                session.checkCancelled();
            }
            return true;
        } catch (WakeupException e) {
            session.checkCancelled();
            throw e;
        } catch (InterruptException e) {
            Thread.currentThread().interrupt();
            throw ThirdPartyApiException.as().with(e, KafkaDsI18nKeys.KAFKA_QUERY_CANCELLED);
        }
    }

    @Override
    public Object[] next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        ConsumerRecord<byte[], byte[]> record = batch.next();
        Long timestamp = null;
        if (record.timestamp() >= 0) {
            timestamp = record.timestamp();
        }
        String key = null;
        if (record.key() != null) {
            key = new String(record.key(), StandardCharsets.UTF_8);
        }
        String value = null;
        if (record.value() != null) {
            value = new String(record.value(), StandardCharsets.UTF_8);
        }
        return new Object[] { record.topic(), record.partition(), record.offset(), timestamp, record.timestampType().toString(), key, value };
    }

    @Override
    public void close() {
        // Unregister before close so a late UI cancellation cannot leave a wakeup on a client being disposed.
        session.releaseConsumer();
        consumer.close(Duration.ofSeconds(5));
    }
}
