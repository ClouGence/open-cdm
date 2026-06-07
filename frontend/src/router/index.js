import { createRouter, createWebHashHistory } from 'vue-router';
import UserCenter from '@/views/system/UserCenter';
import Preference from '@/views/system/Preference';
import Ticket from '@/views/ticket';
import System from './system';
import store from '@/store';

async function fetchMyAuthIfNeeded() {
  if (store.state.myAuth.length) {
    return;
  }

  try {
    const res = await fetch('/rdp/console/api/v1/user/listMyAuth', {
      method: 'POST',
      credentials: 'include',
      headers: {
        'Content-Type': 'application/json'
      },
      body: '{}'
    });
    const payload = await res.json();
    if (payload?.success && Array.isArray(payload.data)) {
      store.commit('UPDATE_MY_AUTH', payload.data);
    }
  } catch {
    // Auth will be rejected below when the probe fails.
  }
}

const systemChildren = [
  {
    path: '/system/preference',
    name: '/system/preference',
    component: Preference,
    meta: { requiredAuth: 'RDP_PRI_USER_KV_CONF_R' }
  }
].concat(System);

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import(/* webpackChunkName: "login" */ '@/views/login/index')
  },
  {
    path: '/',
    name: 'Home',
    component: () => import(/* webpackChunkName: "login" */ '@/views/home/index'),
    children: [
      {
        path: 'sql',
        name: 'SQL',
        component: () => import(/* webpackChunkName: "sql" */ '@/views/sql/index')
      },
      {
        path: 'project',
        name: 'Project',
        component: () => import(/* webpackChunkName: "project" */ '@/views/project/index')
      },
      {
        path: 'project/:id',
        name: 'project/id',
        component: () => import(/* webpackChunkName: "ticket" */ '../views/project/projectDetail')
      },
      {
        path: 'project/change/:id',
        name: 'project/change/id',
        component: () => import(/* webpackChunkName: "ticket" */ '@/views/project/changeDetail')
      },
      {
        path: 'ticket',
        name: 'Ticket',
        component: Ticket
      },
      {
        path: '/ticket/:id',
        name: 'Ticket/id',
        component: () => import(/* webpackChunkName: "ticket" */ '../views/ticket/ticketDetail')
      },
      {
        path: '/ticket_create',
        name: 'Ticket_create',
        component: () => import(/* webpackChunkName: "ticket" */ '../views/ticket/ticket')
      },
      {
        path: 'dmdatasource',
        name: 'System_DataSource_list',
        redirect: '/system/ccdatasource'
      },
      {
        path: 'ccdatasource',
        redirect: (to) => ({
          path: '/system/ccdatasource',
          query: to.query,
          hash: to.hash
        })
      },
      {
        path: 'ccdatasource/params/:id/:instanceId',
        redirect: (to) => ({
          path: `/system/ccdatasource/params/${to.params.id}/${to.params.instanceId}`,
          query: to.query,
          hash: to.hash
        })
      },
      {
        path: 'ccdatasource/add',
        redirect: (to) => ({
          path: '/system/ccdatasource/add',
          query: to.query,
          hash: to.hash
        })
      },
      {
        path: 'dmspeclist',
        redirect: (to) => ({
          path: '/system/dmspeclist',
          query: to.query,
          hash: to.hash
        })
      },
      {
        path: 'dmspec/:specId',
        redirect: (to) => ({
          path: `/system/dmspec/${to.params.specId}`,
          query: to.query,
          hash: to.hash
        })
      },
      {
        path: 'dmspec/:specId/rule/:ruleId/range',
        redirect: (to) => ({
          path: `/system/dmspec/${to.params.specId}/rule/${to.params.ruleId}/range`,
          query: to.query,
          hash: to.hash
        })
      },
      {
        path: 'dmrulelist',
        redirect: (to) => ({
          path: '/system/dmrulelist',
          query: to.query,
          hash: to.hash
        })
      },
      {
        path: 'dmrule/create',
        redirect: (to) => ({
          path: '/system/dmrule/create',
          query: to.query,
          hash: to.hash
        })
      },
      {
        path: 'dmrule/detail/:id',
        redirect: (to) => ({
          path: `/system/dmrule/detail/${to.params.id}`,
          query: to.query,
          hash: to.hash
        })
      },
      {
        path: 'dmdatasource/params/:id',
        name: 'DM_DataSource_Params_Id',
        redirect: '/system/ccdatasource'
      },
      {
        path: 'dmmachine',
        redirect: (to) => ({
          path: '/system/dmmachine',
          query: to.query,
          hash: to.hash
        })
      },
      {
        path: 'dmmachine/list/:clusterId',
        redirect: (to) => ({
          path: `/system/dmmachine/list/${to.params.clusterId}`,
          query: to.query,
          hash: to.hash
        })
      },
      {
        path: '/userCenter',
        name: 'userCenter',
        component: UserCenter
      },
      {
        path: 'system',
        name: 'System',
        component: () => import(/* webpackChunkName: "ccsystem" */ '@/views/system/index'),
        children: systemChildren
        // children: [
        //   {
        //     path: 'info_center',
        //     name: 'InfoCenter',
        //     component: () => import(/* webpackChunkName: "ticket" */'@/views/consoleJob/index')
        //   },
        //   {
        //     path: 'console_job/:id',
        //     name: 'ConsoleJob/id',
        //     component: () => import(/* webpackChunkName: "ticket" */'@/views/consoleJob/consoleJobDetail')
        //   },
        //   {
        //     path: 'async_job_list',
        //     name: 'ASYNC_JOB_LIST',
        //     component: () => import(/* webpackChunkName: "async-job-list" */'@/views/system/AsyncJobList/index')
        //   },
        //   {
        //     path: 'async_job/:id',
        //     name: 'ASYNC_JOB_DETAIL',
        //     component: () => import(/* webpackChunkName: "async-job-list" */'@/views/system/AsyncJobList/asyncJobDetail')
        //   },
        //   {
        //     path: '',
        //     name: 'System_Home',
        //     component: () => import(/* webpackChunkName: "system-home" */'@/views/system/home')
        //   },
        //   {
        //     path: 'user/config',
        //     name: 'User_Config',
        //     component: () => import(/* webpackChunkName: "system-home" */'@/views/system/user/userConfig')
        //   },
        //   {
        //     path: 'role',
        //     name: 'System_Role',
        //     component: () => import(/* webpackChunkName: "system-role" */'@/views/system/role/index')
        //   },
        //   {
        //     path: 'machine',
        //     name: 'System_Machine',
        //     component: () => import(/* webpackChunkName: "system-cluster" */'@/views/system/cluster/index')
        //   },
        //   {
        //     path: 'machine/list/:clusterId',
        //     name: 'System_Machine_List',
        //     component: () => import(/* webpackChunkName: "system-cluster-list" */'@/views/system/cluster/workerList')
        //   },
        //   {
        //     path: 'datasource',
        //     name: 'System_DataSource',
        //     component: () => import(/* webpackChunkName: "system-datasource" */'@/views/system/datasource/index')
        //   },
        //   {
        //     path: 'auth',
        //     name: 'System_Auth',
        //     component: () => import(/* webpackChunkName: "system-auth" */'@/views/system/auth/index')
        //   },
        //   {
        //     path: 'datasource/params/:id',
        //     name: 'System_DataSource_Params/id',
        //     component: () => import(/* webpackChunkName: "system-datasource" */'@/views/system/user/components/Params')
        //   },
        //   {
        //     path: 'datasource/add',
        //     name: 'System_DataSource_Add',
        //     component: () => import(/* webpackChunkName: "system-datasource" */'@/views/system/datasource/steps/Index')
        //   },
        //   {
        //     path: 'env',
        //     name: 'System_Env',
        //     component: () => import(/* webpackChunkName: "system-env" */'@/views/system/env')
        //   },
        //   {
        //     path: 'rules',
        //     name: 'System_Rule',
        //     component: () => import(/* webpackChunkName: "system-env" */'@/views/system/rule/index')
        //   },
        //   {
        //     path: 'desensitization',
        //     name: 'System_Desensitization',
        //     component: () => import(/* webpackChunkName: "system-desensitization" */'@/views/system/desensitization/index')
        //   },
        //   {
        //     path: 'desensitization/add',
        //     name: 'System_Desensitization_Add',
        //     component: () => import(/* webpackChunkName: "system-desensitization" */'@/views/system/desensitization/addDesensitization')
        //   },
        //   {
        //     path: 'data_rules',
        //     name: 'System_Data_Rules',
        //     component: () => import(/* webpackChunkName: "system-data-rules" */'@/views/system/dataRule/index')
        //   },
        //   {
        //     path: 'data_rules/add',
        //     name: 'System_Data_Rules_Add',
        //     component: () => import(/* webpackChunkName: "system-data-rules-add" */'@/views/system/dataRule/addDataRule')
        //   },
        //   {
        //     path: 'data_code',
        //     name: 'System_Data_Code',
        //     component: () => import(/* webpackChunkName: "system-env" */'@/views/system/dataCode/index')
        //   }
        // ]
      }
    ]
  },
  {
    path: '/initialization',
    name: 'Initialization',
    component: () => import(/* webpackChunkName: "initialization" */ '@/views/initialization/index')
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/sql'
  }
];

const router = createRouter({
  history: createWebHashHistory(process.env.BASE_URL),
  routes
});

router.beforeEach(async (to, from, next) => {
  const requiredAuth = to.meta.requiredAuth;
  if (requiredAuth) {
    await fetchMyAuthIfNeeded();
  }

  if (requiredAuth && !store.state.myAuth.includes(requiredAuth)) {
    next({ path: store.state.defaultRedirectUrl || '/sql', replace: true });
    return;
  }

  next();
});

export default router;
