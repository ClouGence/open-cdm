import i18n from '@/i18n';

const projectTableColumns = [
  {
    title: '标记',
    slot: 'mark',
    width: 70,
    align: 'center'
  },
  {
    title: i18n.global.t('xiang-mu-ming-cheng'),
    key: 'name'
  },
  {
    title: i18n.global.t('fu-ze-ren'),
    key: 'projectOwnerName',
    width: 120
  },
  {
    title: i18n.global.t('chuang-jian-shi-jian'),
    key: 'createTime',
    width: 120
  },
  {
    title: i18n.global.t('cao-zuo'),
    slot: 'action',
    fixed: 'right',
    width: 100
  }
];

const projectDetailTableColumns = [
  {
    title: i18n.global.t('zhuang-tai'),
    slot: 'status',
    width: 70
  },
  {
    title: i18n.global.t('bian-geng-ming-cheng'),
    key: 'changeName'
  },
  {
    title: i18n.global.t('bian-geng-shi-jian'),
    key: 'changeTime',
    width: 180
  },
  {
    title: i18n.global.t('git-ops'),
    slot: 'flow',
    width: 260
  },
  {
    title: i18n.global.t('jie-duan'),
    slot: 'step',
    width: 150
  },
  {
    title: i18n.global.t('cao-zuo'),
    slot: 'action',
    fixed: 'right',
    width: 100
  }
];

const projectBecomeTableColumns = [
  {
    title: i18n.global.t('deng-ji'),
    slot: 'level',
    width: 120
  },
  {
    title: i18n.global.t('ming-cheng'),
    key: 'ruleName',
    minWidth: 200
  },
  {
    title: i18n.global.t('miao-shu'),
    key: 'ruleDesc',
    minWidth: 600
  }
];

const projectFormBasicRule = {
  projectName: [{ required: true, message: '项目名称不能为空' }],
  projectDesc: [{ required: false, message: '项目描述不能为空' }],
  projectOwnerUid: [{ required: true, message: '管理员不能为空' }]
};

const projectPipelineRule = {
  repoScmId: [{ required: true, message: 'Git服务商不能为空' }],
  repoName: [{ required: true, message: '仓库不能为空' }],
  repoBranch: [{ required: true, message: '分支不能为空' }],
  eventType: [{ required: true, message: '事件不能为空' }],
  repoScriptPath: [{ required: false, message: '脚本路径不能为空' }]
};

const projectOptionRule = {
  instanceId: [{ required: true, message: '项目名称不能为空' }],
  databaseName: [{ required: true, message: 'catalog不能为空' }],
  schemaName: [{ required: true, message: 'schema不能为空' }],
  initScript: [{ required: true, message: '初始化方式不能为空' }],
  checkStrategy: [{ required: true, message: 'sql审核不能为空' }],
  executeStrategy: [{ required: true, message: '发布方式不能为空' }],
  errorStrategy: [{ required: true, message: '错误策略不能为空' }]
};

const formDevopsRules = {
  repoScmId: [{ required: true, message: '服务商不能为空' }],
  repoName: [{ required: true, message: '仓库不能为空' }],
  repoBranch: [{ required: true, message: '目标分支不能为空' }],
  repoScriptPath: [{ required: true, message: '脚本路径不能为空' }],
  instanceId: [{ required: true, message: '实例不能为空' }],
  databaseName: [{ required: true, message: '数据库（catalog）不能为空' }],
  schemaName: [{ required: true, message: 'schema不能为空' }],
  initScript: [{ required: true, message: '初始化方式不能为空' }],
  checkStrategy: [{ required: true, message: 'SQL审核策略不能为空' }],
  executeStrategy: [{ required: true, message: '发布方式不能为空' }],
  errorStrategy: [{ required: true, message: '错误策略不能为空' }]
};

const formRule = {
  repoScmId: [{ required: true, message: '请选择服务商' }],
  repoName: [{ required: true, message: '请选择仓库' }],
  repoBranch: [{ required: true, message: '请输入分支' }],
  repoScriptPath: [{ required: false, message: '请输入脚本路径' }],
  eventType: [{ required: true, message: '请选择事件类型' }],
  instanceId: [{ required: true, message: '请选择实例' }],
  catalogName: [{ required: false, message: '请选择数据库' }],
  schemaName: [{ required: true, message: '请选择Schema' }],
  initScript: [{ required: true, message: '请选择初始化方式' }]
};

const ERROR_LEVEL_MAP = {
  PASS: '通过',
  SUGGEST: '提示',
  TICKET: '工单',
  FAILURE: '失败'
};
const ERROR_LEVEL_COLOR_MAP = {
  PASS: 'success',
  SUGGEST: 'blue',
  TICKET: 'orange',
  FAILURE: 'error'
};

const PROJECT_STEP_NUM = {
  INIT_SNAPSHOT: 0,
  INIT: 1,
  CHECK: 2,
  APPROVAL: 3,
  EXECUTE: 4,
  FINISH: 5
};

const PROJECT_STEP = {
  BASIC1: -2,
  BASIC2: -1,
  S0: 0,
  S1: 1,
  S2: 2,
  FINISH: 3
};

const IM_PROVIDER_MAP = {
  Feishu: 'icon-v2-FeiShu',
  Youjian: 'icon-v2-YouJian',
  Dingding: 'icon-v2-DingDing',
  Qiyeweixin: 'icon-v2-QiYeWeiXin'
};

const PROJECT_MARK_MAP = {
  CircleGray: 'icon-v2-CircleGray',
  CircleBlue: 'icon-v2-CircleBlue',
  CircleGreen: 'icon-v2-CircleGreen',
  CircleWarn: 'icon-v2-CircleWarn',
  CircleRed: 'icon-v2-CircleRed'
};

