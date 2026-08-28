import i18n from '../i18n';

export const SECOND_CONFIRM_EVENT_LIST = {
  DELETE_POSITION: 'DELETE_POSITION',
  DELETE_JOB: 'DELETE_JOB',
  DELETE_DATASOURCE: 'DELETE_DATASOURCE',
  DELETE_WORKER: 'DELETE_WORKER',
  RESET_POSITION: 'RESET_POSITION'
};

export const LANG_OPTIONS = ['zh-CN', 'en-US'];

export const LANG_I18N = {
  'zh-CN': '中文',
  'en-US': 'English'
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

export const TICKET_STATUS = {
  PRE_INIT_WAIT: i18n.global.t('deng-dai-fen-xi'),
  PRE_INIT_RUN: i18n.global.t('ticket-analysis-running'),
  WAIT_APPROVAL: i18n.global.t('deng-dai-shen-pi'),
  WAIT_CONFIRM: i18n.global.t('deng-dai-que-ren'),
  WAIT_EXEC: i18n.global.t('ticket-execution-preparing'),
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
  PRE_INIT_WAIT: '#FFA30E',
  PRE_INIT_RUN: '#FFA30E',
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

export const BIZ_TYPE = {
  TICKETS_WORKFLOW: 'TICKETS_WORKFLOW',
  QUERY_CONSOLE: 'QUERY_CONSOLE'
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

export const ACTION_TYPE = {
  CREATE_TABLE: 'CREATE',
  EDIT_TABLE: 'ALTER'
};

export const TAB_TYPE = {
  QUERY: 'QUERY',
  STRUCT: 'STRUCT',
  DATA: 'DATA'
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
