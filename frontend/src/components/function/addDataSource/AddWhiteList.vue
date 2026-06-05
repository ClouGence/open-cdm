<template>
  <div class="add-datasource-step2">
    <p class="add-datasouce-title">{{ $t('shu-ju-yuan-xin-xi') }}</p>
    <p class="add-datasource-basic">
      <span class="add-datasource-basic-label">{{ $t('bu-shu-lei-xing') }}</span>
      <span class="add-datasource-basic-value">
        {{ Mapping.cloudOrIdcName[addDataSourceForm.instanceType] }}
      </span>
      <span class="add-datasource-basic-label">{{ $t('yuan-ku-lei-xing') }}</span>
      <span class="add-datasource-basic-value">
        {{ addDataSourceForm.instanceType === 'ALIBABA_CLOUD_HOSTED' ? Mapping.aliyunType[addDataSourceForm.type] : addDataSourceForm.type }}
      </span>
    </p>
    <p class="add-datasouce-title">{{ $t('tian-jia-bai-ming-dan') }}</p>
    <Table border :columns="dataSourceColumn" :data="addDataSourceForm.rdsList">
      <template #action="{ row }">
        <a @click="handleShowAddWhiteList(row)">{{ $t('tian-jia-bai-ming-dan') }}</a>
        <!--        <SelectCluster :clusters="clusters" :row="row" :handleChangeType="handleChangeType"-->
        <!--                       :handleChangeCluster="handleChangeCluster"-->
        <!--                       ref="selectCluster"></SelectCluster>-->
      </template>
      <template #host="{ row }">
        <div class="host-type">
          <p v-if="row.privateHost">
            <span class="host-type-label">{{ $t('nei') }}</span>
            {{ row.privateHost }}
          </p>
          <p v-if="row.publicHost" style="margin-top: 3px">
            <span class="host-type-label">{{ $t('wai') }}</span>
            {{ row.publicHost }}
          </p>
        </div>
      </template>
      <template #version="{ row }">
        <div>
          <p v-if="row.version">{{ row.version }}</p>
        </div>
      </template>
    </Table>
    <AddWhiteList :showAddWhiteList="showAddWhiteList" :selectedRow="selectedRow" :handleCancelEdit="handleCancelEdit"></AddWhiteList>
  </div>
</template>
<script>
import Mapping from '@/views/util';
import AddWhiteList from '@/components/modal/AddWhiteList';
// import SelectCluster from './SelectCluster';

export default {
  components: { AddWhiteList },
  props: {
    addDataSourceForm: Object,
    clusters: Array
  },
  created() {
    this.addDataSourceForm.rdsList.map((rds) => {
      if (rds.publicHost) {
        rds.whiteListAddType = 'PUBLIC_IP_ONLY';
      } else {
        rds.whiteListAddType = 'PRIVATE_IP_ONLY';
      }
      return null;
    });
  },
  data() {
    return {
      showClusterList: false,
      Mapping,
      dataSourceColumn: [
        {
          title: this.$t('shi-li-id'),
          key: 'instanceId',
          minWidth: 200
        },
        {
          title: 'Host',
          key: 'host',
          slot: 'host',
          minWidth: 470
        },
        {
          title: this.$t('ban-ben-hao'),
          key: 'version',
          slot: 'version',
          minWidth: 120
        },
        {
          title: this.$t('miao-shu'),
          key: 'instanceDesc',
          width: 300
        },
        {
          title: this.$t('tian-jia-bai-ming-dan'),
          key: '',
          slot: 'action',
          minWidth: 100
        }
      ],
      dataSourceData: [],
      clusterList: [],
      selectedRow: {},
      showAddWhiteList: false
    };
  },
  methods: {
    handleCancelEdit() {
      this.showAddWhiteList = false;
    },
    handleShowAddWhiteList(row) {
      this.showAddWhiteList = true;
      this.selectRow = row;
    },
    handleAddWhiteList() {
      this.addDataSourceForm.rdsList.map((item) => {
        if (this.$refs.selectCluster.selectedCluster[item.instanceId] && this.$refs.selectCluster.selectedCluster[item.instanceId].length > 0) {
          item.selectedCluster = this.$refs.selectCluster.selectedCluster[item.instanceId];
        }
        return null;
      });
    },
    handleShowClusterList() {
      this.showClusterList = !this.showClusterList;
    },
    handleChangeType(type, row) {
      row.whiteListAddType = type;
      this.addDataSourceForm.rdsList.map((item) => {
        if (item.instanceId === row.instanceId) {
          item.whiteListAddType = type;
        }
        return null;
      });
    },
    handleChangeCluster(row, selectedCluster) {
      this.addDataSourceForm.rdsList.map((item) => {
        if (item.instanceId === row.instanceId) {
          item.clusters = selectedCluster;
        }
        return null;
      });
    }
  }
};
</script>
<style lang="less" scoped>
.add-datasource-step2 {
  .add-datasouce-title {
    height: 40px;
    line-height: 40px;
    background-color: #ececec;
    border-bottom: 1px solid #dadada;
    padding: 0 20px;
    font-weight: 500;
  }

  .add-datasource-basic {
    height: 40px;
    line-height: 40px;
    border-bottom: 1px solid #dadada;
    box-sizing: border-box;

    .add-datasource-basic-label {
      width: 10%;
      height: 39px;
      background-color: #f4f4f4;
      padding: 0 10px;
      text-align: right;
      display: inline-block;
      border-right: 1px solid #dadada;
    }

    .add-datasource-basic-value {
      width: 39%;
      padding: 0 10px;
      display: inline-block;
      border-right: 1px solid #dadada;
    }

    .add-datasource-basic-value:last-child {
      border-right: none;
    }
  }
}

.show-cluster-btn {
  display: inline-block;
  width: 100%;
  text-align: right;
  cursor: pointer;
}

.white-list-cluster-item {
  padding: 0 16px;

  .ivu-tree {
    display: inline-block;
    vertical-align: top;
  }

  .ivu-checkbox-wrapper {
    vertical-align: top;
    margin-top: 10px;
  }
}

.host-type {
  padding: 12px 0;
}

.host-type-label {
  font-size: 12px;
  color: #333;
  background-color: #deefff;
  display: inline-block;
  //width: 16px;
  height: 16px;
  border-radius: 4px;
  text-align: center;
  line-height: 16px;
  margin-right: 4px;
}
</style>
