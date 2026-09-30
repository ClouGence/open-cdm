/** Must stay in sync with KafkaKeys ATTR_* / JSON_* on the backend. */
export const KAFKA_ATTR = {
  CLUSTER_ID: 'kafka_cluster_id',
  BROKER_ID: 'kafka_broker_id',
  HOST: 'kafka_host',
  PORT: 'kafka_port',
  IS_CONTROLLER: 'kafka_is_controller',
  PARTITION_COUNT: 'kafka_partition_count',
  REPLICA_COUNT: 'kafka_replica_count',
  UNDER_REPLICATED: 'kafka_under_replicated',
  PARTITIONS_JSON: 'kafka_partitions_json',
  STATE: 'kafka_state',
  MEMBER_COUNT: 'kafka_member_count',
  TOPICS_JSON: 'kafka_topics_json'
};

export const KAFKA_JSON = {
  PARTITION_ID: 'id',
  PARTITION_LEADER: 'leader',
  PARTITION_REPLICAS: 'replicas',
  PARTITION_ISR: 'isr',
  TOPIC_NAME: 'topic',
  TOPIC_COMMITTED: 'committed',
  TOPIC_LATEST: 'latest',
  TOPIC_LAG: 'lag'
};

export const KAFKA_LEAF = {
  TOPIC: 'TOPIC',
  CONSUMER_GROUP: 'CONSUMER_GROUP',
  ENDPOINT: 'ENDPOINT'
};

export function attrOf(node, key) {
  if (!node || !node.objAttr) {
    return '';
  }
  const value = node.objAttr[key];
  if (value === undefined || value === null) {
    return '';
  }
  return String(value);
}

export function parseJsonArray(raw) {
  if (!raw) {
    return [];
  }
  try {
    const parsed = JSON.parse(raw);
    if (Array.isArray(parsed)) {
      return parsed;
    }
    return [];
  } catch (e) {
    return [];
  }
}
