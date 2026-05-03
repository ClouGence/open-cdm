<template>
  <div class="step step-two">
    <div class="datasource">
      <div class="left">
        <div class="header">
          <div class="title">{{ $t('dai-xuan-ze-shu-ju-yuan') }}</div>
          <div class="num">
            {{ $t('gong') }}
            <span>{{ datasourceList.length }}</span>
            {{ $t('ge-shi-li') }}
          </div>
        </div>
        <div class="content">
          <div class="query">
            <a-input
              v-model="query.text"
              :placeholder="$t('qing-shu-ru-shu-ju-yuan-guan-jian-zi')"
              style="margin-left: 10px; margin-right: 8px"
              :disabled="!datasourceList.length"
            />
            <a-button
              :disabled="!datasourceList.length"
              ghost
              type="primary"
              style="height: 32px; width: 60px; margin-right: 10px"
              @click="handleQuery"
            >
              {{ $t('shai-xuan') }}
            </a-button>
            <cc-region-select
              v-model="query.region"
              :env="CLUSTER_ENV.ALIBABA_CLOUD_HOSTED"
              :handleClickBtn="getRdsList"
              :default-text="$t('huo-qu-shu-ju-yuan')"
              style="border: 1px solid rgba(11, 185, 248, 1); color: #0bb9f8"
              width="92"
            />
            <!--            <a-button type="primary" @click="getRdsList">获取数据源</a-button>-->
          </div>
          <a-table
            :columns="datasourceColumns"
            :data-source="searchDatasourceList"
            :pagination="datasourceListPagination"
            :row-selection="rowSelection"
            :rowKey="(record) => record.instanceId"
            size="small"
            :loading="loading"
          >
            <template #name="record">
              {{ record.instanceId }}/{{ record.instanceDesc }}
              <a-popover v-if="record.existInDb" :content="$t('gai-shu-ju-yuan-yi-bei-nin-huo-tong-shi-tian-jia-guo')">
                <span class="already-added">{{ $t('yi-tian-jia') }}</span>
              </a-popover>
            </template>
          </a-table>
        </div>
      </div>
      <div class="right">
        <div class="header">
          <div class="title">{{ $t('yi-xuan-ze-shu-ju-yuan') }}</div>
          <div class="num">
            {{ $t('gong') }}
            <span>{{ selectedDatasourceList.length }}</span>
            {{ $t('ge-shi-li') }}
          </div>
        </div>
        <div class="content">
          <a-table :columns="selectedDatasourceColumns" :data-source="selectedDatasourceList" :rowKey="(record) => record.instanceId" size="small">
            <template #name="record">{{ record.instanceId }}/{{ record.instanceDesc }}</template>
            <template #host="record">
              <div v-if="record.privateHost">{{ $t('nei-wang') }}: {{ record.privateHost.connectionString }}:{{ record.privateHost.port }}</div>
              <div v-if="record.publicHost">{{ $t('wai-wang') }}: {{ record.publicHost.connectionString }}:{{ record.publicHost.port }}</div>
            </template>
            <template #actions="record">
              <a-button type="link" @click="handleDeleteDatasource(record)">
                {{ $t('shan-chu') }}
              </a-button>
            </template>
          </a-table>
        </div>
      </div>
    </div>
    <div class="footer">
      <a-button @click="handleBack">{{ $t('shang-yi-bu') }}</a-button>
      <a-button type="primary" @click="handleSubmit">
        {{ $t('xia-yi-bu-tian-jia-bai-ming-dan') }}
      </a-button>
    </div>
  </div>
</template>

<script>
import { Modal } from 'ant-design-vue';
import { CLUSTER_ENV } from '@/const';

