<template>
  <Button size="small" class="transaction-control-button">
    <Dropdown trigger="click" placement="bottom-start" @on-click="handleSet" transfer>
      {{ currentModeLabel }}
      <Icon type="ios-arrow-down"></Icon>
      <template #list>
        <DropdownMenu>
          <DropdownItem disabled>
            {{ $t('shi-wu') }}
          </DropdownItem>
          <DropdownItem
            v-for="option in modeOptions"
            :key="option.value"
            :name="option.value"
            :selected="currentMode === option.value"
            :disabled="disabled"
          >
            <div v-show="currentMode === option.value">
              <CustomIcon type="icon-v2-seleted" size="12px" />
              <span style="padding-left: 11px">{{ option.label }}</span>
            </div>
            <div v-show="currentMode !== option.value">
              <span style="padding-left: 23px">{{ option.label }}</span>
            </div>
          </DropdownItem>
          <DropdownItem disabled>
            {{ $t('ge-li-ji-bie') }}
          </DropdownItem>
          <DropdownItem
            v-for="option in isolationOptions"
            :key="option.value"
            :name="option.value"
            :selected="currentIsolation === option.value"
            :disabled="disabled"
          >
            <div v-show="currentIsolation === option.value">
              <CustomIcon type="icon-v2-seleted" size="12px" />
              <span style="padding-left: 11px">{{ option.label }}</span>
            </div>
            <div v-show="currentIsolation !== option.value">
              <span style="padding-left: 23px">{{ option.label }}</span>
            </div>
          </DropdownItem>
        </DropdownMenu>
      </template>
    </Dropdown>
  </Button>
</template>

<script>
export default {
  name: 'TransactionControlField',
  props: {
    field: {
      type: Object,
      required: true
    },
    form: {
      type: Object,
      required: true
    },
    disabled: {
      type: Boolean,
      default: false
    }
  },
  computed: {
    modeOptions() {
      return [
        { value: 'txAuto', label: this.$t('zi-dong') },
        { value: 'txManual', label: this.$t('shou-dong') }
      ];
    },
    isolationOptions() {
      return this.normalizedOptions;
    },
    currentMode() {
      return String(this.form.autoCommit) === 'false' ? 'txManual' : 'txAuto';
    },
    currentModeLabel() {
      const matched = this.modeOptions.find((option) => option.value === this.currentMode);
      if (this.currentMode !== 'txManual') {
        return matched ? matched.label : this.$t('zi-dong');
      }
      return `${matched ? matched.label : this.$t('shou-dong')} (${this.currentIsolationLabel})`;
    },
    currentIsolation() {
      return this.form.isolation || 'DEFAULT';
    },
    currentIsolationLabel() {
      const matched = this.isolationOptions.find((option) => option.value === this.currentIsolation);
      return matched ? matched.label : this.currentIsolation;
    },
    normalizedOptions() {
      return (this.field.options || []).map((option) => ({
        value: option.value,
        label: option.label || option.labelI18N || option.value
      }));
    }
  },
  created() {
    this.initDefaults();
  },
  methods: {
    initDefaults() {
      const defaults = this.field.defaultValue || {};
      if (!Object.prototype.hasOwnProperty.call(this.form, 'autoCommit')) {
        this.form.autoCommit = defaults.autoCommit ?? 'true';
      }
      if (!Object.prototype.hasOwnProperty.call(this.form, 'isolation')) {
        this.form.isolation = defaults.isolation ?? 'DEFAULT';
      }
    },
    handleSet(value) {
      if (value === 'txAuto') {
        this.form.autoCommit = 'true';
        this.form.isolation = 'DEFAULT';
      } else if (value === 'txManual') {
        this.applyIsolation('DEFAULT');
      } else {
        this.applyIsolation(value);
      }
    },
    applyIsolation(value) {
      this.form.autoCommit = 'false';
      this.form.isolation = value;
    }
  }
};
</script>

<style lang="less" scoped>
.transaction-control-button {
  display: inline-flex;
  align-items: center;
}
</style>
