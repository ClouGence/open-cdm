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

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.Node;
import org.apache.kafka.common.TopicPartitionInfo;
import org.apache.kafka.common.config.ConfigResource;
import org.apache.kafka.common.errors.TopicExistsException;
import org.apache.kafka.common.errors.UnknownTopicOrPartitionException;

import com.clougence.clouddm.ds.kafka.i18n.KafkaDsI18nKeys;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;
import com.clougence.sql.kafka.KafkaCommand;

public class KafkaTopicCommands {
    private final KafkaAdminContext context;

    public KafkaTopicCommands(KafkaAdminContext context){
        this.context = context;
    }

    public void execute(KafkaCommand command, Map<TargetType, List<String>> resolvedResources) throws Exception {
        if (command.has("--create")) {
            create(command);
            return;
        }
        List<String> targets = null;
        if (resolvedResources != null) {
            targets = resolvedResources.get(TargetType.Topic);
        }
        if (targets != null && !command.has("--list")) {
            if (command.has("--describe")) {
                if (command.has("--topic") && targets.isEmpty()) {
                    throw ThirdPartyApiException.as().with(KafkaDsI18nKeys.KAFKA_TOPIC_NOT_FOUND, command.value("--topic"));
                }
                describe(targets);
            } else {
                delete(command, targets);
            }
            return;
        }
        Collection<TopicListing> listings = context.await(timeout -> context.getAdmin().listTopics(new ListTopicsOptions().listInternal(true).timeoutMs(timeout)).listings());
        Pattern pattern = null;
        if (command.has("--topic")) {
            pattern = Pattern.compile(command.value("--topic"));
        }
        List<TopicListing> selected = new ArrayList<>();
        for (TopicListing listing : listings) {
            context.checkActive();
            if ((targets == null || targets.contains(listing.name())) && (pattern == null || pattern.matcher(listing.name()).matches())) {
                selected.add(listing);
            }
        }
        selected.sort(Comparator.comparing(TopicListing::name));
        if (command.has("--list")) {
            List<Object[]> rows = new ArrayList<>();
            for (TopicListing listing : selected) {
                rows.add(new Object[] { listing.name(), listing.topicId().toString(), listing.isInternal() });
            }
            context.table(List.of(KafkaResultColumn.TOPIC, KafkaResultColumn.TOPIC_ID, KafkaResultColumn.INTERNAL), rows);
        } else if (command.has("--describe")) {
            if (command.has("--topic") && selected.isEmpty()) {
                throw ThirdPartyApiException.as().with(KafkaDsI18nKeys.KAFKA_TOPIC_NOT_FOUND, command.value("--topic"));
            }
            describe(selected.stream().map(TopicListing::name).toList());
        } else {
            delete(command, selected.stream().map(TopicListing::name).toList());
        }
    }

    private void describe(List<String> topics) throws Exception {
        List<Object[]> summaries = new ArrayList<>();
        List<Object[]> partitions = new ArrayList<>();
        if (!topics.isEmpty()) {
            var descriptions = context.await(timeout -> context.getAdmin().describeTopics(topics, new DescribeTopicsOptions().timeoutMs(timeout)).allTopicNames());
            List<ConfigResource> resources = topics.stream().map(name -> new ConfigResource(ConfigResource.Type.TOPIC, name)).toList();
            var configs = context.await(timeout -> context.getAdmin().describeConfigs(resources, new DescribeConfigsOptions().timeoutMs(timeout)).all());
            for (String name : topics) {
                context.checkActive();
                var topic = descriptions.get(name);
                String config = configs.get(new ConfigResource(ConfigResource.Type.TOPIC, name))
                    .entries()
                    .stream()
                    .filter(entry -> entry.source() == ConfigEntry.ConfigSource.DYNAMIC_TOPIC_CONFIG && !entry.isSensitive())
                    .sorted(Comparator.comparing(ConfigEntry::name))
                    .map(entry -> entry.name() + "=" + entry.value())
                    .collect(Collectors.joining(", "));
                summaries.add(new Object[] { name, topic.topicId().toString(), topic.isInternal(), topic.partitions().size(), config });
                for (TopicPartitionInfo partition : topic.partitions().stream().sorted(Comparator.comparingInt(TopicPartitionInfo::partition)).toList()) {
                    Integer leader = null;
                    if (partition.leader() != null && partition.leader().id() >= 0) {
                        leader = partition.leader().id();
                    }
                    partitions.add(new Object[] { name, partition.partition(), leader, brokerIds(partition.replicas()), brokerIds(partition.isr()) });
                }
            }
        }
        context.table(List
            .of(KafkaResultColumn.TOPIC, KafkaResultColumn.TOPIC_ID, KafkaResultColumn.INTERNAL, KafkaResultColumn.PARTITION_COUNT, KafkaResultColumn.CONFIGS), summaries);
        context.table(List.of(KafkaResultColumn.TOPIC, KafkaResultColumn.PARTITION, KafkaResultColumn.LEADER, KafkaResultColumn.REPLICAS, KafkaResultColumn.ISR), partitions);
    }

