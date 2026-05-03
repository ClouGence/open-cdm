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

export const DATASOURCE_LIFECYCLE_STATE = {
  CREATED: 'CREATED'
};

export const CONNECT_TYPE = {
  ORACLE: [
    {
      label: 'ORACLE_SID',
      value: 'ORACLE_SID'
    },
    {
      label: 'ORACLE_SERVICE',
      value: 'ORACLE_SERVICE'
    }
  ]
};

export const ORACLE_CONTENT_TYPE = {
  ORACLE_SID: 'ORACLE_SID',
  ORACLE_SERVICE: 'ORACLE_SERVICE'
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

export const WORKER_STATE = {
  WAIT_TO_ONLINE: 'WAIT_TO_ONLINE',
  ONLINE: 'ONLINE',
  ABNORMAL: 'ABNORMAL',
  WAIT_TO_OFFLINE: 'WAIT_TO_OFFLINE',
  OFFLINE: 'OFFLINE'
};

export const LANG_I18N = {
  'zh-CN': '中文',
  'en-US': 'ENGLISH'
};

export const VERIFY_TYPE = {
  EMAIL_VERIFY_CODE: 'EMAIL_VERIFY_CODE',
  SMS_VERIFY_CODE: 'SMS_VERIFY_CODE'
};

export const UPDATE_HOST_TYPE = {
  PUBLICIP: 'publicIp',
  PRIVATEIP: 'privateIp',
  PUBLICHTTPHOST: 'publicHttpHost',
  PRIVATEHTTPHOST: 'privateHttpHost'
};
