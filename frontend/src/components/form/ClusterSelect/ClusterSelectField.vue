<template>
  <div class="cluster-select-field">
    <Select :model-value="modelValue" transfer filterable style="width: 280px" @on-change="$emit('change', $event)">
      <Option
        v-for="cluster in clusterList"
        :key="cluster.id"
        :value="cluster.id"
        :label="cluster.clusterDesc ? cluster.clusterDesc : cluster.clusterName"
        :style="`${cluster.runningCount ? '' : 'cursor: not-allowed'}`"
      >
        <p>{{ cluster.clusterName }}</p>
        <p class="cluster-select-desc">
          {{ cluster.clusterDesc }}
          <span class="cluster-select-worker-count">{{ cluster.runningCount }}/{{ cluster.workerCount }}</span>
        </p>
      </Option>
    </Select>
    <div v-if="showNoRunningWorkerWarning" class="cluster-select-warning">
      <i class="iconfont iconTIP"></i>
      {{ $t('gai-ji-qun-wu-cun-huo-ji-qi') }}
      <a class="text-cc-primary" :href="`/#/system/dmmachine/list/${modelValue}`">
        {{ $t('tian-jia-ji-qi') }}
      </a>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ClusterSelectField',
  props: {
    modelValue: {
      type: [Number, String],
      default: ''
    },
    clusterList: {
      type: Array,
      default: () => []
    },
    currentCluster: {
      type: Object,
      default: () => ({})
    }
  },
  emits: ['change'],
  computed: {
    showNoRunningWorkerWarning() {
      return this.currentCluster && Number(this.currentCluster.runningCount) === 0;
    }
  }
};
</script>

<style lang="less" scoped>
.cluster-select-field {
  display: inline-flex;
  flex-direction: column;
  align-items: flex-start;
}

.cluster-select-desc {
  margin: 5px 0;
  color: #ccc;
}

.cluster-select-worker-count {
  margin-left: 8px;
}

.cluster-select-warning {
  margin-top: 8px;
  color: #ff6e0c;

  .iconTIP {
    margin-right: 8px;
  }
}
</style>