    private String brokerIds(List<Node> nodes) {
        // Replica order carries preferred-leader semantics and must not be sorted.
        return nodes.stream().map(node -> Integer.toString(node.id())).collect(Collectors.joining(","));
    }

    private void create(KafkaCommand command) throws Exception {
        String name = command.value("--topic");
        Optional<Integer> partitions = Optional.ofNullable(command.value("--partitions")).map(Integer::valueOf);
        Optional<Short> replication = Optional.ofNullable(command.value("--replication-factor")).map(Short::valueOf);
        Map<String, String> configs = new LinkedHashMap<>();
        for (String config : command.getOptions().getOrDefault("--config", List.of())) {
            int separator = config.indexOf('=');
            configs.put(config.substring(0, separator), config.substring(separator + 1));
        }
        NewTopic topic = new NewTopic(name, partitions, replication).configs(configs);
        String status = "CREATED";
        long affected = 1;
        try {
            context.await(timeout -> context.getAdmin().createTopics(List.of(topic), new CreateTopicsOptions().retryOnQuotaViolation(false).timeoutMs(timeout)).all());
        } catch (TopicExistsException e) {
            if (!command.has("--if-not-exists")) {
                throw e;
            }
            status = "ALREADY_EXISTS";
            affected = 0;
        }
        context.managementReport(List.of(KafkaResultColumn.TOPIC, KafkaResultColumn.STATUS), List.<Object[]> of(new Object[] { name, status }));
        context.affected(affected);
    }

    private void delete(KafkaCommand command, List<String> topics) throws Exception {
        if (topics.isEmpty()) {
            if (!command.has("--if-exists")) {
                throw ThirdPartyApiException.as().with(KafkaDsI18nKeys.KAFKA_TOPIC_NOT_FOUND, command.value("--topic"));
            }
            context.managementReport(List.of(KafkaResultColumn.TOPIC, KafkaResultColumn.STATUS), List.<Object[]> of(new Object[] { command.value("--topic"), "NOT_FOUND" }));
            context.affected(0);
            return;
        }
        Map<String, KafkaFuture<Void>> results = new LinkedHashMap<>();
        Exception failure = null;
        try {
            context.await(timeout -> {
                results.putAll(context.getAdmin().deleteTopics(topics, new DeleteTopicsOptions().retryOnQuotaViolation(false).timeoutMs(timeout)).topicNameValues());
                return KafkaFuture.allOf(results.values().toArray(KafkaFuture[]::new));
            });
        } catch (Exception e) {
            failure = e;
        }
        List<Object[]> rows = new ArrayList<>();
        long affected = 0;
        boolean failed = false;
        for (String topic : topics) {
            KafkaFuture<Void> future = results.get(topic);
            String status = "UNKNOWN";
            String error = null;
            if (future == null || !future.isDone() || future.isCancelled()) {
                failed = true;
            } else {
                try {
                    future.get();
                    status = "DELETED";
                    affected++;
                } catch (ExecutionException e) {
                    if (command.has("--if-exists") && e.getCause() instanceof UnknownTopicOrPartitionException) {
                        status = "NOT_FOUND";
                    } else {
                        status = "FAILED";
                        error = e.getCause().getMessage();
                        failed = true;
                    }
                }
            }
            rows.add(new Object[] { topic, status, error });
        }
        context.managementReport(List.of(KafkaResultColumn.TOPIC, KafkaResultColumn.STATUS, KafkaResultColumn.ERROR), rows);
        if (failed) {
            throw ThirdPartyApiException.as().with(failure, KafkaDsI18nKeys.KAFKA_TOPIC_DELETE_FAILED);
        }
        // An ignored missing topic is the only batch failure that --if-exists suppresses.
        if (failure != null && !(failure instanceof UnknownTopicOrPartitionException)) {
            throw failure;
        }
        context.affected(affected);
    }
}
