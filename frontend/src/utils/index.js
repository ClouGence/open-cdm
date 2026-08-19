import dayjs from '@/utils/dayjsSetup';
import i18n from '@/i18n';
import DataSourceGroup from '../views/dataSourceGroup.json';

export const formatTime = (value, fmt = 'YYYY/MM/DD') => dayjs(value).format(fmt);

export const toUtcISOString = (value) => {
  const time = dayjs(value);
  return time.isValid() ? time.toISOString() : '';
};

export const generateData = (list) => list;

export const dsGroup = {
  noStruct: ['Redis'],
  supportTransaction: ['MySQL', 'PostgreSQL', 'Greenplum', 'Cloudberry']
};

export const noStruct = (type) => dsGroup.noStruct.includes(type);

export const dateRange = {
  1: [dayjs(), dayjs().add(1, 'd')],
  7: [dayjs(), dayjs().add(1, 'w')],
  30: [dayjs(), dayjs().add(1, 'M')]
};

export const isDb2 = (type) => DataSourceGroup.db2.includes(type);
export const isMaxCompute = (type) => DataSourceGroup.maxCompute.includes(type);
export const isOb = (type) => DataSourceGroup.ob.includes(type);
export const isHudi = (type) => DataSourceGroup.hudi.includes(type);
export const isTunnel = (type) => DataSourceGroup.tunnel.includes(type);
export const isMQ = (type) => DataSourceGroup.mq.includes(type);
export const isPG = (type) => DataSourceGroup.pg.includes(type);
export const isGP = (type) => DataSourceGroup.gp.includes(type);
export const isPolar = (type) => DataSourceGroup.polar.includes(type);
export const isPolarDbX = (type) => DataSourceGroup.polarDbX.includes(type);
export const isMySQL = (type) => DataSourceGroup.mysql.includes(type);
export const isMariaDB = (type) => DataSourceGroup.mariaDb.includes(type);
export const isOracle = (type) => DataSourceGroup.oracle.includes(type);
export const isHana = (type) => DataSourceGroup.hana.includes(type);
export const isMongoDB = (type) => DataSourceGroup.mongo.includes(type);
export const isIceberg = (type) => DataSourceGroup.iceberg.includes(type);
export const isStarRocks = (type) => DataSourceGroup.starrocks.includes(type);
export const isDuckDB = (type) => DataSourceGroup.duckDB.includes(type);
export const isDynamoDB = (type) => DataSourceGroup.dynamoDB.includes(type);
export const isTiDB = (type) => DataSourceGroup.tidb.includes(type);
export const isSQLServer = (type) => DataSourceGroup.sqlServer.includes(type);
export const isES = (type) => DataSourceGroup.es.includes(type);
export const isKudu = (type) => DataSourceGroup.kudu.includes(type);
export const isRedis = (type) => DataSourceGroup.redis.includes(type);
export const isCk = (type) => DataSourceGroup.ck.includes(type);
export const isKafka = (type) => DataSourceGroup.kafka.includes(type);
export const isHive = (type) => DataSourceGroup.hive.includes(type);
export const isPulsar = (type) => DataSourceGroup.pulsar.includes(type);
export const isPaimon = (type) => DataSourceGroup.paimon.includes(type);
export const isGrepTime = (type) => DataSourceGroup.grepTime.includes(type);
export const isTDengine = (type) => DataSourceGroup.tdEngine.includes(type);
export const isDameng = (type) => DataSourceGroup.dameng.includes(type);
export const isSqlserver = (type) => DataSourceGroup.sqlServer.includes(type);
export const isBedrock = (type) => DataSourceGroup.Bedrock.includes(type);
export const isGoogleDrive = (type) => DataSourceGroup.googleDrive.includes(type);
export const isYuque = (type) => DataSourceGroup.yuque.includes(type);
export const isGaussDB = (type) => DataSourceGroup.gaussDB.includes(type);
export const isAmazonMsk = (type) => DataSourceGroup.amazonMsk.includes(type);
export const isDeltaLake = (type) => DataSourceGroup.deltaLake.includes(type);
export const isTdsqlMySQL = (type) => DataSourceGroup.tdsqlMysql.includes(type);
export const isTdsqlCMySQL = (type) => DataSourceGroup.tdsqlCMysql.includes(type);
export const isRocketMQ = (type) => DataSourceGroup.rocketMq.includes(type);
export const isRabbitMQ = (type) => DataSourceGroup.rabbitMq.includes(type);

