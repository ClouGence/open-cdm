<template>
  <div class="change-progress-branch">
    <div class="change-progress-node-dropzone">
      <div class="change-progress-node">
        <div class="change-progress-node__header">
          <img v-if="node.flowType === 'BUILT_IN'" class="change-progress-node__product-logo" src="/dm.ico" alt="CloudDM" />
          <CustomIcon v-else-if="node.scmType" :resource="getScmIconResource(node.scmType)" :alt="node.scmType" size="18px" />
          <span class="change-progress-node__level">
            {{ isRoot ? $t('cicd-dependency-root-flow') : $t('cicd-dependency-level-flow', { level: depth }) }}
          </span>
          <span class="change-progress-node__status" :class="statusClass">
            <Icon :type="statusIcon" />
            {{ statusLabel }}
          </span>
        </div>
        <strong :title="node.flowName">{{ node.flowName || '-' }}</strong>
        <span class="change-progress-node__manager" :title="node.flowManagerName">{{ $t('fu-ze-ren') }}：{{ node.flowManagerName || '-' }}</span>
        <router-link v-if="node.ticketId" class="change-progress-node__ticket" :to="`/ticket/${node.ticketId}`">
          <Icon type="ios-paper-outline" />
          <span>{{ $t('cicd-change-ticket-link', { id: node.ticketId }) }}</span>
        </router-link>
      </div>
    </div>

    <div v-if="children.length" class="change-progress-children">
      <div v-for="child in children" :key="child.transferId" class="change-progress-child">
        <ChangeRecordProgressNode :node="child" :depth="depth + 1" />
      </div>
    </div>
  </div>
</template>

<script>
import CustomIcon from '@/components/function/CustomIcon';
import { getScmIconResource } from '../utils';

export default {
  name: 'ChangeRecordProgressNode',
  components: { CustomIcon },
  props: {
    node: { type: Object, required: true },
    depth: { type: Number, default: 0 }
  },
  computed: {
    isRoot() {
      return this.depth === 0;
    },
    children() {
      return Array.isArray(this.node.children) ? this.node.children : [];
    },
    statusClass() {
      if (this.node.transferStatus === 'FAILED' || this.node.currentStatus === 'FAILED') {
        return 'is-danger';
      }
      if (!this.node.changeId) {
        return 'is-muted';
      }
      if (this.node.currentStatus === 'CLOSED') {
        return 'is-muted';
      }
      if (this.node.currentStatus === 'FINISH') {
        return 'is-success';
      }
      return 'is-progress';
    },
    statusIcon() {
      if (this.statusClass === 'is-danger') {
        return 'ios-close';
      }
      if (!this.node.changeId) {
        return 'ios-time-outline';
      }
      if (this.statusClass === 'is-muted') {
        return 'ios-remove';
      }
      if (this.statusClass === 'is-success') {
        return 'md-checkmark';
      }
      return 'ios-time-outline';
    },
    statusLabel() {
      if (this.node.transferStatus === 'FAILED') {
        return this.$t('chuan-di-shi-bai');
      }
      if (!this.node.changeId) {
        return this.$t('wei-lai-shi');
      }
      const statusLabels = {
        CLOSED: this.$t('yi-guan-bi'),
        FAILED: this.$t('shi-bai'),
        FINISH: this.$t('wan-cheng')
      };
      if (statusLabels[this.node.currentStatus]) {
        return statusLabels[this.node.currentStatus];
      }
      const stepLabels = {
        INIT: this.$t('di-jiao'),
        APPROVAL: this.$t('deng-dai-shen-pi')
      };
      return stepLabels[this.node.currentStep] || this.$t('jin-hang-zhong');
    }
  },
  methods: {
    getScmIconResource
  }
};
</script>

<style lang="less" scoped>
.change-progress-branch {
  --progress-connector-length: 24px;
  --progress-node-width: 208px;

  position: relative;
  display: flex;
  min-width: max-content;
  align-items: center;
}

.change-progress-node-dropzone {
  position: relative;
  z-index: 1;
  display: flex;
  padding: 12px;
  margin: -12px;
}

.change-progress-node {
  position: relative;
  display: flex;
  width: var(--progress-node-width);
  min-height: 116px;
  padding: 16px;
  flex-direction: column;
  align-items: flex-start;
  border: 1px solid var(--border-primary);
  border-radius: 8px;
  color: var(--text-primary);
  background: var(--bg-card);
  text-align: left;

  strong {
    width: 100%;
    margin-top: 12px;
    overflow: hidden;
    font-size: 14px;
    font-weight: 500;
    line-height: 22px;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
}

.change-progress-node__header {
  display: flex;
  width: 100%;
  align-items: center;
  gap: 8px;
}

.change-progress-node__product-logo {
  display: block;
  width: 18px;
  height: 18px;
  flex: 0 0 auto;
  object-fit: contain;
}

.change-progress-node__level {
  color: var(--text-secondary);
  font-size: 12px;
  line-height: 18px;
  font-weight: 500;
}

.change-progress-node__manager {
  width: 100%;
  margin-top: 4px;
  overflow: hidden;
  color: var(--text-tertiary);
  font-size: 12px;
  line-height: 18px;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.change-progress-node__ticket {
  display: inline-flex;
  max-width: 100%;
  margin-top: 6px;
  align-items: center;
  color: var(--primary-color);
  font-size: 12px;
  line-height: 18px;
  gap: 4px;

  &,
  span {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  &:focus,
  &:hover {
    border-bottom: 0;
    color: var(--primary-color);
    text-decoration: none;
  }
}

.change-progress-node__status {
  display: inline-flex;
  height: 22px;
  padding: 0 8px;
  margin-left: auto;
  flex: 0 0 auto;
  align-items: center;
  border-radius: 11px;
  font-size: 12px;
  line-height: 22px;
  gap: 4px;

  &.is-success {
    color: #19be6b;
    background: #e7f8ee;
  }

  &.is-progress {
    color: #2d6ccb;
    background: #e8f2ff;
  }

  &.is-danger {
    color: #ed4014;
    background: #fff1f0;
  }

  &.is-muted {
    color: #64748b;
    background: #eef2f7;
  }
}

.change-progress-children {
  position: relative;
  display: flex;
  padding-left: calc(var(--progress-connector-length) * 2);
  flex-direction: column;
  gap: 24px;

  &::before {
    position: absolute;
    top: 50%;
    left: 0;
    width: var(--progress-connector-length);
    border-top: 1px solid var(--border-primary);
    content: '';
  }
}

.change-progress-child {
  position: relative;

  &::before,
  &::after {
    position: absolute;
    content: '';
  }

  &::before {
    top: 50%;
    left: calc(var(--progress-connector-length) * -1);
    width: var(--progress-connector-length);
    border-top: 1px solid var(--border-primary);
  }

  &::after {
    top: -12px;
    bottom: -12px;
    left: calc(var(--progress-connector-length) * -1);
    border-left: 1px solid var(--border-primary);
  }

  &:first-child::after {
    top: 50%;
  }

  &:last-child::after {
    bottom: 50%;
  }

  &:only-child::after {
    display: none;
  }
}

@media (max-width: 767px) {
  .change-progress-branch {
    --progress-connector-length: 20px;
    --progress-node-width: 196px;
  }
}
</style>
