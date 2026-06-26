<template>
  <div class="check-field" :class="{ 'check-field-disabled': disabled }" @click="handleWrapperClick">
    <Checkbox v-model="checkedValue" :true-value="true" :false-value="false" :disabled="disabled" />
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
  computed: {
    checked() {
      return this.booleanValue(this.form[this.field.field]);
    },
    checkedValue: {
      get() {
        return this.checked;
      },
      set(value) {
        this.setChecked(value);
      }
    }
  },
  methods: {
    booleanValue(value) {
      if (typeof value === 'boolean') {
        return value;
      }
      if (typeof value === 'string') {
        return value.toLowerCase() === 'true';
      }
      return !!value;
    },
    setChecked(value) {
      if (this.disabled) {
        return;
      }
      this.form[this.field.field] = this.booleanValue(value);
    },
    handleWrapperClick(event) {
      if (this.disabled) {
        return;
      }
      if (event.target && event.target.closest) {
        if (event.target.closest('a') || event.target.closest('.ivu-checkbox-wrapper')) {
          return;
        }
      }
      this.setChecked(!this.checked);
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
