<template>
  <div style="display: inline-block" class="datasource-icon" :style="`background:${background}`">
    <cc-iconfont name="hana" :color="color" :size="size" v-if="type === 'Hana' && instanceType !== 'ALIBABA_CLOUD_HOSTED'"></cc-iconfont>
    <cc-iconfont name="dameng" :color="color" :size="size" v-if="type === 'Dameng' && instanceType !== 'ALIBABA_CLOUD_HOSTED'"></cc-iconfont>
    <cc-iconfont name="selectdb" :color="color" :size="size" v-if="type === 'SelectDB' && instanceType !== 'ALIBABA_CLOUD_HOSTED'"></cc-iconfont>
    <cc-iconfont name="oceanbase" :color="color" :size="size" v-if="type === 'OceanBase' && instanceType !== 'ALIBABA_CLOUD_HOSTED'"></cc-iconfont>
    <cc-iconfont name="db2" :color="color" :size="size" v-if="type === 'Db2' && instanceType !== 'ALIBABA_CLOUD_HOSTED'"></cc-iconfont>
    <cc-iconfont name="polardbx" :color="color" :size="size" v-if="type === 'PolarDBX' && instanceType !== 'ALIBABA_CLOUD_HOSTED'"></cc-iconfont>
    <cc-iconfont name="doris" :color="color" :size="size" v-if="type === 'Doris' && instanceType !== 'ALIBABA_CLOUD_HOSTED'"></cc-iconfont>
    <cc-iconfont name="clickhouse" :color="color" :size="size" v-if="type === 'ClickHouse' && instanceType !== 'ALIBABA_CLOUD_HOSTED'"></cc-iconfont>
    <cc-iconfont name="starrocks" :color="color" :size="size" v-if="type === 'StarRocks' && instanceType !== 'ALIBABA_CLOUD_HOSTED'"></cc-iconfont>
    <cc-iconfont name="mongodb" :color="color" :size="size" v-if="type === 'MongoDB' && instanceType !== 'ALIBABA_CLOUD_HOSTED'"></cc-iconfont>
    <cc-iconfont name="oracle" :color="color" :size="size" v-if="type === 'Oracle' && instanceType !== 'ALIBABA_CLOUD_HOSTED'"></cc-iconfont>
    <cc-iconfont name="tidb" :color="color" :size="size" v-if="type === 'TiDB' && instanceType !== 'ALIBABA_CLOUD_HOSTED'"></cc-iconfont>
    <cc-iconfont name="mysql1" :color="color" :size="size" v-if="type === 'MySQL' && instanceType !== 'ALIBABA_CLOUD_HOSTED'"></cc-iconfont>
    <cc-iconfont name="rds_mysql" :color="color" :size="size" v-if="type === 'MySQL' && instanceType === 'ALIBABA_CLOUD_HOSTED'"></cc-iconfont>
    <cc-iconfont name="PG" :color="color" :size="size" v-if="type === 'PostgreSQL' && instanceType !== 'ALIBABA_CLOUD_HOSTED'"></cc-iconfont>
    <cc-iconfont
      name="rds_postgresql"
      :color="color"
      :size="size"
      v-if="type === 'PostgreSQL' && instanceType === 'ALIBABA_CLOUD_HOSTED'"
    ></cc-iconfont>
    <cc-iconfont name="Greenplum" :color="color" :size="size" v-if="type === 'Greenplum' && instanceType !== 'ALIBABA_CLOUD_HOSTED'"></cc-iconfont>
    <cc-iconfont name="GP" :color="color" :size="size" v-if="type === 'Greenplum' && instanceType === 'ALIBABA_CLOUD_HOSTED'"></cc-iconfont>
    <cc-iconfont name="redis" :color="color" :size="size" v-if="type === 'Redis'"></cc-iconfont>
    <cc-iconfont name="sql-server" :color="color" :size="size" v-if="type === 'SQLServer'"></cc-iconfont>
  </div>
</template>
<script>
export default {
  props: {
    type: String,
    instanceType: String,
    color: String,
    size: Number,
    background: {
      type: String,
      default: ''
    }
  }
};
</script>
<style lang="less" scoped>
.datasource-icon {
  //background: #0071AF;
  width: 18px;
  height: 18px;
  line-height: 18px;
  text-align: center;
  margin-right: 4px;
  vertical-align: middle;
}
</style>