export const isDDLFilter = (type) => DataSourceGroup.ddlFilter.includes(type);
export const isNoColumn = (type) => DataSourceGroup.noColumn.includes(type);
export const isNoDb = (type) => DataSourceGroup.noDb.includes(type);
export const isNoStruct = (type) => DataSourceGroup.noStruct.includes(type);
export const isDrDs = (type) => DataSourceGroup.drds.includes(type);
export const separatePort = (type) =>
  ![
    'Kudu',
    'Redis',
    'ElastiCache',
    'ClickHouse',
    'ElasticSearch',
    'RabbitMQ',
    'RocketMQ',
    'Kafka',
    'MongoDB',
    'DocumentDB',
    'PolarDbX',
    'MaxCompute',
    'StarRocks',
    'Doris',
    'SelectDB',
    'Iceberg',
    'Pulsar',
    'OssFile',
    'S3File',
    'RagApi',
    'OpenAI',
    'DashScope',
    'HuggingFace',
    'Cohere',
    'DeepSeek',
    'LocalAI',
    'Ollama',
    'ZhipuAI',
    'Anthropic',
    'Bedrock',
    'DuckDB',
    'GoogleDrive',
    'Paimon',
    'Yuque',
    'DynamoDB',
    'DataLakeFormation',
    'AmazonMSK'
  ].includes(type);

export const showActiveLicense = (status) => [550, 555, 556, 557, 558, 559, 560, 561, 562, 563, 564, 565, 566].includes(status);
export const hasPartitionExpr = (type) => isStarRocks(type) || isIceberg(type) || isPaimon(type);
export const isSchemaMapping = (type) => DataSourceGroup.schemaMapping.includes(type);
export const isFile = (type) => DataSourceGroup.File.includes(type);
export const isRagApi = (type) => DataSourceGroup.RagApi.includes(type);

export const NODE_TYPE = {
  ENV: 'ENV',
  INSTANCE: 'INSTANCE',
  CATALOG: 'CATALOG',
  SCHEMA: 'SCHEMA'
};

export const DS_RIGHT_CLICK_MENU_ITEM = {
  MENU_BROWSE_CONSOLE: 'MENU_BROWSE_CONSOLE',
  MENU_BROWSE_COPY_NAME: 'MENU_BROWSE_COPY_NAME',
  MENU_BROWSE_COPY_JDBC: 'MENU_BROWSE_COPY_JDBC',
  MENU_BROWSE_PERMISSIONS: 'MENU_BROWSE_PERMISSIONS',
  // ***** Copy / /
  MENU_BROWSE_REFRESH: 'MENU_BROWSE_REFRESH',
  MENU_BROWSE_INSTANCE_REFRESH: 'MENU_BROWSE_INSTANCE_REFRESH',
  MENU_BROWSE_CATALOG_REFRESH: 'MENU_BROWSE_CATALOG_REFRESH',
  MENU_BROWSE_SCHEMA_REFRESH: 'MENU_BROWSE_SCHEMA_REFRESH',
  // ***** //
  MENU_BROWSE_INSTANCE_CREATE: 'MENU_BROWSE_INSTANCE_CREATE',
  MENU_BROWSE_INSTANCE_RENAME: 'MENU_BROWSE_INSTANCE_RENAME',
  MENU_BROWSE_INSTANCE_DROP: 'MENU_BROWSE_INSTANCE_DROP',
  // ***** //
  MENU_BROWSE_CATALOG_CREATE: 'MENU_BROWSE_CATALOG_CREATE',
  MENU_BROWSE_CATALOG_RENAME: 'MENU_BROWSE_CATALOG_RENAME',
  MENU_BROWSE_CATALOG_DROP: 'MENU_BROWSE_CATALOG_DROP',
  // ***** //
  MENU_BROWSE_SCHEMA_CREATE: 'MENU_BROWSE_SCHEMA_CREATE',
  MENU_BROWSE_SCHEMA_DROP: 'MENU_BROWSE_SCHEMA_DROP',
  MENU_BROWSE_SCHEMA_RENAME: 'MENU_BROWSE_SCHEMA_RENAME',
  // ***** //
  MENU_BROWSE_TABLE_CREATE: 'MENU_BROWSE_TABLE_CREATE'
};

