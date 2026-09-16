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
package com.clougence.clouddm.sdk.execute.kafka;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.sdk.Spi;

/**
 * Kafka admin write operations (topic / consumer group).
 * Connection must come from the Kafka driver ClassLoader (unwrap AdminClient).
 */
public interface KafkaOpsSpi extends Spi {

    void createTopic(Connection connection, String topicName, int partitions, short replicationFactor, Map<String, String> configs);

    void deleteTopic(Connection connection, String topicName);

    Map<String, String> describeTopicConfigs(Connection connection, String topicName);

    void alterTopicConfigs(Connection connection, String topicName, Map<String, String> configs);

    /**
     * Adjust topic partitions and/or replication factor.
     * Partition count can only increase. Replication factor is applied via partition reassignment.
     */
    void alterTopicLayout(Connection connection, String topicName, int partitions, short replicationFactor);

    void deleteConsumerGroup(Connection connection, String groupId);

    /**
     * @param mode EARLIEST / LATEST / TIMESTAMP / OFFSET
     * @param topics empty means all topics that currently have committed offsets
     * @param timestampMs used when mode=TIMESTAMP
     * @param offset used when mode=OFFSET (same absolute offset for every partition)
     */
    void resetConsumerGroupOffsets(Connection connection, String groupId, String mode, List<String> topics, Long timestampMs, Long offset);
}
