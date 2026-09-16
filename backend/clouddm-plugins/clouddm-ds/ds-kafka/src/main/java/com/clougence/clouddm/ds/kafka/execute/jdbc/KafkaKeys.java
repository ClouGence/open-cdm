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

import com.clougence.drivers.adapter.JdbcDriver;

public final class KafkaKeys {

    public static final String ADAPTER_NAME        = JdbcDriver.P_ADAPTER_NAME;
    public static final String ADAPTER_NAME_VALUE  = "kafka";
    public static final String START_URL           = JdbcDriver.START_URL + ADAPTER_NAME_VALUE + ":";
    public static final String SERVER              = "server";
    public static final String BOOTSTRAP_SERVERS   = "bootstrap.servers";
    public static final String USERNAME            = "username";
    public static final String PASSWORD            = "password";
    public static final String SECURITY_PROTOCOL   = "security.protocol";
    public static final String CONN_TIMEOUT        = "connTimeout";
    public static final String SO_TIMEOUT          = "soTimeout";
    public static final String CLIENT_NAME         = "clientName";
    public static final String DATABASE            = "database";
    public static final String DRIVER_VERSION      = "driverVersion";
    public static final String DEFAULT_CLIENT_NAME       = "CloudDM";
    /** Default logical schema (= Topic dimension), like Redis default DB. */
    public static final String SCHEMA_TOPIC              = "TOPIC";
    public static final String SCHEMA_CONSUMER_GROUP     = "CONSUMER_GROUP";
    /** Logical schema for broker nodes; leaf type is UmiTypes.Endpoint. */
    public static final String SCHEMA_ENDPOINT           = "ENDPOINT";
    public static final String DEFAULT_SCHEMA            = "TOPIC";
    public static final String DEFAULT_SECURITY_PROTOCOL = "PLAINTEXT";
    public static final int    DEFAULT_PORT              = 9092;

    /** Browse listLeaf attribute keys (flow to frontend objAttr as-is). */
    public static final String ATTR_CLUSTER_ID           = "kafka_cluster_id";
    public static final String ATTR_BROKER_ID            = "kafka_broker_id";
    public static final String ATTR_HOST                 = "kafka_host";
    public static final String ATTR_PORT                 = "kafka_port";
    public static final String ATTR_IS_CONTROLLER        = "kafka_is_controller";
    public static final String ATTR_PARTITION_COUNT      = "kafka_partition_count";
    public static final String ATTR_REPLICA_COUNT        = "kafka_replica_count";
    public static final String ATTR_UNDER_REPLICATED     = "kafka_under_replicated";
    public static final String ATTR_PARTITIONS_JSON      = "kafka_partitions_json";
    public static final String ATTR_STATE                = "kafka_state";
    public static final String ATTR_MEMBER_COUNT         = "kafka_member_count";
    public static final String ATTR_TOPICS_JSON          = "kafka_topics_json";

    public static final String JSON_PARTITION_ID         = "id";
    public static final String JSON_PARTITION_LEADER     = "leader";
    public static final String JSON_PARTITION_REPLICAS   = "replicas";
    public static final String JSON_PARTITION_ISR        = "isr";
    public static final String JSON_TOPIC_NAME           = "topic";
    public static final String JSON_TOPIC_COMMITTED      = "committed";
    public static final String JSON_TOPIC_LATEST         = "latest";
    public static final String JSON_TOPIC_LAG            = "lag";

    /** Placeholder when committed/latest/lag is unavailable. */
    public static final String JSON_NA                   = "n/a";

    /** Stable i18n key names resolved by console KafkaOpsService. */
    public static final String ERR_INVALID_LAYOUT              = "CONSOLE_KAFKA_OPS_INVALID_LAYOUT";
    public static final String ERR_TOPIC_NOT_FOUND             = "CONSOLE_KAFKA_OPS_TOPIC_NOT_FOUND";
    public static final String ERR_PARTITIONS_INCREASE_ONLY    = "CONSOLE_KAFKA_OPS_PARTITIONS_INCREASE_ONLY";
    public static final String ERR_REPLICATION_EXCEEDS_BROKERS = "CONSOLE_KAFKA_OPS_REPLICATION_EXCEEDS_BROKERS";
    public static final String ERR_TOPIC_NO_PARTITIONS         = "CONSOLE_KAFKA_OPS_TOPIC_NO_PARTITIONS";
    public static final String ERR_RESET_NO_PARTITIONS         = "CONSOLE_KAFKA_OPS_RESET_NO_PARTITIONS";
    public static final String ERR_OFFSET_REQUIRED             = "CONSOLE_KAFKA_OPS_OFFSET_REQUIRED";
    public static final String ERR_TIMESTAMP_REQUIRED          = "CONSOLE_KAFKA_OPS_TIMESTAMP_REQUIRED";
    public static final String ERR_GROUP_NOT_EMPTY             = "CONSOLE_KAFKA_OPS_GROUP_NOT_EMPTY";

    private KafkaKeys(){
    }
}