export const TABLE_RIGHT_CLICK_MENU_ITEM = {
  MENU_BROWSE_COPY_NAME: 'MENU_BROWSE_COPY_NAME',
  MENU_BROWSE_PROPERTY: 'MENU_BROWSE_PROPERTY',
  // ***** //
  MENU_BROWSE_REFRESH: 'MENU_BROWSE_REFRESH',
  MENU_BROWSE_KEY_REFRESH: 'MENU_BROWSE_KEY_REFRESH',
  MENU_BROWSE_KEY_RENAME: 'MENU_BROWSE_KEY_RENAME',
  MENU_BROWSE_KEY_DROP: 'MENU_BROWSE_KEY_DROP',
  MENU_BROWSE_VIEW_REFRESH: 'MENU_BROWSE_VIEW_REFRESH',
  MENU_BROWSE_TABLE_REFRESH: 'MENU_BROWSE_TABLE_REFRESH',
  MENU_BROWSE_COLUMN_REFRESH: 'MENU_BROWSE_COLUMN_REFRESH',
  MENU_BROWSE_INDEX_REFRESH: 'MENU_BROWSE_INDEX_REFRESH',
  MENU_BROWSE_PARTITION_REFRESH: 'MENU_BROWSE_PARTITION_REFRESH',
  MENU_BROWSE_PRIMARY_REFRESH: 'MENU_BROWSE_PRIMARY_REFRESH',
  MENU_BROWSE_REFRESH_DICT: 'MENU_BROWSE_REFRESH_DICT',
  // TABLE
  MENU_BROWSE_TABLE_CREATE: 'MENU_BROWSE_TABLE_CREATE',
  MENU_BROWSE_TABLE_RENAME: 'MENU_BROWSE_TABLE_RENAME',
  MENU_BROWSE_TABLE_ALTER: 'MENU_BROWSE_TABLE_ALTER',
  MENU_BROWSE_TABLE_DATA: 'MENU_BROWSE_TABLE_DATA',
  MENU_BROWSE_TABLE_TRUNCATE: 'MENU_BROWSE_TABLE_TRUNCATE',
  MENU_BROWSE_TABLE_DROP: 'MENU_BROWSE_TABLE_DROP',
  MENU_BROWSE_TABLE_IMPORT: 'MENU_BROWSE_TABLE_IMPORT',
  MENU_BROWSE_TABLE_EXPORT: 'MENU_BROWSE_TABLE_EXPORT',
  MENU_BROWSE_TABLE_REQUEST: 'MENU_BROWSE_TABLE_REQUEST',
  MENU_BROWSE_TABLE_GENERATE: 'MENU_BROWSE_TABLE_GENERATE',
  MENU_BROWSE_TABLE_GET_DDL: 'MENU_BROWSE_TABLE_GET_DDL',
  MENU_BROWSE_TABLE_FAKER: 'MENU_BROWSE_TABLE_FAKER',
  MENU_BROWSE_TABLE_FAKER_INCREMENT: 'MENU_BROWSE_TABLE_FAKER_INCREMENT',
  MENU_BROWSE_INDEX_DROP: 'MENU_BROWSE_INDEX_DROP',
  MENU_BROWSE_PRIMARY_DROP: 'MENU_BROWSE_PRIMARY_DROP',
  // VIEW
  MENU_BROWSE_VIEW_CREATE: 'MENU_BROWSE_VIEW_CREATE',
  MENU_BROWSE_VIEW_ALTER: 'MENU_BROWSE_VIEW_ALTER',
  MENU_BROWSE_VIEW_DATA: 'MENU_BROWSE_VIEW_DATA',
  MENU_BROWSE_VIEW_RENAME: 'MENU_BROWSE_VIEW_RENAME',
  MENU_BROWSE_VIEW_DROP: 'MENU_BROWSE_VIEW_DROP',
  MENU_BROWSE_VIEW_REQUEST: 'MENU_BROWSE_VIEW_REQUEST',
  MENU_BROWSE_VIEW_COMPILE: 'MENU_BROWSE_VIEW_COMPILE',
  // MATERIALIZED
  MENU_BROWSE_MATERIALIZED_REQUEST: 'MENU_BROWSE_MATERIALIZED_REQUEST',
  MENU_BROWSE_MATERIALIZED_DROP: 'MENU_BROWSE_MATERIALIZED_DROP',
  // SYNONYM
  MENU_BROWSE_SYNONYM_REQUEST: 'MENU_BROWSE_SYNONYM_REQUEST',
  // TRIGGER
  MENU_BROWSE_TRIGGER_CREATE: 'MENU_BROWSE_TRIGGER_CREATE',
  MENU_BROWSE_TRIGGER_DROP: 'MENU_BROWSE_TRIGGER_DROP',
  MENU_BROWSE_TRIGGER_ALTER: 'MENU_BROWSE_TRIGGER_ALTER',
  MENU_BROWSE_TRIGGER_COMPILE: 'MENU_BROWSE_TRIGGER_COMPILE',
  MENU_BROWSE_TRIGGER_REQUEST: 'MENU_BROWSE_TRIGGER_REQUEST',
  // FUNCTION
  MENU_BROWSE_FUNCTION_CREATE: 'MENU_BROWSE_FUNCTION_CREATE',
  MENU_BROWSE_FUNCTION_ALTER: 'MENU_BROWSE_FUNCTION_ALTER',
  MENU_BROWSE_FUNCTION_DROP: 'MENU_BROWSE_FUNCTION_DROP',
  MENU_BROWSE_FUNCTION_COMPILE: 'MENU_BROWSE_FUNCTION_COMPILE',
  MENU_BROWSE_FUNCTION_REQUEST: 'MENU_BROWSE_FUNCTION_REQUEST',
  // PROCEDURE
  MENU_BROWSE_PROCEDURE_CREATE: 'MENU_BROWSE_PROCEDURE_CREATE',
  MENU_BROWSE_PROCEDURE_ALTER: 'MENU_BROWSE_PROCEDURE_ALTER',
  MENU_BROWSE_PROCEDURE_DROP: 'MENU_BROWSE_PROCEDURE_DROP',
  MENU_BROWSE_PROCEDURE_COMPILE: 'MENU_BROWSE_PROCEDURE_COMPILE',
  MENU_BROWSE_PROCEDURE_REQUEST: 'MENU_BROWSE_PROCEDURE_REQUEST',
  // DBLINK
  MENU_BROWSE_DBLINK_CREATE: 'MENU_BROWSE_DBLINK_CREATE',
  MENU_BROWSE_DBLINK_DROP: 'MENU_BROWSE_DBLINK_DROP',
  MENU_BROWSE_DBLINK_TEST: 'MENU_BROWSE_DBLINK_TEST',
  // JOB
  MENU_BROWSE_JOB_CREATE: 'MENU_BROWSE_JOB_CREATE',
  MENU_BROWSE_JOB_ALTER: 'MENU_BROWSE_JOB_ALTER',
  MENU_BROWSE_JOB_DROP: 'MENU_BROWSE_JOB_DROP',
  MENU_BROWSE_JOB_DISABLE: 'MENU_BROWSE_JOB_DISABLE',
  MENU_BROWSE_JOB_ENABLE: 'MENU_BROWSE_JOB_ENABLE',
  MENU_BROWSE_JOB_RUN: 'MENU_BROWSE_JOB_RUN',
  // SCHEDULE JOB
  MENU_BROWSE_SCHEDULE_DROP: 'MENU_BROWSE_SCHEDULE_DROP',
  MENU_BROWSE_SCHEDULE_ENABLE: 'MENU_BROWSE_SCHEDULE_ENABLE',
  MENU_BROWSE_SCHEDULE_DISABLE: 'MENU_BROWSE_SCHEDULE_DISABLE',
  MENU_BROWSE_SCHEDULE_RUN: 'MENU_BROWSE_SCHEDULE_RUN',
  MENU_BROWSE_SCHEDULE_CREATE: 'MENU_BROWSE_SCHEDULE_CREATE',
  // CONSTRAINT
  MENU_BROWSE_CONSTRAINT_ENABLE: 'MENU_BROWSE_CONSTRAINT_ENABLE',
  MENU_BROWSE_CONSTRAINT_DISABLE: 'MENU_BROWSE_CONSTRAINT_DISABLE',
  // USER
  MENU_BROWSE_USER_DROP: 'MENU_BROWSE_USER_DROP'
};

