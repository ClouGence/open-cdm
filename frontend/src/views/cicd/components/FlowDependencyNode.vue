<template>
  <div class="flow-dependency-branch">
    <div class="flow-dependency-node-dropzone">
      <button type="button" class="flow-dependency-node" @click="$emit('navigate', flow)">
        <div class="flow-dependency-node__header">
          <img v-if="flow.flowType === 'BUILT_IN'" class="flow-dependency-node__product-logo" src="/dm.ico" alt="CloudDM" />
          <CustomIcon v-else-if="flow.scmType" :resource="getScmIconResource(flow.scmType)" :alt="flow.scmType" size="18px" />
          <span class="flow-dependency-node__level">
            {{ isRoot ? $t('cicd-dependency-root-flow') : $t('cicd-dependency-level-flow', { level: depth }) }}
          </span>
        </div>
        <strong :title="flow.flowName">{{ flow.flowName || '-' }}</strong>
        <span class="flow-dependency-node__manager" :title="flow.flowManagerName">{{ $t('fu-ze-ren') }}：{{ flow.flowManagerName || '-' }}</span>
      </button>
    </div>

    <div v-if="children.length" class="flow-dependency-children">
      <div v-for="child in children" :key="child.flowId" class="flow-dependency-child">
        <FlowDependencyNode :flow="child" :depth="depth + 1" @navigate="$emit('navigate', $event)" />
      </div>
    </div>
  </div>
</template>

<script>
import CustomIcon from '@/components/function/CustomIcon';
import { getScmIconResource } from '../utils';

export default {
  name: 'FlowDependencyNode',
  components: { CustomIcon },
  props: {
    flow: { type: Object, required: true },
    depth: { type: Number, default: 0 }
  },
  emits: ['navigate'],
  computed: {
    isRoot() {
      return this.depth === 0;
    },
    children() {
      return Array.isArray(this.flow.children) ? this.flow.children : [];
    }
  },
  methods: {
    getScmIconResource
  }
};
</script>

<style lang="less" scoped>
.flow-dependency-branch {
  --dependency-connector-length: 24px;
  --dependency-node-width: 208px;

  position: relative;
  display: flex;
  min-width: max-content;
  align-items: center;
}

.flow-dependency-node-dropzone {
  position: relative;
  z-index: 1;
  display: flex;
  padding: 12px;
  margin: -12px;
}

.flow-dependency-node {
  position: relative;
  display: flex;
  width: var(--dependency-node-width);
  min-height: 116px;
  padding: 16px;
  flex-direction: column;
  align-items: flex-start;
  border: 1px solid var(--border-primary);
  border-radius: 8px;
  color: var(--text-primary);
  background: var(--bg-card);
  text-align: left;
  cursor: pointer;
  transition:
    transform 0.2s ease,
    border-color 0.2s ease,
    background 0.2s ease;

  &:hover,
  &:focus-visible {
    border-color: var(--text-secondary);
    background: var(--bg-secondary);
    outline: none;
    transform: translateY(-2px);
  }

  &:active {
    transform: translateY(0);
  }

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

.flow-dependency-node__header {
  display: flex;
  width: 100%;
  align-items: center;
  gap: 8px;
}

.flow-dependency-node__product-logo {
  display: block;
  width: 18px;
  height: 18px;
  flex: 0 0 auto;
  object-fit: contain;
}

.flow-dependency-node__level {
  color: var(--text-secondary);
  font-size: 12px;
  font-weight: 500;
  line-height: 18px;
}

.flow-dependency-node__manager {
  width: 100%;
  margin-top: 4px;
  overflow: hidden;
  color: var(--text-tertiary);
  font-size: 12px;
  line-height: 18px;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.flow-dependency-children {
  position: relative;
  display: flex;
  padding-left: calc(var(--dependency-connector-length) * 2);
  flex-direction: column;
  gap: 24px;

  &::before {
    position: absolute;
    top: 50%;
    left: 0;
    width: var(--dependency-connector-length);
    border-top: 1px solid var(--border-primary);
    content: '';
  }
}

.flow-dependency-child {
  position: relative;

  &::before,
  &::after {
    position: absolute;
    content: '';
  }

  &::before {
    top: 50%;
    left: calc(var(--dependency-connector-length) * -1);
    width: var(--dependency-connector-length);
    border-top: 1px solid var(--border-primary);
  }

  &::after {
    top: -12px;
    bottom: -12px;
    left: calc(var(--dependency-connector-length) * -1);
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
  .flow-dependency-branch {
    --dependency-connector-length: 20px;
    --dependency-node-width: 196px;
  }
}
</style>
