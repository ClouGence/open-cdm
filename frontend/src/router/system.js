export default [
  {
    path: '/integrations/git',
    name: 'DMDevops',
    component: () => import(/* webpackChunkName: "ccsystem-datasource" */ '@/views/devops')
  },
  {
    path: 'devops',
    redirect: '/integrations/git'
  },
  {
    path: '/integrations/sso',
    name: 'DMSso',
    component: () => import(/* webpackChunkName: "ccsystem-sso" */ '@/views/system/sso/index')
  },
  {
    path: 'sso',
    redirect: '/integrations/sso'
  },
  {
    path: '/integrations/im',
    name: 'DMIm',
    component: () => import(/* webpackChunkName: "ccsystem-datasource" */ '@/views/im')
  },
  {
    path: 'im',
    redirect: '/integrations/im'
  },
  {
    path: 'info_center',
    name: 'InfoCenter',
    component: () => import(/* webpackChunkName: "ticket" */ '@/views/consoleJob/index')
  },
  {
    path: 'console_job/:id',
    name: 'ConsoleJob/id',
    component: () => import(/* webpackChunkName: "ticket" */ '@/views/consoleJob/consoleJobDetail')
  },
  {
    path: '',
    name: 'System_Home',
    component: () => import(/* webpackChunkName: "ccsystem-home" */ '@/views/system/home')
  },
  {
    path: 'user/config',
    name: 'User_Config',
    component: () => import(/* webpackChunkName: "ccsystem-home" */ '@/views/system/user/userConfig')
  },
  {
    path: 'role',
    redirect: '/manager/account/role'
  },
  {
    path: 'authdm',
    name: 'System_Auth',
    component: () => import(/* webpackChunkName: "ccsystem-auth" */ '@/views/system/subaccount/auth/authDm')
  },
  {
    path: 'account/authdm/:uid',
    name: 'System_Sub_Account_AuthDm',
    component: () => import(/* webpackChunkName: "ccsystem-subaccount-auth" */ '@/views/system/subaccount/auth/authDm')
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
    redirect: '/manager/account/role'
  },
  {
    path: '/manager/account',
    component: () => import(/* webpackChunkName: "ccsystem-management-accounts" */ '@/views/system/management/ManagementAccountsLayout'),
    children: [
      {
        path: '',
        name: 'Management_Accounts_Account',
        component: () => import(/* webpackChunkName: "ccsystem-subaccount" */ '@/views/system/subaccount/index'),
        meta: { managementTab: 'account' }
      },
      {
        path: 'role',
        name: 'Management_Accounts_Role',
        component: () => import(/* webpackChunkName: "ccsystem-role" */ '@/views/system/role/index'),
        meta: { requiredAuth: 'RDP_ROLE_READ', managementTab: 'role' }
      }
    ]
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
    component: () => import(/* webpackChunkName: "ccsystem-env" */ '@/views/system/env')
  },
  {
    path: 'env',
    redirect: '/env'
  },
  {
    path: '/datasource',
    name: 'System_DataSource',
    component: () => import(/* webpackChunkName: "system-datasource" */ '@/views/dataSource/DataSource'),
    meta: { requiredAuth: 'RDP_DS_READ' }
  },
  {
    path: 'ccdatasource',
    redirect: '/datasource'
  },
  {
    path: '/datasource/params/:id/:instanceId',
    name: 'System_DataSource_Params',
    component: () => import(/* webpackChunkName: "system-datasource" */ '@/views/dataSource/DsParams'),
    meta: { requiredAuth: 'RDP_DS_READ' }
  },
  {
    path: 'ccdatasource/params/:id/:instanceId',
    redirect: (to) => `/datasource/params/${to.params.id}/${to.params.instanceId}`
  },
  {
    path: '/datasource/add',
    name: 'System_DataSource_Add',
    component: () => import(/* webpackChunkName: "system-datasource" */ '@/views/dataSource/AddDataSource'),
    meta: { requiredAuth: 'RDP_DS_READ' }
  },
  {
    path: 'ccdatasource/add',
    redirect: '/datasource/add'
  },
  {
    path: '/data-access/cluster',
    name: 'System_Machine',
    component: () => import(/* webpackChunkName: "ccsystem-cluster" */ '@/views/worker/Cluster')
  },
  {
    path: 'dmmachine',
    redirect: '/data-access/cluster'
  },
  {
    path: '/data-access/cluster/list/:clusterId',
    name: 'System_Machine_List',
    component: () => import(/* webpackChunkName: "ccsystem-cluster-list" */ '@/views/system/cluster/workerList')
  },
  {
    path: 'dmmachine/list/:clusterId',
    redirect: (to) => `/data-access/cluster/list/${to.params.clusterId}`
  },
  {
    path: '/data-access/rules',
    name: 'DMRuleList',
    component: () => import(/* webpackChunkName: "ccsystem-datasource" */ '@/views/security/rule/index')
  },
  {
    path: 'dmrulelist',
    redirect: '/data-access/rules'
  },
  {
    path: '/data-access/rules/create',
    name: 'DMRuleCreate',
    component: () => import('@/views/security/rule/ruleDetail')
  },
  {
    path: 'dmrule/create',
    redirect: '/data-access/rules/create'
  },
  {
    path: '/data-access/rules/detail/:id',
    name: 'DMRuleDetail',
    component: () => import('@/views/security/rule/ruleDetail')
  },
  {
    path: 'dmrule/detail/:id',
    redirect: (to) => `/data-access/rules/detail/${to.params.id}`
  },
  {
    path: 'dmspeclist',
    name: 'DMSpecList',
    component: () => import(/* webpackChunkName: "ccsystem-datasource" */ '@/views/security/spec/index')
  },
  {
    path: 'dmspec/:specId',
    name: 'DMSpecDetail',
    component: () => import(/* webpackChunkName: "ccsystem-datasource" */ '@/views/security/spec/specDetail')
  },
  {
    path: 'dmspec/:specId/rule/:ruleId/range',
    name: 'DMSpecDetailRuleRange',
    component: () => import(/* webpackChunkName: "ccsystem-datasource" */ '@/views/security/spec/ruleRange')
  },
  {
    path: 'desensitization',
    name: 'System_Desensitization',
    component: () => import(/* webpackChunkName: "ccsystem-desensitization" */ '@/views/system/desensitization/index')
  },
  {
    path: 'desensitization/add',
    name: 'System_Desensitization_Add',
    component: () => import(/* webpackChunkName: "ccsystem-desensitization" */ '@/views/system/desensitization/addDesensitization')
  },
  {
    path: 'data_rules',
    name: 'System_Data_Rules',
    component: () => import(/* webpackChunkName: "ccsystem-data-rules" */ '@/views/system/dataRule/index')
  },
  {
    path: 'data_rules/add',
    name: 'System_Data_Rules_Add',
    component: () => import(/* webpackChunkName: "ccsystem-data-rules-add" */ '@/views/system/dataRule/addDataRule')
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
    component: () => import(/* webpackChunkName: "ccsystem-env" */ '@/views/system/UserCenter')
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
