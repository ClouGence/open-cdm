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

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.kafka.common.Node;
import org.apache.kafka.common.TopicPartitionInfo;

import com.clougence.clouddm.ds.kafka.execute.jdbc.KafkaKeys;

/**
 * Minimal JSON builders for Kafka browse attributes (no extra JSON library).
 */
public final class KafkaMetaJson {

    private KafkaMetaJson(){
    }

    public static String partitionsJson(List<TopicPartitionInfo> partitions) {
        if (partitions == null || partitions.isEmpty()) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder(partitions.size() * 48);
        sb.append('[');
        for (int i = 0; i < partitions.size(); i++) {
            if (i > 0) {
                sb.append(',');
            }
            TopicPartitionInfo part = partitions.get(i);
            Node leader = part.leader();
            sb.append('{');
            appendNumber(sb, KafkaKeys.JSON_PARTITION_ID, part.partition());
            sb.append(',');
            appendNumber(sb, KafkaKeys.JSON_PARTITION_LEADER, leader == null ? -1 : leader.id());
            sb.append(',');
            appendString(sb, KafkaKeys.JSON_PARTITION_REPLICAS, joinNodeIds(part.replicas()));
            sb.append(',');
            appendString(sb, KafkaKeys.JSON_PARTITION_ISR, joinNodeIds(part.isr()));
            sb.append('}');
        }
        sb.append(']');
        return sb.toString();
    }

    /**
     * topicOffsets: topic -> [committedSum, latestSum, lagSum]. Null value means offsets unavailable.
     */
    public static String topicsOffsetJson(Map<String, long[]> topicOffsets) {
        if (topicOffsets == null || topicOffsets.isEmpty()) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder(topicOffsets.size() * 80);
        sb.append('[');
        boolean first = true;
        for (Map.Entry<String, long[]> entry : topicOffsets.entrySet()) {
            if (!first) {
                sb.append(',');
            }
            first = false;
            sb.append('{');
            appendString(sb, KafkaKeys.JSON_TOPIC_NAME, entry.getKey());
            sb.append(',');
            long[] offsets = entry.getValue();
            if (offsets == null || offsets.length < 3) {
                appendString(sb, KafkaKeys.JSON_TOPIC_COMMITTED, KafkaKeys.JSON_NA);
                sb.append(',');
                appendString(sb, KafkaKeys.JSON_TOPIC_LATEST, KafkaKeys.JSON_NA);
                sb.append(',');
                appendString(sb, KafkaKeys.JSON_TOPIC_LAG, KafkaKeys.JSON_NA);
            } else {
                appendLong(sb, KafkaKeys.JSON_TOPIC_COMMITTED, offsets[0]);
                sb.append(',');
                appendLong(sb, KafkaKeys.JSON_TOPIC_LATEST, offsets[1]);
                sb.append(',');
                appendLong(sb, KafkaKeys.JSON_TOPIC_LAG, offsets[2]);
            }
            sb.append('}');
        }
        sb.append(']');
        return sb.toString();
    }

    private static String joinNodeIds(List<Node> nodes) {
        if (nodes == null || nodes.isEmpty()) {
            return "";
        }
        return nodes.stream().map(n -> String.valueOf(n.id())).collect(Collectors.joining(","));
    }

    private static void appendNumber(StringBuilder sb, String key, int value) {
        sb.append('"').append(key).append('"').append(':').append(value);
    }

    private static void appendLong(StringBuilder sb, String key, long value) {
        sb.append('"').append(key).append('"').append(':').append(value);
    }

    private static void appendString(StringBuilder sb, String key, String value) {
        sb.append('"').append(key).append('"').append(':').append('"').append(escape(value)).append('"');
    }

    private static String escape(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
