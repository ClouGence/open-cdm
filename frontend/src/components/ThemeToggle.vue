<template>
  <Tooltip :content="label" transfer placement="bottom">
    <button type="button" class="theme-toggle" :aria-label="label" :aria-pressed="isDark" @click="$store.dispatch('toggleTheme')">
      <Icon :type="isDark ? 'ios-sunny-outline' : 'ios-moon-outline'" :size="20" aria-hidden="true" />
    </button>
  </Tooltip>
</template>

<script>
import { mapGetters } from 'vuex';

export default {
  name: 'ThemeToggle',
  computed: {
    ...mapGetters(['isDark']),
    label() {
      return this.$t(this.isDark ? 'appearance-switch-light' : 'appearance-switch-dark');
    }
  }
};
</script>

<style scoped>
.theme-toggle {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 28px;
  height: 28px;
  padding: 0;
  border: none;
  border-radius: 6px;
  color: var(--text-secondary);
  background: transparent;
  cursor: pointer;
}

.theme-toggle:hover {
  color: var(--text-primary);
  background: var(--bg-hover);
}

.theme-toggle:focus-visible {
  outline: 2px solid var(--info-color);
  outline-offset: 2px;
}
</style>
