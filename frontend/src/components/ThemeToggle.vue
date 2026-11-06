<template>
  <ThemeDropdown v-model:open="open" :trigger="['click']" placement="bottomRight">
    <button
      type="button"
      ref="trigger"
      class="theme-toggle"
      :aria-label="label"
      :title="label"
      aria-haspopup="menu"
      :aria-expanded="open"
      @keydown.down.prevent="focusMenu"
      @keydown.esc="open = false"
    >
      <ThemeIcon :mode="themeMode" />
    </button>
    <template #overlay>
      <ThemeMenu ref="menu" class="theme-menu" @keydown="navigateMenu" :selected-keys="[themeMode]" @click="selectTheme">
        <ThemeMenuItem
          v-for="mode in modes"
          :key="mode"
          role="menuitemradio"
          :aria-checked="themeMode === mode"
          :aria-label="$t(`appearance-${mode}`)"
          :title="$t(`appearance-${mode}`)"
          class="theme-option"
        >
          <ThemeIcon :mode="mode" />
        </ThemeMenuItem>
      </ThemeMenu>
    </template>
  </ThemeDropdown>
</template>

<script>
import { mapState } from 'vuex';
import ThemeIcon from './ThemeIcon.vue';
import { Dropdown as ThemeDropdown, Menu as ThemeMenu, MenuItem as ThemeMenuItem } from 'ant-design-vue';

export default {
  name: 'ThemeToggle',
  components: { ThemeDropdown, ThemeMenu, ThemeMenuItem, ThemeIcon },
  data() {
    return { open: false, modes: ['light', 'dark', 'system'] };
  },
  computed: {
    ...mapState(['themeMode']),
    label() {
      return this.$t('appearance-menu-label', { mode: this.$t(`appearance-${this.themeMode}`) });
    }
  },
  methods: {
    focusMenu() {
      if (this.open) {
        this.$refs.menu.$el.querySelector('[aria-checked="true"]').focus();
      } else {
        this.open = true;
      }
    },
    navigateMenu(event) {
      if (event.key === 'Enter') {
        event.preventDefault();
        return;
      }

      if (event.key === 'Escape') {
        this.open = false;
        this.$refs.trigger.focus();
        return;
      }

      if (!['ArrowDown', 'ArrowUp'].includes(event.key)) return;

      event.preventDefault();
      const options = [...this.$refs.menu.$el.querySelectorAll('[role="menuitemradio"]')];
      const index = options.indexOf(event.target.closest('[role="menuitemradio"]'));
      let next = index + 1;
      if (event.key === 'ArrowUp') next = index - 1;

      options[(next + options.length) % options.length].focus();
    },
    selectTheme({ key }) {
      this.$store.dispatch('setTheme', key);
      this.open = false;
      this.$refs.trigger.focus();
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
  color: var(--text-primary);
  background: transparent;
  cursor: pointer;
}

.theme-toggle:hover {
  background: var(--bg-hover);
}

.theme-toggle:focus-visible {
  outline: 2px solid var(--info-color);
  outline-offset: 2px;
}

.theme-menu :deep(.theme-option) {
  min-width: 36px;
  padding-inline: 12px;
  text-align: center;
  color: var(--text-primary);
}

.theme-menu :deep(.theme-option svg) {
  display: block;
  margin: 7px auto;
}
</style>
