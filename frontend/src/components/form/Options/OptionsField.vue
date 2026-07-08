<template>
  <Select v-model="form[field.field]" :multiple="field.type === 'MultipleOptions'" :disabled="disabled" filterable transfer style="width: 280px">
    <Option v-for="option in normalizedOptions" :key="option.value" :value="option.value">
      {{ option.label }}
    </Option>
  </Select>
</template>

<script>
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
    normalizedOptions() {
      return (this.field.options || []).map((option) => {
        if (option && typeof option === 'object') {
          return {
            value: option.value ?? option.securityType,
            label: option.label || option.labelI18N || option.securityTypeI18nName || option.value || option.securityType
          };
        }
        return {
          value: option,
          label: option
        };
      });
    }
  }
};
</script>
