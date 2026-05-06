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
        <a-button
          v-if="field.propertyKey === 'clougence.init.admin.password'"
          class="security-generate-button"
          size="small"
          type="link"
          @click="generateAdminPassword"
        >
          {{ $t('initialization.generate') }}
        </a-button>
      </a-form-item>
    </a-form>
  </div>
</template>

<script>
function shuffleCharacters(chars) {
  const values = [...chars];
  for (let index = values.length - 1; index > 0; index--) {
    const target = Math.floor(Math.random() * (index + 1));
    [values[index], values[target]] = [values[target], values[index]];
  }
  return values.join('');
}

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
      const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789' + '!@#$%^&*()_+-=[]{}|;:,.<>?';
      let result = '';
      for (let i = 0; i < 64; i++) {
        result += chars.charAt(Math.floor(Math.random() * chars.length));
      }
      this.$emit('update:formValues', { 'jwt.secret': result });
    },
    generateAdminPassword() {
      const upper = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
      const lower = 'abcdefghijklmnopqrstuvwxyz';
      const digits = '0123456789';
      const symbols = '!@#$%^&*()_+-=[]{}|;:,.<>?';
      const allChars = upper + lower + digits + symbols;
      const passwordChars = [
        upper.charAt(Math.floor(Math.random() * upper.length)),
        lower.charAt(Math.floor(Math.random() * lower.length)),
        digits.charAt(Math.floor(Math.random() * digits.length)),
        symbols.charAt(Math.floor(Math.random() * symbols.length))
      ];

      while (passwordChars.length < 16) {
        passwordChars.push(allChars.charAt(Math.floor(Math.random() * allChars.length)));
      }

      this.$emit('update:formValues', {
        'clougence.init.admin.password': shuffleCharacters(passwordChars)
      });
    }
  }
};
</script>

<style scoped>
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
.security-generate-button {
  margin-top: 4px;
  padding-left: 0;
}
</style>
