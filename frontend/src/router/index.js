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
    const res = await fetch('/api/entry/user/listMyAuth', {
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
    path: '/settings/preferences',
    name: '/settings/preferences',
    component: Preference,
    meta: { requiredAuth: 'RDP_PRI_USER_KV_CONF_R' }
  },
  {
    path: 'preference',
    redirect: '/settings/preferences'
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
        path: 'cicd',
        name: 'CICD',
        component: () => import(/* webpackChunkName: "cicd" */ '@/views/cicd/index')
      },
      {
        path: 'cicd/create',
        name: 'cicd/create',
        component: () => import(/* webpackChunkName: "cicd-release-flow" */ '@/views/cicd/ReleaseFlowPage'),
        meta: { requiredAuth: 'DM_CICD_FLOW_MANAGE' }
      },
      {
        path: 'cicd/:id/release-flow/add',
        name: 'cicd/release-flow/add',
        component: () => import(/* webpackChunkName: "cicd-release-flow" */ '@/views/cicd/ReleaseFlowPage'),
        meta: { requiredAuth: 'DM_CICD_FLOW_MANAGE' }
      },
      {
        path: 'cicd/:id/change-records',
        redirect: (to) => `/cicd/${to.params.id}`
      },
      {
        path: 'cicd/:id/config',
        name: 'cicd/config',
        component: () => import(/* webpackChunkName: "cicd-release-flow-config" */ '@/views/cicd/flowConfig')
      },
      {
        path: 'cicd/change/:id',
        redirect: (to) => (to.query.flowId ? `/cicd/${to.query.flowId}` : '/cicd')
      },
      {
        path: 'cicd/:id',
        name: 'cicd/id',
        component: () => import(/* webpackChunkName: "ticket" */ '../views/cicd/flowDetail')
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
        redirect: '/datasource'
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
        path: 'dmspec/:specId/rule/:ruleId/detail',
        redirect: (to) => ({
          path: `/system/dmspec/${to.params.specId}/rule/${to.params.ruleId}/detail`,
          query: to.query,
          hash: to.hash
        })
      },
      {
        path: 'dmrulelist',
        redirect: (to) => ({
          path: '/data-access/rules',
          query: to.query,
          hash: to.hash
        })
      },
      {
        path: 'dmrule/create',
        redirect: (to) => ({
          path: '/data-access/rules/create',
          query: to.query,
          hash: to.hash
        })
      },
      {
        path: 'dmrule/detail/:id',
        redirect: (to) => ({
          path: `/data-access/rules/detail/${to.params.id}`,
          query: to.query,
          hash: to.hash
        })
      },
      {
        path: 'dmmachine',
        redirect: (to) => ({
          path: '/data-access/cluster',
          query: to.query,
          hash: to.hash
        })
      },
      {
        path: 'dmmachine/list/:clusterId',
        redirect: (to) => ({
          path: `/data-access/cluster/list/${to.params.clusterId}`,
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
        component: () => import(/* webpackChunkName: "system" */ '@/views/system/index'),
        children: systemChildren
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

  if (to.matched.some((record) => record.meta.subAccountOnly)) {
    if (store.state.userInfo?.accountType === 'PRIMARY_ACCOUNT') {
      next({ path: store.state.defaultRedirectUrl || '/sql', replace: true });
      return;
    }
  }

  next();
});

export default router;
