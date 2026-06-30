import i18n from '@/i18n';

function linkItem(key, href, labelKey, iconName) {
  return {
    type: 'link',
    key,
    href,
    label: i18n.global.t(labelKey),
    iconName
  };
}

function group(key, labelKey, iconName, children) {
  return {
    type: 'group',
    key,
    label: i18n.global.t(labelKey),
    iconName,
    children
  };
}

export function buildSidebarMenu({ myCatLog, myAuth, includesDM, isDesktop, accountType }) {
  const primary = [];

  if (myCatLog.includes('CAT_RDP_DS')) {
    primary.push(linkItem('/datasource', '/#/datasource', 'nav-shu-ju-ku-guan-li', 'icon-v2-peizhishujuyuan'));
  }
  if (includesDM && myCatLog.includes('CAT_DM_CICD_FLOW') && !isDesktop) {
    primary.push(linkItem('cicd', '/#/cicd', 'nav-ci-cd', 'icon-v2-DataBase2'));
  }
  if (myCatLog.includes('CAT_RDP_ENV')) {
    primary.push(linkItem('/env', '/#/env', 'huan-jing', 'icon-v2-env'));
  }
  if (includesDM && myCatLog.includes('CAT_RDP_WORKER_ORDER') && !isDesktop) {
    primary.push(linkItem('ticket', '/#/ticket', 'gong-dan', 'icon-v2-TicketAuth'));
  }

  const groups = [];

  const managementChildren = [];
  const hasLogSection = myCatLog.includes('CAT_RDP_OP_AUDIT') || myCatLog.includes('CAT_DM_SQL_AUDIT');

  if (myCatLog.includes('CAT_RDP_USER')) {
    managementChildren.push(linkItem('/manager/account', '/#/manager/account', 'nav-zhang-hu', 'icon-v2-sub_account'));
  }
  if (myCatLog.includes('CAT_RDP_ROLE')) {
    managementChildren.push(linkItem('/manager/role', '/#/manager/role', 'jiao-se', 'icon-v2-role'));
  }
  if (hasLogSection) {
    managementChildren.push(linkItem('/manager/logs', '/#/manager/logs', 'nav-ri-zhi', 'icon-v2-audit'));
  }
  if (managementChildren.length) {
    groups.push(group('management', 'nav-guan-li', 'icon-v2-role', managementChildren));
  }

  const dataAccessChildren = [];
  if (myCatLog.includes('CAT_DM_SYS') && myCatLog.includes('CAT_DM_WORKER')) {
    dataAccessChildren.push(linkItem('/data-access/cluster', '/#/data-access/cluster', 'nav-cha-xun-ji-qi-lie-biao', 'icon-v2-cluster'));
  }
  if (myAuth.includes('DM_SSH_CHANNEL_READ')) {
    dataAccessChildren.push(linkItem('/system/sshConfig', '/#/system/sshConfig', 'nav-ssh-tong-dao', 'icon-v2-MyAuth'));
  }
  if (myCatLog.includes('CAT_DM_SYS') && myCatLog.includes('CAT_DM_SECRULES')) {
    dataAccessChildren.push(linkItem('/data-access/rules', '/#/data-access/rules', 'an-quan-gui-fan', 'icon-v2-audit'));
  }
  if (dataAccessChildren.length) {
    groups.push(group('data-access', 'nav-shu-ju-fang-wen', 'icon-v2-MyAuth', dataAccessChildren));
  }

  const integrationChildren = [];
  if (myCatLog.includes('CAT_DM_IM')) {
    integrationChildren.push(linkItem('/integrations/im', '/#/integrations/im', 'nav-webhook', 'icon-v2-sub_account'));
  }
  if (myCatLog.includes('CAT_DM_GIT_OPS')) {
    integrationChildren.push(linkItem('/integrations/git', '/#/integrations/git', 'nav-git-ops', 'icon-v2-sub_account'));
  }
  if (includesDM && accountType === 'PRIMARY_ACCOUNT') {
    integrationChildren.push(linkItem('/integrations/approval', '/#/integrations/approval', 'nav-shen-pi-yin-qing', 'icon-v2-sub_account'));
  }
  if (includesDM && accountType === 'PRIMARY_ACCOUNT') {
    integrationChildren.push(linkItem('/integrations/sso', '/#/integrations/sso', 'nav-sso', 'icon-v2-sub_account'));
  }
  if (integrationChildren.length) {
    groups.push(group('integration', 'nav-ji-cheng', 'icon-v2-sub_account', integrationChildren));
  }

  const settingsChildren = [];
  settingsChildren.push(linkItem('/settings/profile', '/#/settings/profile', 'ge-ren-zi-liao', 'profile'));
  if (accountType && accountType !== 'PRIMARY_ACCOUNT') {
    settingsChildren.push(linkItem('/system/permission', '/#/system/permission', 'wo-de-quan-xian', 'icon-v2-MyAuth'));
    settingsChildren.push(linkItem('/system/permission/apply', '/#/system/permission?type=apply', 'shen-qing-quan-xian', 'icon-v2-TicketAuth'));
  }
  if (myCatLog.includes('CAT_RDP_PRI_PREFERENCE_CONF') && myAuth.includes('RDP_PRI_USER_KV_CONF_R')) {
    settingsChildren.push(linkItem('/settings/preferences', '/#/settings/preferences', 'nav-tong-yong', 'icon-v2-preference'));
  }
  if (settingsChildren.length) {
    groups.push(group('settings', 'nav-she-zhi', 'icon-v2-preference', settingsChildren));
  }

  return { primary, groups };
}

export function flattenSidebarMenu(menu) {
  const flat = [];

  function walk(nodes) {
    nodes.forEach((node) => {
      if (node.type === 'link') {
        flat.push(node);
        return;
      }
      if (node.children) {
        walk(node.children);
      }
    });
  }

  walk(menu.primary || []);
  (menu.groups || []).forEach((groupItem) => {
    walk(groupItem.children || []);
  });

  return flat;
}

export function collectSidebarKeys(menu) {
  const keys = [];

  function walk(nodes) {
    nodes.forEach((node) => {
      if (node.type === 'link') {
        keys.push(node.key);
        return;
      }
      if (node.type === 'subgroup' || node.type === 'group') {
        keys.push(node.key);
      }
      if (node.children) {
        walk(node.children);
      }
    });
  }

  walk(menu.primary || []);
  (menu.groups || []).forEach((groupItem) => {
    walk(groupItem.children || []);
  });

  return keys;
}

export function findSidebarParentKeys(menu, activeKey) {
  const parents = [];

  function walk(nodes, ancestors) {
    nodes.forEach((node) => {
      const nextAncestors = [...ancestors, node.key];
      if (node.type === 'link' && node.key === activeKey) {
        parents.push(...ancestors);
        return;
      }
      if (node.children) {
        walk(node.children, nextAncestors);
      }
    });
  }

  (menu.groups || []).forEach((groupItem) => {
    walk(groupItem.children || [], [groupItem.key]);
  });

  return parents;
}
