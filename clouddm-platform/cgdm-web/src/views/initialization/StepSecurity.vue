<template>
  <div class="step-security">
    <h3>{{ $t('initialization.step.security') }}</h3>
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
      const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789' + '!@#$%^&*()_+-=[]{}|;:,.<>?';
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
.step-security h3 {
  margin-bottom: 24px;
}
.step-security-form :deep(.ant-form-item) {
  display: flex;
  align-items: flex-start;
  width: 100%;
}
.step-security-form :deep(.ant-form-item-row) {
  display: flex;
  width: 100%;
}
.step-security-form :deep(.ant-form-item-label) {
  flex: 0 0 120px;
  max-width: 120px;
  padding-right: 12px;
  text-align: left;
  line-height: 32px;
}
.step-security-form :deep(.ant-form-item-label > label) {
  display: inline-flex;
  align-items: center;
  justify-content: flex-start;
  min-height: 32px;
  white-space: normal;
  text-align: left;
}
.step-security-form :deep(.ant-form-item-required::before) {
  display: none !important;
}
.step-security-form :deep(.ant-form-item-control-wrapper) {
  flex: 1;
  max-width: calc(100% - 120px);
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
</style>
