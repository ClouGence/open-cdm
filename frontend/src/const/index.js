import DATASOURCE_TYPE from '@/const/datasourceType';
import i18n from '../i18n';

const CREATE_TASK_STEPS = {
  ORIGINAL: 'ORIGINAL',
  FUNCTIONAL: 'FUNCTIONAL',
  TABLE_FILTER: 'TABLE_FILTER',
  CLEAN_DATA: 'CLEAN_DATA'
};

export const DATASOURCE_DEPLOY_TYPE = {
  SELF_MAINTENANCE: 'SELF_MAINTENANCE',
  ALIBABA_CLOUD_HOSTED: 'ALIBABA_CLOUD_HOSTED'
};

export const DATASOURCE_DEPLOY_TYPE_I18N = {
  SELF_MAINTENANCE: i18n.global.t('zi-jian-shu-ju-ku'),
  ALIBABA_CLOUD_HOSTED: i18n.global.t('a-li-yun'),
  AWS_CLOUD_HOSTED: i18n.global.t('ya-ma-xun-aws'),
  MICROSOFT_AZURE_CLOUD_HOSTED: i18n.global.t('wei-ruan-azure')
};

export const HOST_TYPE = {
  PUBLIC: 'PUBLIC',
  PRIVATE: 'PRIVATE'
};

export const SECOND_CONFIRM_EVENT_LIST = {
  DELETE_POSITION: 'DELETE_POSITION',
  DELETE_JOB: 'DELETE_JOB',
  DELETE_DATASOURCE: 'DELETE_DATASOURCE',
  DELETE_WORKER: 'DELETE_WORKER',
  RESET_POSITION: 'RESET_POSITION'
};

export { DATASOURCE_TYPE, CREATE_TASK_STEPS };

export const MYSQL_DEFAULT_STRATEGY = [
  {
    label: 'NOTHING',
    value: 'NOTHING'
  },
  {
    label: 'ZERO',
    value: 'ZERO'
  },
  {
    label: 'IS_NULL',
    value: 'IS_NULL'
  },
  {
    label: 'CURRENT',
    value: 'CURRENT'
  }
];

export const PG_DEFAULT_STRATEGY = [
  {
    label: 'NOTHING',
    value: 'NOTHING'
  },
  {
    label: 'IS_NULL',
    value: 'IS_NULL'
  },
  {
    label: 'CURRENT',
    value: 'CURRENT'
  },
  {
    label: 'UTC_ZERO',
    value: 'UTC_ZERO'
  }
];

export const EDITIONS_I18N = {
  COMMUNITY_VERSION: i18n.global.t('she-qu-ban'),
  POC_VERSION: i18n.global.t('poc-ban'),
  ENTERPRISE_VERSION: i18n.global.t('qi-ye-ban'),
  EXPERIENCE_VERSION: i18n.global.t('ti-yan-ban'),
  UNLIMITED: 'SVIP',
  FLAGSHIP_VERSION: i18n.global.t('qi-jian-ban')
};

export const JOB_TYPE_ICON = {
  MIGRATION: 'iconshujuqianyi',
  SYNC: 'iconshujutongbu',
  SUBSCRIBE: 'md-checkmark-circle-outline',
  CHECK: 'iconshujujiaoyan',
  STRUCT_MIGRATION: 'iconjiegouqianyi'
};

export const EDITIONS = {
  COMMUNITY_VERSION: 'COMMUNITY_VERSION',
  POC_VERSION: 'POC_VERSION',
  ENTERPRISE_VERSION: 'ENTERPRISE_VERSION',
  EXPERIENCE_VERSION: 'EXPERIENCE_VERSION'
};

export const AUTH_SHOW_TYPE = {
  AUTHED: 'AUTHED',
  DIFF_AUTHED: 'DIFF_AUTHED',
  NOT_AUTHED: 'NOT_AUTHED'
};

export const WORKER_OPERATION = {
  INSTALL: 'INSTALL',
  UNINSTALL: 'UNINSTALL',
  START_CLIENT: 'START_CLIENT',
  UPGRADE_ALL: 'UPGRADE_ALL',
  CANCEL_UPGRADE: 'CANCEL_UPGRADE',
  ROLLBACK_CLIENT: 'ROLLBACK_CLIENT',
  CANCEL_ROLLBACK: 'CANCEL_ROLLBACK'
};

export const WORKER_OPERATION_I18N = {
  INSTALL: i18n.global.t('an-zhuang'),
  UNINSTALL: i18n.global.t('xie-zai'),
  START_CLIENT: i18n.global.t('qi-dong'),
  UPGRADE_ALL: i18n.global.t('sheng-ji'),
  ROLLBACK_CLIENT: i18n.global.t('hui-gun'),
  CANCEL_ROLLBACK: i18n.global.t('qu-xiao-hui-gun'),
  CANCEL_UPGRADE: i18n.global.t('qu-xiao-sheng-ji')
};

