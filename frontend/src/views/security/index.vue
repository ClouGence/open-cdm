<template>
  <div class="security-rule-page">
    <nav class="security-rule-tabs">
      <button
        v-for="tab in sectionTabs"
        :key="tab.name"
        class="security-rule-tabs__item"
        :class="{ 'is-active': activeSection === tab.name }"
        @click="handleSectionClick(tab.name)"
      >
        {{ tab.label }}
      </button>
    </nav>
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

.security-rule-tabs {
  display: flex;
  align-items: center;
  gap: 4px;
  flex-shrink: 0;
  padding: 0;
  background: var(--bg-card);

  &__item {
    position: relative;
    padding: 12px 20px 10px;
    color: var(--text-secondary);
    font-size: 13px;
    font-weight: 400;
    line-height: 1.4;
    border: none;
    background: none;
    cursor: pointer;
    transition: color 0.12s ease;

    &:hover {
      color: var(--text-primary);
    }

    &.is-active {
      color: var(--text-primary);
      font-weight: 500;

      &::after {
        content: '';
        position: absolute;
        left: 20px;
        right: 20px;
        bottom: 0;
        height: 2px;
        border-radius: 2px 2px 0 0;
        background: var(--primary-color);
      }
    }
  }
}

.security-rule-content {
  flex: 1;
  min-height: 0;
}
</style>
