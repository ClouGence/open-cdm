<template>
  <header class="app-content-header">
    <div class="app-content-header__left">
      <h1 class="app-content-header__title">
        <template v-for="(item, index) in pageBreadcrumbs" :key="`${item.label}-${index}`">
          <router-link v-if="item.to" class="app-content-header__crumb" :class="{ 'is-current': index === pageBreadcrumbs.length - 1 }" :to="item.to">
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
      <a v-if="showSqlLink" href="/#/sql" class="app-content-header__link">
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

export default {
  name: 'AppContentHeader',
  components: { AppUserActions },
  emits: ['check-version'],
  computed: {
    ...mapGetters(['includesDM', 'isDesktop']),
    ...mapState(['myCatLog', 'mySystemMenuItems', 'sidebarMenu']),
    showSqlLink() {
      return this.includesDM && this.myCatLog.includes('CAT_DM_CONSOLE') && this.$route.path.indexOf('/sql') === -1;
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
      if (path.indexOf('/datasource') === 0 || path.indexOf('/system/ccdatasource') > -1) {
        return this.$t('nav-shu-ju-ku-guan-li');
      }
      if (path === '/env' || path.indexOf('/env/') === 0 || path.indexOf('/system/env') > -1) {
        return this.$t('huan-jing');
      }
      if (
        path.indexOf('/manager/account') === 0 ||
        path.indexOf('/system/management/accounts') > -1 ||
        path.indexOf('/system/account') > -1 ||
        path.indexOf('/system/role') > -1
      ) {
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
      if (path.indexOf('/data-access/rules') === 0 || path.indexOf('/system/dmrule') > -1) {
        return this.$t('an-quan-gui-ze');
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
      if (/^\/cicd\/[^/]+\/change-records$/.test(path)) {
        return this.$t('bian-geng-ji-lu');
      }
      if (/^\/cicd\/[^/]+$/.test(path)) {
        return this.$t('cicd-bian-geng-liu-gai-lan');
      }

      return '';
    },
    pageBreadcrumbs() {
      const path = this.$route.path;
      const cicdRoot = { label: this.$t('nav-ci-cd'), to: '/cicd' };
      const flowDetail = (flowId) => ({
        label: this.$t('cicd-bian-geng-liu-xiang-qing'),
        to: flowId ? `/cicd/${flowId}` : ''
      });
      const changeRecords = (flowId) => ({
        label: this.$t('bian-geng-ji-lu'),
        to: flowId ? `/cicd/${flowId}/change-records` : ''
      });

      if (path === '/cicd' || path === '/cicd/') {
        return [cicdRoot];
      }
      if (path === '/cicd/create') {
        return [cicdRoot, { label: this.$t('chuang-jian-xiang-mu'), to: path }];
      }
      if (/^\/cicd\/[^/]+\/release-flow\/add$/.test(path)) {
        const flowId = this.$route.params.id;
        return [cicdRoot, flowDetail(flowId), { label: this.$t('tian-jia-git-ops'), to: path }];
      }
      if (/^\/cicd\/[^/]+\/change-records$/.test(path)) {
        const flowId = this.$route.params.id;
        return [cicdRoot, flowDetail(flowId), changeRecords(flowId)];
      }
      if (/^\/cicd\/change\/[^/]+$/.test(path)) {
        const flowId = this.$route.query.flowId;
        return [cicdRoot, flowDetail(flowId), changeRecords(flowId), { label: this.$t('ji-lu-xiang-qing'), to: this.$route.fullPath }];
      }
      if (/^\/cicd\/[^/]+$/.test(path)) {
        return [cicdRoot, { label: this.$t('cicd-bian-geng-liu-xiang-qing'), to: path }];
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

.app-content-header__separator {
  color: #6b7280;
  font-weight: 600;
}
</style>
