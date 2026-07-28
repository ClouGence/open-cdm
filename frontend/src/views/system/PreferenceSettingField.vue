<template>
  <div class="preference-setting-field">
    <i-switch
      v-if="field.widget === 'switch'"
      v-model="innerValue"
      :disabled="disabled"
      true-color="var(--primary-color)"
      :aria-label="$t(field.labelKey)"
    />

    <Select
      v-else-if="field.widget === 'select'"
      v-model="innerValue"
      :disabled="disabled"
      :placeholder="field.placeholderKey ? $t(field.placeholderKey) : ''"
      transfer
    >
      <Option v-for="option in field.options" :key="option.value" :value="option.value">
        {{ $t(option.labelKey) }}
      </Option>
    </Select>

    <div v-else-if="field.widget === 'number'" class="preference-setting-field__number">
      <InputNumber v-model="innerValue" :disabled="disabled" :min="field.min" :max="field.max" :step="1" :precision="0" style="width: 100%" />
      <span v-if="field.unitKey" class="preference-setting-field__unit">{{ $t(field.unitKey) }}</span>
    </div>

    <Input v-else v-model="innerValue" :disabled="disabled" :placeholder="field.placeholderKey ? $t(field.placeholderKey) : ''" />

    <div v-if="field.specialValueKey" class="preference-setting-field__special">
      {{ $t(field.specialValueKey, [field.specialValue]) }}
    </div>
  </div>
</template>

<script>
export default {
  name: 'PreferenceSettingField',
  props: {
    field: {
      type: Object,
      required: true
    },
    modelValue: {
      type: [String, Number, Boolean],
      default: null,
      required: false
    },
    disabled: Boolean
  },
  emits: ['update:modelValue'],
  computed: {
    innerValue: {
      get() {
        return this.modelValue;
      },
      set(value) {
        this.$emit('update:modelValue', value);
      }
    }
  }
};
</script>

<style lang="less" scoped>
.preference-setting-field {
  width: 100%;
}

.preference-setting-field__number {
  display: flex;
  align-items: center;
  gap: 12px;
}

.preference-setting-field__unit {
  flex: 0 0 52px;
  color: var(--text-secondary);
  font-size: 13px;
}

.preference-setting-field__special {
  margin-top: 6px;
  color: var(--text-tertiary);
  font-size: 12px;
  line-height: 1.5;
}
</style>
