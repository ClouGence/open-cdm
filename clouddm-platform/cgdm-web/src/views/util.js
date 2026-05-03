import i18n from '../i18n';

export default {
  jobType: {
    MIGRATION: i18n.global.t('quan-liang-qian-yi'),
    SYNC: i18n.global.t('zeng-liang-tong-bu'),
    SUBSCRIBE: i18n.global.t('shu-ju-ding-yue'),
    CHECK: i18n.global.t('xiao-yan-yu-ding-zheng'),
    STRUCT_MIGRATION: i18n.global.t('jie-gou-qian-yi'),
    REVISE: i18n.global.t('shu-ju-ding-zheng')
  },
  jobTypeIcon: {
    MIGRATION: 'iconshujuqianyi',
    SYNC: 'iconshujutongbu',
    SUBSCRIBE: 'md-checkmark-circle-outline',
    CHECK: 'iconshujujiaoyan',
    STRUCT_MIGRATION: 'iconjiegouqianyi'
  },
  taskState: {
    CREATING: i18n.global.t('chuang-jian'),
    CREATED: i18n.global.t('yi-chuang-jian'),
    INIT: i18n.global.t('ren-wu-chu-shi-hua'),
    FULL: i18n.global.t('quan-liang-qian-yi'),
    INCREMENT: i18n.global.t('zeng-liang-tong-bu'),
    INCRE: i18n.global.t('zeng-liang-tong-bu'),
    CATCH_UP: i18n.global.t('zeng-liang-zhui-ping'),
    FULL_CHECK: i18n.global.t('quan-liang-xiao-yan'),
    FULL_REVERSE_CHECK: i18n.global.t('quan-liang-ding-zheng-xiao-yan'),
    FULL_REVISE: i18n.global.t('quan-liang-ding-zheng'),
    REVIEW_DIFF: i18n.global.t('ding-zheng-yi-chang'),
    SYNCING: i18n.global.t('shu-ju-tong-bu'),
    PAUSE: i18n.global.t('zan-ting'),
    EXPIRED: i18n.global.t('yi-guo-qi'),
    ERROR: i18n.global.t('cuo-wu'),
    COMPLETE: i18n.global.t('yi-wan-cheng'),
    CHECK: i18n.global.t('shu-ju-xiao-yan'),
    R_CHECK: i18n.global.t('zai-ci-shu-ju-xiao-yan'),
    REVISE: i18n.global.t('shu-ju-ding-zheng'),
    R_REVISE: i18n.global.t('zai-ci-shu-ju-ding-zheng'),
    ANY: i18n.global.t('ren-he-jie-duan'),
    DELETE: i18n.global.t('yi-shan-chu'),
    TIMING_SCHEDULE: i18n.global.t('tiao-du-zhong'),
    TIMING_SCHEDULE_PAUSE: i18n.global.t('yi-zan-ting-tiao-du')
  },
  taskStatus: {
    INIT: i18n.global.t('wei-kai-shi'),
    WAIT_START: i18n.global.t('deng-dai-kai-shi'),
    RUNNING: i18n.global.t('yun-hang-zhong'),
    DONE: i18n.global.t('yi-wan-cheng'),
    WAIT_STOP: i18n.global.t('deng-dai-ting-zhi'),
    STOP: i18n.global.t('yi-ting-zhi'),
    ABNORMAL: i18n.global.t('yi-chang'),
    WAIT_COMPLETE: i18n.global.t('deng-dai-wan-cheng'),
    COMPLETE: i18n.global.t('yi-wan-cheng'),
    DELETE: i18n.global.t('yi-shan-chu'),
    WAIT_RESTART: i18n.global.t('deng-dai-zhong-qi')
  },
  taskSateToTaskType: {
    FULL: 'FULL',
    INCRE: 'INCREMENT',
    CATCH_UP: 'INCREMENT',
    CHECK: 'CHECK',
    INIT: 'BUILD_STRUCT',
    REVISE: 'REVISE'
  },
  tagInfo: {
    FILTER: i18n.global.t('shu-ju-guo-lv-xiang-guan-de-can-shu'),
    MAPPING: i18n.global.t('yuan-he-mu-biao-ying-she-xiang-guan-de-pei-zhi'),
    PERFORMANCE: i18n.global.t('xing-neng-xiang-guan-de-pei-zhi'),
    NORMAL: i18n.global.t('pu-tong-can-shu'),
    GENERAL: i18n.global.t('pu-tong-can-shu'),
    USER_PREFER: i18n.global.t('yong-hu-pian-hao'),
    COMMON: i18n.global.t('pu-tong-can-shu'),
    TIME: i18n.global.t('shi-jian-xiang-guan-de-can-shu-bi-ru-xian-cheng-tiao-du-de-zhou-qi-yi-ji-yi-xie-shi-jian-yu-zhi'),
    RESERVERD: i18n.global.t('canal-ji-cheng-guo-lai-de-bao-liu-can-shu-wei-lai-ban-ben-jiang-bei-yi-chu'),
    POSITION: i18n.global.t('tong-bu-ren-wu-wei-dian-xiang-guan-de-can-shu'),
    DATA_EXPORT: i18n.global.t('shu-ju-dao-chu'),
    GH_OST: 'GH_OST'
  },
  consoleJob: {
    DATA_JOB_CREATE: i18n.global.t('chuang-jian-ren-wu'),
    DATA_JOB_DELETE: i18n.global.t('shan-chu-ren-wu'),
    RDS_ADD_PUBLIC_NET: i18n.global.t('rds-kai-fang-gong-wang-quan-xian'),
    RDS_AUTO_ADD_ACCOUNT: i18n.global.t('a-li-yun-shu-ju-yuan-zi-dong-chuang-jian-zhang-hao'),
    MANUAL_SCHEDULE_TASK: i18n.global.t('shou-dong-tiao-du-ren-wu'),
    DATA_TASK_COMPLETE_ACTION: i18n.global.t('wan-cheng-ren-wu'),
    INSTALL_ECS: i18n.global.t('zhu-ce-ecs-ji-qi'),
    UNINSTALL_ECS: i18n.global.t('xie-zai-ecs-ji-qi'),
    INSTALL_LOCAL_MAC: i18n.global.t('zhu-ce-ben-di-ji-qi'),
    UNINSTALL_LOCAL_MAC: i18n.global.t('xie-zai-ben-di-ji-qi'),
    RDS_ADD_IP_WHITE_LIST: i18n.global.t('rds-tian-jia-bai-ming-dan'),
    RDS_ADD_SECURITY_INFO: i18n.global.t('rds-tian-jia-an-quan-xin-xi'),
    START_ECS_CLIENT: i18n.global.t('qi-dong-ecs-shang-de-ke-hu-duan'),
    EDIT_DATA_JOB: i18n.global.t('xiu-gai-ding-yue'),
    ALIYUN_ADD_WHITELIST_INFO: i18n.global.t('a-li-yun-shu-ju-ku-tian-jia-bai-ming-dan'),
    UPGRADE_ECS: i18n.global.t('geng-xin-ecs-shang-de-ke-hu-duan'),
    DATA_JOB_REPLAY: i18n.global.t('zhong-pao-ren-wu'),
    TAKE_OVER_USER_RESOURCE: i18n.global.t('yi-jiao-yong-hu-zi-yuan'),
    DELETE_USER: i18n.global.t('shan-chu-yong-hu'),
    INCRE_TASK_DETACH: i18n.global.t('zhai-chu-zeng-liang-ren-wu'),
    INCRE_TASK_ATTACH: i18n.global.t('gua-zai-zeng-liang-ren-wu'),
    CLEAN_DATA_JOB_RESOURCE: i18n.global.t('qing-li-zi-ding-yi-dai-ma-bao'),
    WORKER_IPS_ADD_TO_ALIYUN_LIST: i18n.global.t('tian-jia-ji-qi-ip-dao-ji-qun-ren-wu-suo-guan-lian-yun-shu-ju-ku-bai-ming-dan')
  },
  consoleJobStatus: {
    WAIT_START: i18n.global.t('deng-dai-kai-shi'),
    EXECUTE: i18n.global.t('zhi-hang-zhong'),
    SUCCESS: i18n.global.t('cheng-gong'),
    FAILED: i18n.global.t('shi-bai'),
    CANCELED: i18n.global.t('yi-qu-xiao'),
    SKIP: i18n.global.t('yi-hu-lve')
  },
  workerStatus: {
    WAIT_TO_ONLINE: i18n.global.t('deng-dai-shang-xian'),
    ONLINE: i18n.global.t('zai-xian'),
    WAIT_TO_OFFLINE: i18n.global.t('deng-dai-xia-xian'),
    OFFLINE: i18n.global.t('li-xian'),
    ABNORMAL: i18n.global.t('yi-chang')
  },
  workerType: {
    BARE_METAL: i18n.global.t('wu-li-ji'),
    VM: i18n.global.t('xu-ni-ji'),
    ECS: i18n.global.t('a-li-yun-fu-wu-qi')
  },
  cloudOrIdcName: {
    SELF_MAINTENANCE: i18n.global.t('zi-jian-ji-fang'),
    ALIBABA_CLOUD: i18n.global.t('a-li-yun'),
    ALIBABA_CLOUD_HOSTED: i18n.global.t('a-li-yun'),
    AWS_CLOUD_HOSTED: i18n.global.t('ya-ma-xun-yun')
  },
  alarmIndicator: {
    DELAY_MS: i18n.global.t('ren-wu-yan-chi-shi-jian'),
    DELAY_MIN: i18n.global.t('ren-wu-yan-chi-shi-jian')
  },
  aliyunType: {
    MySQL: 'RDS for MySQL',
    PostgreSQL: 'RDS for PostgreSQL',
    Greenplum: 'ADB for PG',
    ElasticSearch: 'ElasticSearch',
    RocketMQ: 'RocketMQ',
    Kafka: 'Kafka',
    RabbitMQ: 'RabbitMQ',
    DRDS: 'DRDS',
    PolarDbX: 'PolarDB-X',
    AdbForMySQL: 'ADB for MySQL',
    PolarDbMySQL: 'PolarDbMySQL',
    PolarDBPg: 'PolarDBPg',
    ClickHouse: 'ClickHouse',
    MongoDB: 'MongoDB',
    Redis: 'Redis',
    SQLServer: 'SQLServer',
    OssFile: 'OssFile',
    DashScope: 'DashScope',
    DataLakeFormation: 'DataLakeFormation'
  },
  selfType: {
    PolarDbX: 'PolarDB-X'
  },
  dataTaskType: {
    FULL: i18n.global.t('quan-liang-qian-yi'),
    INCREMENT: i18n.global.t('zeng-liang-tong-bu'),
    CHECK: i18n.global.t('shu-ju-xiao-yan'),
    BUILD_STRUCT: i18n.global.t('jie-gou-qian-yi'),
    REVISE: i18n.global.t('shu-ju-ding-zheng')
  },
  deployType: {
    SELF_MAINTENANCE: i18n.global.t('zi-jian-shu-ju-ku'),
    ALIBABA_CLOUD_HOSTED: i18n.global.t('a-li-yun'),
    AWS_CLOUD_HOSTED: 'AWS',
    MICROSOFT_AZURE_CLOUD_HOSTED: 'AZURE'
  },
  hostType: {
    PRIVATE: i18n.global.t('si-you-wang-luo'),
    PUBLIC: i18n.global.t('gong-you-wang-luo')
  },
  role: {
    SYSTEM: i18n.global.t('xi-tong-guan-li-yuan'),
    DEFAULT: i18n.global.t('pu-tong-yong-hu'),
    ADMIN: i18n.global.t('guan-li-yuan'),
    ADMIN_READONLY: i18n.global.t('zhi-du-guan-li-yuan'),
    ORG_ADMIN: i18n.global.t('pu-tong-yong-hu')
  },
  region: {
    hangzhou: i18n.global.t('hang-zhou'),
    shanghai: i18n.global.t('shang-hai'),
    beijing: i18n.global.t('bei-jing'),
    shenzhen: i18n.global.t('shen-zhen'),
    guangzhou: i18n.global.t('guang-zhou'),
    customer: i18n.global.t('bu-xian'),
    zhangjiakou: i18n.global.t('zhang-jia-kou'),
    huhehaote: i18n.global.t('hu-he-hao-te'),
    hongkong: i18n.global.t('xiang-gang'),
    singapore: i18n.global.t('xin-jia-po'),
    silicon_valley: i18n.global.t('gui-gu'),
    london: i18n.global.t('lun-dun'),
    qingdao: i18n.global.t('qing-dao'),
    mq_internet_access: i18n.global.t('gong-wang'),
    hangzhou_finance: i18n.global.t('hang-zhou-jin-rong-yun'),
    virginia: i18n.global.t('fu-ji-ni-ya'),
    japan: i18n.global.t('ri-ben'),
    australia: i18n.global.t('ao-da-li-ya'),
    malaysia: i18n.global.t('ma-lai-xi-ya'),
    indonesia: i18n.global.t('yin-du-ni-xi-ya'),
    philippine: i18n.global.t('fei-lv-bin'),
    german: i18n.global.t('de-guo'),
    india: i18n.global.t('yin-du'),
    dubai: i18n.global.t('di-bai'),
    shanghai_finance: i18n.global.t('shang-hai-jin-rong-yun'),
    shenzhen_finance: i18n.global.t('shen-zhen-jin-rong-yun'),
    beijing_finance: i18n.global.t('bei-jing-jin-rong-yun'),
    wulanchabu: i18n.global.t('wu-lan-cha-bu')
  },
  statusIcon: {
    'icon-zanting': i18n.global.t('yi-zan-ting'),
    'icon-zhengchang': i18n.global.t('zheng-chang'),
    'icon-yichang': i18n.global.t('yi-chang'),
    'icon-wancheng': i18n.global.t('yi-wan-cheng')
  },
  filterType: {
    autoCreated: i18n.global.t('dai-chuang-jian-biao'),
    noCreate: i18n.global.t('yi-cun-zai-biao'),
    hasPk: i18n.global.t('you-zhu-jian-biao'),
    noPk: i18n.global.t('wu-zhu-jian-biao'),
    column: i18n.global.t('lie-ming-shai-xuan'),
    differPk: i18n.global.t('zhu-jian-wei-dui-qi'),
    hasWhere: i18n.global.t('yi-tian-jia-where-tiao-jian'),
    noWhere: i18n.global.t('wei-tian-jia-where-tiao-jian'),
    hasInJob: i18n.global.t('yi-zai-ren-wu-zhong-de-biao'),
    notInJob: i18n.global.t('wei-zai-ren-wu-zhong-de-biao'),
    hasSelected: i18n.global.t('yi-xuan-ze-de-biao'),
    notSelected: i18n.global.t('wei-xuan-ze-de-biao'),
    superTable: i18n.global.t('chao-ji-biao'), // tdengine
    normalTable: i18n.global.t('pu-tong-biao') // tdengine
  },
  resourceType: {
    WORKER: i18n.global.t('tong-bu-ji-qi'),
    DATASOURCE: i18n.global.t('shu-ju-yuan'),
    DATA_JOB: i18n.global.t('tong-bu-ren-wu')
  },
  createStatus: {
    CREATED: i18n.global.t('chuang-jian-wan-cheng'),
    CREATING: i18n.global.t('chuang-jian-zhong-0')
  },
  licenseType: {
    fullTransferCount: i18n.global.t('quan-liang-qian-yi'),
    incrementDuration: i18n.global.t('zeng-liang-tong-bu'),
    checkCount: i18n.global.t('quan-liang-xiao-yan'),
    structCount: i18n.global.t('jie-gou-qian-yi')
  },
  licenseTypeDefault: {
    FULL_TRANSFER_COUNT: i18n.global.t('quan-liang-qian-yi'),
    INCREMENT_SYNC_DURATION: i18n.global.t('zeng-liang-tong-bu'),
    FULL_CHECK_COUNT: i18n.global.t('quan-liang-xiao-yan'),
    STRUCT_TRANSFER_COUNT: i18n.global.t('jie-gou-qian-yi')
  },
  licenseTypeDefaultMeta: {
    '1_TIME_FULL_TRANSFER_COUNT': i18n.global.t('dan-ci-quan-liang-shu-ju-qian-yi'),
    '5_TIME_FULL_TRANSFER_COUNT': i18n.global.t('5-ci-quan-liang-shu-ju-qian-yi'),
    '10_TIME_FULL_TRANSFER_COUNT': i18n.global.t('10-ci-quan-liang-shu-ju-qian-yi'),
    '30_TIME_FULL_TRANSFER_COUNT': i18n.global.t('30-ci-quan-liang-shu-ju-qian-yi'),
    '100_TIME_FULL_TRANSFER_COUNT': i18n.global.t('100-ci-quan-liang-shu-ju-qian-yi'),
    '24_HOUR_INCREMENT_SYNC_DURATION': i18n.global.t('24-xiao-shi-zeng-liang-tong-bu'),
    '168_HOUR_INCREMENT_SYNC_DURATION': i18n.global.t('7-tian-zeng-liang-tong-bu'),
    '720_HOUR_INCREMENT_SYNC_DURATION': i18n.global.t('30-tian-zeng-liang-tong-bu'),
    '4320_HOUR_INCREMENT_SYNC_DURATION': i18n.global.t('180-tian-zeng-liang-tong-bu'),
    '8640_HOUR_INCREMENT_SYNC_DURATION': i18n.global.t('360-tian-zeng-liang-tong-bu'),
    '1_TIME_FULL_CHECK_COUNT': i18n.global.t('dan-ci-quan-liang-shu-ju-xiao-yan'),
    '5_TIME_FULL_CHECK_COUNT': i18n.global.t('5-ci-quan-liang-shu-ju-xiao-yan'),
    '10_TIME_FULL_CHECK_COUNT': i18n.global.t('10-ci-quan-liang-shu-ju-xiao-yan'),
    '30_TIME_FULL_CHECK_COUNT': i18n.global.t('30-ci-quan-liang-shu-ju-xiao-yan'),
    '100_TIME_FULL_CHECK_COUNT': i18n.global.t('100-ci-quan-liang-shu-ju-xiao-yan'),
    '1_TIME_STRUCT_TRANSFER_COUNT': i18n.global.t('dan-ci-jie-gou-qian-yi'),
    '5_TIME_STRUCT_TRANSFER_COUNT': i18n.global.t('5-ci-jie-gou-qian-yi'),
    '10_TIME_STRUCT_TRANSFER_COUNT': i18n.global.t('10-ci-jie-gou-qian-yi'),
    '30_TIME_STRUCT_TRANSFER_COUNT': i18n.global.t('30-ci-jie-gou-qian-yi'),
    '100_TIME_STRUCT_TRANSFER_COUNT': i18n.global.t('100-ci-jie-gou-qian-yi')
  },
  securityType: {
    KERBEROS: 'KERBEROS',
    USER_PASSWD_WITH_TLS: i18n.global.t('zhang-hao-mi-ma-jia-tls'),
    USER_PASSWD: i18n.global.t('zhang-hao-mi-ma'),
    NONE: i18n.global.t('wu-zhang-hao-mi-ma'),
    ONLY_USER: i18n.global.t('you-zhang-hao-wu-mi-ma'),
    ONLY_PASSWD: i18n.global.t('wu-zhang-hao-you-mi-ma'),
    CA_CERTIFICATE: i18n.global.t('ca-zheng-shu'),
    AK_SK: i18n.global.t('aksk')
  },
  validation: {
    account: ['USER_PASSWD', 'ONLY_USER', 'KERBEROS', 'USER_PASSWD_WITH_TLS'],
    password: ['USER_PASSWD', 'ONLY_PASSWD', 'USER_PASSWD_WITH_TLS']
  },
  testSecurityType: ['USER_PASSWD', 'NONE', 'ONLY_USER', 'ONLY_PASSWD'],
  posType: {
    MYSQL_LOG_FILE_POS: i18n.global.t('wen-jian-wei-dian'),
    MYSQL_GTID_POS: i18n.global.t('gtid-wei-dian'),
    MYSQL_TIMESTAMP_POS: i18n.global.t('shi-jian-chuo-wei-dian'),
    ORACLE_TIMESTAMP_POS: i18n.global.t('shi-jian-chuo-wei-dian'),
    ORACLE_SCN_POS: i18n.global.t('scn-wei-dian'),
    ORACLE_OLR_POS: i18n.global.t('scn-wei-dian'),
    OCEANBASE_LOG_FILE_POS: i18n.global.t('wen-jian-wei-dian'),
    OCEANBASE_TIMESTAMP_POS: i18n.global.t('shi-jian-chuo-wei-dian'),
    OCEANBASE_BINLOG_TIMESTAMP_POS: i18n.global.t('shi-jian-chuo-wei-dian-binlog'),
    POLAR_X_LOG_FILE_POS: i18n.global.t('wen-jian-wei-dian'),
    POLAR_X_TIMESTAMP_POS: i18n.global.t('shi-jian-chuo-wei-dian'),
    SQLSERVER_LSN_POS: i18n.global.t('lsn-wei-dian'),
    SQLSERVER_TIMESTAMP_POS: i18n.global.t('shi-jian-chuo-wei-dian'),
    TDSQLC_MY_LOG_FILE_POS: i18n.global.t('wen-jian-wei-dian'),
    TDSQLC_MY_TIMESTAMP_POS: i18n.global.t('shi-jian-chuo-wei-dian')
  },
  columnMapping: {
    clougence_cloudcanal_filter_after_mapping_column: i18n.global.t('jin-ding-yue-bu-xie-ru-dui-duan')
  },
  deployDsMap: {
    SELF_MAINTENANCE: {
      PolarDbX: 'PolarDB-X',
      GaussDBForOpenGauss: 'OpenGauss'
    },
    ALIBABA_CLOUD_HOSTED: {
      MySQL: 'RDS for MySQL',
      PostgreSQL: 'RDS for PostgreSQL',
      Greenplum: 'ADB for PG',
      ElasticSearch: 'ElasticSearch',
      RocketMQ: 'RocketMQ',
      Kafka: 'Kafka',
      RabbitMQ: 'RabbitMQ',
      DRDS: 'DRDS',
      PolarDbX: 'PolarDB-X',
      AdbForMySQL: 'ADB for MySQL',
      PolarDbMySQL: 'PolarDbMySQL',
      ClickHouse: 'ClickHouse',
      MongoDB: 'MongoDB',
      Redis: 'Redis',
      SQLServer: 'SQLServer'
    },
    AWS_CLOUD_HOSTED: {
      AuroraMySQL: 'Aurora (MySQL)',
      AuroraPostgreSQL: 'Aurora (PostgreSQL)',
      MySQL: 'MySQL',
      PostgreSQL: 'PostgreSQL',
      MariaDB: 'MariaDB',
      Oracle: 'Oracle',
      SQLServer: 'Microsoft SQLServer',
      ElastiCache: 'ElastiCache(redis)'
    },
    MICROSOFT_AZURE_CLOUD_HOSTED: {
      MySQL: 'Azure for MySQL',
      PostgreSQL: 'Azure for PostgreSQL',
      MariaDB: 'Azure for MariaDB',
      SQLServer: 'Azure SQL'
    },
    HUAWEI_CLOUD_HOSTED: {},
    TENCENT_CLOUD_HOSTED: {},
    INDEPENDENT_CLOUD_PLATFORM: {}
  },
  tableType: {
    SUPER_TABLE: 'SUPER_TABLE',
    CHILD_TABLE: 'CHILD_TABLE',
    NORMAL_TABLE: 'NORMAL_TABLE'
  },
  cloudMode: {
    BYOC: i18n.global.t('byoc-mo-shi'),
    MANAGED: i18n.global.t('quan-guan-li-mo-shi')
  }
};
