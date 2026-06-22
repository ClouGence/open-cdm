<template>
  <div class="step-security">
    <a-form layout="horizontal" class="step-security-form">
      <a-form-item v-for="field in fieldDefs" :key="field.propertyKey" :label="field.label" :required="field.required">
        <a-input
          v-if="field.inputType === 'text'"
          class="security-full-width-control"
          :value="formValues[field.propertyKey] || ''"
          @input="(value) => onChange(field.propertyKey, normalizeInputValue(value))"
          :placeholder="field.description"
        >
          <template v-if="field.propertyKey === 'jwt.secret'" #suffix>
            <a-button size="small" type="link" @click="generateJwt">
              {{ $t('initialization.generate') }}
            </a-button>
          </template>
        </a-input>
        <a-input-password
          v-else-if="field.inputType === 'password'"
          class="security-full-width-control"
          :value="formValues[field.propertyKey] || ''"
          @input="(value) => onChange(field.propertyKey, normalizeInputValue(value))"
          :placeholder="field.description"
        />
      </a-form-item>
    </a-form>
  </div>
</template>

<script>
export default {
  name: 'StepSecurity',
  props: {
    fieldDefs: { type: Array, default: () => [] },
    formValues: { type: Object, default: () => ({}) }
  },
  computed: {
    missingRequiredFields() {
      return this.fieldDefs
        .filter((field) => field.required)
        .filter((field) => !(this.formValues[field.propertyKey] || '').trim())
        .map((field) => field.label);
    }
  },
  watch: {
    missingRequiredFields: {
      immediate: true,
      handler(value) {
        this.$emit('validation-change', value);
      }
    }
  },
  methods: {
    normalizeInputValue(payload) {
      if (payload && typeof payload === 'object' && 'target' in payload) {
        return payload.target ? payload.target.value : '';
      }
      return payload;
    },
    onChange(key, value) {
      this.$emit('update:formValues', { [key]: value });
    },
    generateJwt() {
      const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
      let result = '';
      for (let i = 0; i < 64; i++) {
        result += chars.charAt(Math.floor(Math.random() * chars.length));
      }
      this.$emit('update:formValues', { 'jwt.secret': result });
    }
  }
};
</script>

<style scoped>
.step-security-form :deep(.ant-form-item) {
  display: flex;
  align-items: flex-start;
  width: 100%;
  margin-bottom: 14px;
}
.step-security-form :deep(.ant-form-item-row) {
  display: flex;
  width: 100%;
}
.step-security-form :deep(.ant-form-item-label) {
  flex: 0 0 136px;
  max-width: 136px;
  padding-right: 12px;
  text-align: left;
  line-height: 32px;
}
.step-security-form :deep(.ant-form-item-label > label) {
  display: inline-flex;
  align-items: center;
  justify-content: flex-start;
  position: relative;
  min-height: 32px;
  padding-left: 12px;
  white-space: nowrap;
  text-align: left;
}
.step-security-form :deep(.ant-form-item-required::before) {
  position: absolute;
  left: 0;
  margin-right: 0;
}
.step-security-form :deep(.ant-form-item-control-wrapper) {
  flex: 1;
  max-width: calc(100% - 136px);
}
.step-security-form :deep(.ant-form-item-control) {
  flex: 1 1 0;
  min-width: 0;
}
.step-security-form :deep(.ant-form-item-control-input) {
  flex: 1 1 auto;
  min-width: 0;
}
.security-full-width-control {
  width: 100%;
}

@media (max-width: 768px) {
  .step-security-form :deep(.ant-form-item) {
    flex-direction: column;
    align-items: stretch;
  }

  .step-security-form :deep(.ant-form-item-row) {
    flex-direction: column;
  }

  .step-security-form :deep(.ant-form-item-label) {
    flex: none;
    max-width: 100%;
    width: 100%;
    padding: 0 0 6px;
    line-height: 22px;
  }

  .step-security-form :deep(.ant-form-item-label > label) {
    min-height: 22px;
  }

  .step-security-form :deep(.ant-form-item-control-wrapper) {
    flex: none;
    max-width: 100%;
    width: 100%;
  }

  .step-security-form :deep(.ant-form-item-control),
  .step-security-form :deep(.ant-form-item-control-input),
  .step-security-form :deep(.ant-form-item-control-input-content) {
    flex: none;
    height: auto;
    min-height: 0;
    width: 100%;
  }
}
</style>
