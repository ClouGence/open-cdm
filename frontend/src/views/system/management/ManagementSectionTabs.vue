<template>
  <nav class="management-section-tabs">
    <router-link
      v-for="tab in tabs"
      :key="tab.name"
      :to="tab.to"
      class="management-section-tabs__item"
      :class="{ 'is-active': activeName === tab.name }"
    >
      {{ tab.label }}
    </router-link>
  </nav>
</template>

<script>
export default {
  name: 'ManagementSectionTabs',
  props: {
    tabs: {
      type: Array,
      required: true
    }
  },
  computed: {
    activeName() {
      const matched = this.$route.matched.find((record) => record.meta && record.meta.managementTab);
      if (matched) {
        return matched.meta.managementTab;
      }
      return this.tabs[0] ? this.tabs[0].name : '';
    }
  }
};
</script>

<style lang="less" scoped>
.management-section-tabs {
  display: flex;
  align-items: center;
  gap: 4px;
  flex-shrink: 0;
  padding: 0 20px;
  background: var(--bg-card);

  &__item {
    position: relative;
    padding: 12px 14px 10px;
    color: var(--text-secondary);
    font-size: 13px;
    font-weight: 400;
    line-height: 1.4;
    text-decoration: none;
    border-bottom: none;
    transition: color 0.12s ease;

    &:hover {
      color: var(--text-primary);
      border-bottom: none;
    }

    &.is-active {
      color: var(--text-primary);
      font-weight: 500;

      &::after {
        content: '';
        position: absolute;
        left: 14px;
        right: 14px;
        bottom: 0;
        height: 2px;
        border-radius: 2px 2px 0 0;
        background: var(--primary-color);
      }
    }
  }
}
</style>
