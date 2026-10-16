export default [
  {
    path: '/integrations/git',
    name: 'DMDevops',
    component: () => import(/* webpackChunkName: "system-datasource" */ '@/views/devops')
  },
  {
    path: '/integrations/git/create',
    name: 'DMDevopsCreate',
    component: () => import(/* webpackChunkName: "system-datasource" */ '@/views/devops/form')
  },
  {
    path: '/integrations/git/:scmId/edit',
    name: 'DMDevopsEdit',
    component: () => import(/* webpackChunkName: "system-datasource" */ '@/views/devops/form')
  },
  {
    path: 'devops',
    redirect: '/integrations/git'
  },
  {
    path: '/integrations/sso',
    name: 'DMSso',
    component: () => import(/* webpackChunkName: "system-sso" */ '@/views/system/sso/index')
  },
  {
    path: '/integrations/sso/create',
    name: 'DMSsoCreate',
    component: () => import(/* webpackChunkName: "system-sso" */ '@/views/system/sso/form')
  },
  {
    path: '/integrations/sso/:type/edit',
    name: 'DMSsoEdit',
    component: () => import(/* webpackChunkName: "system-sso" */ '@/views/system/sso/form')
  },
  {
    path: 'sso',
    redirect: '/integrations/sso'
  },
  {
    path: '/integrations/approval',
    name: 'DMApproval',
    component: () => import(/* webpackChunkName: "system-approval" */ '@/views/system/approval/index')
  },
  {
    path: '/integrations/approval/create',
    name: 'DMApprovalCreate',
    component: () => import(/* webpackChunkName: "system-approval" */ '@/views/system/approval/form')
  },
  {
    path: '/integrations/approval/:type/edit',
    name: 'DMApprovalEdit',
    component: () => import(/* webpackChunkName: "system-approval" */ '@/views/system/approval/form')
  },
  {
    path: '/integrations/im',
    name: 'DMIm',
    component: () => import(/* webpackChunkName: "system-datasource" */ '@/views/im')
  },
  {
    path: '/integrations/im/create',
    name: 'DMImCreate',
    component: () => import(/* webpackChunkName: "system-datasource" */ '@/views/im/form')
  },
  {
    path: '/integrations/im/:imId/edit',
    name: 'DMImEdit',
    component: () => import(/* webpackChunkName: "system-datasource" */ '@/views/im/form')
  },
  {
    path: 'im',
    redirect: '/integrations/im'
  },
  {
    path: '',
    name: 'System_Home',
    component: () => import(/* webpackChunkName: "system-home" */ '@/views/system/home')
  },
  {
    path: 'user/config',
    name: 'User_Config',
    component: () => import(/* webpackChunkName: "system-home" */ '@/views/system/user/userConfig')
  },
  {
    path: 'role',
    redirect: '/manager/role'
  },
  {
    path: 'authdm',
    name: 'System_Auth',
    component: () => import(/* webpackChunkName: "system-auth" */ '@/views/system/subaccount/auth/authDm')
  },
  {
    path: '/manager/account/batch_authorization',
    name: 'Management_Accounts_Batch_Authorization',
    component: () => import(/* webpackChunkName: "system-subaccount-batch-auth" */ '@/views/system/subaccount/BatchAuthorizationPage'),
    meta: { requiredAuth: 'RDP_AUTH_MANAGE' }
  },
  {
    path: '/manager/account/batch_authorization/permissions',
    name: 'Management_Accounts_Batch_Authorization_Permissions',
    component: () => import(/* webpackChunkName: "system-subaccount-auth" */ '@/views/system/subaccount/auth/authDm'),
    meta: { requiredAuth: 'RDP_AUTH_MANAGE' }
  },
  {
    path: 'account/authdm/batch',
    name: 'System_Sub_Account_Batch_AuthDm',
    component: () => import(/* webpackChunkName: "system-subaccount-auth" */ '@/views/system/subaccount/auth/authDm'),
    meta: { requiredAuth: 'RDP_AUTH_MANAGE' }
  },
  {
    path: 'account/authdm/:uid',
    name: 'System_Sub_Account_AuthDm',
    component: () => import(/* webpackChunkName: "system-subaccount-auth" */ '@/views/system/subaccount/auth/authDm')
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
    component: () => import(/* webpackChunkName: "system-subaccount" */ '@/views/system/subaccount/index')
  },
  {
    path: '/manager/role/create',
    name: 'Management_Role_Create',
    component: () => import(/* webpackChunkName: "system-role" */ '@/views/system/role/RoleEditorPage'),
    meta: { requiredAuth: 'RDP_ROLE_MANAGE' }
  },
  {
    path: '/manager/role/:roleId/edit',
    name: 'Management_Role_Edit',
    component: () => import(/* webpackChunkName: "system-role" */ '@/views/system/role/RoleEditorPage'),
    meta: { requiredAuth: 'RDP_ROLE_MANAGE' }
  },
  {
    path: '/manager/role/:roleId/view',
    name: 'Management_Role_View',
    component: () => import(/* webpackChunkName: "system-role" */ '@/views/system/role/RoleEditorPage'),
    meta: { requiredAuth: 'RDP_ROLE_READ' }
  },
  {
    path: '/manager/role',
    name: 'Management_Role',
    component: () => import(/* webpackChunkName: "system-role" */ '@/views/system/role/index'),
    meta: { requiredAuth: 'RDP_ROLE_READ' }
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
    component: () => import(/* webpackChunkName: "system-management-logs" */ '@/views/system/management/ManagementLogsLayout'),
    children: [
      {
        path: '',
        name: 'Management_Logs_Operation',
        component: () => import(/* webpackChunkName: "system-env" */ '@/views/system/OperationLog'),
        meta: { managementTab: 'operation' }
      },
      {
        path: 'sql',
        name: 'Management_Logs_Sql',
        component: () => import(/* webpackChunkName: "system-env" */ '@/views/system/SqlLog'),
        meta: { managementTab: 'sql' }
      }
    ]
  },
  {
    path: '/env',
    name: 'System_Env',
    component: () => import(/* webpackChunkName: "system-env" */ '@/views/system/env')
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
    path: '/datasource/add',
    name: 'System_DataSource_Add',
    component: () => import(/* webpackChunkName: "system-datasource" */ '@/views/dataSource/AddDataSource'),
    meta: { requiredAuth: 'RDP_DS_READ' }
  },
  {
    path: '/data-access/cluster',
    name: 'System_Machine',
    component: () => import(/* webpackChunkName: "system-cluster" */ '@/views/worker/Cluster')
  },
  {
    path: 'dmmachine',
    redirect: '/data-access/cluster'
  },
  {
    path: '/data-access/cluster/list/:clusterId',
    name: 'System_Machine_List',
    component: () => import(/* webpackChunkName: "system-cluster-list" */ '@/views/system/cluster/workerList')
  },
  {
    path: 'dmmachine/list/:clusterId',
    redirect: (to) => `/data-access/cluster/list/${to.params.clusterId}`
  },
  {
    path: '/data-access/rules',
    name: 'DMRuleList',
    component: () => import(/* webpackChunkName: "system-datasource" */ '@/views/security/index')
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
    component: () => import(/* webpackChunkName: "system-datasource" */ '@/views/security/spec/specDetail')
  },
  {
    path: 'dmspec/:specId/rule/:ruleId/range',
    name: 'DMSpecDetailRuleRange',
    component: () => import(/* webpackChunkName: "system-datasource" */ '@/views/security/spec/ruleRange')
  },
  {
    path: 'dmspec/:specId/rule/:ruleId/detail',
    name: 'DMSpecDetailRuleDetail',
    component: () => import(/* webpackChunkName: "system-datasource" */ '@/views/security/spec/ruleDetail')
  },
  {
    path: 'operation_log',
    name: 'rdpOperationLog',
    component: () => import(/* webpackChunkName: "system-env" */ '@/views/system/OperationLog'),
    meta: { requiredAuth: 'RDP_OP_AUDIT_READ' }
  },
  {
    path: 'sql_log',
    redirect: '/manager/logs/sql'
  },
  {
    path: '/settings/profile',
    name: 'Profile',
    component: () => import(/* webpackChunkName: "system-env" */ '@/views/system/UserCenter')
  },
  {
    path: 'profile',
    redirect: '/settings/profile'
  },
  {
    path: 'permission',
    name: 'Permission',
    component: () => import(/* webpackChunkName: "system-env" */ '@/views/system/Permission'),
    meta: { subAccountOnly: true }
  }
];
