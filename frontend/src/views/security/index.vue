<template>
  <div class="security-rule-page">
    <div class="security-rule-tabs">
      <Tabs v-model="activeSection" @on-click="handleSectionClick">
        <TabPane :label="$t('an-quan-gui-fan')" name="security"></TabPane>
        <TabPane :label="$t('gui-ze-mo-ban')" name="template"></TabPane>
      </Tabs>
    </div>
    <div class="security-rule-content">
      <SpecList v-if="activeSection === 'security'" />
      <RuleList v-if="activeSection === 'template'" />
    </div>
  </div>
</template>

<script>
import SpecList from '@/views/security/spec/index';
import RuleList from '@/views/security/rule/index';

export default {
  name: 'SecurityRules',
  components: {
    SpecList,
    RuleList
  },
  data() {
    return {
      activeSection: 'security'
    };
  },
  watch: {
    '$route.query': {
      handler() {
        this.syncActiveSection();
      },
      immediate: true
    }
  },
  methods: {
    syncActiveSection() {
      const { tab, ruleKind } = this.$route.query || {};
      this.activeSection = tab === 'template' || ruleKind ? 'template' : 'security';
    },
    handleSectionClick(name) {
      const query = { ...(this.$route.query || {}), tab: name };
      if (name === 'security') {
        delete query.ruleKind;
      }
      this.$router.replace({
        path: '/data-access/rules',
        query
      });
    }
  }
};
</script>

<style lang="less" scoped>
.security-rule-page {
  height: 100%;
  min-height: 0;
  display: flex;
  flex-direction: column;
}

.security-rule-tabs {
  flex-shrink: 0;
  padding: 0 16px;
  background: var(--bg-card);
  border-bottom: 1px solid var(--border-color-split, #e8eaec);

  :deep(.ivu-tabs-bar) {
    margin-bottom: 0;
    border-bottom: 0;
  }
}

.security-rule-content {
  flex: 1;
  min-height: 0;
}
</style>
