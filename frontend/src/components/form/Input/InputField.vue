<template>
  <Input v-model="form[field.field]" :type="inputType" :password="field.type === 'Password'" :disabled="disabled" style="width: 280px" />
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
    }
  }
};
</script>
