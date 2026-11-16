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
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.admin.DescribeTopicsOptions;
import org.apache.kafka.clients.admin.TopicDescription;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.UnknownTopicOrPartitionException;

import com.clougence.clouddm.ds.kafka.i18n.KafkaDsI18nKeys;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.sql.kafka.KafkaCommand;

class KafkaProducerCommand {
    private final KafkaAdminContext context;
    private final KafkaClients      clients;

    KafkaProducerCommand(KafkaAdminContext context, KafkaClients clients){
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
        context.checkActive();
        Producer<String, String> producer = clients.createProducer();
        // Force close any pending send on failure instead of flushing it during cleanup.
        try (AutoCloseable closeProducer = () -> producer.close(Duration.ZERO)) {
            context.checkActive();
            producer.send(record).get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw ThirdPartyApiException.as().with(e, KafkaDsI18nKeys.KAFKA_QUERY_CANCELLED);
        } catch (ExecutionException e) {
            throw ThirdPartyApiException.as().with(e.getCause());
        }
        context.affected(1);
    }
}