export const SOCKET_TYPE = {
  WS_ASYNC_EVENT: 'WS_ASYNC_EVENT'
};

export const ASYNC_TASK_STATUS = {
  INIT: 'INIT',
  BLOCK: 'BLOCK',
  WAIT_START: 'WAIT_START',
  RUNNING: 'RUNNING',
  FAILURE: 'FAILURE',
  CANCEL: 'CANCEL',
  COMPLETE: 'COMPLETE',
  PAUSE: 'PAUSE'
};

export const FAKER_TASK_STATUS = {
  INIT: 'INIT',
  RUNNING: 'RUNNING',
  PAUSE: 'PAUSE',
  COMPLETE: 'COMPLETE',
  WAITING_RESUME: 'WAITING_RESUME',
  WAITING_PAUSE: 'WAITING_PAUSE'
};

export const WS_TYPE = {
  WS_REQ_QUERY: 'WS_REQ_QUERY',
  WS_REQ_ECHO: 'WS_REQ_ECHO',
  WS_RES_QUERY: 'WS_RES_QUERY',
  WS_RES_ECHO: 'WS_RES_ECHO',
  WS_REQ_LANGUAGE: 'WS_REQ_LANGUAGE',
  WS_RES_LANGUAGE: 'WS_RES_LANGUAGE',
  WS_RES_ERROR: 'WS_RES_ERROR',
  WS_RES_ASYNC_EVENT: 'WS_RES_ASYNC_EVENT',
  WS_RES_DRIVER_DOWNLOAD_EVENT: 'WS_RES_DRIVER_DOWNLOAD_EVENT',
  WS_SYS_STATUS: 'WS_SYS_STATUS',
  WS_RES_EXPORT_EVENT: 'WS_RES_EXPORT_EVENT'
};

