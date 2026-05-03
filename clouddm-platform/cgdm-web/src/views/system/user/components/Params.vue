<template>
  <div class="param-list">
    <div class="table-list-layout">
      <div class="table-list">
        <div class="header">
          <Breadcrumb>
            <BreadcrumbItem to="/system/ccdatasource">{{ $t('cha-xun-pei-zhi') }}</BreadcrumbItem>
            <BreadcrumbItem>{{ $t('xiu-gai-can-shu-pei-zhi') }}</BreadcrumbItem>
          </Breadcrumb>
        </div>
        <DmDsParamsPanel :data-source-id="dataSourceId" />
      </div>
    </div>
  </div>
</template>
<script>
import DmDsParamsPanel from '@/views/system/datasource/components/DmDsParamsPanel';

export default {
  name: 'Params',
  components: { DmDsParamsPanel },
  mounted() {
    this.dataSourceId = this.$route.params.id;
    if (this.$route.name === 'User_Config') {
      this.type = 'user';
    }
  },
  data() {
    return {
      type: 'ds',
      dataSourceId: 0
    };
  }
};
</script>
<style lang="less" scoped>
.param-list {
  height: 100%;
  display: flex;
  flex-direction: column;
}

:deep(.ant-table-content) {
  overflow: auto;
}
</style>
