<template>
  <header class="app-content-header">
    <div class="app-content-header__left">
      <h1 class="app-content-header__title">
        <span v-if="pageBreadcrumbs.length > 2" class="app-content-header__mobile-ellipsis" aria-hidden="true" />
        <template v-for="(item, index) in pageBreadcrumbs" :key="`${item.label}-${index}`">
          <button
            v-if="item.event"
            type="button"
            class="app-content-header__crumb app-content-header__crumb-button"
            :class="{
              'is-current': index === pageBreadcrumbs.length - 1,
              'is-mobile-previous': index === pageBreadcrumbs.length - 2
            }"
            @click="handleBreadcrumbEvent(item.event)"
          >
            {{ item.label }}
          </button>
          <router-link
            v-else-if="item.to"
            class="app-content-header__crumb"
            :class="{
              'is-current': index === pageBreadcrumbs.length - 1,
              'is-mobile-previous': index === pageBreadcrumbs.length - 2
            }"
            :to="item.to"
          >
            {{ item.label }}
          </router-link>
          <span
            v-else
            class="app-content-header__crumb"
            :class="{
              'is-current': index === pageBreadcrumbs.length - 1,
              'is-mobile-previous': index === pageBreadcrumbs.length - 2
            }"
          >
            {{ item.label }}
          </span>
          <span
            v-if="index < pageBreadcrumbs.length - 1"
            class="app-content-header__separator"
            :class="{ 'is-mobile-visible': index === pageBreadcrumbs.length - 2 }"
          >
            /
          </span>
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
    ...mapGetters(['includesDM', 'isDesktop']),
    ...mapState(['dmGlobalSetting', 'myCatLog', 'mySystemMenuItems', 'sidebarMenu', 'userInfo']),
    showSqlLink() {
      return this.includesDM && this.myCatLog.includes('CAT_DM_CONSOLE') && !this.$route.path.startsWith('/sql');
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
    pageBreadcrumbs() {
      if (this.$route.meta.breadcrumbType === 'datasource-form') {
        return this.dataSourceBreadcrumbs();
      }

      const routeBreadcrumbs = this.$route.meta.breadcrumbs;
      if (!Array.isArray(routeBreadcrumbs) || !routeBreadcrumbs.length) {
        return [{ label: this.pageTitle, to: '' }];
      }

      return routeBreadcrumbs.map((item, index) => ({
        label: this.resolveBreadcrumbLabel(item),
        to: index === routeBreadcrumbs.length - 1 ? '' : this.resolveBreadcrumbTarget(item.to),
        event: index === routeBreadcrumbs.length - 1 ? '' : item.event
      }));
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
    resolveBreadcrumbLabel(item) {
      const label = item.queryLabel ? this.$route.query[item.queryLabel] || this.$t(item.labelKey) : this.$t(item.labelKey);
      const value = item.param ? this.$route.params[item.param] : '';
      return value ? `${label} #${value}` : label;
    },
    resolveBreadcrumbTarget(target) {
      return typeof target === 'function' ? target(this.$route) : target || '';
    },
    dataSourceBreadcrumbs() {
      const dsType = this.$route.query.dsType;
      const instanceId = this.$route.query.instanceId;
      const isEditMode = this.$route.query.mode === 'edit';
      const dsDisplayName = this.dataSourceDisplayName(dsType);
      const actionBreadcrumb = isEditMode
        ? { label: this.$t('bian-ji'), to: '/datasource' }
        : { label: this.$t('xin-zeng-shu-ju-yuan'), event: EVENT_BUS_NAME_LIST.SHOW_ADD_DATASOURCE_TYPE_MODAL };
      const breadcrumbs = [{ label: this.$t('nav-shu-ju-ku-guan-li'), to: '/datasource' }, actionBreadcrumb];
      if (dsDisplayName) {
        breadcrumbs.push({ label: isEditMode && instanceId ? `${dsDisplayName} (${instanceId})` : dsDisplayName, to: '' });
      }
      return breadcrumbs.map((item, index) => ({
        ...item,
        to: index === breadcrumbs.length - 1 ? '' : item.to,
        event: index === breadcrumbs.length - 1 ? '' : item.event
      }));
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
  display: flex;
  align-items: center;
  gap: 8px;
  width: 100%;
  min-width: 0;
  flex-wrap: nowrap;
}

.app-content-header__crumb {
  flex-shrink: 0;
  color: var(--text-secondary);
  font-size: 14px;
  font-weight: 500;
  line-height: 24px;
  white-space: nowrap;
  text-decoration: none;
  transition: color 0.18s ease;

  &:hover {
    color: #0fac69;
  }

  &.is-current {
    flex: 1 1 auto;
    min-width: 0;
    overflow: hidden;
    color: var(--text-primary);
    font-size: 18px;
    font-weight: 600;
    text-overflow: ellipsis;

    &:hover {
      color: var(--text-primary);
    }
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
  flex-shrink: 0;
  color: var(--text-tertiary);
  font-size: 13px;
  font-weight: 500;
}

.app-content-header__mobile-ellipsis {
  display: none;
  flex-shrink: 0;
  color: var(--text-tertiary);
  font-size: 14px;

  &::before {
    content: '\2026';
  }
}

@media (max-width: 767px) {
  .app-content-header__crumb:not(.is-current):not(.is-mobile-previous),
  .app-content-header__separator:not(.is-mobile-visible) {
    display: none;
  }

  .app-content-header__mobile-ellipsis {
    display: inline;
  }
}
</style>