export const OPERATION_STATUS = {
  PREPARING_INSTALL: 'PREPARING_INSTALL',
  INSTALLING: 'INSTALLING',
  INSTALLED: 'INSTALLED',
  UNINSTALLING: 'UNINSTALLING',
  UNINSTALLED: 'UNINSTALLED',
  PREPARING_UPGRADE: 'PREPARING_UPGRADE',
  UPGRADING: 'UPGRADING',
  UPGRADED: 'UPGRADED',
  CANCEL_UPGRADE: 'CANCEL_UPGRADE',
  PREPARING_ROLL_BACK: 'PREPARING_ROLL_BACK',
  ROLLING_BACK: 'ROLLING_BACK',
  ROLLED_BACK: 'ROLLED_BACK',
  CANCEL_ROLL_BACK: 'CANCEL_ROLL_BACK'
};

export const OPERATION_STATUS_I18N = {
  PREPARING_INSTALL: i18n.global.t('zhun-bei-an-zhuang'),
  INSTALLING: i18n.global.t('an-zhuang-zhong'),
  INSTALLED: i18n.global.t('yi-an-zhuang'),
  UNINSTALLING: i18n.global.t('xie-zai-zhong'),
  UNINSTALLED: i18n.global.t('yi-xie-zai'),
  PREPARING_UPGRADE: i18n.global.t('zhun-bei-sheng-ji'),
  UPGRADING: i18n.global.t('sheng-ji-zhong'),
  UPGRADED: i18n.global.t('yi-sheng-ji'),
  CANCEL_UPGRADE: i18n.global.t('qu-xiao-sheng-ji'),
  PREPARING_ROLL_BACK: i18n.global.t('zhun-bei-hui-gun'),
  ROLLING_BACK: i18n.global.t('hui-gun-zhong'),
  ROLLED_BACK: i18n.global.t('yi-hui-gun'),
  CANCEL_ROLL_BACK: i18n.global.t('qu-xiao-hui-gun')
};

export const LANG_OPTIONS = ['zh-CN', 'en-US', 'ja', 'ko', 'vi', 'th', 'fr', 'es', 'ms'];

export const LANG_I18N = {
  'zh-CN': '中文',
  'en-US': 'English',
  ja: '日本語',
  ko: '한국어',
  vi: 'Tiếng Việt',
  th: 'ไทย',
  fr: 'Français',
  es: 'Español',
  ms: 'Bahasa Melayu'
};

export const VERIFY_TYPE = {
  EMAIL_VERIFY_CODE: 'EMAIL_VERIFY_CODE',
  SMS_VERIFY_CODE: 'SMS_VERIFY_CODE',
  SMS: 'SMS_VERIFY_CODE'
};

export const UPDATE_HOST_TYPE = {
  PUBLICIP: 'publicIp',
  PRIVATEIP: 'privateIp',
  PUBLICHTTPHOST: 'publicHttpHost',
  PRIVATEHTTPHOST: 'privateHttpHost'
};

export const CLUSTER_ENV = {
  SELF_MAINTENANCE: 'SELF_MAINTENANCE',
  ALIBABA_CLOUD_HOSTED: 'ALIBABA_CLOUD_HOSTED'
};

export const CLUSTER_TYPE = {
  SELF_MAINTENANCE: {
    name: i18n.global.t('zi-jian-ji-fang'),
    value: 'self'
  },
  ALIBABA_CLOUD_HOSTED: {
    name: i18n.global.t('a-li-yun'),
    value: 'aliyun'
  }
};

export const STATUS_COLOR = {
  success: '#52C41A',
  warning: '#FFA30E',
  error: '#FF1815'
};

export const ACCOUNT_TYPE = {
  PRIMARY_ACCOUNT: 'PRIMARY_ACCOUNT',
  SUB_ACCOUNT: 'SUB_ACCOUNT',
  LDAP_ACCOUNT: 'LDAP_ACCOUNT'
};

export const LOGIN_TYPE = {
  LOGIN_PASSWORD: 'PASSWORD',
  LOGIN_LDAP: 'LDAP',
  LOGIN_AD: 'AD',
  OIDC: 'OIDC',
  WECHAT: 'Wechat',
  DINGTALK: 'DingTalk',
  FEISHU: 'Feishu'
};

