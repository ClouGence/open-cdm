<template>
  <div class="management-layout">
    <div class="management-layout__body">
      <router-view />
    </div>
  </div>
</template>

<script>
import { mapState } from 'vuex';

export default {
  name: 'ManagementLogsLayout',
  computed: {
    ...mapState(['myCatLog']),
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

  &__body {
    flex: 1;
    min-height: 0;
    overflow-x: hidden;
    overflow-y: auto;
  }
}
</style>
