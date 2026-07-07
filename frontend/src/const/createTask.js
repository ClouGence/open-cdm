import { DATASOURCE_DEPLOY_TYPE, HOST_TYPE } from '@/const';

export const INIT_ORIGINAL_CONFIG = {
  clusterId: '',
  history: false,
  // source
  sourceDataSourceDeployType: DATASOURCE_DEPLOY_TYPE.ALIBABA_CLOUD_HOSTED, // Type of data source deployment
  sourceDataSourceType: '', // Type of data source
  sourceHostType: HOST_TYPE.PRIVATE, // Network type
  sourceInstanceId: '', // Example id
  sourceInstance: {}, // Example
  sourceConnection: {}, // Test connection successful
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
