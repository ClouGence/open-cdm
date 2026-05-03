export default {
  taskInfoDbMapHistory: {
    functionConfig: null,
    tableFilterConfig: null,
    cleanDataConfig: null
  },
  taskInfoHistory: {
    functionConfig: null,
    tableFilterConfig: null,
    cleanDataConfig: null
  },
  ruleSetting: {
    queryConf: {
      supportDs: [],
      targets: []
    },
    senConf: {
      matchMode: [],
      senMode: []
    }
  },
  socket: {
    connected: false,
    msg: ''
  },
  editorSet: {},
  userInfo: {
    username: '',
    menuItems: [],
    auth: [],
    authArr: []
  },
  globalSetting: {
    features: {}
  },
  ccGlobalSetting: {},
  dmGlobalSetting: {},
  globalDsSetting: {},
  dsTypeList: {},
  clusterListMap: {},
  allClusterWorkers: 0,
  allClusterRunningWorkers: 0,
  deployEnvListMap: {},
  productClusterList: [],
  selectCcProductCluster: null,
  aliyunRegionListMap: {},
  selfRegionListMap: {},
  regionList: [],
  metaCenterSearchParams: {},
  selectedWorker: {},
  logo: {
    loginLogo: '',
    headerLogo: ''
  },
  blackUri: [],
  setPkAndIdFlags: {},
  globalConfig: {},
  sourceConnection: false,
  sinkConnection: false,
  dataSourceDeployTypes: [],
  sourceDataSourceTypes: [],
  sinkDataSourceTypes: [],
  sourceInstance: [],
  sinkInstance: [],
  sourceDbList: [],
  sinkDbList: [],
  showTest1: false,
  showTest2: false,
  tableDatas: [],
  selectedTables: {},
  compareSelectedTables: [],
  compareSelectedDbs: [],
  firstToTableFilter: false,
  firstToCleanData: false,
  sinkTableList: {},
  selectedColumns: {},
  tableInfo: [],
  cleanDataData: [],
  sinkColumns: {},
  cleanDataSinkTables: [],
  urlLabels: [],
  userRole: '',
  specsMap: {},
  rdsData: [],
  addedRdsList: [],
  firstAddDataSource: true,
  selectedCluster: {},
  clusterList: [],
  jobData: null,
  licenseStatus: {
    success: false
  },
  docUrlPrefix: 'https://www.clougence.com/cc-doc',
  contactUsUrl: 'https://www.clougence.com/about',
  dmDocUrlPrefix: 'https://www.clougence.com/dm-doc',
  bladePipeApply: 'https://www.clougence.com/dm-doc/clouddm',
  myCatLog: [],
  mySystemMenuItems: [],
  myAuth: [],
  defaultRedirectUrl: '',
  publicKey: '',
  remainTrialDay: null,
  lastChildTableSubOptions: {},
  theme: 'light' // 当前主题：'light' | 'dark'
};
