<template>
  <div class="check-field" :class="{ 'check-field-disabled': disabled }" @click="toggle">
    <Checkbox v-model="form[field.field]" :disabled="disabled" @click.stop />
    <span v-if="field.descI18N" class="check-field-desc" v-html="field.descI18N"></span>
  </div>
</template>

<script>
export default {
  name: 'CheckField',
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
  methods: {
    toggle(event) {
      if (this.disabled) {
        return;
      }
      if (event.target && event.target.closest && event.target.closest('a')) {
        return;
      }
      this.form[this.field.field] = !this.form[this.field.field];
    }
  }
};
</script>

<style lang="less" scoped>
.check-field {
  display: inline-flex;
  align-items: center;
  min-height: 32px;
  cursor: pointer;
  user-select: none;
}

.check-field-disabled {
  cursor: not-allowed;
}

.check-field-desc {
  margin-left: 8px;
  color: #808695;
  white-space: nowrap;
}
</style>
