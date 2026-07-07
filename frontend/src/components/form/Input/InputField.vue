<template>
  <Input
    v-model="form[field.field]"
    class="ui-input-field"
    :type="inputType"
    :password="field.type === 'Password'"
    :disabled="disabled"
    :placeholder="placeholder"
    autocomplete="new-password"
  />
</template>

<script>
export default {
  name: 'InputField',
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
    inputType() {
      return this.field.type === 'Password' ? 'password' : 'text';
    },
    placeholder() {
      return this.field.type === 'Password' ? this.$t('mi-ma-yi-yin-cang') : '';
    }
  }
};
</script>

<style lang="less" scoped>
.ui-input-field {
  width: 280px;

  :deep(.ivu-input-wrapper),
  :deep(.ivu-input) {
    width: 100%;
  }
}
</style>
