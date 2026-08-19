<template>
  <header class="app-content-header">
    <div class="app-content-header__left">
      <h1 class="app-content-header__title">
        <template v-for="(item, index) in pageBreadcrumbs" :key="`${item.label}-${index}`">
          <button
            v-if="item.event"
            type="button"
            class="app-content-header__crumb app-content-header__crumb-button"
            :class="{ 'is-current': index === pageBreadcrumbs.length - 1 }"
            @click="handleBreadcrumbEvent(item.event)"
          >
            {{ item.label }}
          </button>
          <router-link
            v-else-if="item.to"
            class="app-content-header__crumb"
            :class="{ 'is-current': index === pageBreadcrumbs.length - 1 }"
            :to="item.to"
          >
            {{ item.label }}
          </router-link>
          <span v-else class="app-content-header__crumb" :class="{ 'is-current': index === pageBreadcrumbs.length - 1 }">
            {{ item.label }}
          </span>
          <span v-if="index < pageBreadcrumbs.length - 1" class="app-content-header__separator">/</span>
        </template>
      </h1>
    </div>
    <div class="app-content-header__right">
      <a v-if="showSqlLink" href="/#/sql" class="app-content-header__link" @click.prevent="handleGoSql">
        <CustomIcon type="icon-v2-SqlLog" size="14px" />
        <span>{{ $t('sql-cha-xun') }}</span>
      </a>
      <AppUserActions compact @check-version="$emit('check-version')" />
    </div>
  </header>
</template>

<script>
import { mapGetters, mapState } from 'vuex';
import AppUserActions from '@/components/layout/AppUserActions';
import { saveLastWorkbenchRoute } from '@/utils/workbenchRoute';
import { findDsSupportName } from '@/utils/datasourceSupport';
import { EVENT_BUS_NAME_LIST } from '@/utils/eventBusName';