export default {
  name: 'Two',
  props: {
    handleSubmitData: Function,
    handleBack: Function,
    stepData: Array
  },
  computed: {
    rowSelection() {
      return {
        selectedRowKeys: this.selectedDatasourceKeys,
        onChange: (selectedRowKeys, selectedRows) => {
          this.selectedDatasourceList = selectedRows;
          this.selectedDatasourceKeys = selectedRowKeys;
        },
        getCheckboxProps: (record) => ({
          props: {
            disabled: record.existInDb
          }
        })
      };
    }
  },
  data() {
    return {
      datasourceColumns: [
        {
          title: this.$t('ming-cheng'),
          scopedSlots: { customRender: 'name' }
        }
      ],
      leftCurrent: 1,
      CLUSTER_ENV,
      query: this.stepData[1].query || {
        region: '',
        text: ''
      },
      stepTwoData: {},
      datasourceList: [],
      searchDatasourceList: [{}],
      datasourceListFilter: {
        query: ''
      },
      datasourceListPagination: {
        pageSize: 10,
        onChange: (page, pageSize) => this.handlePaginationChange(page, pageSize)
      },
      selectedDatasourceKeys: [],
      selectedDatasourceList: [{}],
      selectedDatasourceColumns: [
        {
          title: this.$t('shi-li-id-ming-cheng'),
          scopedSlots: { customRender: 'name' }
        },
        {
          title: 'Host',
          scopedSlots: { customRender: 'host' }
        },
        {
          title: this.$t('cao-zuo'),
          scopedSlots: { customRender: 'actions' }
        }
      ],
      loading: false
    };
  },
  methods: {
    async handleQuery() {
      const searchDatasourceList = [];

      this.datasourceList.forEach((ds) => {
        if (ds.instanceId.includes(this.query.text) || ds.instanceDesc.includes(this.query.text)) {
          searchDatasourceList.push(ds);
        }
      });

      this.searchDatasourceList = searchDatasourceList;
      await this.handlePaginationChange(1, this.datasourceListPagination.pageSize);
    },
    async getDsList() {
      const res = await this.$services.rdpDataSourceListByCondition({ data: { useVisibility: true } });

      if (res.success) {
        this.datasourceList = res.data;
        this.searchDatasourceList = res.data;
      }
    },
    handleSubmit() {
      if (this.selectedDatasourceList.length <= 0) {
        Modal.error({
          content: this.$t('qing-xuan-ze-shu-ju-yuan')
        });
      } else {
        const data = {
          selectedDatasourceList: this.selectedDatasourceList,
          query: this.query,
          datasourceList: this.datasourceList,
          selectedDatasourceKeys: this.selectedDatasourceKeys
        };
        this.handleSubmitData(1, data);
      }
    },
    async handlePaginationChange(page, pageSize) {
      if (!this.searchDatasourceList.length) {
        return;
      }
      const currentDs = this.searchDatasourceList.slice((page - 1) * pageSize, page * pageSize);
      const instanceIds = [];
      currentDs.forEach((ds) => {
        instanceIds.push(ds.instanceId);
      });
      const data = {
        aliyunRegion: currentDs[0].aliyunRegion,
        bindClusterId: this.stepData[0].bindClusterId,
        dataSourceType: this.stepData[0].dataSourceType,
        instanceIds
      };
      const res = await this.$services.dmAliyunRdsQueryNetInfo({ data });

      if (res.success) {
        for (const [key, host] of Object.entries(res.data)) {
          this.searchDatasourceList.forEach((ds) => {
            ds.ticket = {
              turn: false,
              platform: 'DINGDING',
              templates: []
            };
            if (ds.instanceId === key) {
              host.forEach((ip) => {
                if (ip.netIpType === 'VPC_Private') {
                  ds.privateHost = ip;
                } else {
                  ds.publicHost = ip;
                }
              });
            }
          });
        }
      }
    },
    handleDeleteDatasource(record) {
      this.selectedDatasourceKeys = this.selectedDatasourceKeys.filter((key) => key !== record.instanceId);
      this.selectedDatasourceList = this.selectedDatasourceList.filter((ds) => ds.instanceId !== record.instanceId);
    },
    async getRdsList(city = '') {
      this.loading = true;
      this.datasourceListFilter = { query: '' };
      const { dataSourceType } = this.stepData[0];
      const data = {
        dataSourceType,
        region: city || this.query.region
      };
      const res = await this.$services.dmAliyunRdsListRds({ data });
      if (res.success) {
        res.data.forEach((ds) => {
          ds.region = this.query.region;
        });
        this.datasourceList = res.data;
        this.searchDatasourceList = res.data;
        await this.handlePaginationChange(1, this.datasourceListPagination.pageSize);
      }
      this.loading = false;
    }
  },
  created() {
    if (this.stepData[1].selectedDatasourceList) {
      const { query, selectedDatasourceList, datasourceList, selectedDatasourceKeys } = this.stepData[1];
      this.query = query;
      this.selectedDatasourceList = selectedDatasourceList;
      this.datasourceList = datasourceList;
      this.searchDatasourceList = datasourceList;
      this.selectedDatasourceKeys = selectedDatasourceKeys;
    }
  }
};
</script>

<style lang="less" scoped>
.step-two {
  .datasource {
    .ant-table-small {
      border: none !important;
    }

    flex: 1;
    display: flex;

    .header {
      display: flex;
      align-items: baseline;

      .title {
        font-size: 14px;
        font-weight: bold;
        margin-bottom: 7px;
      }

      .num {
        margin-left: 8px;
        font-size: 12px;

        span {
          font-weight: bold;
        }
      }
    }

    .left,
    .right {
      width: 50%;
      display: flex;
      flex-direction: column;

      .content {
        border: 1px solid rgba(234, 234, 234, 1);
        flex: 1;
      }
    }

    .left {
      margin-right: 10px;

      .content {
        .query {
          display: flex;
          padding: 10px 12px;
          background: #fafafa;
        }

        .already-added {
          color: #ff6e0d;
          margin-left: 10px;
        }
      }
    }
  }
}
</style>
