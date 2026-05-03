<template>
  <div class="query-header">
    <query
      :handle-query="handleQuery"
      :select-options="selectOptions"
      :show-select="showSelect"
      :title="title"
      :query="query"
      :placeholder="placeholder"
    />
    <div class="op">
      <a-button ghost type="primary" @click="handleClickAddBtn" v-if="showAddBtn" icon="plus">
        {{ addBtnText }}
      </a-button>
      <slot></slot>
      <a-button class="refresh" @click="handleRefresh" :loading="loading">
        <CustomIcon type="icon-v2-Refresh" v-if="!loading" />
      </a-button>
    </div>
  </div>
</template>

<script>
import Query from './Query';

export default {
  name: 'QueryHeader',
  components: { Query },
  props: {
    placeholder: {
      type: String,
      default: ''
    },
    query: {
      type: Object
    },
    showSelect: {
      type: Boolean,
      default: true
    },
    showAddBtn: {
      type: Boolean,
      default: true
    },
    title: {
      type: String,
      default: ''
    },
    loading: {
      type: Boolean,
      default: false
    },
    selectOptions: Array,
    addBtnText: String,
    handleQuery: Function,
    handleRefresh: Function,
    handleClickAddBtn: Function
  },
  created() {
    if (!this.title) {
      this.title = this.$t('lei-xing');
    }
  }
};
</script>

<style lang="less" scoped>
.query-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 12px;

  .op {
    & > button + button {
      margin-left: 10px;
    }
  }
}
</style>
