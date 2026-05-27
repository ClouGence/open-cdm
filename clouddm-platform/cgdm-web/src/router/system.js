export default [
  {
    path: 'devops',
    name: 'DMDevops',
    component: () => import(/* webpackChunkName: "ccsystem-datasource" */ '@/views/devops')
  },
  {
    path: 'im',
    name: 'DMIm',
    component: () => import(/* webpackChunkName: "ccsystem-datasource" */ '@/views/im')
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
    name: 'System_Role',
    component: () => import(/* webpackChunkName: "ccsystem-role" */ '@/views/system/role/index'),
    meta: { requiredAuth: 'RDP_ROLE_READ' }
  },
  {
    path: 'authdm',
    name: 'System_Auth',
    component: () => import(/* webpackChunkName: "ccsystem-auth" */ '@/views/system/subaccount/auth/authDm')
  },
  {
    path: 'sub_account',
    name: 'System_Sub_Account',
    component: () => import(/* webpackChunkName: "ccsystem-subaccount" */ '@/views/system/subaccount/index')
  },
  {
    path: 'sub_account/authdm/:uid',
    name: 'System_Sub_Account_AuthDm',
    component: () => import(/* webpackChunkName: "ccsystem-subaccount-auth" */ '@/views/system/subaccount/auth/authDm')
  },
  {
    path: 'env',
    name: 'System_Env',
    component: () => import(/* webpackChunkName: "ccsystem-env" */ '@/views/system/env')
  },
  {
    path: 'ccdatasource',
    name: 'System_DataSource',
    component: () => import(/* webpackChunkName: "system-datasource" */ '@/views/dataSource/DataSource'),
    meta: { requiredAuth: 'RDP_DS_READ' }
  },
  {
    path: 'ccdatasource/params/:id/:instanceId',
    name: 'System_DataSource_Params',
    component: () => import(/* webpackChunkName: "system-datasource" */ '@/views/dataSource/DsParams'),
    meta: { requiredAuth: 'RDP_DS_READ' }
  },
  {
    path: 'ccdatasource/add',
    name: 'System_DataSource_Add',
    component: () => import(/* webpackChunkName: "system-datasource" */ '@/views/dataSource/AddDataSource'),
    meta: { requiredAuth: 'RDP_DS_READ' }
  },
  {
    path: 'dmmachine',
    name: 'System_Machine',
    component: () => import(/* webpackChunkName: "ccsystem-cluster" */ '@/views/worker/Cluster')
  },
  {
    path: 'dmmachine/list/:clusterId',
    name: 'System_Machine_List',
    component: () => import(/* webpackChunkName: "ccsystem-cluster-list" */ '@/views/system/cluster/workerList')
  },
  {
    path: 'dmrulelist',
    name: 'DMRuleList',
    component: () => import(/* webpackChunkName: "ccsystem-datasource" */ '@/views/security/rule/index')
  },
  {
    path: 'dmrule/create',
    name: 'DMRuleCreate',
    component: () => import('@/views/security/rule/ruleDetail')
  },
  {
    path: 'dmrule/detail/:id',
    name: 'DMRuleDetail',
    component: () => import('@/views/security/rule/ruleDetail')
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
    component: () => import(/* webpackChunkName: "ccsystem-env" */ '@/views/system/OperationLog')
  },
  {
    path: 'sql_log',
    name: 'sqlLog',
    component: () => import(/* webpackChunkName: "ccsystem-env" */ '@/views/system/SqlLog')
  },
  {
    path: 'payment',
    name: 'Payment',
    component: () => import(/* webpackChunkName: "ccsystem-env" */ '@/views/system/billingandpayment/Payment')
  },
  {
    path: 'credits',
    name: 'Credits',
    component: () => import(/* webpackChunkName: "ccsystem-env" */ '@/views/system/billingandpayment/Credits')
  },
  {
    path: 'license',
    name: 'License',
    component: () => import(/* webpackChunkName: "ccsystem-env" */ '@/views/system/billingandpayment/License')
  },
  {
    path: 'order',
    name: 'Order',
    component: () => import(/* webpackChunkName: "ccsystem-env" */ '@/views/system/billingandpayment/Order')
  },
  {
    path: 'measurement',
    name: 'Measurement',
    component: () => import(/* webpackChunkName: "ccsystem-env" */ '@/views/system/billingandpayment/Measurement')
  },
  {
    path: 'billing',
    name: 'Billing',
    component: () => import(/* webpackChunkName: "ccsystem-env" */ '@/views/system/billingandpayment/Billing')
  },
  {
    path: 'billing/:id',
    name: 'BillingDetail',
    component: () => import(/* webpackChunkName: "ccsystem-env" */ '@/views/system/billingandpayment/BillingDetail')
  },
  {
    path: 'invoice',
    name: 'Invoice',
    component: () => import(/* webpackChunkName: "ccsystem-env" */ '@/views/system/billingandpayment/Invoice')
  },
  {
    path: 'invoiceApplyList',
    name: 'InvoiceApplyList',
    component: () => import(/* webpackChunkName: "ccsystem-env" */ '@/views/system/billingandpayment/InvoiceApplyList')
  },
  {
    path: 'invoiceApply',
    name: 'InvoiceApply',
    component: () => import(/* webpackChunkName: "ccsystem-env" */ '@/views/system/billingandpayment/InvoiceApply')
  },
  {
    path: 'balance',
    name: 'Balance',
    component: () => import(/* webpackChunkName: "ccsystem-env" */ '@/views/system/billingandpayment/Balance')
  },
  {
    path: 'prepay',
    name: 'Prepay',
    component: () => import(/* webpackChunkName: "ccsystem-env" */ '@/views/system/billingandpayment/Prepay')
  },
  {
    path: 'profile',
    name: 'Profile',
    component: () => import(/* webpackChunkName: "ccsystem-env" */ '@/views/system/UserCenter')
  },
  {
    path: 'permission',
    name: 'Permission',
    component: () => import(/* webpackChunkName: "ccsystem-env" */ '@/views/system/Permission')
  }
];
