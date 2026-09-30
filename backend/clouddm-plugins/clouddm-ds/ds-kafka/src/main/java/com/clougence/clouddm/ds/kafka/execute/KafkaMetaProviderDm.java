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

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ConsumerGroupDescription;
import org.apache.kafka.clients.admin.ConsumerGroupListing;
import org.apache.kafka.clients.admin.DescribeClusterResult;
import org.apache.kafka.clients.admin.ListOffsetsResult;
import org.apache.kafka.clients.admin.MemberDescription;
import org.apache.kafka.clients.admin.OffsetSpec;
import org.apache.kafka.clients.admin.TopicDescription;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.Node;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.TopicPartitionInfo;

import com.clougence.clouddm.ds.kafka.execute.jdbc.KafkaAdminCompat;
import com.clougence.clouddm.ds.kafka.execute.jdbc.KafkaKeys;
import com.clougence.clouddm.dsfamily.execute.AbstractMetadataProvider;
import com.clougence.schema.metadata.MetaDataService;
import com.clougence.schema.umi.special.rdb.RdbAttributeNames;
import com.clougence.schema.umi.special.rdb.RdbColumn;
import com.clougence.schema.umi.special.rdb.RdbForeignKey;
import com.clougence.schema.umi.special.rdb.RdbIndex;
import com.clougence.schema.umi.special.rdb.RdbTable;
import com.clougence.schema.umi.special.rdb.RdbValue;
import com.clougence.schema.umi.struts.UmiConstraint;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;
import com.clougence.schema.umi.struts.constraint.ConstraintObject;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class KafkaMetaProviderDm extends AbstractMetadataProvider implements MetaDataService {

    private static final long ADMIN_TIMEOUT_MS = 30000L;

    public KafkaMetaProviderDm(Connection connection){
        super(connection);
    }

    @Override
    public String getVersion() {
        try (Connection conn = this.connectSupplier.eGet()) {
            AdminClient admin = unwrapAdmin(conn);
            DescribeClusterResult cluster = admin.describeCluster();
            String clusterId = cluster.clusterId().get(ADMIN_TIMEOUT_MS, TimeUnit.MILLISECONDS);
            return StringUtils.defaultIfBlank(clusterId, "kafka");
        } catch (Exception e) {
            String msg = "getVersion failed, " + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    public List<Value> selectSchemas() throws SQLException {
        List<Value> result = new ArrayList<>(3);
        result.add(schemaValue(KafkaKeys.SCHEMA_TOPIC));
        result.add(schemaValue(KafkaKeys.SCHEMA_CONSUMER_GROUP));
        result.add(schemaValue(KafkaKeys.SCHEMA_ENDPOINT));
        return result;
    }

    private static RdbValue schemaValue(String name) {
        RdbValue value = new RdbValue();
        value.setValue(name);
        value.setUmiType(UmiTypes.Schema);
        return value;
    }

    public List<Value> selectTopics(String pattern) throws SQLException {
        try (Connection conn = this.connectSupplier.eGet()) {
            AdminClient admin = unwrapAdmin(conn);
            Set<String> names = admin.listTopics().names().get(ADMIN_TIMEOUT_MS, TimeUnit.MILLISECONDS);
            List<String> matched = names.stream().sorted().filter(name -> matchPattern(name, pattern)).collect(Collectors.toList());
            if (matched.isEmpty()) {
                return Collections.emptyList();
            }
            Map<String, TopicDescription> descMap = KafkaAdminCompat.awaitTopicDescriptions(admin.describeTopics(matched), ADMIN_TIMEOUT_MS);
            List<Value> result = new ArrayList<>(matched.size());
            for (String name : matched) {
                result.add(toTopicValue(name, descMap.get(name)));
            }
            return result;
        } catch (SQLException e) {
            throw e;
        } catch (Exception e) {
            throw toSqlException("list topics failed", e);
        }
    }

    public List<Value> selectConsumerGroups(String pattern) throws SQLException {
        try (Connection conn = this.connectSupplier.eGet()) {
            AdminClient admin = unwrapAdmin(conn);
            List<ConsumerGroupListing> listings = new ArrayList<>(admin.listConsumerGroups().all().get(ADMIN_TIMEOUT_MS, TimeUnit.MILLISECONDS));
            listings.sort((a, b) -> StringUtils.compare(a.groupId(), b.groupId()));
            List<String> matchedIds = new ArrayList<>();
            for (ConsumerGroupListing listing : listings) {
                String groupId = listing.groupId();
                if (matchPattern(groupId, pattern)) {
                    matchedIds.add(groupId);
                }
            }
            if (matchedIds.isEmpty()) {
                return Collections.emptyList();
            }
            Map<String, ConsumerGroupDescription> descMap = admin.describeConsumerGroups(matchedIds).all().get(ADMIN_TIMEOUT_MS, TimeUnit.MILLISECONDS);
            List<Value> result = new ArrayList<>(matchedIds.size());
            for (String groupId : matchedIds) {
                result.add(toConsumerGroupValue(admin, groupId, descMap.get(groupId)));
            }
            return result;
        } catch (SQLException e) {
            throw e;
        } catch (Exception e) {
            throw toSqlException("list consumer groups failed", e);
        }
    }

    public List<Value> selectBrokers(String pattern) throws SQLException {
        try (Connection conn = this.connectSupplier.eGet()) {
            AdminClient admin = unwrapAdmin(conn);
            DescribeClusterResult cluster = admin.describeCluster();
            String clusterId = cluster.clusterId().get(ADMIN_TIMEOUT_MS, TimeUnit.MILLISECONDS);
            List<Node> nodes = new ArrayList<>(cluster.nodes().get(ADMIN_TIMEOUT_MS, TimeUnit.MILLISECONDS));
            nodes.sort((a, b) -> Integer.compare(a.id(), b.id()));
            Node controller = cluster.controller().get(ADMIN_TIMEOUT_MS, TimeUnit.MILLISECONDS);
            Integer controllerId = controller == null ? null : controller.id();
            List<Value> result = new ArrayList<>();
            for (Node node : nodes) {
                String name = node.host() + ":" + node.port();
                if (!matchPattern(name, pattern) && !matchPattern(String.valueOf(node.id()), pattern)) {
                    continue;
                }
                result.add(toBrokerValue(node, clusterId, controllerId));
            }
            return result;
        } catch (SQLException e) {
            throw e;
        } catch (Exception e) {
            throw toSqlException("list brokers failed", e);
        }
    }

    public Value loadTopic(String topicName) throws SQLException {
        try (Connection conn = this.connectSupplier.eGet()) {
            AdminClient admin = unwrapAdmin(conn);
            Map<String, TopicDescription> descMap = KafkaAdminCompat.awaitTopicDescriptions(admin.describeTopics(Collections.singletonList(topicName)), ADMIN_TIMEOUT_MS);
            return toTopicValue(topicName, descMap.get(topicName));
        } catch (SQLException e) {
            throw e;
        } catch (Exception e) {
            throw toSqlException("describe topic failed", e);
        }
    }

    public Value loadConsumerGroup(String groupId) throws SQLException {
        try (Connection conn = this.connectSupplier.eGet()) {
            AdminClient admin = unwrapAdmin(conn);
            Map<String, ConsumerGroupDescription> descMap = admin.describeConsumerGroups(Collections.singletonList(groupId)).all().get(ADMIN_TIMEOUT_MS, TimeUnit.MILLISECONDS);
            return toConsumerGroupValue(admin, groupId, descMap.get(groupId));
        } catch (SQLException e) {
            throw e;
        } catch (Exception e) {
            throw toSqlException("describe consumer group failed", e);
        }
    }

    public Value loadBroker(String brokerName) throws SQLException {
        List<Value> brokers = selectBrokers(null);
        for (Value broker : brokers) {
            if (StringUtils.equals(brokerName, broker.asValue())) {
                return broker;
            }
        }
        return null;
    }

    public void testConnect() throws SQLException {
        try (Connection conn = this.connectSupplier.eGet()) {
            AdminClient admin = unwrapAdmin(conn);
            DescribeClusterResult cluster = admin.describeCluster();
            cluster.clusterId().get(ADMIN_TIMEOUT_MS, TimeUnit.MILLISECONDS);
            cluster.nodes().get(ADMIN_TIMEOUT_MS, TimeUnit.MILLISECONDS);
        } catch (SQLException e) {
            throw e;
        } catch (Exception e) {
            throw toSqlException("testConnect failed", e);
        }
    }

    private static RdbValue toBrokerValue(Node node, String clusterId, Integer controllerId) {
        RdbValue value = new RdbValue();
        String name = node.host() + ":" + node.port();
        value.setValue(name);
        value.setUmiType(UmiTypes.Endpoint);
        boolean isController = controllerId != null && controllerId == node.id();
        value.setAttribute(KafkaKeys.ATTR_CLUSTER_ID, StringUtils.defaultString(clusterId));
        value.setAttribute(KafkaKeys.ATTR_BROKER_ID, String.valueOf(node.id()));
        value.setAttribute(KafkaKeys.ATTR_HOST, node.host());
        value.setAttribute(KafkaKeys.ATTR_PORT, String.valueOf(node.port()));
        value.setAttribute(KafkaKeys.ATTR_IS_CONTROLLER, String.valueOf(isController));
        String tip = "id=" + node.id();
        if (isController) {
            tip = tip + ", controller";
        }
        value.setAttribute(RdbAttributeNames.OBJ_UI_TIPS, "[" + tip + "]");
        return value;
    }

    private static RdbValue toTopicValue(String topicName, TopicDescription description) {
        if (description == null) {
            return null;
        }
        RdbValue value = new RdbValue();
        value.setValue(topicName);
        value.setUmiType(UmiTypes.Topic);
        List<TopicPartitionInfo> partitions = description.partitions();
        int partitionCount = partitions == null ? 0 : partitions.size();
        int replicaCount = 0;
        int underReplicated = 0;
        if (partitions != null && !partitions.isEmpty()) {
            if (partitions.get(0).replicas() != null) {
                replicaCount = partitions.get(0).replicas().size();
            }
            for (TopicPartitionInfo part : partitions) {
                int replicas = part.replicas() == null ? 0 : part.replicas().size();
                int isr = part.isr() == null ? 0 : part.isr().size();
                if (isr < replicas) {
                    underReplicated++;
                }
            }
        }
        value.setAttribute(KafkaKeys.ATTR_PARTITION_COUNT, String.valueOf(partitionCount));
        value.setAttribute(KafkaKeys.ATTR_REPLICA_COUNT, String.valueOf(replicaCount));
        value.setAttribute(KafkaKeys.ATTR_UNDER_REPLICATED, String.valueOf(underReplicated));
        value.setAttribute(KafkaKeys.ATTR_PARTITIONS_JSON, KafkaMetaJson.partitionsJson(partitions));
        value.setAttribute(RdbAttributeNames.OBJ_UI_TIPS, "partitions=" + partitionCount + ", replicas=" + replicaCount);
        return value;
    }

    private static RdbValue toConsumerGroupValue(AdminClient admin, String groupId, ConsumerGroupDescription description) throws Exception {
        if (description == null) {
            return null;
        }
        RdbValue value = new RdbValue();
        value.setValue(groupId);
        value.setUmiType(UmiTypes.ConsumerGroup);
        int members = description.members() == null ? 0 : description.members().size();
        String state = description.state() == null ? "" : description.state().toString();
        Set<String> assignedTopics = new TreeSet<>();
        if (description.members() != null) {
            for (MemberDescription member : description.members()) {
                if (member.assignment() == null || member.assignment().topicPartitions() == null) {
                    continue;
                }
                for (TopicPartition tp : member.assignment().topicPartitions()) {
                    assignedTopics.add(tp.topic());
                }
            }
        }
        Map<String, long[]> topicOffsets = new java.util.LinkedHashMap<>();
        topicOffsets.putAll(computeTopicOffsets(admin, groupId));
        for (String topic : assignedTopics) {
            if (!topicOffsets.containsKey(topic)) {
                topicOffsets.put(topic, null);
            }
        }
        value.setAttribute(KafkaKeys.ATTR_STATE, state);
        value.setAttribute(KafkaKeys.ATTR_MEMBER_COUNT, String.valueOf(members));
        value.setAttribute(KafkaKeys.ATTR_TOPICS_JSON, KafkaMetaJson.topicsOffsetJson(topicOffsets));
        value.setAttribute(RdbAttributeNames.OBJ_UI_TIPS, "state=" + state + ", members=" + members);
        return value;
    }

    /**
     * Per topic: [sumCommitted, sumLatest, sumLag]. Empty when the group has no committed offsets.
     */
    private static Map<String, long[]> computeTopicOffsets(AdminClient admin, String groupId) throws Exception {
        Map<TopicPartition, OffsetAndMetadata> committed = admin.listConsumerGroupOffsets(groupId)
                                                                .partitionsToOffsetAndMetadata()
                                                                .get(ADMIN_TIMEOUT_MS, TimeUnit.MILLISECONDS);
        Map<String, long[]> topicOffsets = new TreeMap<>();
        if (committed == null || committed.isEmpty()) {
            return topicOffsets;
        }
        Map<TopicPartition, OffsetSpec> endRequest = new HashMap<>();
        for (TopicPartition tp : committed.keySet()) {
            endRequest.put(tp, OffsetSpec.latest());
        }
        Map<TopicPartition, ListOffsetsResult.ListOffsetsResultInfo> ends = admin.listOffsets(endRequest).all().get(ADMIN_TIMEOUT_MS, TimeUnit.MILLISECONDS);
        for (Map.Entry<TopicPartition, OffsetAndMetadata> entry : committed.entrySet()) {
            TopicPartition tp = entry.getKey();
            OffsetAndMetadata meta = entry.getValue();
            if (meta == null) {
                continue;
            }
            long committedOffset = meta.offset();
            ListOffsetsResult.ListOffsetsResultInfo endInfo = ends.get(tp);
            long latestOffset = endInfo == null ? committedOffset : endInfo.offset();
            long lag = latestOffset - committedOffset;
            if (lag < 0) {
                lag = 0;
            }
            long[] sums = topicOffsets.get(tp.topic());
            if (sums == null) {
                topicOffsets.put(tp.topic(), new long[] { committedOffset, latestOffset, lag });
            } else {
                sums[0] = sums[0] + committedOffset;
                sums[1] = sums[1] + latestOffset;
                sums[2] = sums[2] + lag;
            }
        }
        return topicOffsets;
    }

    private static AdminClient unwrapAdmin(Connection conn) throws SQLException {
        AdminClient admin = conn.unwrap(AdminClient.class);
        if (admin == null) {
            throw new SQLException("failed to unwrap AdminClient from " + conn.getClass().getName());
        }
        return admin;
    }

    private static boolean matchPattern(String name, String pattern) {
        if (StringUtils.isBlank(pattern) || "*".equals(pattern)) {
            return true;
        }
        String lowerName = name.toLowerCase();
        String lowerPattern = pattern.toLowerCase();
        if (lowerPattern.contains("*")) {
            String regex = lowerPattern.replace(".", "\\.").replace("*", ".*");
            return lowerName.matches(regex);
        }
        return lowerName.contains(lowerPattern);
    }

    private static SQLException toSqlException(String message, Exception e) {
        String msg = message + ", " + ExceptionUtils.getRootCauseMessage(e);
        log.error(msg, e);
        return new SQLException(msg, e);
    }

    @Override
    protected List<RdbTable> fetchTableByPart(Connection conn, String catalog, String schema, List<String> tabs) {
        return Collections.emptyList();
    }

    @Override
    protected List<RdbTable> fetchViewByPart(Connection conn, String catalog, String schema, List<String> tabs) {
        return Collections.emptyList();
    }

    @Override
    protected List<RdbTable> fetchMaterializedByPart(Connection conn, String catalog, String schema, List<String> tabs) {
        return Collections.emptyList();
    }

    @Override
    protected Map<String, List<RdbColumn>> fetchViewColumns(Connection conn, String catalog, String schema, List<String> tabs) {
        return Collections.emptyMap();
    }

    @Override
    protected Map<String, List<RdbColumn>> fetchTableColumns(Connection conn, String catalog, String schema, List<String> tabs) {
        return Collections.emptyMap();
    }

    @Override
    protected Map<String, List<ConstraintObject>> fetchTableConstraints(Connection conn, String catalog, String schema, List<String> tabs) {
        return Collections.emptyMap();
    }

    @Override
    protected Map<String, Map<String, UmiConstraint>> fetchPrimaryUnique(Connection conn, String catalog, String schema, List<String> tabs) {
        return Collections.emptyMap();
    }

    @Override
    protected Map<String, List<RdbForeignKey>> fetchForeignKeys(Connection conn, String catalog, String schema, List<String> tabs) {
        return Collections.emptyMap();
    }

    @Override
    protected Map<String, List<RdbIndex>> fetchIndexes(Connection conn, String catalog, String schema, List<String> tabs) {
        return Collections.emptyMap();
    }
}
