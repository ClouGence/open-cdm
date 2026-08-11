<template>
  <div class="app-page-tabs" role="tablist">
    <div
      v-for="tab in tabs"
      :key="tab.name"
      class="app-page-tabs__tab"
      :class="{
        'app-page-tabs__tab--active': modelValue === tab.name,
        'app-page-tabs__tab--disabled': tab.disabled
      }"
      role="tab"
      :aria-disabled="tab.disabled ? 'true' : 'false'"
      :aria-selected="modelValue === tab.name ? 'true' : 'false'"
      :tabindex="tab.disabled ? -1 : 0"
      @click="selectTab(tab)"
      @keydown.enter.prevent="selectTab(tab)"
      @keydown.space.prevent="selectTab(tab)"
    >
      <slot name="label" :tab="tab">
        {{ tab.label }}
      </slot>
    </div>
  </div>
</template>

<script>
export default {
  name: 'AppPageTabs',
  props: {
    modelValue: {
      type: String,
      required: true
    },
    tabs: {
      type: Array,
      required: true
    }
  },
  emits: ['update:modelValue', 'change'],
  methods: {
    selectTab(tab) {
      if (tab.disabled) {
        return;
      }
      if (tab.actionOnly) {
        this.$emit('change', tab.name);
        return;
      }
      if (tab.name === this.modelValue) {
        return;
      }
      this.$emit('update:modelValue', tab.name);
      this.$emit('change', tab.name);
    }
  }
};
</script>

<style lang="less" scoped>
.app-page-tabs {
  display: flex;
  align-items: stretch;
  gap: 4px;
  width: 100%;
  overflow-x: auto;
  overflow-y: hidden;
  scrollbar-width: none;
}

.app-page-tabs::-webkit-scrollbar {
  display: none;
}

.app-page-tabs__tab {
  position: relative;
  display: inline-flex;
  flex: 0 0 auto;
  align-items: center;
  padding: 12px 20px 10px;
  color: var(--text-secondary);
  font-size: 13px;
  font-weight: 400;
  line-height: 1.4;
  white-space: nowrap;
  cursor: pointer;
  outline: none;
  transition: color 0.12s ease;
}

.app-page-tabs__tab::after {
  position: absolute;
  right: 20px;
  bottom: 0;
  left: 20px;
  height: 2px;
  border-radius: 2px 2px 0 0;
  background: var(--primary-color);
  content: '';
  opacity: 0;
  transform: scaleX(0.5);
  transition:
    opacity 0.2s ease,
    transform 0.2s ease;
}

.app-page-tabs__tab:hover {
  color: var(--text-primary);
}

.app-page-tabs__tab--active {
  color: var(--text-primary);
  font-weight: 500;
}

.app-page-tabs__tab--active::after {
  opacity: 1;
  transform: scaleX(1);
}

.app-page-tabs__tab--disabled {
  color: var(--text-disabled);
  cursor: not-allowed;
}

.app-page-tabs__tab:focus-visible {
  outline: 1px solid var(--primary-color);
  outline-offset: -2px;
}
</style>
