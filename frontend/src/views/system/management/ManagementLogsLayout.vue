<template>
  <div class="management-layout">
    <AppPageTabs v-if="availableAuditTypes.length" :model-value="activeAuditType" :tabs="availableAuditTypes" @change="handleChangeAuditType" />
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
  components: { AppPageTabs },
  computed: {
    ...mapState(['myCatLog']),
    activeAuditType() {
      if (this.$route.meta.managementTab) {
        return this.$route.meta.managementTab;
      }
      return this.availableAuditTypes.length ? this.availableAuditTypes[0].name : '';
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
          label: this.$t('sql-shen-ji'),
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
    handleChangeAuditType(name) {
      const tab = this.availableAuditTypes.find((item) => item.name === name);
      if (tab && tab.to !== this.$route.path) {
        this.$router.push(tab.to);
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

  &__body {
    flex: 1;
    min-height: 0;
    overflow: hidden;
  }
}
</style>
