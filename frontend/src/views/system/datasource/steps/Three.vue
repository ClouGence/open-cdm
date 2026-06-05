<template>
  <div class="step step-three">
    <a-table :columns="datasourceListColumns" :data-source="datasourceList" :rowKey="(record) => record.instanceId" size="small">
      <template #white-title>
        {{ $t('tian-jia-bai-ming-dan') }}
        <a-popover>
          <template #content>
            <p>
              {{ $t('bang-ding-de-ji-qun-ji-qi-de-ip-hui-zi-dong-bei-tian-jia-dao-dui-ying-shu-ju-ku-shi-li-de-bai-ming-dan-lie-biao-zhong') }}
            </p>
          </template>
          <a-icon theme="filled" type="question-circle" />
        </a-popover>
      </template>
      <template #name="record">{{ record.instanceId }} / {{ record.instanceDesc }}</template>
      <template #host="record">
        <div v-if="record.privateHost">{{ $t('nei-wang') }}:{{ record.privateHost.connectionString }}:{{ record.privateHost.port }}</div>
        <div v-if="record.publicHost">{{ $t('wai-wang') }}:{{ record.publicHost.connectionString }}:{{ record.publicHost.port }}</div>
      </template>
      <template #defaultHost="record">
        {{ '' }}
        <div v-if="record.publicHost && !record.privateHost">{{ $t('wai-wang') }}</div>
        <div v-if="!record.publicHost && record.privateHost">{{ $t('nei-wang') }}</div>
        <a-select
          v-if="record.privateHost && record.publicHost"
          :default-value="record.defaultHost"
          size="small"
          @change="handleDefaultHostChange(record, $event)"
        >
          <a-select-option rowKey="out" :value="$t('wai-wang')">
            {{ $t('wai-wang') }}
          </a-select-option>
          <a-select-option rowKey="in" :value="$t('nei-wang')">
            {{ $t('nei-wang') }}
          </a-select-option>
        </a-select>
      </template>
      <template #white="record">
        <a-checkbox @change="handleAddWhiteIp(record, $event)" />
      </template>
      <template #whiteType="record">
        <a-select
          v-if="record.whiteList.addIpWhiteList"
          :default-value="record.whiteList.type"
          size="small"
          style="width: 100px"
          @change="handleChangeWhiteType(record, $event)"
        >
          <a-select-option v-for="type in ipTypeOptions" :key="type" :rowKey="type" :value="type">
            {{ type }}
          </a-select-option>
        </a-select>
      </template>
    </a-table>
    <div class="footer">
      <a-button @click="handleBack">{{ $t('shang-yi-bu') }}</a-button>
      <a-button type="primary" @click="handleSubmitData(2, datasourceList)">
        {{ $t('xia-yi-bu-shu-ju-ku-zhang-hao') }}
      </a-button>
    </div>
  </div>
</template>

<script>
import * as Vue from 'vue';

export default {
  name: 'Three',
  props: {
    stepData: Array,
    handleBack: Function,
    handleSubmitData: Function
  },
  computed: {
    rowSelection() {
      return {
        onChange: (selectedRowKeys, selectedRows) => {
          this.selectedDatasource.whiteList.children = selectedRows;
          let num = 0;
          selectedRows.forEach((row) => {
            num += row.workerCount;
          });
          this.selectedDatasource.whiteList.num = num;
        }
      };
    }
  },
  data() {
    return {
      selectedDatasource: {},
      selectedCluster: {},
      ipTypeOptions: [this.$t('quan-bu'), this.$t('nei-wang-ip'), this.$t('gong-wang-chu-kou-ip')],
      stepThreeData: {},
      datasourceList: [{}],
      datasourceListColumns: [
        {
          title: this.$t('shi-li-ming-cheng-id'),
          scopedSlots: { customRender: 'name' }
        },
        {
          title: 'Host',
          scopedSlots: { customRender: 'host' }
        },
        {
          title: this.$t('mo-ren-fang-wen-di-zhi'),
          scopedSlots: { customRender: 'defaultHost' },
          width: 150
        },
        {
          slots: { title: 'white-title' },
          scopedSlots: { customRender: 'white' },
          width: 120
        },
        {
          title: this.$t('bai-ming-dan-lei-xing'),
          scopedSlots: { customRender: 'whiteType' },
          width: 150
        }
      ],
      clusterList: [],
      clusterListColumns: [
        {
          title: this.$t('ji-qun-ming-cheng-id'),
          width: 400,
          scopedSlots: { customRender: 'name' }
        },
        {
          title: this.$t('ke-yong-shu-liang-ji-qi-shu-liang'),
          width: 200,
          scopedSlots: { customRender: 'num' }
        }
      ],
      innerColumns: [
        {
          title: 'ip',
          scopedSlots: { customRender: 'ip' }
        }
      ],
      ipMap: {}
    };
  },
  methods: {
    handleChangeWhiteType(record, value) {
      this.datasourceList.forEach((ds) => {
        if (ds.id === record.id) {
          ds.whiteList.type = value;
        }
      });
      this.datasourceList = [...this.datasourceList];
    },
    handleAddWhiteIp(record, e) {
      this.datasourceList.forEach((ds) => {
        if (ds.instanceId === record.instanceId) {
          ds.whiteList.addIpWhiteList = e.target.checked;
        }
      });
      this.datasourceList = [...this.datasourceList];
    },
    async expand(expanded, record) {
      if (expanded) {
        const res = await this.$services.dmWorkerListWorkers({ data: { clusterId: record.id } });
        if (res.success) {
          this.selectedCluster = record;
          this.ipMap.record.id = res.data;
        }
      }
    },
    handleIpTypeChange(e) {
      this.selectedDatasource.whiteList.type = e.target.value;
    },
    async handleVisibilityChange(ds, visible) {
      if (visible) {
        this.selectedDatasource = ds;
        const res = await this.$services.dmClusterListByCondition({ data: {} });
        if (res.success) {
          this.clusterList = res.data;
        }
      } else {
        const { datasourceList } = this;
        datasourceList.forEach((ds1) => {
          if (ds1.instanceId === this.selectedDatasource.instanceId) {
            ds1 = this.selectedDatasource;
          }
        });
        this.datasourceList = datasourceList;
        this.$forceUpdate();
      }
    },
    handleDefaultHostChange(record, value) {
      const { datasourceList } = this;
      datasourceList.forEach((ds) => {
        if (ds.instanceId === record.instanceId) {
          ds.defaultHost = value;
        }
      });
      this.datasourceList = datasourceList;
    }
  },
  created() {
    const datasourceList = [];
    this.stepData[1].selectedDatasourceList.forEach((ds) => {
      ds.defaultHost = ds.publicHost ? this.$t('wai-wang') : this.$t('nei-wang');
      ds.whiteList = {
        type: this.$t('quan-bu'),
        num: 0,
        children: [],
        addIpWhiteList: false
      };
      datasourceList.push(ds);
    });
    this.datasourceList = datasourceList;
  }
};
</script>

<style lang="less" scoped></style>
