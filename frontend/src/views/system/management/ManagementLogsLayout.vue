<template>
  <div class="management-layout">
    <AppPageTabs
      v-if="availableAuditTypes.length"
      class="management-layout__tabs"
      :model-value="activeAuditType"
      :tabs="availableAuditTypes"
      @change="handleAuditTypeChange"
    />
    <div class="management-layout__body">
      <router-view />
    </div>
  </div>
</template>

<script>
import { mapState } from 'vuex';
import AppPageTabs from '@/components/layout/AppPageTabs';

export default {
  name: 'ManagementLogsLayout',
  components: {
    AppPageTabs
  },
  computed: {
    ...mapState(['myCatLog']),
    activeAuditType() {
      return this.$route.meta.managementTab || '';
    },
    availableAuditTypes() {
      const tabs = [];

      if (this.myCatLog.includes('CAT_RDP_OP_AUDIT')) {
        tabs.push({
          name: 'operation',
          label: this.$t('cao-zuo-shen-ji'),
          to: '/manager/logs'
        });
      }
      if (this.myCatLog.includes('CAT_DM_SQL_AUDIT')) {
        tabs.push({
          name: 'sql',
          label: this.$t('nav-ri-zhi-shen-ji'),
          to: '/manager/logs/sql'
        });
      }

      return tabs;
    }
  },
  watch: {
    availableAuditTypes: {
      handler(tabs) {
        this.ensureValidAuditType(tabs);
      },
      immediate: true
    }
  },
  methods: {
    handleAuditTypeChange(name) {
      const target = this.availableAuditTypes.find((tab) => tab.name === name);
      if (target) {
        this.$router.push(target.to);
      }
    },
    ensureValidAuditType(tabs) {
      if (!tabs.length) {
        return;
      }
      const currentTab = this.$route.meta.managementTab;
      if (tabs.some((tab) => tab.name === currentTab)) {
        return;
      }
      this.$router.replace(tabs[0].to);
    }
  }
};
</script>

<style lang="less" scoped>
.management-layout {
  display: flex;
  flex-direction: column;
  height: 100%;
  min-height: 0;

  &__tabs {
    box-sizing: border-box;
    flex-shrink: 0;
    padding: 0 16px;
  }

  &__body {
    flex: 1;
    min-height: 0;
    overflow: hidden;
  }
}
</style>
