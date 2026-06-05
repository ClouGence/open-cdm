export const MYSQL = 'MySQL';
export const POLAR = ['PolarDbMySQL'];
export const PG = ['PostgreSQL', 'Greenplum'];
export const ORACLE = ['Oracle'];
export const HAS_SCHEMA = ['Oracle', 'PostgreSQL', 'Greenplum'];
export const MQ = ['Kafka', 'RocketMQ', 'RabbitMQ'];
export const ES = ['ElasticSearch'];
export const HIVE = ['Hive'];
export const RABBITMQ = ['RabbitMQ'];
export const NEED_AK_SK = ['RocketMQ', 'RabbitMQ'];
export const KAFKA = ['Kafka'];
export const DRDS = ['DRDS', 'PolarDbX'];
export const DISABLE_EDIT_SOURCE_DATASOURCE = ['Kafka', 'RocketMQ', 'RabbitMQ', 'Oracle'];
export const DISABLE_EDIT_TARGET_DATASOURCE = ['Oracle', 'ElasticSearch', 'Hive'];
export const REDIS = ['Redis'];
export const KUDU = ['Kudu'];
export const CK = ['ClickHouse'];
export const MONGO = ['MongoDB'];
export const NO_DB = ['Kafka', 'RocketMQ', 'RabbitMQ', 'MongoDB', 'Redis', 'ElasticSearch'];

export default {
  MYSQL,
  POLAR,
  PG,
  ORACLE,
  HAS_SCHEMA,
  MQ,
  ES,
  HIVE,
  RABBITMQ,
  NEED_AK_SK,
  KAFKA,
  DRDS,
  DISABLE_EDIT_TARGET_DATASOURCE,
  DISABLE_EDIT_SOURCE_DATASOURCE,
  REDIS,
  KUDU,
  CK,
  MONGO,
  NO_DB
};
