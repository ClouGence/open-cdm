<template>
  <div class="cc-cluster-type-select">
    <a-button
      v-for="env in deployEnvList"
      :key="env.cloudOrIdcName"
      :type="env.cloudOrIdcName === selectedClusterEnv ? 'primary' : 'default'"
      @click="handleSelectClusterEnv(env.cloudOrIdcName)"
    >
      {{ env.i18nName }}
    </a-button>
  </div>
</template>

<script>
import { CLUSTER_ENV } from '@/const';

export default {
  name: 'CCClusterTypeSelect',
  model: {
    prop: '__selected_cluster_env__',
    event: '__handle_select_cluster_env__'
  },
  props: {
    __selected_cluster_env__: String,
    deployEnvList: Array,
    cluster: Object
  },
  data() {
    return {
      selectedClusterEnv: ''
    };
  },
  created() {
    console.log('cluster', this.cluster);
    this.selectedClusterEnv = this.__selected_cluster_env__ || CLUSTER_ENV.ALIBABA_CLOUD_HOSTED;
    console.log('selectedClusterEnv', this.selectedClusterEnv);
  },
  methods: {
    handleSelectClusterEnv(env) {
      this.selectedClusterEnv = env;
    }
  },
  watch: {
    selectedClusterEnv(value) {
      this.$emit('__handle_select_cluster_env__', value);
      console.log('value', value);
    }
  }
};
</script>

<style scoped></style>
