<template>
  <div class="tree-node-label-wrap">
    <Tooltip :content="text" placement="top" transfer :disabled="!truncated">
      <div
        ref="labelRef"
        class="tree-node-label"
        :style="labelStyle"
        v-html="html || text"
        @mouseenter="checkTruncated"
        @mouseleave="truncated = false"
      ></div>
    </Tooltip>
  </div>
</template>

<script>
export default {
  name: 'TreeNodeLabel',
  props: {
    text: {
      type: String,
      required: true
    },
    html: {
      type: String,
      default: ''
    },
    labelStyle: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      truncated: false
    };
  },
  methods: {
    checkTruncated() {
      const el = this.$refs.labelRef;
      if (!el) {
        return;
      }
      this.truncated = el.scrollWidth > el.clientWidth;
    }
  }
};
</script>

<style scoped lang="less">
.tree-node-label-wrap {
  flex: 1 1 0%;
  width: 0;
  min-width: 0;
  overflow: hidden;

  :deep(.ivu-tooltip) {
    display: block !important;
    width: 100%;
    max-width: 100%;
  }

  :deep(.ivu-tooltip-rel) {
    display: block;
    width: 100%;
    max-width: 100%;
    min-width: 0;
    overflow: hidden;
  }
}

.tree-node-label {
  display: block;
  width: 100%;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>
