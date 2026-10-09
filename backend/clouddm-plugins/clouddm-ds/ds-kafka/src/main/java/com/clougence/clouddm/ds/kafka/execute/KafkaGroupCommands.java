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

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.kafka.clients.admin.*;
import org.apache.kafka.clients.admin.ListOffsetsResult.ListOffsetsResultInfo;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.GroupState;
import org.apache.kafka.common.IsolationLevel;
import org.apache.kafka.common.TopicPartition;

import com.clougence.clouddm.ds.kafka.i18n.KafkaDsI18nKeys;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.sql.kafka.KafkaCommand;

public class KafkaGroupCommands {
    private static final Comparator<TopicPartition> PARTITION_ORDER = Comparator.comparing(TopicPartition::topic).thenComparingInt(TopicPartition::partition);
    private final KafkaAdminContext                 context;

    public KafkaGroupCommands(KafkaAdminContext context){
        this.context = context;
    }

    public void execute(KafkaCommand command) throws Exception {
        if (command.has("--list")) {
            list();
            return;
        }
        String group = command.value("--group");
        if (command.has("--delete")) {
            context.await(timeout -> context.getAdmin().deleteConsumerGroups(List.of(group), new DeleteConsumerGroupsOptions().timeoutMs(timeout)).all());
            context.managementReport(List.of(KafkaResultColumn.GROUP, KafkaResultColumn.STATUS), List.<Object[]> of(new Object[] { group, "DELETED" }));
            context.affected(1);
            return;
        }
        ConsumerGroupDescription description = context
            .await(timeout -> context.getAdmin().describeConsumerGroups(List.of(group), new DescribeConsumerGroupsOptions().timeoutMs(timeout)).describedGroups().get(group));
        if (description.groupState() == GroupState.DEAD) {
            throw ThirdPartyApiException.as().with(KafkaDsI18nKeys.KAFKA_GROUP_NOT_FOUND, group);
        }
        if (command.has("--members")) {
            members(description, command.has("--verbose"));
        } else if (command.has("--state")) {
            state(description);
        } else {
            offsets(description);
        }
    }

    private void list() throws Exception {
        var groups = context.await(timeout -> context.getAdmin().listGroups(ListGroupsOptions.forConsumerGroups().timeoutMs(timeout)).all());
        List<Object[]> rows = new ArrayList<>();
        for (GroupListing group : groups.stream().sorted(Comparator.comparing(GroupListing::groupId)).toList()) {
            context.checkActive();
            rows.add(new Object[] { group.groupId(), group.groupState().map(Object::toString).orElse(null), group.type().map(Object::toString).orElse(null),
                                    group.isSimpleConsumerGroup() });
        }
        context.table(List.of(KafkaResultColumn.GROUP, KafkaResultColumn.STATE, KafkaResultColumn.GROUP_TYPE, KafkaResultColumn.IS_SIMPLE), rows);
    }

    private void state(ConsumerGroupDescription group) throws Exception {
        String coordinator = null;
        if (group.coordinator() != null && !group.coordinator().isEmpty()) {
            coordinator = group.coordinator().id() + "@" + group.coordinator().host() + ":" + group.coordinator().port();
        }
        context.table(List
            .of(KafkaResultColumn.GROUP, KafkaResultColumn.STATE, KafkaResultColumn.GROUP_TYPE, KafkaResultColumn.COORDINATOR, KafkaResultColumn.ASSIGNMENT_STRATEGY, KafkaResultColumn.MEMBER_COUNT, KafkaResultColumn.IS_SIMPLE), List
                .<Object[]> of(new Object[] { group.groupId(), group.groupState().toString(), group.type().toString(), coordinator, group.partitionAssignor(),
                                              group.members().size(), group.isSimpleConsumerGroup() }));
    }

    private void members(ConsumerGroupDescription group, boolean verbose) throws Exception {
        List<KafkaResultColumn> columns = new ArrayList<>(
            List.of(KafkaResultColumn.GROUP, KafkaResultColumn.CONSUMER_ID, KafkaResultColumn.HOST, KafkaResultColumn.CLIENT_ID, KafkaResultColumn.PARTITION_COUNT));
        if (verbose) {
            columns.add(KafkaResultColumn.GROUP_INSTANCE_ID);
            columns.add(KafkaResultColumn.ASSIGNMENT);
        }
        List<Object[]> rows = new ArrayList<>();
        for (MemberDescription member : group.members().stream().sorted(Comparator.comparing(MemberDescription::consumerId)).toList()) {
            context.checkActive();
            List<Object> values = new ArrayList<>(List.of(group.groupId(), member.consumerId(), member.host(), member.clientId(), member.assignment().topicPartitions().size()));
            if (verbose) {
                values.add(member.groupInstanceId().orElse(null));
                values.add(member.assignment()
                    .topicPartitions()
                    .stream()
                    .sorted(PARTITION_ORDER)
                    .map(partition -> partition.topic() + ":" + partition.partition())
                    .collect(Collectors.joining(", ")));
            }
            rows.add(values.toArray());
        }
        context.table(columns, rows);
    }

