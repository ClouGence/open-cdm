/*
 * Copyright 2026 杭州开云集致科技有限公司
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.clouddm.ds.kafka.execute;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.*;

import org.apache.kafka.clients.admin.DescribeTopicsOptions;
import org.apache.kafka.clients.admin.TopicDescription;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.errors.InterruptException;
import org.apache.kafka.common.errors.UnknownTopicOrPartitionException;

import com.clougence.clouddm.ds.kafka.i18n.KafkaDsI18nKeys;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.sql.kafka.KafkaCommand;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class KafkaProducerCommand {
    private final KafkaSession      session;
    private final KafkaAdminContext context;
    private final KafkaClients      clients;

    KafkaProducerCommand(KafkaSession session, KafkaAdminContext context, KafkaClients clients){
        this.session = session;
        this.context = context;
        this.clients = clients;
    }

    void execute(KafkaCommand command) throws Exception {
        String topic = command.value("--topic");
        TopicDescription description;
        try {
            description = context.await(timeout -> context.getAdmin().describeTopics(List.of(topic), new DescribeTopicsOptions().timeoutMs(timeout)).topicNameValues().get(topic));
        } catch (UnknownTopicOrPartitionException e) {
            throw ThirdPartyApiException.as().with(e, KafkaDsI18nKeys.KAFKA_TOPIC_NOT_FOUND, topic);
        }

        Integer partition = null;
        if (command.has("--partition")) {
            int requested = Integer.parseInt(command.value("--partition"));
            if (description.partitions().stream().noneMatch(info -> info.partition() == requested)) {
                throw ThirdPartyApiException.as().with(KafkaDsI18nKeys.KAFKA_PARTITION_NOT_FOUND, topic, requested);
            }
            partition = requested;
        }

        ProducerRecord<String, String> record = new ProducerRecord<>(topic, partition, command.value("--key"), command.value("--value"));
        Producer<String, String> producer = clients.createProducer(context.requestTimeoutMs());
        RecordMetadata metadata;
        try {
            context.checkActive();
            metadata = sendAndAwait(producer, record);
        } finally {
            try {
                // Give pending batches no extra time to send after cancellation or timeout.
                producer.close(Duration.ZERO);
            } catch (RuntimeException e) {
                if (e instanceof InterruptException) {
                    Thread.interrupted();
                }
                String msg = "Close Kafka producer failed, topic=" + topic;
                log.error(msg, e);
            }
        }
        context.affected(1);
        context.outcomeTable(List.of(KafkaResultColumn.TOPIC, KafkaResultColumn.PARTITION, KafkaResultColumn.OFFSET), List
            .<Object[]> of(new Object[] { metadata.topic(), metadata.partition(), metadata.offset() }));
    }

    private RecordMetadata sendAndAwait(Producer<String, String> producer, ProducerRecord<String, String> record) throws Exception {
        CompletableFuture<RecordMetadata> receipt = new CompletableFuture<>();
        Thread sendThread = Thread.currentThread();
        try {
            session.dispatchProduce(receipt, () -> producer.send(record, (metadata, error) -> {
                if (error == null) {
                    receipt.complete(metadata);
                } else if (error instanceof org.apache.kafka.common.errors.TimeoutException) {
                    // Kafka invokes pre-enqueue failures on the send thread and delivery callbacks on its I/O thread.
                    String messageKey = KafkaDsI18nKeys.KAFKA_PRODUCER_OUTCOME_UNKNOWN;
                    if (Thread.currentThread() == sendThread) {
                        messageKey = KafkaDsI18nKeys.KAFKA_PRODUCER_SEND_TIMEOUT;
                    }
                    receipt.completeExceptionally(ThirdPartyApiException.as().with(error, messageKey));
                } else {
                    receipt.completeExceptionally(error);
                }
            }));
            return receipt.get(context.remainingNanos(), TimeUnit.NANOSECONDS);
        } catch (InterruptException e) {
            // Kafka restores the interrupt flag. Consume it here so the query can publish its audit result.
            Thread.interrupted();
            throw ThirdPartyApiException.as().with(e, KafkaDsI18nKeys.KAFKA_QUERY_CANCELLED);
        } catch (InterruptedException | CancellationException | TimeoutException e) {
            throw ThirdPartyApiException.as().with(e, KafkaDsI18nKeys.KAFKA_PRODUCER_OUTCOME_UNKNOWN);
        } catch (ExecutionException e) {
            if (e.getCause() instanceof ThirdPartyApiException failure) {
                throw failure;
            }
            throw ThirdPartyApiException.as().with(e.getCause());
        } finally {
            session.clearPendingProduce(receipt);
        }
    }
}
