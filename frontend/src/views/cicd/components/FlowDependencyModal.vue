<template>
  <CCModal
    :model-value="modelValue"
    :title="$t('cicd-dependency-modal-title')"
    :width="1120"
    footer-hide
    @update:model-value="handleVisibleChange"
    @on-cancel="handleVisibleChange(false)"
  >
    <div v-if="rootFlow" class="flow-dependency-modal">
      <div class="flow-dependency-viewport">
        <div class="flow-dependency-canvas">
          <FlowDependencyNode :flow="rootFlow" @navigate="$emit('navigate', $event)" />
        </div>
      </div>
    </div>
  </CCModal>
</template>

<script>
import FlowDependencyNode from './FlowDependencyNode.vue';

export default {
  name: 'FlowDependencyModal',
  components: { FlowDependencyNode },
  props: {
    modelValue: { type: Boolean, required: true },
    rootFlow: { type: Object, default: null }
  },
  emits: ['update:modelValue', 'navigate'],
  methods: {
    handleVisibleChange(visible) {
      this.$emit('update:modelValue', visible);
    }
  }
};
</script>

<style lang="less" scoped>
.flow-dependency-modal {
  display: flex;
  min-height: 0;
  flex-direction: column;
}

.flow-dependency-viewport {
  min-height: 280px;
  padding: 24px;
  overflow: auto;
  border-radius: 8px;
  background: var(--bg-secondary);
}

.flow-dependency-canvas {
  display: flex;
  width: max-content;
  min-width: 100%;
  min-height: 232px;
  align-items: center;
  justify-content: center;
}

@media (max-width: 767px) {
  .flow-dependency-viewport {
    min-height: 260px;
    max-height: 56vh;
    padding: 16px;
  }

  .flow-dependency-canvas {
    min-height: 228px;
    justify-content: flex-start;
  }
}
</style>
