<template>
  <Table :columns="row.explainInfo.columns" :data="row.explainInfo.tableData" size="small" border />
</template>

<script>
export default {
  name: 'ExplainDetail',
  props: {
    row: Object
  }
};
</script>

<style scoped></style>
