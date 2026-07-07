<template>
  <Input v-model="form[field.field]" type="textarea" :autosize="{ minRows: 2, maxRows: 5 }" :disabled="disabled" style="width: 420px" />
</template>

<script>
export default {
  name: 'TextAreaField',
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
  }
};
</script>
