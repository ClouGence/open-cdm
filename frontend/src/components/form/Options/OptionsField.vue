<template>
  <Select
    v-model="selectedValue"
    :multiple="field.type === 'MultipleOptions'"
    :disabled="disabled"
    :allow-create="field.props?.allowCreate === true"
    filterable
    transfer
    style="width: 280px"
  >
    <Option v-for="option in normalizedOptions" :key="option.value" :value="option.value">
      {{ option.label }}
    </Option>
  </Select>
</template>

<script>
const EMPTY_OPTION_VALUE = '__CLOUDDM_UI_EMPTY_OPTION__';

export default {
  name: 'OptionsField',
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
    selectedValue: {
      get() {
        const value = this.form[this.field.field];
        if (!this.hasEmptyOption) {
          return value;
        }
        if (Array.isArray(value)) {
          return value.map((item) => (item === '' ? EMPTY_OPTION_VALUE : item));
        }
        return value === '' ? EMPTY_OPTION_VALUE : value;
      },
      set(value) {
        if (Array.isArray(value)) {
          this.form[this.field.field] = value.map((item) => (item === EMPTY_OPTION_VALUE ? '' : item));
        } else {
          this.form[this.field.field] = value === EMPTY_OPTION_VALUE ? '' : value;
        }
      }
    },
    hasEmptyOption() {
      return (this.field.options || []).some((option) => this.optionValue(option) === '');
    },
    normalizedOptions() {
      return (this.field.options || []).map((option) => {
        if (option && typeof option === 'object') {
          const value = this.optionValue(option);
          return {
            value: value === '' ? EMPTY_OPTION_VALUE : value,
            label: option.label || option.labelI18N || option.securityTypeI18nName || option.value || option.securityType
          };
        }
        return {
          value: option === '' ? EMPTY_OPTION_VALUE : option,
          label: option
        };
      });
    }
  },
  methods: {
    optionValue(option) {
      if (option && typeof option === 'object') {
        return option.value ?? option.securityType;
      }
      return option;
    }
  }
};
</script>
