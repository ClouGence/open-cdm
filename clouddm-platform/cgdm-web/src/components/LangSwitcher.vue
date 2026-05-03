<template>
  <Dropdown @on-click="handleLangChange" transfer trigger="click">
    <slot name="trigger">
      <span class="lang-trigger">
        <CustomIcon hover-style type="icon-v2-yuyanqiehuan" size="20px" />
      </span>
    </slot>
    <template #list>
      <DropdownMenu>
        <DropdownItem v-for="lang in showOptions" :key="lang" :name="lang" :selected="currentLang === lang" translate="no">
          <div class="lang-item">{{ LANG_I18N[lang] || lang }}</div>
        </DropdownItem>
      </DropdownMenu>
    </template>
  </Dropdown>
</template>

<script>
import { LANG_I18N, LANG_OPTIONS } from '@/const';
import { BASE_LOCALES, setAppLanguage } from '@/i18n';
import CustomIcon from '@/components/function/CustomIcon';

export default {
  name: 'LangSwitcher',
  components: { CustomIcon },
  data() {
    return {
      LANG_I18N,
      LANG_OPTIONS,
      currentLang: localStorage.getItem('lang') || this.$i18n.global.locale.value
    };
  },
  computed: {
    showOptions() {
      return LANG_OPTIONS || BASE_LOCALES;
    }
  },
  methods: {
    async handleLangChange(value) {
      this.currentLang = value;
      await setAppLanguage(value);
    }
  }
};
</script>

<style scoped>
.lang-trigger {
  display: inline-flex;
  align-items: center;
  justify-content: center;
}

.lang-item {
  text-align: center;
  height: 30px;
  line-height: 30px;
}
</style>
