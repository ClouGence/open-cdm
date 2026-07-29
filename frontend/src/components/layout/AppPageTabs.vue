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
      if (tab.disabled || tab.name === this.modelValue) {
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
  min-height: 44px;
  padding: 0 16px;
  color: var(--text-secondary);
  font-size: 14px;
  font-weight: 500;
  line-height: 1.4;
  cursor: pointer;
  outline: none;
}

.app-page-tabs__tab::after {
  position: absolute;
  right: 16px;
  bottom: 0;
  left: 16px;
  height: 2px;
  background: var(--primary-color);
  content: '';
  opacity: 0;
  transform: scaleX(0.5);
  transition:
    opacity 0.2s ease,
    transform 0.2s ease;
}

.app-page-tabs__tab--active {
  color: var(--primary-color);
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
