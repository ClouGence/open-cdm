<template>
  <Table :columns="columns" :data="rows" border size="small">
    <template #logical="{ row }">
      <span class="explain-logical-operation" :style="{ paddingLeft: `${row.depth * 16}px` }">
        <Icon v-if="row.depth" class="explain-logical-operation__branch" type="ios-return-right" />
        {{ row.logical || '--' }}
      </span>
    </template>
    <template #properties="{ row }">
      <span class="explain-properties">{{ formatProperties(row.properties) }}</span>
    </template>
  </Table>
</template>

<script>
export default {
  name: 'DmlExplainPlan',
  props: {
    plan: {
      type: Object,
      required: true
    }
  },
  computed: {
    columns() {
      return [
        { title: this.$t('ticket-explain-node-id'), key: 'nodeId', width: 90 },
        { title: this.$t('ticket-explain-parent-node-id'), key: 'parentNodeId', width: 100 },
        { title: this.$t('ticket-explain-logical-operation'), slot: 'logical', minWidth: 180 },
        { title: this.$t('ticket-explain-physical-operation'), key: 'physical', minWidth: 150 },
        { title: this.$t('ticket-explain-object'), key: 'objectPath', minWidth: 180 },
        { title: this.$t('ticket-explain-estimated-rows'), key: 'estimatedRows', width: 130 },
        { title: this.$t('ticket-explain-estimated-cost'), key: 'estimatedSubtreeCost', width: 130 },
        { title: this.$t('ticket-explain-properties'), slot: 'properties', minWidth: 320 }
      ];
    },
    rows() {
      const nodes = this.plan.nodes || [];
      const nodesById = new Map(nodes.map((node) => [node.nodeId, node]));
      const depths = new Map();
      const depthOf = (node) => {
        if (depths.has(node.nodeId)) {
          return depths.get(node.nodeId);
        }
        const parent = nodesById.get(node.parentNodeId);
        const depth = parent ? depthOf(parent) + 1 : 0;
        depths.set(node.nodeId, depth);
        return depth;
      };
      return nodes.map((node) => ({ ...node, depth: depthOf(node) }));
    }
  },
  methods: {
    formatProperties(properties) {
      const entries = Object.entries(properties || {});
      if (!entries.length) {
        return '--';
      }
      return entries.map(([key, value]) => `${key}=${value}`).join('; ');
    }
  }
};
</script>

<style scoped lang="less">
.explain-logical-operation {
  display: inline-flex;
  align-items: center;
  gap: 4px;
}

.explain-logical-operation__branch {
  color: var(--text-secondary);
}

.explain-properties {
  white-space: normal;
  overflow-wrap: anywhere;
}
</style>
