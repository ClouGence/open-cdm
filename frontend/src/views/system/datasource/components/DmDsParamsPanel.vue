<template>
  <div class="param-list">
    <div class="table-list-layout">
      <div class="table-list">
        <div class="content">
          <div class="option border-radius-card">
            <div class="left">
              <Input
                v-model="queryForm.text"
                :placeholder="$t('qing-shu-ru-can-shu-ming-cheng-cha-xun')"
                style="width: 280px; margin-right: 10px"
                clearable
                @on-clear="handleQuery"
              />
              <Button @click="handleQuery" type="primary">{{ $t('cha-xun') }}</Button>
            </div>
            <div class="right">
              <Button type="primary" @click="handleShowConfirmEditParams" style="margin-right: 10px">
                {{ $t('sheng-xiao-pei-zhi') }}
              </Button>
              <Button @click="getDsParams">
                <CustomIcon type="icon-v2-Refresh" />
              </Button>
            </div>
          </div>
          <div class="table-container">
            <config-params-edit :ds-kv-configs="showConfigData" />
          </div>
        </div>
      </div>
      <div class="footer">
        <Page
          :total="total"
          show-total
          show-elevator
          @on-change="handlePageChange"
          show-sizer
          :model-value="pageNum"
          :page-size="pageSize"
          @on-page-size-change="handlePageSizeChange"
        />
      </div>
    </div>
    <CCModal v-model="showParamsEditConfirm" :title="$t('que-ding-yao-sheng-xiao-yi-xia-pei-zhi-ma')" footer-hide :width="800">
      <div style="overflow: auto">
        <div id="elem"></div>
        <a-table border size="small" :columns="editColumns" :data-source="editedParams">
          <template #configName="record">
            <div class="config-name">
              <div class="name" :style="`color: ${record.needCreated ? '#ff6e0d' : ''}`">
                {{ record.configName }}
                {{ record.needCreated ? $t('dai-chuang-jian') : '' }}
              </div>
            </div>
          </template>
        </a-table>
        <div class="footer" style="margin-top: 20px">
          <a-button @click="handleCancelEditParams">{{ $t('qu-xiao') }}</a-button>
          <a-button type="primary" @click="handleConfirmEditParams">{{ $t('que-ren') }}</a-button>
        </div>
      </div>
    </CCModal>
  </div>
</template>
<script>
import { PARAMS_CONFIG } from '@/const';
import ConfigParamsEdit from '@/views/system/ConfigParamsEdit';

export default {
  name: 'DmDsParamsPanel',
  components: { ConfigParamsEdit },
  props: {
    dataSourceId: {
      type: [String, Number],
      required: true
    }
  },
  watch: {
    dataSourceId: {
      immediate: true,
      handler(dataSourceId) {
        if (this.isValidDataSourceId(dataSourceId)) {
          this.getDsParams();
        }
      }
    }
  },
  data() {
    return {
      queryForm: {
        text: ''
      },
      trueData: [],
      pageSize: 20,
      pageNum: 1,
      total: 0,
      configData: [],
      showConfigData: [],
      editedParams: [],
      showParamsEditConfirm: false,
      editColumns: [
        {
          title: this.$t('can-shu-ming'),
          dataIndex: 'configName',
          key: 'configName',
          width: 300
        },
        {
          title: this.$t('dang-qian-yun-hang-zhi'),
          dataIndex: 'configValue',
          key: 'configValue'
        },
        {
          title: this.$t('xiu-gai-hou-de-zhi'),
          dataIndex: 'currentCount',
          key: 'currentCount'
        }
      ]
    };
  },
  methods: {
    isValidDataSourceId(dataSourceId) {
      return Number(dataSourceId) > 0;
    },
    handlePageChange(pageNum) {
      this.pageNum = pageNum;
      this.setTableShowData();
    },
    handlePageSizeChange(pageSize) {
      this.pageSize = pageSize;
      this.handlePageChange(1);
    },
    setTableShowData() {
      const { pageNum, pageSize } = this;
      this.showConfigData = this.configData.slice((pageNum - 1) * pageSize, pageNum * pageSize);
    },
    handleQuery() {
      this.configData = [];
      this.trueData.forEach((data) => {
        if (data.configName.indexOf(this.queryForm.text) > -1) {
          this.configData.push(data);
        }
      });
      this.total = this.configData.length;
      this.handlePageSizeChange(this.pageSize);
    },
    async getDsParams() {
      const res = await this.$services[PARAMS_CONFIG.ds.get]({
        data: { dataSourceId: this.dataSourceId }
      });
      if (res.success) {
        this.queryForm.text = '';
        this.trueData = res.data;
        this.configData = res.data;
        this.total = this.configData.length;
        this.setTableShowData();
      }
    },
    handleShowConfirmEditParams() {
      this.editedParams = [];
      this.showParamsEditConfirm = true;
      Object.values(this.configData).forEach((item) => {
        if (item.needCreated || ((item.currentCount === 0 || item.currentCount) && item.currentCount !== item.configValue)) {
          this.editedParams.push(item);
        }
      });
    },
    handleConfirmEditParams() {
      this.showParamsEditConfirm = false;
      if (this.editedParams.length === 0) {
        this.$Modal.warning({
          title: this.$t('cao-zuo-yi-chang'),
          content: this.$t('qing-xiu-gai-xu-yao-sheng-xiao-de-can-shu')
        });
        return;
      }
      const updateConfigMap = {};
      const needCreateConfigMap = {};

      this.editedParams.forEach((item) => {
        if (item.needCreated) {
          needCreateConfigMap[item.configName] = item.currentCount || item.defaultValue;
        } else {
          updateConfigMap[item.configName] = item.currentCount;
        }
      });

      this.$services[PARAMS_CONFIG.ds.edit]({
        data: {
          dataSourceId: this.dataSourceId,
          updateConfigMap,
          needCreateConfigMap
        }
      }).then((res) => {
        if (res.code === '1') {
          this.getDsParams();
          this.editedParams = [];
          this.$Message.success(this.$t('pei-zhi-yi-xiu-gai-cheng-gong'));
        }
      });
    },
    handleCancelEditParams() {
      this.editedParams = [];
      this.showParamsEditConfirm = false;
    }
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

.config-name {
  display: flex;
  justify-content: space-between;
}

.config-label {
  display: inline-block;
  padding: 2px 6px;
  border: 1px solid;
}
</style>