export const WS_REQ_QUERY_TYPE = {
  REQUEST_QUERY: 'RequestQuery',
  REQUEST_PLAN: 'RequestPlan',
  CANCEL_QUERY: 'CancelQuery',
  SWITCH_CTX: 'SwitchCtx',
  TX_STATUS: 'TxStatus',
  TX_COMMIT: 'TxCommit',
  TX_ROLLBACK: 'TxRollback',
  RECOVER_STATUS: 'RecoveryStatus',
  EXPLAIN: 'ExplainQuery'
};

export const RULE_WARN_LEVEL = {
  SUGGEST: i18n.global.t('ti-shi'),
  FAILURE: i18n.global.t('zu-sai')
};

export const ORDER_STATUS = {
  FINISH: 'FINISH',
  CLOSED: 'CLOSED',
  WAIT_PAID: 'WAIT_PAID',
  FAILED: 'FAILED'
};

export const STATUS_COLOR = {
  FINISH: '#52C41A',
  CLOSED: '#86909C',
  WAIT_PAID: '#F7BA1E',
  FAILED: '#FF4D4F'
};

export const STATUS_BG_COLOR = {
  FINISH: '#E8FFEA',
  CLOSED: '#F2F3F5',
  WAIT_PAID: '#FFFCE8',
  FAILED: '#FFECE8'
};

export const INVOICE_STATUS_COLOR = {
  true: '#52C41A',
  false: '#F7BA1E'
};

export const INVOICE_STATUS_BG_COLOR = {
  true: '#E8FFEA',
  false: '#FFFCE8'
};

export const VOUCHER_TYPE = {
  PRODUCT_SUBSCRIPTION: i18n.global.t('ding-yue'),
  PRODUCT_LICENSE: i18n.global.t('xu-ke-zheng'),
  SERVICE: i18n.global.t('fu-wu')
};