export const TICKET_WAIT_STATUS = {
  WAIT_APPROVAL: i18n.global.t('deng-dai-shen-pi'),
  WAIT_CONFIRM: i18n.global.t('deng-dai-que-ren')
};
export const TICKET_STATUS = {
  PRE_INIT: i18n.global.t('deng-dai-fen-xi'),
  WAIT_APPROVAL: i18n.global.t('deng-dai-shen-pi'),
  WAIT_CONFIRM: i18n.global.t('deng-dai-que-ren'),
  WAIT_EXEC: i18n.global.t('deng-dai-zhi-hang'),
  RUNNING: i18n.global.t('zhi-hang-zhong'),
  REJECTED: i18n.global.t('yi-ju-jue'),
  EXEC_FAIL: i18n.global.t('zhi-hang-shi-bai'),
  FINISHED: i18n.global.t('gong-dan-wan-cheng'),
  CLOSED: i18n.global.t('yi-guan-bi'),
  CANCELED: i18n.global.t('yi-qu-xiao'),
  FAILED: i18n.global.t('yi-shi-bai'),
  EXEC_PAUSE: i18n.global.t('zhi-hang-zhong-duan')
};

export const TICKET_STATUS_COLOR = {
  PRE_INIT: '#FFA30E',
  WAIT_APPROVAL: '#FFA30E',
  WAIT_CONFIRM: '#FFA30E',
  WAIT_EXEC: '#FFA30E',
  RUNNING: '#FFA30E',
  REJECTED: '#FF1815',
  EXEC_FAIL: '#FF6E0D',
  FINISHED: '#52C41A',
  CANCELING: '#FFA30E',
  CANCELED: '#999999',
  CLOSED: '#999999',
  EXEC_PAUSE: '#FFA30E',
  FAILED: '#FF1815'
};

export const TICKET_PROCESS_STATUS = {
  INIT: i18n.global.t('chu-shi-hua'),
  REJECT: i18n.global.t('yi-ju-jue'),
  FINISH: i18n.global.t('yi-wan-cheng'),
  FAIL: i18n.global.t('yi-shi-bai'),
  CLOSED: i18n.global.t('yi-guan-bi')
};

export const HEALTH_LEVEL_COLOR = {
  SubHealth: 'warning',
  Health: 'success',
  Unhealthy: 'error'
};

export const WORKER_STATE = {
  OFFLINE: {
    name: i18n.global.t('li-xian'),
    value: 'OFFLINE'
  },
  WAIT_TO_ONLINE: {
    name: i18n.global.t('deng-dai-shang-xian'),
    value: 'WAIT_TO_ONLINE'
  },
  ABNORMAL: {
    name: i18n.global.t('yi-chang'),
    value: 'ABNORMAL'
  },
  ONLINE: {
    name: i18n.global.t('zai-xian'),
    value: 'ONLINE'
  },
  WAIT_TO_OFFLINE: {
    name: i18n.global.t('deng-dai-li-xian'),
    value: 'WAIT_TO_OFFLINE'
  }
};

export const DEPLOY_STATUS = {
  INSTALLED: 'INSTALLED',
  UNINSTALLED: 'UNINSTALLED'
};

export const WHITE_LIST_ADD_TYPE = {
  全部: 'ADD_ALL',
  内网IP: 'PRIVATE_IP_ONLY',
  公网出口IP: 'PUBLIC_IP_ONLY'
};

export const SECURITY_TYPE = {
  KERBEROS: 'KERBEROS',
  USER_PASSWD_WITH_TLS: 'USER_PASSWD_WITH_TLS',
  USER_PASSWD: 'USER_PASSWD',
  NONE: 'NONE',
  ONLY_PASSWD: 'ONLY_PASSWD',
  ONLY_USER: 'ONLY_USER'
};

export const CONSOLE_TASK_STATE = {
  SUCCESS: i18n.global.t('cheng-gong'),
  WAIT_START: i18n.global.t('deng-dai-kai-shi'),
  EXECUTE: i18n.global.t('zhi-hang-zhong'),
  FAILED: i18n.global.t('shi-bai'),
  CANCELED: i18n.global.t('yi-qu-xiao'),
  SKIP: i18n.global.t('yi-hu-lve')
};

