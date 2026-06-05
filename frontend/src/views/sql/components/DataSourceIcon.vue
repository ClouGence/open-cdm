<template>
  <div style="display: inline-block" class="datasource-icon">
    <cc-data-source-icon color="#ffffff" background="#0087c7" :size="12" :type="type" :instance-type="instanceType" />
  </div>
</template>
<script>
export default {
  name: 'DataSourceIcon',
  props: {
    type: String,
    instanceType: String
  }
};
</script>
<style lang="less" scoped>
.datasource-icon {
  background: #0071af;
  width: 16px;
  height: 16px;
  line-height: 16px;
  margin-right: 4px;
  vertical-align: middle;
}
</style>