export const MySQL = 'MySQL';
export const ClickHouse = 'ClickHouse';
export const PostgreSQL = 'PostgreSQL';
export const RagApi = 'RagApi';
export const MariaDB = 'MariaDB';
export const GoogleDrive = 'GoogleDrive';
export const Kafka = 'Kafka';
export const Oracle = 'Oracle';
export const TiDB = 'TiDB';
export const PolarDbX = 'PolarDbX';
export const SQLServer = 'SQLServer';
export const OceanBase = 'OceanBase';
export const GaussDBForOpenGauss = 'GaussDBForOpenGauss';
export const Dameng = 'Dameng';
export const AmazonMSK = 'AmazonMSK';
export const DeltaLake = 'DeltaLake';
export const RabbitMQ = 'RabbitMQ';

export const NEW_TASK_LINK = [
  // `${MySQL}_${MySQL}`,
  // `${MariaDB}_${MySQL}`,
  // `${MariaDB}_${MariaDB}`,
  // `${MariaDB}_${TiDB}`,
  // `${MariaDB}_${PostgreSQL}`,
  // `${MariaDB}_${PolarDbX}`,
  // `${MariaDB}_${Oracle}`,
  // `${MariaDB}_${SQLServer}`,
  // `${MariaDB}_${ClickHouse}`,
  // `${MariaDB}_${GaussDBForOpenGauss}`,
  // `${MariaDB}_${Dameng}`,
  // `${MariaDB}_${Kafka}`,
  // `${MySQL}_${ClickHouse}`,
  // `${MySQL}_${DeltaLake}`,
  // `${MySQL}_${AmazonMSK}`,
  // `${Kafka}_${AmazonMSK}`,
  // `${AmazonMSK}_${Kafka}`,
  // `${AmazonMSK}_${AmazonMSK}`,
  // `${PostgreSQL}_${RabbitMQ}`
];

export const NEW_TASK_LINK_EDIT = [
  // `${MySQL}_${MySQL}`
];

export const NEW_TASK_LINK_SIMILAR = [
  // `${MariaDB}_${ClickHouse}`
  // `${MySQL}_${MySQL}`
];

export const SELECT_MODE = {
  LIST_ALL_AND_SELECT: 'LIST_ALL_AND_SELECT',
  SEARCH_AND_NOT_SELECT: 'SEARCH_AND_NOT_SELECT'
};

export const LLM_TYPE = {
  EMBEDDING: 'EMBEDDING',
  CHAT: 'CHAT'
};

export const PROCESSOR_CONFIG_TYPE = {
  FIELD_MAKER_PROCESSOR: 'FIELD_MAKER_PROCESSOR',
  FIELD_TRANSFORM_PROCESSOR: 'FIELD_TRANSFORM_PROCESSOR',
  WIDE_TABLE_PROCESSOR: 'WIDE_TABLE_PROCESSOR'
};

export const INVOICE_TYPE = {
  LICENSE: 'LICENSE',
  MONTHLY_BILL: 'MONTHLY_BILL'
};

export const INVOICE_TYPE_I18N = {
  LICENSE: i18n.global.t('xu-ke-zheng'),
  MONTHLY_BILL: i18n.global.t('yu-chong-zhi-zhang-dan')
};

export const INVOICE_APPLY_STATUS = {
  START_APPLY: 'START_APPLY',
  HANDLING: 'HANDLING',
  WAIT_TO_MODIFY: 'WAIT_TO_MODIFY',
  CANCEL: 'CANCEL',
  FINISH: 'FINISH'
};

export const INVOICE_APPLY_STATUS_I18N = {
  START_APPLY: i18n.global.t('shen-qing-fa-qi'),
  HANDLING: i18n.global.t('chu-li-zhong'),
  WAIT_TO_MODIFY: i18n.global.t('dai-xiu-gai'),
  CANCEL: i18n.global.t('yi-qu-xiao'),
  FINISH: i18n.global.t('yi-wan-cheng')
};

export const PREPAY_STATUS = {
  WAIT_PAID: 'WAIT_PAID',
  FAILED: 'FAILED',
  CLOSED: 'CLOSED',
  FINISHED: 'FINISHED'
};

export const DOWNLOAD_STATUS = {
  NULL: 'null',
  NOT_DOWNLOADED: 'NOT_DOWNLOADED',
  DOWNLOADING: 'DOWNLOADING',
  DOWNLOADED: 'DOWNLOADED'
};
