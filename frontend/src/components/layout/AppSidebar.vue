<template>
  <aside class="app-sidebar">
    <div class="app-sidebar-brand" @click="handleGoHome">
      <AppBrandLogo />
    </div>

    <nav class="app-sidebar-nav">
      <div v-if="sidebarMenu.primary.length" class="app-sidebar-section">
        <a
          v-for="item in sidebarMenu.primary"
          :key="item.key"
          :href="item.href"
          class="app-sidebar-item"
          :class="{ 'is-active': activeKey === item.key }"
        >
          <CustomIcon :type="item.iconName" size="16px" />
          <span>{{ item.label }}</span>
        </a>
      </div>

      <template v-if="sidebarMenu.groups.length">
        <div class="app-sidebar-divider" />
        <div class="app-sidebar-section">
          <div v-for="groupItem in sidebarMenu.groups" :key="groupItem.key" class="app-sidebar-group">
            <button
              type="button"
              class="app-sidebar-group-toggle"
              :class="{ 'is-expanded': isGroupExpanded(groupItem.key) }"
              @click="toggleGroup(groupItem.key)"
            >
              <CustomIcon :type="groupItem.iconName" size="16px" />
              <span class="app-sidebar-group-toggle__label">{{ groupItem.label }}</span>
              <span class="app-sidebar-group-toggle__chevron" :class="{ 'is-expanded': isGroupExpanded(groupItem.key) }" />
            </button>
            <div v-show="isGroupExpanded(groupItem.key)" class="app-sidebar-group-body">
              <a
                v-for="child in groupItem.children"
                :key="child.key"
                :href="child.href"
                class="app-sidebar-item app-sidebar-item--depth-1"
                :class="{ 'is-active': activeKey === child.key }"
              >
                <span>{{ child.label }}</span>
              </a>
            </div>
          </div>
        </div>
      </template>
    </nav>
  </aside>
</template>

<script>
import { mapGetters, mapState } from 'vuex';
import AppBrandLogo from '@/components/layout/AppBrandLogo';
import { findSidebarParentKeys } from '@/utils/buildSidebarMenu';

export default {
  name: 'AppSidebar',
  components: { AppBrandLogo },
  emits: ['check-version'],
  data() {
    return {
      expandedGroups: {}
    };
  },
  computed: {
    ...mapGetters(['includesDM', 'isDesktop']),
    ...mapState(['myCatLog', 'userInfo', 'sidebarMenu', 'defaultRedirectUrl']),
    activeKey() {
      const path = this.$route.path;
      if (path.indexOf('/sql') > -1) {
        return 'sql';
      }
      if (path === '/cicd' || path === '/cicd/') {
        return 'cicd';
      }
      if (path.indexOf('/cicd') > -1) {
        return 'cicd';
      }
      if (path === '/ticket' || path === '/ticket/') {
        return 'ticket';
      }
      if (path.indexOf('/ticket') > -1) {
        return 'ticket';
      }
      if (path.indexOf('/datasource') === 0 || path.indexOf('/system/ccdatasource') > -1) {
        return '/datasource';
      }
      if (path === '/env' || path.indexOf('/env/') === 0 || path.indexOf('/system/env') > -1) {
        return '/env';
      }
      if (path.indexOf('/data-access/cluster') === 0 || path.indexOf('/system/dmmachine') > -1) {
        return '/data-access/cluster';
      }
      if (path.indexOf('/data-access/rules') === 0 || path.indexOf('/system/dmrule') > -1) {
        return '/data-access/rules';
      }
      if (path.indexOf('/integrations/im') === 0 || path.indexOf('/system/im') > -1) {
        return '/integrations/im';
      }
      if (path.indexOf('/integrations/git') === 0 || path.indexOf('/system/devops') > -1) {
        return '/integrations/git';
      }
      if (path.indexOf('/integrations/sso') === 0 || path.indexOf('/system/sso') > -1) {
        return '/integrations/sso';
      }
      if (path.indexOf('/settings/profile') === 0 || path.indexOf('/system/profile') > -1) {
        return '/settings/profile';
      }
      if (path.indexOf('/settings/preferences') === 0 || path.indexOf('/system/preference') > -1) {
        return '/settings/preferences';
      }
      if (path.indexOf('/system/permission') > -1) {
        return this.$route.query.type === 'apply' ? '/system/permission/apply' : '/system/permission';
      }
      if (path.indexOf('/manager/role') === 0 || path.indexOf('/system/role') > -1) {
        return '/manager/role';
      }
      if (path.indexOf('/manager/account') === 0 || path.indexOf('/system/account') > -1) {
        return '/manager/account';
      }
      if (path.indexOf('/manager/logs') === 0 || path.indexOf('/system/operation_log') > -1 || path.indexOf('/system/sql_log') > -1) {
        return '/manager/logs';
      }
      if (path.indexOf('/system/management/accounts/role') > -1) {
        return '/manager/role';
      }
      if (path.indexOf('/system/management/accounts') > -1 || path.indexOf('/system/account') > -1) {
        return '/manager/account';
      }
      if (path.indexOf('/system/management/logs') > -1 || path.indexOf('/system/operation_log') > -1 || path.indexOf('/system/sql_log') > -1) {
        return '/manager/logs';
      }
      if (path.indexOf('/system/profile') > -1) {
        return '/settings/profile';
      }
      if (path.indexOf('/system') > -1) {
        const parts = path.split('/').filter(Boolean);
        if (parts.length >= 2) {
          return `/${parts[0]}/${parts[1]}`;
        }
      }
      return '';
    }
  },
  watch: {
    activeKey: {
      handler() {
        this.syncExpandedGroups();
      },
      immediate: true
    },
    sidebarMenu: {
      handler() {
        this.syncExpandedGroups();
      },
      deep: true
    }
  },
  methods: {
    handleGoHome() {
      const target = this.defaultRedirectUrl || '/cicd';
      if (this.$route.path !== target) {
        this.$router.push({ path: target });
      }
    },
    isGroupExpanded(key) {
      return !!this.expandedGroups[key];
    },
    toggleGroup(key) {
      this.expandedGroups = {
        ...this.expandedGroups,
        [key]: !this.isGroupExpanded(key)
      };
    },
    syncExpandedGroups() {
      if (!this.sidebarMenu || !this.activeKey) {
        return;
      }
      const parentKeys = findSidebarParentKeys(this.sidebarMenu, this.activeKey);
      if (!parentKeys.length) {
        return;
      }
      const next = { ...this.expandedGroups };
      parentKeys.forEach((key) => {
        next[key] = true;
      });
      this.expandedGroups = next;
    }
  }
};
</script>
