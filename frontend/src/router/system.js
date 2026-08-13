export default [
  {
    path: '/integrations/git',
    name: 'DMDevops',
    component: () => import(/* webpackChunkName: "ccsystem-datasource" */ '@/views/devops'),
    meta: { breadcrumbs: [{ labelKey: 'nav-git-ops' }] }
  },
  {
    path: '/integrations/git/create',
    name: 'DMDevopsCreate',
    component: () => import(/* webpackChunkName: "ccsystem-datasource" */ '@/views/devops/form'),
    meta: {
      breadcrumbs: [{ labelKey: 'nav-git-ops', to: '/integrations/git' }, { labelKey: 'xin-zeng' }]
    }
  },
  {
    path: '/integrations/git/:scmId/edit',
    name: 'DMDevopsEdit',
    component: () => import(/* webpackChunkName: "ccsystem-datasource" */ '@/views/devops/form'),
    meta: {
      breadcrumbs: [{ labelKey: 'nav-git-ops', to: '/integrations/git' }, { labelKey: 'bian-ji' }]
    }
  },
  {
    path: 'devops',
    redirect: '/integrations/git'
  },
  {
    path: '/integrations/sso',
    name: 'DMSso',
    component: () => import(/* webpackChunkName: "ccsystem-sso" */ '@/views/system/sso/index'),
    meta: { breadcrumbs: [{ labelKey: 'nav-sso' }] }
  },
  {
    path: '/integrations/sso/create',
    name: 'DMSsoCreate',
    component: () => import(/* webpackChunkName: "ccsystem-sso" */ '@/views/system/sso/form'),
    meta: {
      breadcrumbs: [{ labelKey: 'nav-sso', to: '/integrations/sso' }, { labelKey: 'xin-zeng' }]
    }
  },
  {
    path: '/integrations/sso/:type/edit',
    name: 'DMSsoEdit',
    component: () => import(/* webpackChunkName: "ccsystem-sso" */ '@/views/system/sso/form'),
    meta: {
      breadcrumbs: [{ labelKey: 'nav-sso', to: '/integrations/sso' }, { labelKey: 'pei-zhi' }]
    }
  },
  {
    path: 'sso',
    redirect: '/integrations/sso'
  },
  {
    path: '/integrations/approval',
    name: 'DMApproval',
    component: () => import(/* webpackChunkName: "ccsystem-approval" */ '@/views/system/approval/index'),
    meta: { breadcrumbs: [{ labelKey: 'nav-shen-pi-yin-qing' }] }
  },
  {
    path: '/integrations/approval/create',
    name: 'DMApprovalCreate',
    component: () => import(/* webpackChunkName: "ccsystem-approval" */ '@/views/system/approval/form'),
    meta: {
      breadcrumbs: [{ labelKey: 'nav-shen-pi-yin-qing', to: '/integrations/approval' }, { labelKey: 'xin-zeng' }]
    }
  },
  {
    path: '/integrations/approval/:type/edit',
    name: 'DMApprovalEdit',
    component: () => import(/* webpackChunkName: "ccsystem-approval" */ '@/views/system/approval/form'),
    meta: {
      breadcrumbs: [{ labelKey: 'nav-shen-pi-yin-qing', to: '/integrations/approval' }, { labelKey: 'pei-zhi' }]
    }
  },
  {
    path: '/integrations/im',
    name: 'DMIm',
    component: () => import(/* webpackChunkName: "ccsystem-datasource" */ '@/views/im'),
    meta: { breadcrumbs: [{ labelKey: 'nav-webhook' }] }
  },
  {
    path: '/integrations/im/create',
    name: 'DMImCreate',
    component: () => import(/* webpackChunkName: "ccsystem-datasource" */ '@/views/im/form'),
    meta: {
      breadcrumbs: [{ labelKey: 'nav-webhook', to: '/integrations/im' }, { labelKey: 'xin-zeng' }]
    }
  },
  {
    path: '/integrations/im/:imId/edit',
    name: 'DMImEdit',
    component: () => import(/* webpackChunkName: "ccsystem-datasource" */ '@/views/im/form'),
    meta: {
      breadcrumbs: [{ labelKey: 'nav-webhook', to: '/integrations/im' }, { labelKey: 'bian-ji' }]
    }
  },
  {
    path: 'im',
    redirect: '/integrations/im'
  },
  {
    path: 'info_center',
    name: 'InfoCenter',
    component: () => import(/* webpackChunkName: "ticket" */ '@/views/consoleJob/index'),
    meta: { breadcrumbs: [{ labelKey: 'xiao-xi-zhong-xin' }] }
  },
  {
    path: 'console_job/:id',
    name: 'ConsoleJob/id',
    component: () => import(/* webpackChunkName: "ticket" */ '@/views/consoleJob/consoleJobDetail'),
    meta: {
      breadcrumbs: [
        { labelKey: 'xiao-xi-zhong-xin', to: '/system/info_center' },
        { labelKey: 'ren-wu-xiang-qing', param: 'id' }
      ]
    }
  },
  {
    path: '',
    name: 'System_Home',
    component: () => import(/* webpackChunkName: "ccsystem-home" */ '@/views/system/home')
  },
  {
    path: 'user/config',
    name: 'User_Config',
    component: () => import(/* webpackChunkName: "ccsystem-home" */ '@/views/system/user/userConfig'),
    meta: {
      breadcrumbs: [{ labelKey: 'cha-xun-pei-zhi', to: '/datasource' }, { labelKey: 'xiu-gai-can-shu-pei-zhi' }]
    }
  },
  {
    path: 'role',
    redirect: '/manager/role'
  },
  {
    path: 'authdm',
    name: 'System_Auth',
    component: () => import(/* webpackChunkName: "ccsystem-auth" */ '@/views/system/subaccount/auth/authDm'),
    meta: {
      breadcrumbs: [{ labelKey: 'nav-zhang-hu', to: '/manager/account' }, { labelKey: 'shou-quan' }]
    }
  },
  {
    path: '/manager/account/batch_authorization',
    name: 'Management_Accounts_Batch_Authorization',
    component: () => import(/* webpackChunkName: "ccsystem-subaccount-batch-auth" */ '@/views/system/subaccount/BatchAuthorizationPage'),
    meta: {
      requiredAuth: 'RDP_AUTH_MANAGE',
      breadcrumbs: [{ labelKey: 'nav-zhang-hu', to: '/manager/account' }, { labelKey: 'pi-liang-shou-quan' }]
    }
  },
  {
    path: '/manager/account/batch_authorization/permissions',
    name: 'Management_Accounts_Batch_Authorization_Permissions',
    component: () => import(/* webpackChunkName: "ccsystem-subaccount-auth" */ '@/views/system/subaccount/auth/authDm'),
    meta: {
      requiredAuth: 'RDP_AUTH_MANAGE',
      breadcrumbs: [
        { labelKey: 'nav-zhang-hu', to: '/manager/account' },
        {
          labelKey: 'pi-liang-shou-quan',
          to: (route) => ({
            path: '/manager/account/batch_authorization',
            query: { operation: route.query.operation, uids: route.query.uids }
          })
        },
        { labelKey: 'xuan-ze-quan-xian' }
      ]
    }
  },
  {
    path: 'account/authdm/batch',
    name: 'System_Sub_Account_Batch_AuthDm',
    component: () => import(/* webpackChunkName: "ccsystem-subaccount-auth" */ '@/views/system/subaccount/auth/authDm'),
    meta: {
      requiredAuth: 'RDP_AUTH_MANAGE',
      breadcrumbs: [
        { labelKey: 'nav-zhang-hu', to: '/manager/account' },
        {
          labelKey: 'pi-liang-shou-quan',
          to: (route) => ({
            path: '/manager/account/batch_authorization',
            query: { operation: route.query.operation, uids: route.query.uids }
          })
        },
        { labelKey: 'xuan-ze-quan-xian' }
      ]
    }
  },
  {
    path: 'account/authdm/:uid',
    name: 'System_Sub_Account_AuthDm',
    component: () => import(/* webpackChunkName: "ccsystem-subaccount-auth" */ '@/views/system/subaccount/auth/authDm'),
    meta: {
      breadcrumbs: [{ labelKey: 'nav-zhang-hu', to: '/manager/account' }, { labelKey: 'shou-quan' }]
    }
  },
  {
    path: 'account',
    redirect: '/manager/account'
  },
  {
    path: 'management/accounts',
    redirect: '/manager/account'
  },
  {
    path: 'management/accounts/account',
    redirect: '/manager/account'
  },
  {
    path: 'management/accounts/role',
    redirect: '/manager/role'
  },
  {
    path: '/manager/account/role',
    redirect: '/manager/role'
  },
  {
    path: '/manager/account',
    name: 'Management_Accounts_Account',
    component: () => import(/* webpackChunkName: "ccsystem-subaccount" */ '@/views/system/subaccount/index'),
    meta: { breadcrumbs: [{ labelKey: 'nav-zhang-hu' }] }
  },
  {
    path: '/manager/role/create',
    name: 'Management_Role_Create',
    component: () => import(/* webpackChunkName: "ccsystem-role" */ '@/views/system/role/RoleEditorPage'),
    meta: {
      requiredAuth: 'RDP_ROLE_MANAGE',
      breadcrumbs: [{ labelKey: 'jiao-se', to: '/manager/role' }, { labelKey: 'chuang-jian-jiao-se' }]
    }
  },
  {
    path: '/manager/role/:roleId/edit',
    name: 'Management_Role_Edit',
    component: () => import(/* webpackChunkName: "ccsystem-role" */ '@/views/system/role/RoleEditorPage'),
    meta: {
      requiredAuth: 'RDP_ROLE_MANAGE',
      breadcrumbs: [{ labelKey: 'jiao-se', to: '/manager/role' }, { labelKey: 'bian-ji-jue-se' }]
    }
  },
  {
    path: '/manager/role/:roleId/view',
    name: 'Management_Role_View',
    component: () => import(/* webpackChunkName: "ccsystem-role" */ '@/views/system/role/RoleEditorPage'),
    meta: {
      requiredAuth: 'RDP_ROLE_READ',
      breadcrumbs: [{ labelKey: 'jiao-se', to: '/manager/role' }, { labelKey: 'cha-kan-jue-se' }]
    }
  },
  {
    path: '/manager/role',
    name: 'Management_Role',
    component: () => import(/* webpackChunkName: "ccsystem-role" */ '@/views/system/role/index'),
    meta: { requiredAuth: 'RDP_ROLE_READ', breadcrumbs: [{ labelKey: 'jiao-se' }] }
  },
  {
    path: 'management/logs',
    redirect: '/manager/logs'
  },
  {
    path: 'management/logs/operation',
    redirect: '/manager/logs'
  },
  {
    path: 'management/logs/sql',
    redirect: '/manager/logs/sql'
  },
  {
    path: '/manager/logs',
    component: () => import(/* webpackChunkName: "ccsystem-management-logs" */ '@/views/system/management/ManagementLogsLayout'),
    children: [
      {
        path: '',
        name: 'Management_Logs_Operation',
        component: () => import(/* webpackChunkName: "ccsystem-env" */ '@/views/system/OperationLog'),
        meta: { managementTab: 'operation' }
      },
      {
        path: 'sql',
        name: 'Management_Logs_Sql',
        component: () => import(/* webpackChunkName: "ccsystem-env" */ '@/views/system/SqlLog'),
        meta: { managementTab: 'sql' }
      }
    ]
  },
  {
    path: '/env',
    name: 'System_Env',
    component: () => import(/* webpackChunkName: "ccsystem-env" */ '@/views/system/env'),
    meta: { breadcrumbs: [{ labelKey: 'huan-jing' }] }
  },
  {
    path: 'env',
    redirect: '/env'
  },
  {
    path: '/datasource',
    name: 'System_DataSource',
    component: () => import(/* webpackChunkName: "system-datasource" */ '@/views/dataSource/DataSource'),
    meta: { requiredAuth: 'RDP_DS_READ', breadcrumbs: [{ labelKey: 'nav-shu-ju-ku-guan-li' }] }
  },
  {
    path: 'ccdatasource',
    redirect: '/datasource'
  },
  {
    path: '/datasource/add',
    name: 'System_DataSource_Add',
    component: () => import(/* webpackChunkName: "system-datasource" */ '@/views/dataSource/AddDataSource'),
    meta: { requiredAuth: 'RDP_DS_READ', breadcrumbType: 'datasource-form' }
  },
  {
    path: 'ccdatasource/add',
    redirect: '/datasource/add'
  },
  {
    path: '/data-access/cluster',
    name: 'System_Machine',
    component: () => import(/* webpackChunkName: "ccsystem-cluster" */ '@/views/worker/Cluster'),
    meta: { breadcrumbs: [{ labelKey: 'nav-cha-xun-ji-qi-lie-biao' }] }
  },
  {
    path: 'dmmachine',
    redirect: '/data-access/cluster'
  },
  {
    path: '/data-access/cluster/list/:clusterId',
    name: 'System_Machine_List',
    component: () => import(/* webpackChunkName: "ccsystem-cluster-list" */ '@/views/system/cluster/workerList'),
    meta: {
      breadcrumbs: [{ labelKey: 'nav-cha-xun-ji-qi-lie-biao', to: '/data-access/cluster' }, { labelKey: 'ji-qi-lie-biao' }]
    }
  },
  {
    path: 'dmmachine/list/:clusterId',
    redirect: (to) => `/data-access/cluster/list/${to.params.clusterId}`
  },
  {
    path: '/data-access/rules',
    name: 'DMRuleList',
    component: () => import(/* webpackChunkName: "ccsystem-datasource" */ '@/views/security/index'),
    meta: { breadcrumbs: [{ labelKey: 'an-quan-gui-fan' }] }
  },
  {
    path: 'dmrulelist',
    redirect: '/data-access/rules'
  },
  {
    path: '/data-access/rules/create',
    name: 'DMRuleCreate',
    component: () => import('@/views/security/rule/ruleDetail'),
    meta: {
      breadcrumbs: [
        { labelKey: 'an-quan-gui-fan', to: { path: '/data-access/rules', query: { tab: 'template' } } },
        { labelKey: 'xin-jian-gui-ze-mo-ban' }
      ]
    }
  },
  {
    path: 'dmrule/create',
    redirect: '/data-access/rules/create'
  },
  {
    path: '/data-access/rules/detail/:id',
    name: 'DMRuleDetail',
    component: () => import('@/views/security/rule/ruleDetail'),
    meta: {
      breadcrumbs: [
        { labelKey: 'an-quan-gui-fan', to: { path: '/data-access/rules', query: { tab: 'template' } } },
        { labelKey: 'gui-ze-mo-ban-xiang-qing', param: 'id' }
      ]
    }
  },
  {
    path: 'dmrule/detail/:id',
    redirect: (to) => `/data-access/rules/detail/${to.params.id}`
  },
  {
    path: 'dmspeclist',
    redirect: (to) => ({
      path: '/data-access/rules',
      query: {
        ...to.query,
        tab: 'security'
      },
      hash: to.hash
    })
  },
  {
    path: 'dmspec/:specId',
    name: 'DMSpecDetail',
    component: () => import(/* webpackChunkName: "ccsystem-datasource" */ '@/views/security/spec/specDetail'),
    meta: {
      breadcrumbs: [
        { labelKey: 'an-quan-gui-fan', to: { path: '/data-access/rules', query: { tab: 'security' } } },
        { labelKey: 'gui-ze-xiang-qing', param: 'specId' }
      ]
    }
  },
  {
    path: 'dmspec/:specId/rule/:ruleId/range',
    name: 'DMSpecDetailRuleRange',
    component: () => import(/* webpackChunkName: "ccsystem-datasource" */ '@/views/security/spec/ruleRange'),
    meta: {
      breadcrumbs: [
        { labelKey: 'an-quan-gui-fan', to: { path: '/data-access/rules', query: { tab: 'security' } } },
        {
          labelKey: 'gui-ze-xiang-qing',
          param: 'specId',
          to: (route) => ({
            path: `/system/dmspec/${route.params.specId}`,
            query: route.query.ruleKind ? { ruleKind: route.query.ruleKind } : {}
          })
        },
        { labelKey: 'gui-ze-fan-wei' }
      ]
    }
  },
  {
    path: 'dmspec/:specId/rule/:ruleId/detail',
    name: 'DMSpecDetailRuleDetail',
    component: () => import(/* webpackChunkName: "ccsystem-datasource" */ '@/views/security/spec/ruleDetail'),
    meta: {
      breadcrumbs: [
        { labelKey: 'an-quan-gui-fan', to: { path: '/data-access/rules', query: { tab: 'security' } } },
        {
          labelKey: 'gui-ze-xiang-qing',
          param: 'specId',
          to: (route) => ({
            path: `/system/dmspec/${route.params.specId}`,
            query: route.query.ruleKind ? { ruleKind: route.query.ruleKind } : {}
          })
        },
        { labelKey: 'gui-ze-xiang-qing', queryLabel: 'ruleName' }
      ]
    }
  },
  {
    path: 'desensitization',
    name: 'System_Desensitization',
    component: () => import(/* webpackChunkName: "ccsystem-desensitization" */ '@/views/system/desensitization/index'),
    meta: { breadcrumbs: [{ labelKey: 'shu-ju-tuo-min' }] }
  },
  {
    path: 'desensitization/add',
    name: 'System_Desensitization_Add',
    component: () => import(/* webpackChunkName: "ccsystem-desensitization" */ '@/views/system/desensitization/addDesensitization'),
    meta: {
      breadcrumbs: [{ labelKey: 'shu-ju-tuo-min', to: '/system/desensitization' }, { labelKey: 'xin-zeng-tuo-min-ce-lve' }]
    }
  },
  {
    path: 'data_rules',
    name: 'System_Data_Rules',
    component: () => import(/* webpackChunkName: "ccsystem-data-rules" */ '@/views/system/dataRule/index'),
    meta: { breadcrumbs: [{ labelKey: 'shu-ju-chu-li-gui-ze-guan-li' }] }
  },
  {
    path: 'data_rules/add',
    name: 'System_Data_Rules_Add',
    component: () => import(/* webpackChunkName: "ccsystem-data-rules-add" */ '@/views/system/dataRule/addDataRule'),
    meta: {
      breadcrumbs: [{ labelKey: 'shu-ju-chu-li-gui-ze-guan-li', to: '/system/data_rules' }, { labelKey: 'xin-zeng-gui-ze' }]
    }
  },
  {
    path: 'data_code',
    name: 'System_Data_Code',
    component: () => import(/* webpackChunkName: "ccsystem-env" */ '@/views/system/dataCode/index')
  },
  {
    path: 'operation_log',
    name: 'rdpOperationLog',
    component: () => import(/* webpackChunkName: "ccsystem-env" */ '@/views/system/OperationLog'),
    meta: { requiredAuth: 'RDP_OP_AUDIT_READ' }
  },
  {
    path: 'sql_log',
    redirect: '/manager/logs/sql'
  },
  {
    path: '/settings/profile',
    name: 'Profile',
    component: () => import(/* webpackChunkName: "ccsystem-env" */ '@/views/system/UserCenter'),
    meta: { breadcrumbs: [{ labelKey: 'ge-ren-zi-liao' }] }
  },
  {
    path: 'profile',
    redirect: '/settings/profile'
  },
  {
    path: 'permission',
    name: 'Permission',
    component: () => import(/* webpackChunkName: "ccsystem-env" */ '@/views/system/Permission'),
    meta: { subAccountOnly: true }
  }
];
