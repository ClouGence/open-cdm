<template>
  <Select
    ref="select"
    :model-value="value"
    :placeholder="placeholder || $t('qing-xuan-ze-shu-ju-ku-shi-li')"
    placement="bottom-start"
    transfer
    transfer-class-name="release-flow-select-dropdown"
    events-enabled
    filterable
    :not-found-text="$t('zan-wu-shu-ju')"
    :disabled="disabled"
    @on-change="handleChange"
    @on-open-change="handleOpenChange"
  >
    <Option v-for="instance in options" :key="instance.objId" :value="instance.objId" :label="instance.objName">
      <span class="release-flow-instance-option">
        <CustomIcon :type="instance.objAttr?.dsType" size="18px" />
        <span>{{ instance.objName }}</span>
      </span>
    </Option>
  </Select>
</template>

<script>
export default {
  name: 'ReleaseFlowInstanceSelect',
  props: {
    value: { type: [String, Number], default: '' },
    options: { type: Array, default: () => [] },
    placeholder: { type: String, default: '' },
    disabled: { type: Boolean, default: false }
  },
  emits: ['input', 'change', 'open-change'],
  methods: {
    handleChange(instanceId) {
      this.$emit('input', instanceId);
      this.$emit('change', instanceId);
    },
    handleOpenChange(open) {
      this.$emit('open-change', open, this.$refs.select);
    }
  }
};
</script>

<style scoped>
.release-flow-instance-option {
  display: inline-flex;
  min-width: 0;
  align-items: center;
  gap: 8px;
}

.release-flow-instance-option span {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>