export default {
  name: 'AppContentHeader',
  components: { AppUserActions },
  emits: ['check-version'],
  computed: {
    ...mapGetters(['isDesktop']),
    ...mapState(['dmGlobalSetting', 'myCatLog', 'mySystemMenuItems', 'sidebarMenu', 'userInfo']),
    showSqlLink() {
      return this.myCatLog.includes('CAT_DM_CONSOLE') && !this.$route.path.startsWith('/sql');
    },
    pageTitle() {
      const path = this.$route.path;

      if (path === '/cicd' || path === '/cicd/') {
        return this.$t('nav-ci-cd');
      }
      if (path.indexOf('/cicd') === 0) {
        return this.$t('nav-ci-cd');
      }
      if (path === '/ticket' || path === '/ticket/') {
        return this.$t('gong-dan');
      }
      if (path.indexOf('/ticket') === 0) {
        return this.$t('gong-dan');
      }
      if (path.indexOf('/settings/profile') === 0 || path.indexOf('/system/profile') > -1) {
        return this.$t('ge-ren-zi-liao');
      }
      if (path.indexOf('/settings/preferences') === 0 || path.indexOf('/system/preference') > -1) {
        return this.$t('nav-tong-yong');
      }
      if (path.indexOf('/system/permission') > -1) {
        if (this.$route.query.type === 'apply') {
          return this.$t('shen-qing-quan-xian');
        }
        return this.$t('my-permissions');
      }
      if (path.indexOf('/datasource') === 0) {
        return this.$t('nav-shu-ju-ku-guan-li');
      }
      if (path === '/env' || path.indexOf('/env/') === 0 || path.indexOf('/system/env') > -1) {
        return this.$t('huan-jing');
      }
      if (path.indexOf('/manager/role') === 0 || path.indexOf('/system/role') > -1) {
        return this.$t('jiao-se');
      }
      if (path.indexOf('/manager/account') === 0 || path.indexOf('/system/management/accounts') > -1 || path.indexOf('/system/account') > -1) {
        return this.$t('nav-zhang-hu');
      }
      if (
        path.indexOf('/manager/logs') === 0 ||
        path.indexOf('/system/management/logs') > -1 ||
        path.indexOf('/system/operation_log') > -1 ||
        path.indexOf('/system/sql_log') > -1
      ) {
        return this.$t('nav-ri-zhi');
      }
      if (path.indexOf('/data-access/cluster') === 0 || path.indexOf('/system/dmmachine') > -1) {
        return this.$t('nav-cha-xun-ji-qi-lie-biao');
      }
      if (path.indexOf('/data-access/rules') === 0 || path.indexOf('/system/dmrule') > -1 || path.indexOf('/system/dmspec') > -1) {
        return this.$t('an-quan-gui-fan');
      }
      if (path.indexOf('/integrations/im') === 0 || path.indexOf('/system/im') > -1) {
        return this.$t('nav-webhook');
      }
      if (path.indexOf('/integrations/git') === 0 || path.indexOf('/system/devops') > -1) {
        return this.$t('nav-git-ops');
      }
      if (path.indexOf('/integrations/sso') === 0 || path.indexOf('/system/sso') > -1) {
        return this.$t('nav-sso');
      }
      if (path.indexOf('/integrations/approval') === 0) {
        return this.$t('nav-shen-pi-yin-qing');
      }

      const parts = path.split('/').filter(Boolean);
      if (parts[0] === 'system' && parts.length >= 2) {
        const key = `/${parts[0]}/${parts[1]}`;
        const menuItem = this.mySystemMenuItems.find((item) => item.key === key);
        if (menuItem) {
          return menuItem.label;
        }
      }
      if (parts[0] === 'cicd') {
        return this.$t('nav-ci-cd');
      }

      return this.$t('pei-zhi');
    },
    pageSubTitle() {
      const path = this.$route.path;

      if (path === '/cicd/create') {
        return this.$t('chuang-jian-xiang-mu');
      }
      if (/^\/cicd\/[^/]+\/config$/.test(path)) {
        return this.$t('cicd-pei-zhi-xiang');
      }
      if (/^\/cicd\/[^/]+$/.test(path)) {
        return this.$t('cicd-bian-geng-liu-gai-lan');
      }

      return '';
    },
    pageBreadcrumbs() {
      const path = this.$route.path;
      const securityRoot = {
        label: this.$t('an-quan-gui-fan'),
        to: {
          path: '/data-access/rules',
          query: { tab: 'security' }
        }
      };
      const securityTemplateRoot = {
        ...securityRoot,
        to: {
          path: '/data-access/rules',
          query: { tab: 'template' }
        }
      };
      const securityDetail = (specId) => ({
        label: this.$t('gui-ze-xiang-qing'),
        to: {
          path: `/system/dmspec/${specId}`,
          query: this.$route.query.ruleKind ? { ruleKind: this.$route.query.ruleKind } : {}
        }
      });
      const cicdRoot = { label: this.$t('nav-ci-cd'), to: '/cicd' };
      const ticketRoot = { label: this.$t('gong-dan'), to: '/ticket' };
      const flowDetail = (flowId) => ({
        label: this.$t('cicd-bian-geng-liu-xiang-qing'),
        to: flowId ? `/cicd/${flowId}` : ''
      });
      const machineRoot = {
        label: this.$t('nav-cha-xun-ji-qi-lie-biao'),
        to: '/data-access/cluster'
      };
      const roleRoot = {
        label: this.$t('jiao-se'),
        to: '/manager/role'
      };
      const accountRoot = {
        label: this.$t('nav-zhang-hu'),
        to: '/manager/account'
      };

      if (path === '/manager/account/batch_authorization') {
        return [accountRoot, { label: this.$t('pi-liang-shou-quan'), to: this.$route.fullPath }];
      }
      if (
        path === '/manager/account/batch_authorization/permissions' ||
        (path === '/system/account/authdm/batch' && this.$route.query.type === 'batch')
      ) {
        const batchAuthorizationQuery = {
          operation: this.$route.query.operation,
          uids: this.$route.query.uids
        };
        return [
          accountRoot,
          {
            label: this.$t('pi-liang-shou-quan'),
            to: { path: '/manager/account/batch_authorization', query: batchAuthorizationQuery }
          },
          { label: this.$t('xuan-ze-quan-xian'), to: this.$route.fullPath }
        ];
      }
      if (path === '/system/authdm' || /^\/system\/account\/authdm\/[^/]+$/.test(path)) {
        return [accountRoot, { label: this.$t('shou-quan'), to: this.$route.fullPath }];
      }
      if (path === '/data-access/rules' || path === '/data-access/rules/') {
        return [{ ...securityRoot, to: this.$route.fullPath }];
      }
      if (path === '/data-access/rules/create') {
        return [
          securityTemplateRoot,
          {
            label: this.$t('xin-jian-gui-ze-mo-ban'),
            to: this.$route.fullPath
          }
        ];
      }
      if (/^\/data-access\/rules\/detail\/[^/]+$/.test(path)) {
        return [
          securityTemplateRoot,
          {
            label: this.$t('gui-ze-mo-ban-xiang-qing'),
            to: this.$route.fullPath
          }
        ];
      }
      if (/^\/system\/dmspec\/[^/]+$/.test(path)) {
        return [
          securityRoot,
          {
            ...securityDetail(this.$route.params.specId),
            to: this.$route.fullPath
          }
        ];
      }
      if (/^\/system\/dmspec\/[^/]+\/rule\/[^/]+\/range$/.test(path)) {
        return [
          securityRoot,
          securityDetail(this.$route.params.specId),
          {
            label: this.$t('gui-ze-fan-wei'),
            to: this.$route.fullPath
          }
        ];
      }
      if (/^\/system\/dmspec\/[^/]+\/rule\/[^/]+\/detail$/.test(path)) {
        return [
          securityRoot,
          securityDetail(this.$route.params.specId),
          {
            label: this.$route.query.ruleName || this.$t('gui-ze-xiang-qing'),
            to: this.$route.fullPath
          }
        ];
      }
      if (path === '/cicd' || path === '/cicd/') {
        return [cicdRoot];
      }
      if (path === '/cicd/create') {
        return [cicdRoot, { label: this.$t('chuang-jian-xiang-mu'), to: path }];
      }
      if (/^\/ticket\/[^/]+$/.test(path)) {
        return [ticketRoot, { label: this.$t('gong-dan-xiang-qing'), to: this.$route.fullPath }];
      }
      if (path === '/datasource/add') {
        const dsType = this.$route.query.dsType;
        const instanceId = this.$route.query.instanceId;
        const isEditMode = this.$route.query.mode === 'edit';
        const dsDisplayName = this.dataSourceDisplayName(dsType);
        const actionLabel = isEditMode ? this.$t('bian-ji') : this.$t('xin-zeng-shu-ju-yuan');
        const actionBreadcrumb = isEditMode
          ? { label: actionLabel, to: '/datasource' }
          : { label: actionLabel, event: EVENT_BUS_NAME_LIST.SHOW_ADD_DATASOURCE_TYPE_MODAL };
        const breadcrumbs = [
          {
            label: this.$t('nav-shu-ju-ku-guan-li'),
            to: '/datasource'
          },
          actionBreadcrumb
        ];
        if (dsDisplayName) {
          breadcrumbs.push({
            label: isEditMode && instanceId ? `${dsDisplayName}(${instanceId})` : dsDisplayName,
            to: ''
          });
        }
        return breadcrumbs;
      }
      if (/^\/cicd\/[^/]+\/release-flow\/add$/.test(path)) {
        const flowId = this.$route.params.id;
        return [cicdRoot, flowDetail(flowId), { label: this.$t('tian-jia-git-ops'), to: path }];
      }
      if (/^\/cicd\/[^/]+\/config$/.test(path)) {
        const flowId = this.$route.params.id;
        return [cicdRoot, flowDetail(flowId), { label: this.$t('cicd-pei-zhi-xiang'), to: path }];
      }
      if (/^\/cicd\/[^/]+$/.test(path)) {
        return [cicdRoot, { label: this.$t('cicd-bian-geng-liu-xiang-qing'), to: path }];
      }
      if (path === '/data-access/cluster' || path === '/data-access/cluster/') {
        return [{ ...machineRoot, to: this.$route.fullPath }];
      }
      if (/^\/data-access\/cluster\/list\/[^/]+$/.test(path)) {
        return [machineRoot, { label: this.$t('ji-qi-lie-biao'), to: this.$route.fullPath }];
      }
      if (path === '/manager/role' || path === '/manager/role/') {
        return [{ ...roleRoot, to: this.$route.fullPath }];
      }
      if (path === '/manager/role/create') {
        return [roleRoot, { label: this.$t('chuang-jian-jiao-se'), to: this.$route.fullPath }];
      }
      if (/^\/manager\/role\/[^/]+\/edit$/.test(path)) {
        return [roleRoot, { label: this.$t('bian-ji-jue-se'), to: this.$route.fullPath }];
      }
      if (/^\/manager\/role\/[^/]+\/view$/.test(path)) {
        return [roleRoot, { label: this.$t('cha-kan-jue-se'), to: this.$route.fullPath }];
      }
      if (path === '/integrations/git/create') {
        return [
          { label: this.$t('nav-git-ops'), to: '/integrations/git' },
          { label: this.$t('xin-zeng'), to: path }
        ];
      }
      if (/^\/integrations\/git\/[^/]+\/edit$/.test(path)) {
        return [
          { label: this.$t('nav-git-ops'), to: '/integrations/git' },
          { label: this.$t('bian-ji'), to: path }
        ];
      }
      if (path === '/integrations/im/create') {
        return [
          { label: this.$t('nav-webhook'), to: '/integrations/im' },
          { label: this.$t('xin-zeng'), to: path }
        ];
      }
      if (/^\/integrations\/im\/[^/]+\/edit$/.test(path)) {
        return [
          { label: this.$t('nav-webhook'), to: '/integrations/im' },
          { label: this.$t('bian-ji'), to: path }
        ];
      }
      if (path === '/integrations/sso/create') {
        return [
          { label: this.$t('nav-sso'), to: '/integrations/sso' },
          { label: this.$t('xin-zeng'), to: path }
        ];
      }
      if (/^\/integrations\/sso\/[^/]+\/edit$/.test(path)) {
        return [
          { label: this.$t('nav-sso'), to: '/integrations/sso' },
          { label: this.$t('pei-zhi'), to: path }
        ];
      }
      if (path === '/integrations/approval/create') {
        return [
          { label: this.$t('nav-shen-pi-yin-qing'), to: '/integrations/approval' },
          { label: this.$t('xin-zeng'), to: path }
        ];
      }
      if (/^\/integrations\/approval\/[^/]+\/edit$/.test(path)) {
        return [
          { label: this.$t('nav-shen-pi-yin-qing'), to: '/integrations/approval' },
          { label: this.$t('pei-zhi'), to: path }
        ];
      }
      if (this.pageSubTitle) {
        return [
          { label: this.pageTitle, to: path },
          { label: this.pageSubTitle, to: path }
        ];
      }
      return [{ label: this.pageTitle, to: path }];
    }
  },
  methods: {
    handleGoSql() {
      saveLastWorkbenchRoute(this.$route, this.userInfo?.uid);
      this.$router.push({ path: '/sql' }).catch(() => {});
    },
    handleBreadcrumbEvent(eventName) {
      if (!eventName) {
        return;
      }
      this.$bus.emit(eventName);
    },
    dataSourceDisplayName(dsType) {
      if (!dsType) {
        return '';
      }
      return findDsSupportName(dsType, this.dmGlobalSetting?.dsSupportNames)?.displayName || dsType;
    }
  }
};
</script>

<style lang="less" scoped>
.app-content-header__title {
  display: inline-flex;
  align-items: center;
  gap: 12px;
  min-width: 0;
  flex-wrap: wrap;
}

.app-content-header__crumb {
  color: #4b5563;
  font-size: 18px;
  font-weight: 600;
  line-height: 24px;
  text-decoration: none;
  transition: color 0.18s ease;

  &:hover {
    color: #0fac69;
  }

  &.is-current {
    color: #1f2937;
  }
}

.app-content-header__crumb-button {
  border: 0;
  padding: 0;
  background: transparent;
  cursor: pointer;
  font-family: inherit;
  appearance: none;
}

.app-content-header__separator {
  color: #6b7280;
  font-weight: 600;
}
</style>
