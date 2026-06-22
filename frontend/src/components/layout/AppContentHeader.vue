<template>
  <header class="app-content-header">
    <div class="app-content-header__left">
      <h1 class="app-content-header__title">{{ pageTitle }}</h1>
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
      if (path.indexOf('/system/profile') > -1) {
        return this.$t('ge-ren-zi-liao');
      }
      if (path.indexOf('/system/permission') > -1) {
        return this.$t('my-permissions');
      }
      if (path.indexOf('/system/ccdatasource') > -1) {
        return this.$t('nav-shu-ju-ku-guan-li');
      }
      if (path.indexOf('/system/env') > -1) {
        return this.$t('huan-jing');
      }
      if (path.indexOf('/system/management/accounts') > -1 || path.indexOf('/system/account') > -1 || path.indexOf('/system/role') > -1) {
        return this.$t('nav-zhang-hu');
      }
      if (path.indexOf('/system/management/logs') > -1 || path.indexOf('/system/operation_log') > -1 || path.indexOf('/system/sql_log') > -1) {
        return this.$t('nav-ri-zhi');
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
    }
  }
};
</script>
