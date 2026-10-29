<template>
  <div class="flow-canvas-zoom" :aria-label="$t('cicd-batch-zoom-controls')">
    <button
      type="button"
      :title="$t('cicd-batch-zoom-out')"
      :aria-label="$t('cicd-batch-zoom-out')"
      :disabled="modelValue <= 0.5"
      @click="$emit('update:modelValue', modelValue - 0.1)"
    >
      <Icon type="ios-remove" />
    </button>
    <span class="flow-canvas-zoom__value">{{ zoomPercent }}%</span>
    <button
      type="button"
      :title="$t('cicd-batch-zoom-in')"
      :aria-label="$t('cicd-batch-zoom-in')"
      :disabled="modelValue >= 1.5"
      @click="$emit('update:modelValue', modelValue + 0.1)"
    >
      <Icon type="ios-add" />
    </button>
    <span class="flow-canvas-zoom__divider" aria-hidden="true"></span>
    <button type="button" :title="$t('cicd-batch-fit-canvas')" :aria-label="$t('cicd-batch-fit-canvas')" @click="$emit('fit')">
      <Icon type="md-expand" />
    </button>
  </div>
</template>
<script>
export default {
  name: 'FlowCanvasZoom',
  props: { modelValue: { type: Number, default: 1 } },
  emits: ['update:modelValue', 'fit'],
  computed: {
    zoomPercent() {
      return Math.round(this.modelValue * 100);
    }
  }
};
</script>
<style scoped>
.flow-canvas-zoom {
  position: absolute;
  z-index: 4;
  top: 12px;
  right: 12px;
  display: flex;
  align-items: center;
  gap: 0;
  padding: 2px;
  border: 1px solid var(--border-primary);
  border-radius: 8px;
  background: #fff;
}

.flow-canvas-zoom button {
  display: inline-flex;
  width: 30px;
  height: 30px;
  align-items: center;
  justify-content: center;
  padding: 0;
  border: 0;
  border-radius: 5px;
  color: var(--text-secondary);
  background: transparent;
  font-size: 18px;
  cursor: pointer;
}

.flow-canvas-zoom button:hover:not(:disabled) {
  color: var(--primary-color);
  background: var(--bg-hover);
}

.flow-canvas-zoom button:disabled {
  color: var(--text-disabled);
  cursor: not-allowed;
}

.flow-canvas-zoom button:focus-visible {
  outline: 2px solid rgba(36, 180, 126, 0.3);
  outline-offset: -2px;
}

.flow-canvas-zoom__value {
  width: 44px;
  color: var(--text-secondary);
  font-size: 12px;
  font-variant-numeric: tabular-nums;
  line-height: 30px;
  text-align: center;
  user-select: none;
}

.flow-canvas-zoom__divider {
  width: 1px;
  height: 18px;
  margin: 0 2px;
  background: var(--border-primary);
}
</style>
