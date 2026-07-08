<template>
  <a-input-password
    v-if="isPasswordField"
    class="dm-input"
    v-bind="passwordInputAttrs"
    :value="innerValue"
    :allow-clear="clearable"
    @update:value="handleInput"
    @pressEnter="handleEnter"
  />
  <a-input
    v-else
    class="dm-input"
    v-bind="inputAttrs"
    :value="innerValue"
    :allow-clear="clearable"
    @update:value="handleInput"
    @pressEnter="handleEnter"
  />
</template>

<script>
export default {
  name: 'DmInput',
  inheritAttrs: false,
  props: {
    modelValue: {
      type: [String, Number],
      default: undefined
    },
    value: {
      type: [String, Number],
      default: undefined
    },
    clearable: Boolean,
    password: Boolean,
    type: {
      type: String,
      default: 'text'
    }
  },
  emits: ['update:modelValue', 'update:value', 'input', 'on-enter', 'on-clear'],
  computed: {
    innerValue() {
      if (this.modelValue !== undefined && this.modelValue !== null) {
        return this.modelValue;
      }
      if (this.value !== undefined && this.value !== null) {
        return this.value;
      }
      return '';
    },
    isPasswordField() {
      return this.password || this.type === 'password';
    },
    passwordInputAttrs() {
      const attrs = { ...this.$attrs };
      delete attrs.password;
      delete attrs.type;
      return attrs;
    },
    inputAttrs() {
      const attrs = { ...this.$attrs };
      delete attrs.password;
      if (this.type === 'textarea') {
        attrs.type = 'text';
      } else if (this.type) {
        attrs.type = this.type;
      }
      return attrs;
    }
  },
  methods: {
    handleInput(val) {
      this.$emit('update:modelValue', val);
      this.$emit('update:value', val);
      this.$emit('input', val);
      if (this.clearable && (val === '' || val === null || val === undefined)) {
        this.$emit('on-clear');
      }
    },
    handleEnter() {
      this.$emit('on-enter');
    }
  }
};
</script>
