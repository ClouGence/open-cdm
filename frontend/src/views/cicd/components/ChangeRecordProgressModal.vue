<template>
  <CCModal
    :model-value="modelValue"
    :title="$t('cicd-change-progress-modal-title')"
    :width="1120"
    footer-hide
    @update:model-value="handleVisibleChange"
    @on-cancel="handleVisibleChange(false)"
  >
    <div v-if="rootNode" class="change-progress-modal">
      <div class="change-progress-viewport">
        <div class="change-progress-canvas">
          <ChangeRecordProgressNode :node="rootNode" />
        </div>
      </div>
    </div>
  </CCModal>
</template>

<script>
import ChangeRecordProgressNode from './ChangeRecordProgressNode.vue';

export default {
  name: 'ChangeRecordProgressModal',
  components: { ChangeRecordProgressNode },
  props: {
    modelValue: { type: Boolean, required: true },
    record: { type: Object, default: null },
    flowTree: { type: Object, default: null }
  },
  emits: ['update:modelValue'],
  computed: {
    rootNode() {
      if (!this.record || !this.flowTree) {
        return null;
      }

      const progressByFlowId = new Map([
        [
          String(this.record.flowId),
          {
            changeId: this.record.changeId,
            ticketId: this.record.ticketId,
            currentStep: this.record.currentStep,
            currentStatus: this.record.currentStatus,
            scmType: this.record.scmType
          }
        ]
      ]);
      for (const transfer of this.record.downstream || []) {
        progressByFlowId.set(String(transfer.targetFlowId), {
          transferId: transfer.transferId,
          changeId: transfer.targetChangeId,
          ticketId: transfer.targetTicketId,
          currentStep: transfer.targetChangeStep,
          currentStatus: transfer.targetChangeStatus,
          transferStatus: transfer.status,
          errorMessage: transfer.errorMessage
        });
      }

      const buildNode = (flow) => {
        const progress = progressByFlowId.get(String(flow.flowId)) || {};
        return {
          transferId: progress.transferId || `flow-${flow.flowId}`,
          flowId: flow.flowId,
          flowName: flow.flowName,
          flowManagerName: flow.flowManagerName,
          flowType: flow.flowType,
          scmType: progress.scmType,
          changeId: progress.changeId,
          ticketId: progress.ticketId,
          currentStep: progress.currentStep,
          currentStatus: progress.currentStatus,
          transferStatus: progress.transferStatus,
          errorMessage: progress.errorMessage,
          children: (flow.children || []).map(buildNode)
        };
      };
      return buildNode(this.flowTree);
    }
  },
  methods: {
    handleVisibleChange(visible) {
      this.$emit('update:modelValue', visible);
    }
  }
};
</script>

<style lang="less" scoped>
.change-progress-modal {
  display: flex;
  min-height: 0;
  flex-direction: column;
}

.change-progress-viewport {
  min-height: 280px;
  padding: 24px;
  overflow: auto;
  border-radius: 8px;
  background: var(--bg-secondary);
}

.change-progress-canvas {
  display: flex;
  width: max-content;
  min-width: 100%;
  min-height: 232px;
  align-items: center;
  justify-content: center;
}

@media (max-width: 767px) {
  .change-progress-viewport {
    min-height: 260px;
    max-height: 56vh;
    padding: 16px;
  }

  .change-progress-canvas {
    min-height: 228px;
    justify-content: flex-start;
  }
}
</style>