const BECOME_STATUS_MAP = {
  warn: 'icon-v2-Warning2',
  success: 'icon-v2-Success2',
  progress: 'icon-v2-Progress',
  disable: 'icon-v2-Disable'
};

const APPROVE_MAP = {
  Disable: i18n.global.t('jin-yong'),
  Enable: i18n.global.t('qi-yong')
};

const SQL_REVIEW_MAP = {
  skip: i18n.global.t('jin-yong'),
  always: i18n.global.t('shi-zhong'),
  suggest: i18n.global.t('ti-shi'),
  failure: i18n.global.t('zu-sai')
};

const INIT_TYPE_MAP = {
  none: i18n.global.t('bu-chu-li'),
  snapshot: i18n.global.t('chuang-jian-kuai-zhao'),
  change: i18n.global.t('chuang-jian-bian-geng')
};

const PROJECT_FLOW_DESCRIPTION = {
  check: {
    Always: i18n.global.t('project-flow-check-always'),
    Suggest: i18n.global.t('project-flow-check-suggest'),
    Failure: i18n.global.t('project-flow-check-failure'),
    Skip: i18n.global.t('project-flow-check-skip')
  },
  approve: {
    Enable: i18n.global.t('project-flow-approve-enable'),
    Disable: i18n.global.t('project-flow-approve-disable')
  },
  execute: {
    Auto: i18n.global.t('project-flow-execute-auto'),
    Manual: i18n.global.t('project-flow-execute-manual'),
    Disabled: i18n.global.t('project-flow-execute-disabled')
  },
  transactional: {
    true: i18n.global.t('project-flow-transactional-enable'),
    false: i18n.global.t('project-flow-transactional-disable')
  },
  error: {
    NONE: i18n.global.t('project-flow-error-none'),
    RETRY: i18n.global.t('project-flow-error-retry'),
    SKIP: i18n.global.t('project-flow-error-skip')
  }
};

const GITOPS_DESCRIPTION = {
  Snapshot: i18n.global.t('gitops-init-snapshot'),
  CreateChange: i18n.global.t('gitops-init-change'),
  None: i18n.global.t('gitops-init-none')
};

const PUBLISH_MAP = {
  auto: i18n.global.t('zi-dong'),
  manual: i18n.global.t('shou-dong'),
  disabled: i18n.global.t('jin-yong')
};

const ERROR_STRATEGY_MAP = {
  abort: i18n.global.t('zhong-duan'),
  ignore: i18n.global.t('hu-lve'),
  retry: i18n.global.t('zhong-shi')
};

const defaultLanguageMap = {
  zh: i18n.global.t('zhong-wen'),
  en: i18n.global.t('ying-wen')
};

const EVEN_TYPE_MAP = {
  push: 'Push',
  pr: 'Pull Request'
};

const AUTO_EXEC_JOB_STATUS_COLOR = {
  INIT: 'default',
  WAIT_EXEC: 'default',
  EXECUTING: 'default',
  FAILED: 'error',
  PAUSE: 'default',
  PAUSING: 'default',
  FINISH: 'success',
  TERMINATION: 'error'
};

const AUTO_EXEC_JOB_STATUS_I18N = {
  INIT: '待执行',
  WAIT_EXEC: '待执行',
  EXECUTING: '执行中',
  FAILED: '失败',
  PAUSE: '暂停',
  PAUSING: '暂停中',
  FINISH: '已完成',
  TERMINATION: '终止'
};

const CHANGE_STATUS_MAP = {
  INIT: 0,
  CHECK: 1,
  APPROVAL: 2,
  EXECUTE: 3,
  FINISH: 4,
  INIT_SNAPSHOT: 5
};

const AUTO_EXEC_TASK_STATUS_COLOR = {
  WAIT_EXEC: 'default',
  EXECUTING: 'default',
  WAIT_CONFIRM: 'default',
  FAILED: 'error',
  FINISH: 'success',
  ROLLBACK: 'default',
  CANCELED: 'default'
};

const AUTO_EXEC_TASK_STATUS_I18N = {
  WAIT_EXEC: '待执行',
  EXECUTING: '执行中',
  WAIT_CONFIRM: '等待确认',
  FAILED: '失败',
  FINISH: '完成',
  ROLLBACK: '回滚',
  CANCELED: '取消'
};

const STATUS_MAP = {
  OPEN: 'process',
  READY: 'process',
  WAIT: 'wait',
  FAILED: 'error',
  FINISH: 'finish',
  CLOSED: 'finish'
};

export {
  projectTableColumns,
  formDevopsRules,
  formRule,
  projectDetailTableColumns,
  projectBecomeTableColumns,
  ERROR_LEVEL_MAP,
  ERROR_LEVEL_COLOR_MAP,
  IM_PROVIDER_MAP,
  PROJECT_MARK_MAP,
  BECOME_STATUS_MAP,
  SQL_REVIEW_MAP,
  APPROVE_MAP,
  INIT_TYPE_MAP,
  PUBLISH_MAP,
  PROJECT_FLOW_DESCRIPTION,
  GITOPS_DESCRIPTION,
  ERROR_STRATEGY_MAP,
  defaultLanguageMap,
  EVEN_TYPE_MAP,
  projectFormBasicRule,
  projectPipelineRule,
  projectOptionRule,
  CHANGE_STATUS_MAP,
  AUTO_EXEC_JOB_STATUS_COLOR,
  AUTO_EXEC_JOB_STATUS_I18N,
  AUTO_EXEC_TASK_STATUS_COLOR,
  AUTO_EXEC_TASK_STATUS_I18N,
  STATUS_MAP,
  PROJECT_STEP_NUM,
  PROJECT_STEP
};
