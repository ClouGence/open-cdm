<template>
  <div class="query">
    <div class="title" v-if="showSelect">{{ title }}</div>
    <a-select v-model="query.type" class="type" style="width: 120px" v-if="showSelect">
      <a-select-option key="all" value="all">
        {{ $t('quan-bu') }}
      </a-select-option>
      <a-select-option v-for="option in selectOptions" :key="`${option.value}`" :value="option.value">
        {{ option.name }}
      </a-select-option>
    </a-select>
    <a-input v-model="query.text" class="text" :placeholder="placeholder" style="width: 280px" />
    <a-button class="search-btn" type="primary" @click="handleQuery(query)">
      {{ $t('cha-xun') }}
    </a-button>
  </div>
</template>

<script>
export default {
  name: 'Query',
  props: {
    placeholder: String,
    query: Object,
    showSelect: Boolean,
    title: String,
    selectOptions: Array,
    handleQuery: Function
  }
};
</script>

<style lang="less" scoped>
.query {
  display: flex;
  align-items: center;

  .title {
    word-break: keep-all;
    margin-right: 10px;
  }

  .type {
    margin-right: 20px;
  }

  .text {
    margin-right: 10px;
  }
}
</style>
