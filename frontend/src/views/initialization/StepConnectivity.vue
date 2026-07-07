<template>
  <div class="step-connectivity">
    <a-form layout="horizontal" class="step-connectivity-form">
      <a-form-item v-for="field in fieldDefs" :key="field.propertyKey" :label="field.label" :required="field.required">
        <a-input
          v-if="field.inputType === 'text'"
          class="connectivity-full-width-control"
          :value="formValues[field.propertyKey] || ''"
          :disabled="readonly"
          @input="(value) => onChange(field.propertyKey, normalizeInputValue(value))"
          :placeholder="field.description"
        />
        <a-input
          v-else-if="field.inputType === 'number'"
          class="connectivity-full-width-control"
          :value="formValues[field.propertyKey]"
          type="number"
          :disabled="readonly"
          @input="(value) => onChange(field.propertyKey, normalizeInputValue(value))"
          :placeholder="field.description"
        />
      </a-form-item>
    </a-form>
  </div>
</template>

<script>
export default {
  name: 'StepConnectivity',
  props: {
    fieldDefs: { type: Array, default: () => [] },
    formValues: { type: Object, default: () => ({}) },
    readonly: { type: Boolean, default: false }
  },
  methods: {
    normalizeInputValue(payload) {
      if (payload && typeof payload === 'object' && 'target' in payload) {
        return payload.target ? payload.target.value : '';
      }
      return payload;
    },
    onChange(key, value) {
      if (this.readonly) {
        return;
      }
      this.$emit('update:formValues', { [key]: value });
    }
  }
};
</script>

<style scoped>
.step-connectivity-form :deep(.ant-form-item) {
  display: flex;
  align-items: flex-start;
  width: 100%;
  margin-bottom: 14px;
}
.step-connectivity-form :deep(.ant-form-item-row) {
  display: flex;
  width: 100%;
}
.step-connectivity-form :deep(.ant-form-item-label) {
  flex: 0 0 136px;
  max-width: 136px;
  padding-right: 12px;
  text-align: left;
  line-height: 32px;
}
.step-connectivity-form :deep(.ant-form-item-label > label) {
  display: inline-flex;
  align-items: center;
  justify-content: flex-start;
  position: relative;
  min-height: 32px;
  padding-left: 12px;
  white-space: nowrap;
  text-align: left;
}
.step-connectivity-form :deep(.ant-form-item-required::before) {
  position: absolute;
  left: 0;
  margin-right: 0;
}
.step-connectivity-form :deep(.ant-form-item-control-wrapper) {
  flex: 1;
  max-width: calc(100% - 136px);
}
.step-connectivity-form :deep(.ant-form-item-control) {
  flex: 1 1 0;
  min-width: 0;
}
.step-connectivity-form :deep(.ant-form-item-control-input) {
  flex: 1 1 auto;
  min-width: 0;
}
.connectivity-full-width-control {
  width: 100%;
}

@media (max-width: 768px) {
  .step-connectivity-form :deep(.ant-form-item) {
    flex-direction: column;
    align-items: stretch;
  }

  .step-connectivity-form :deep(.ant-form-item-row) {
    flex-direction: column;
  }

  .step-connectivity-form :deep(.ant-form-item-label) {
    flex: none;
    max-width: 100%;
    width: 100%;
    padding: 0 0 6px;
    line-height: 22px;
  }

  .step-connectivity-form :deep(.ant-form-item-label > label) {
    min-height: 22px;
  }

  .step-connectivity-form :deep(.ant-form-item-control-wrapper) {
    flex: none;
    max-width: 100%;
    width: 100%;
  }

  .step-connectivity-form :deep(.ant-form-item-control),
  .step-connectivity-form :deep(.ant-form-item-control-input),
  .step-connectivity-form :deep(.ant-form-item-control-input-content) {
    flex: none;
    height: auto;
    min-height: 0;
    width: 100%;
  }
}
</style>
