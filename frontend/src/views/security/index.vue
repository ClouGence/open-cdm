<template>
  <div class="security-rule-page">
    <AppPageTabs :model-value="activeSection" :tabs="sectionTabs" @change="handleSectionClick" />
    <div class="security-rule-content">
      <SpecList v-if="activeSection === 'security'" />
      <RuleList v-if="activeSection === 'template'" />
    </div>
  </div>
</template>

<script>
import AppPageTabs from '@/components/layout/AppPageTabs';
import SpecList from '@/views/security/spec/index';
import RuleList from '@/views/security/rule/index';

export default {
  name: 'SecurityRules',
  components: {
    AppPageTabs,
    SpecList,
    RuleList
  },
  data() {
    return {
      activeSection: 'security',
      sectionTabs: [
        { name: 'security', label: this.$t('an-quan-gui-fan') },
        { name: 'template', label: this.$t('gui-ze-mo-ban') }
      ]
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

.security-rule-content {
  flex: 1;
  min-height: 0;
}
</style>
