const DEFAULT_FLOW_INFO = {
  flowName: '',
  flowDesc: '',
  flowManagerUid: ''
};

const DEFAULT_DEVOPS_INFO = {
  repoScmId: '',
  repoScmUrl: '',
  repoSelectionKey: '',
  repoId: '',
  repoPath: '',
  repoSpace: '',
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
    const namespace = item.repoSpace || '/';
    if (!tempGroups[namespace]) {
      tempGroups[namespace] = [];
    }
    tempGroups[namespace].push(item);
  });

  return tempGroups;
};

const getRepoSelectionKey = (repo = {}) => {
  const candidates = [
    ['id', repo.repoId],
    ['url', repo.repoUrl],
    ['path', repo.repoPath],
    ['name', [repo.repoSpace, repo.repoName].filter(Boolean).join('/')]
  ];
  const match = candidates.find(([, value]) => String(value || '').trim());
  return match ? `${match[0]}:${String(match[1]).trim()}` : '';
};

const getScmIconResource = (scmType) => {
  if (!scmType) {
    return '';
  }
  return `webside/${scmType}@scm-icon`;
};

const getScmDisplayName = (scmType) => {
  const displayNames = {
    Gitee: 'Gitee',
    Github: 'GitHub',
    Gitlab: 'GitLab'
  };
  return displayNames[scmType] || scmType || '';
};

export { getRepoSelectionKey, getScmDisplayName, getScmIconResource, groupByRepoNamespace, DEFAULT_FLOW_INFO, DEFAULT_DEVOPS_INFO };
