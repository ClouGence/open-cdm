import { DATASOURCE_DEPLOY_TYPE, HOST_TYPE } from '@/const';

export const INIT_ORIGINAL_CONFIG = {
  clusterId: '',
  history: false,
  // source
  sourceDataSourceDeployType: DATASOURCE_DEPLOY_TYPE.ALIBABA_CLOUD_HOSTED, // 数据源部署类型
  sourceDataSourceType: '', // 数据源类型
  sourceHostType: HOST_TYPE.PRIVATE, // 网络类型
  sourceInstanceId: '', // 实例id
  sourceInstance: {}, // 实例
  sourceConnection: {}, // 测试连接成功
  sourceHasPassword: false,
  sourceAutoCreateAccount: false,
  // target
  targetDataSourceDeployType: DATASOURCE_DEPLOY_TYPE.ALIBABA_CLOUD_HOSTED,
  targetDataSourceType: '',
  targetHostType: HOST_TYPE.PRIVATE,
  targetInstanceId: '',
  targetInstance: {},
  targetAutoCreateAccount: false,
  targetHasPassword: false,
  // data
  dataSourceDeployTypeList: [],
  sourceDataSourceTypeList: [],
  targetDataSourceTypeList: [],
  sourceInstanceList: [],
  targetInstanceList: [],
  sourceInstanceListObj: {},
  targetInstanceListObj: {}
};
export const INIT_FUNCTIONAL_CONFIG = {
  functionalConfigHistory: false,
  jobTypeList: {},
  resourceData: [],
  loopRunTypes: {},
  type: 'SYNC',
  ddl: 'false',
  mode: {
    synchronize: false,
    init: true,
    shortTermNum: 7
  },
  specsMap: {},
  checkMode: 'noCheck',
  fullPeriod: false,
  specKind: 'Balance',
  spec: {},
  desc: '',
  checkPeriodDate: {
    dayType: '',
    day: '',
    time: ''
  },
  fullPeriodDate: {
    dayType: '',
    day: '',
    time: '',
    hour: ''
  },
  autoStart: true
};