    private void offsets(ConsumerGroupDescription group) throws Exception {
        Map<TopicPartition, OffsetAndMetadata> committed = context.await(timeout -> context.getAdmin()
            .listConsumerGroupOffsets(group.groupId(), new ListConsumerGroupOffsetsOptions().timeoutMs(timeout))
            .partitionsToOffsetAndMetadata());
        Map<TopicPartition, List<MemberDescription>> owners = new HashMap<>();
        for (MemberDescription member : group.members()) {
            for (TopicPartition partition : member.assignment().topicPartitions()) {
                owners.computeIfAbsent(partition, key -> new ArrayList<>()).add(member);
            }
        }
        TreeSet<TopicPartition> partitions = new TreeSet<>(PARTITION_ORDER);
        partitions.addAll(committed.keySet());
        partitions.addAll(owners.keySet());
        Map<TopicPartition, ListOffsetsResultInfo> ends = Map.of();
        if (!partitions.isEmpty()) {
            Map<TopicPartition, OffsetSpec> request = new LinkedHashMap<>();
            partitions.forEach(partition -> request.put(partition, OffsetSpec.latest()));
            ends = context.await(timeout -> context.getAdmin().listOffsets(request, new ListOffsetsOptions(IsolationLevel.READ_UNCOMMITTED).timeoutMs(timeout)).all());
        }
        List<Object[]> rows = new ArrayList<>();
        BigDecimal knownLag = BigDecimal.ZERO;
        int unknown = 0;
        for (TopicPartition partition : partitions) {
            context.checkActive();
            OffsetAndMetadata offset = committed.get(partition);
            Long current = null;
            if (offset != null && offset.offset() >= 0) {
                current = offset.offset();
            }
            Long end = null;
            ListOffsetsResultInfo endInfo = ends.get(partition);
            if (endInfo != null && endInfo.offset() >= 0) {
                end = endInfo.offset();
            }
            Long lag = null;
            if (current != null && end != null) {
                lag = end - current;
                knownLag = knownLag.add(BigDecimal.valueOf(lag));
            } else {
                unknown++;
            }
            List<MemberDescription> members = owners.getOrDefault(partition, List.of()).stream().sorted(Comparator.comparing(MemberDescription::consumerId)).toList();
            String consumer = null;
            String host = null;
            String client = null;
            if (!members.isEmpty()) {
                consumer = members.stream().map(MemberDescription::consumerId).collect(Collectors.joining(", "));
                host = members.stream().map(MemberDescription::host).collect(Collectors.joining(", "));
                client = members.stream().map(MemberDescription::clientId).collect(Collectors.joining(", "));
            }
            rows.add(new Object[] { group.groupId(), partition.topic(), partition.partition(), current, end, lag, consumer, host, client });
        }
        BigDecimal totalLag = null;
        if (unknown == 0 && !partitions.isEmpty()) {
            totalLag = knownLag;
        }
        // Summary covers the full partition snapshot even if the displayed detail rows are limited.
        context.table(List
            .of(KafkaResultColumn.GROUP, KafkaResultColumn.STATE, KafkaResultColumn.MEMBER_COUNT, KafkaResultColumn.PARTITION_COUNT, KafkaResultColumn.TOTAL_LAG, KafkaResultColumn.KNOWN_LAG, KafkaResultColumn.UNKNOWN_PARTITIONS), List
                .<Object[]> of(new Object[] { group.groupId(), group.groupState().toString(), group.members().size(), partitions.size(), totalLag, knownLag, unknown }));
        context.table(List
            .of(KafkaResultColumn.GROUP, KafkaResultColumn.TOPIC, KafkaResultColumn.PARTITION, KafkaResultColumn.CURRENT_OFFSET, KafkaResultColumn.LOG_END_OFFSET, KafkaResultColumn.LAG, KafkaResultColumn.CONSUMER_ID, KafkaResultColumn.HOST, KafkaResultColumn.CLIENT_ID), rows);
    }
}