export const CONSOLE_JOB_NAME = {
  RDS_ADD_PUBLIC_NET: i18n.global.t('rds-kai-fang-gong-wang'),
  RDS_AUTO_ADD_ACCOUNT: i18n.global.t('rds-zi-dong-chuang-jian-zhang-hao-mi-ma'),
  INSTALL_ECS: i18n.global.t('ecs-an-zhuang-ke-hu-duan'),
  UPGRADE_ECS: i18n.global.t('ecs-geng-xin-ke-hu-duan'),
  UNINSTALL_ECS: i18n.global.t('ecs-xie-zai-ke-hu-duan'),
  INSTALL_LOCAL_MAC: i18n.global.t('zi-jian-ji-qi-an-zhuang-ke-hu-duan'),
  UNINSTALL_LOCAL_MAC: i18n.global.t('zi-jian-ji-qi-xie-zai-ke-hu-duan'),
  RDS_ADD_IP_WHITE_LIST: i18n.global.t('rds-tian-jia-bai-ming-dan'),
  ALIYUN_ADD_WHITELIST_INFO: i18n.global.t('a-li-yun-tian-jia-bai-ming-dan-xin-xi'),
  START_ECS_CLIENT: i18n.global.t('ecs-qi-dong-ke-hu-duan')
};

export const RESOURCE_TYPE = {
  DATASOURCE: '数据源',
  WORKER: '机器'
};

export const MYSQL_DATA_TYPE = {
  INT: ['bigint', 'tinyint', 'smallint', 'mediumint', 'int'],
  BOOL: ['bool', 'boolean'],
  BIT: ['bit'],
  FLOAT: ['decimal', 'float', 'double'],
  TIME: ['date', 'datetime', 'timestamp', 'time', 'year'],
  CHAR: ['char', 'varchar'],
  BINARY: ['binary', 'varbinary', 'tinyblob', 'blob', 'mediumblob', 'longblob'],
  TEXT: ['tinytext', 'mediumtext', 'longtext', 'text'],
  ENUM: ['enum', 'set'],
  JSON: ['json']
};

export const ALGORITHM_TYPES = [
  {
    name: '无',
    value: 0
  },
  {
    name: '全遮掩',
    value: 1
  },
  {
    name: '固定位置遮掩',
    value: 2
  },
  {
    name: '固定字符遮掩',
    value: 3
  },
  {
    name: '映射替换',
    value: 4
  },
  {
    name: '随机替换',
    value: 5
  }
];

export const ALGORITHM_TYPES_PLACEHOLDER = {
  2: 'format：(1, 4), (8, 10), (-4)',
  3: '目标字符',
  4: '目标字符',
  5: 'format：(1, 4), (8, 10), (-4)'
};

export const PG_GP = ['PostgreSQL', 'Greenplum', 'SQLServer'];

export const BIZ_TYPE = {
  TICKETS_WORKFLOW: 'TICKETS_WORKFLOW',
  QUERY_CONSOLE: 'QUERY_CONSOLE'
};

export const EXPORT_STATUS = {
  INIT: 'INIT',
  WAIT_TO_START: ' WAIT_TO_START',
  RUNNING: 'RUNNING',
  WAIT_TO_CANCEL: 'WAIT_TO_CANCEL',
  CANCEL: 'CANCEL',
  WAIT_TO_STOP: 'WAIT_TO_STOP',
  STOP: 'STOP',
  WAIT_TO_FAIL: 'WAIT_TO_FAIL',
  FAIL: 'FAIL',
  COMPLETE: 'COMPLETE'
};

export const PARAMS_CONFIG = {
  ds: {
    get: 'dmDataSourceQueryDsConfig',
    edit: 'dmDataSourceUpsertDsConfig'
  },
  user: {
    get: 'rdpUserConfigGetCurrUserConfigs',
    edit: 'rdpUserConfigUpsertUserConfigs'
  }
};

export const APPROVAL_BIZ_TYPE = {
  EXECUTE: 'EXECUTE',
  AUTH: 'AUTH'
};

export const ACTION_TYPE = {
  CREATE_TABLE: 'CREATE',
  EDIT_TABLE: 'ALTER'
};

export const TAB_TYPE = {
  QUERY: 'QUERY',
  STRUCT: 'STRUCT',
  DATA: 'DATA'
};

export const AUTH_TYPE_I18N = {
  PASSWORD: i18n.global.t('zi-zhang-hao-deng-lu'),
  LDAP: 'LDAP',
  AD: i18n.global.t('windows-yu')
};

export const EMPTY_FORCE_RULE_MODAL = {
  show: false,
  title: '',
  text: '',
  event: null,
  data: null,
  refererList: [],
  refererColumns: [
    {
      title: i18n.global.t('gui-fan-ming-cheng'),
      key: 'specName'
    },
    {
      title: i18n.global.t('gui-fan-miao-shu'),
      key: 'specDesc'
    }
  ]
};

export const APPROVAL_TYPE_I18N = {
  ticket_info: i18n.global.t('sql-gong-dan-config'),
  ticket_info_of_auth: i18n.global.t('quan-xian-gong-dan-config'),
  ticket_info_of_change: i18n.global.t('bian-geng-gong-dan-config')
};
