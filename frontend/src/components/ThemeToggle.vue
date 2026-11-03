<template>
  <div class="theme-toggle" role="radiogroup" :aria-label="label" @keydown="navigate">
    <button
      v-for="mode in modes"
      :key="mode"
      type="button"
      role="radio"
      class="theme-option"
      :aria-checked="themeMode === mode"
      :aria-label="$t(`appearance-${mode}`)"
      :title="$t(`appearance-${mode}`)"
      :tabindex="themeMode === mode ? 0 : -1"
      @click="selectTheme(mode)"
    >
      <ThemeIcon :mode="mode" />
    </button>
  </div>
</template>

<script>
import { mapState } from 'vuex';
import ThemeIcon from './ThemeIcon.vue';

export default {
  name: 'ThemeToggle',
  components: { ThemeIcon },
  data() {
    return { modes: ['light', 'system', 'dark'] };
  },
  computed: {
    ...mapState(['themeMode']),
    label() {
      return this.$t('appearance-menu-label', { mode: this.$t(`appearance-${this.themeMode}`) });
    }
  },
  methods: {
    selectTheme(mode) {
      this.$store.dispatch('setTheme', mode);
    },
    navigate(event) {
      const steps = { ArrowLeft: -1, ArrowUp: -1, ArrowRight: 1, ArrowDown: 1 };
      if (!(event.key in steps) && !['Home', 'End'].includes(event.key)) return;

      event.preventDefault();
      let index = this.modes.indexOf(this.themeMode);
      if (event.key === 'Home') {
        index = 0;
      } else if (event.key === 'End') {
        index = this.modes.length - 1;
      } else {
        index = (index + steps[event.key] + this.modes.length) % this.modes.length;
      }

      this.selectTheme(this.modes[index]);
      event.currentTarget.querySelectorAll('button')[index].focus();
    }
  }
};
</script>

<style scoped>
.theme-toggle {
  display: inline-flex;
  flex-shrink: 0;
  align-items: center;
  gap: 2px;
  padding: 3px;
  border: 1px solid var(--border-primary);
  border-radius: 999px;
  background: var(--bg-primary);
}

.theme-option {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 32px;
  height: 32px;
  padding: 0;
  border: none;
  border-radius: 50%;
  color: var(--text-secondary);
  background: transparent;
  cursor: pointer;
}

.theme-option:hover {
  background: var(--bg-hover);
}

.theme-option[aria-checked='true'] {
  background: #007bff;
  color: #ffffff;
}

.theme-option:focus-visible {
  outline: 2px solid #007bff;
  outline-offset: 2px;
}
</style>
