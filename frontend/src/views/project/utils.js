const DEFAULT_FLOW_OPTION = {
  checkStrategy: 'Always',
  approveStrategy: 'Enable',
  executeStrategy: 'Auto',
  errorStrategy: 'NONE',
  transactional: 'false'
};

const DEFAULT_PROJECT_INFO = {
  projectName: '',
  projectDesc: '',
  projectOwnerUid: '',
  ...DEFAULT_FLOW_OPTION
};

const DEFAULT_DEVOPS_INFO = {
  repoScmId: '',
  repoScmUrl: '',
  repoName: '',
  repoBranch: '',
  repoScriptPath: '',
  dsLevels: [],
  eventType: 'Push',
  instanceId: '',
  catalogName: '',
  schemaName: '',
  initScript: 'Snapshot',
  devopsInsHasCatalog: false,
  devopsInsHasSchema: false
};

const groupByRepoNamespace = (data) => {
  const tempGroups = {};

  data.forEach((item) => {
    const match = item.repoUrl.match(/\.com\/([^/]+)/);
    if (match && match[1]) {
      const namespace = match[1];
      if (!tempGroups[namespace]) {
        tempGroups[namespace] = [];
      }
      tempGroups[namespace].push(item);
    }
  });

  return tempGroups;
};

export { groupByRepoNamespace, DEFAULT_PROJECT_INFO, DEFAULT_FLOW_OPTION, DEFAULT_DEVOPS_INFO };
