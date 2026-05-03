<template>
  <div style="padding: 16px">
    <Breadcrumb>
      <BreadcrumbItem to="/system/ccdatasource">{{ $t('shu-ju-yuan-guan-li') }}</BreadcrumbItem>
      <BreadcrumbItem>{{ $t('xiu-gai-shu-ju-yuan-can-shu-shi-li', [instanceId]) }}</BreadcrumbItem>
    </Breadcrumb>
    <Tabs v-model="activeTab" style="margin-top: 10px">
      <TabPane :label="$t('xiu-gai-shu-ju-yuan-can-shu')" name="datasource-params"></TabPane>
      <TabPane :label="$t('xiu-gai-can-shu-pei-zhi')" name="query-params"></TabPane>
    </Tabs>
    <div v-show="activeTab === 'datasource-params'">
      <div class="page-header-container" style="margin-top: 10px">
        <div class="page-header-function">
          <Button type="primary" @click="handleShowConfirmEditParams">
            {{ $t('sheng-xiao-pei-zhi') }}
          </Button>
          <Button @click="handleRefresh" :loading="loading" :disabled="loading">
            <CustomIcon type="icon-v2-Refresh" v-if="!loading" />
          </Button>
        </div>
      </div>
      <div class="params-edit-wrapper" style="position: relative">
        <config-params-edit :ds-kv-configs="dsKvConfigs" @updateDsKvConfig="updateDsKvConfig" :loading="loading" />
      </div>
    </div>
    <div v-show="activeTab === 'query-params'" class="params-edit-wrapper" style="position: relative; margin-top: 10px">
      <DmDsParamsPanel :data-source-id="dsId" />
    </div>
    <CCModal v-model="showJson" :title="$t('wan-zheng-can-shu-zhan-shi')" width="700px" footer-hide>
      <div style="position: relative">
        <span style="position: absolute; right: 20px; top: 20px">
          <Icon class="copy-icon" type="ios-photos-outline" @click="handleCopyJson(JSON.stringify(selectedJson))" />
        </span>
        <div style="max-height: 500px; overflow: auto">
          <pre>{{ selectedJson }}</pre>
          <!--                    <vue-json-editor v-model="selectedJson" :mode="currentMode" :show-btns="false" :expandedOnStart="true" @json-change="onJsonChange"></vue-json-editor>-->
        </div>
      </div>
      <template #footer>
        <!--          <Button type="primary" @click="handleEditJson">{{ $t('que-ren') }}</Button>-->
        <Button @click="handleCloseShowJson">{{ $t('guan-bi') }}</Button>
      </template>
    </CCModal>
    <CCModal v-model="showParamsEditConfirm" :title="$t('que-ding-yao-sheng-xiao-yi-xia-pei-zhi-ma')" footer-hide width="800px">
      <div style="max-height: 500px; overflow: auto">
        <div id="elem"></div>
        <Table border size="small" :columns="editColumns" :data="editedParams" height="400">
          <template #configName="{ row }">
            <div :style="`font-style: italic;font-size: 13px;font-weight: 500;color: ${row.needCreated ? '#ff6e0d' : ''}`">
              {{ row.configName }}
              {{ row.needCreated ? $t('dai-chuang-jian') : '' }}
            </div>
          </template>
          <template #editedConfigValue="{ row }">
            <div>
              <p style="position: relative; padding-right: 15px">
                <a
                  v-if="isJSON(row.configValue, row)"
                  @click="handleShowJson(row, 'read')"
                  style="
                    white-space: nowrap;
                    text-overflow: ellipsis;
                    overflow: hidden;
                    word-break: break-all;
                    display: block;
                    color: rgba(0, 0, 0, 0.88);
                  "
                >
                  {{ row.configValue }}
                  <span style="position: absolute; right: 0; font-size: 14px" @click="handleShowJson(row, 'read')">
                    <Icon type="md-arrow-dropdown" />
                  </span>
                </a>
                <span v-if="!isJSON(row.configValue, row)">
                  {{ row.configValue ? row.configValue : '-' }}
                </span>
              </p>
            </div>
          </template>
          <template #editedCurrentCount="{ row }">
            <div>
              <p style="position: relative; padding-right: 15px">
                <a
                  v-if="isJSON(row.currentCount, row)"
                  @click="handleShowJson(row, 'read', 'currentCount')"
                  style="
                    white-space: nowrap;
                    text-overflow: ellipsis;
                    overflow: hidden;
                    word-break: break-all;
                    display: block;
                    color: rgba(0, 0, 0, 0.88);
                  "
                >
                  {{ row.currentCount }}
                  <span style="position: absolute; right: 0; font-size: 14px" @click="handleShowJson(row, 'read')">
                    <Icon type="md-arrow-dropdown" />
                  </span>
                </a>
                <span v-if="!isJSON(row.currentCount, row)">
                  {{ row.currentCount ? row.currentCount : '-' }}
                </span>
              </p>
            </div>
          </template>
        </Table>
      </div>
      <template #footer>
        <div v-if="showParamsEditConfirm">
          <Button @click="handleCancelEditParams">{{ $t('qu-xiao') }}</Button>
          <Button type="primary" @click="handleConfirmEditParams">{{ $t('que-ren') }}</Button>
        </div>
      </template>
    </CCModal>
  </div>
</template>
<script>
import { pick } from '@/components/function/monitor/utils/colors';
import { handleCopy } from '@/utils/clipboard';
import ConfigParamsEdit from '@/views/system/ConfigParamsEdit';
import DmDsParamsPanel from '@/views/system/datasource/components/DmDsParamsPanel';
import UtilJson from '../util';

export default {
  name: 'ParamsEdit',
  components: {
    ConfigParamsEdit,
    DmDsParamsPanel
  },
  data() {
    return {
      activeTab: 'datasource-params',
      loading: false,
      dsKvConfigs: [],
      currentMode: 'view',
      // tagList: [],
      dsId: 0,
      instanceId: '',
      selectedJson: {},
      editJson: {},
      selectedRow: '',
      showJson: false,
      currentValue: 0,
      selectedData: '',
      selectedParam: {},
      showParamsEditConfirm: false,
      showTagList: [],
      templateList: [],
      paramsColumn: [
        {
          title: this.$t('can-shu-ming-cheng'),
          key: 'configName',
          slot: 'paramName',
          width: 280,
          sortable: true
        },
        {
          title: this.$t('can-shu-mo-ren-zhi'),
          key: 'defaultValue',
          width: 150,
          render: (h, params) => h('div', {}, params.row.defaultValue || '-')
        },
        {
          title: this.$t('can-shu-dang-qian-yun-hang-zhi'),
          key: 'configValue',
          slot: 'configValue',
          minWidth: 140
        },
        {
          title: this.$t('xiu-gai-hou-de-can-shu-zhi'),
          key: 'currentCount',
          slot: 'currentValue',
          width: 160,
          filters: [
            {
              label: this.$t('ke-xiu-gai-can-shu'),
              value: 1
            },
            {
              label: this.$t('zhi-du-can-shu'),
              value: 2
            }
          ],
          filterMultiple: false,
          filterMethod(value, row) {
            if (value === 1) {
              return !row.readOnly;
            }
            if (value === 2) {
              return row.readOnly;
            }
          }
        },
        {
          title: this.$t('ke-xiu-gai-fan-wei'),
          key: 'valueRange'
        },
        {
          title: this.$t('biao-qian'),
          width: 150,
          slot: 'tag'
        }
      ],
      editedParams: [],
      editColumns: [
        {
          title: this.$t('can-shu-ming'),
          key: 'configName',
          slot: 'configName',
          width: 300
        },
        {
          title: this.$t('dang-qian-yun-hang-zhi'),
          key: 'configValue',
          slot: 'editedConfigValue'
        },
        {
          title: this.$t('xiu-gai-hou-de-zhi'),
          key: 'currentCount',
          slot: 'editedCurrentCount'
        }
      ]
    };
  },
  mounted() {
    this.dsId = this.$route.params.id;
    this.instanceId = this.$route.params.instanceId;
    this.queryDsConfig();
    // getConfigTagList().then((res) => {
    //   if (res.data.code === '1') {
    //     this.tagList = res.data.data;
    //   }
    // });
  },
  computed: {
    getParamsData() {
      return this[this.currentTab];
    }
  },
  methods: {
    async handleRefresh() {
      await this.queryDsConfig(true);
    },
    handleCloseShowJson() {
      this.showJson = false;
    },
    getTagStyle(row) {
      return {
        color: pick(this.showTagList.indexOf(row.userConfigTagType)),
        borderColor: pick(this.showTagList.indexOf(row.userConfigTagType))
      };
    },
    updateDsKvConfig(dsKvConfigs) {
      this.dsKvConfigs = [...dsKvConfigs];
    },
    async queryDsConfig(isRefresh = false) {
      this.loading = true;

      try {
        const res = await this.$services.rdpDataSourceQueryDsConfig({
          data: {
            dataSourceId: this.dsId
          }
        });

        if (res.success) {
          this.dsKvConfigs = res.data;
          this.dsKvConfigs.forEach((config) => {
            if (config.configValue && config.confValType === 'BOOLEAN') {
              config.formatValue = JSON.parse(config.configValue);
            }
          });
        }
      } catch (error) {
        console.error('error:', error);
      } finally {
        this.loading = false;
      }
    },
    isJSON(str, row) {
      if (row.confValType === 'JSON') {
        return true;
      }
      if (typeof str === 'string') {
        try {
          const obj = JSON.parse(str);

          return Boolean(typeof obj === 'object' && obj);
        } catch (e) {
          return false;
        }
      }
    },
    handleShowJson(row, type, key) {
      if (type === 'edit') {
        this.currentMode = 'tree';
      } else {
        this.currentMode = 'view';
      }
      this.selectedRow = row;
      if (key) {
        if (!row[key]) {
          row[key] = row.configValue;
        }
        this.selectedJson = JSON.parse(row[key]);
        this.editJson = JSON.parse(row[key]);
      } else {
        this.selectedJson = JSON.parse(row.configValue);
        this.editJson = JSON.parse(row.configValue);
      }
      this.showJson = true;
    },
    handleShowConfirmEditParams() {
      this.editedParams = [];
      this.showParamsEditConfirm = true;
      Object.values(this.dsKvConfigs).forEach((item) => {
        if (item.needCreated || ((item.currentCount === 0 || item.currentCount) && item.currentCount !== item.configValue)) {
          this.editedParams.push(item);
          console.log('item123', item);
        }
      });
    },
    async handleConfirmEditParams() {
      this.showParamsEditConfirm = false;
      if (this.editedParams.length === 0) {
        this.$Modal.warning({
          title: this.$t('cao-zuo-yi-chang'),
          content: this.$t('qing-xiu-gai-xu-yao-sheng-xiao-de-can-shu')
        });
        return;
      }
      const updateConfigs = {};
      const needCreateConfigs = {};

      console.log('this.editedParams', this.editedParams);

      this.editedParams.forEach((config) => {
        if (config.needCreated) {
          needCreateConfigs[config.configName] = config.currentCount || config.defaultValue;
        } else {
          updateConfigs[config.configName] = config.currentCount;
        }
      });

      // this.dsKvConfigs.forEach((config) => {
      //   const {
      //     needCreated, defaultValue, configValue, currentCount, configName
      //   } = config;
      //
      //   if (needCreated) {
      //     needCreateConfigs[configName] = currentCount || defaultValue;
      //   } else {
      //     console.log('config', config);
      //     if (currentCount && currentCount !== configValue) {
      //       updateConfigs[configName] = currentCount;
      //     }
      //   }
      // });

      const res = await this.$services.rdpDataSourceUpsertDsConfig({
        data: {
          dataSourceId: this.dsId,
          needCreateConfigs,
          updateConfigs
        }
      });

      if (res.success) {
        this.queryDsConfig();
        this.$Message.success(this.$t('xiu-gai-cheng-gong'));
      }
    },
    handleEditCurrent(row) {
      const type = this.currentTab;
      const data = this[this.currentTab];
      data.map((item) => {
        if (item.configName === row.configName) {
          item.currentCount = this.currentValue;
        }
        return null;
      });
      this[type] = Object.assign([], this[type]);
      // row.currentCount = this.currentValue;
      this.currentValue = '';
      row.visible = false;
    },
    handlePopShow(row) {
      this.currentValue = row.configValue;
    },
    handleCancelEdit(row) {
      const type = this.currentTab;
      const data = this[this.currentTab];
      data.map((item) => {
        if (item.configName === row.configName) {
          item.currentCount = '';
        }
        return null;
      });
      this[type] = Object.assign([], this[type]);
    },
    handleCancelEditParams() {
      this.editedParams = [];
      this.showParamsEditConfirm = false;
      this.showJson = false;
      this.showParamsEdit = false;
    },
    onJsonChange(data) {
      this.editJson = data;
      this.selectedJson = data;
    },
    handleEditJson() {
      this.showJson = false;
      if (this.currentMode === 'tree') {
        this.currentValue = JSON.stringify(this.editJson);
        this.handleEditCurrent('mappingParamsData', this.mappingParamsData, this.selectedRow);
        this.editJson = {};
      } else {
        this.$Modal.warning({
          title: this.$t('xiu-gai-can-shu-ti-shi'),
          content: this.$t('qing-dian-ji-xiu-gai-tu-biao-jin-hang-xiu-gai')
        });
      }
    },
    handleCopyJson(data) {
      handleCopy(data);
      this.$Message.success(this.$t('fu-zhi-cheng-gong'));
    }
  }
};
</script>
<style lang="less">
.page-header {
  /*margin-top: 24px;*/
  /*margin-left: -16px;*/
  /*margin-right: -16px;*/

  .page-header-container {
    background: #fff;
    border-bottom: 1px solid #e8eaec;
  }

  .page-header-function {
    display: flex;
    align-items: center;
    gap: 10px;
  }

  .page-header-detail {
    display: flex;
  }

  .page-header-main {
    width: 100%;
    flex: 0 1 auto;

    .ivu-page-header-row {
      display: -webkit-box;
      display: -ms-flexbox;
      display: flex;
    }
  }

  .ivu-page-header-row {
    width: 100%;
  }

  .ivu-page-header-title {
    margin-bottom: 16px;
    -webkit-box-flex: 1;
    -ms-flex: auto;
    flex: auto;
    display: inline-block;
    color: #17233d;
    font-weight: 500;
    font-size: 20px;
  }

  .ivu-page-header-content {
    margin-bottom: 16px;
    -webkit-box-flex: 1;
    -ms-flex: auto;
    flex: auto;
    font-size: 14px;
  }
}

.params-edit-wrapper {
  /*padding: 24px;*/
  /*background: #ffffff;*/
  margin-top: 10px;
  /*border: 1px solid #DADADA;*/
  height: 660px;
  /*overflow: auto;*/

  .ivu-tabs-nav-scroll {
    background-color: #ffffff;
    border-top: 1px solid #dadada;
    border-left: 1px solid #dadada;
    border-right: 1px solid #dadada;
  }

  .iconfont {
    color: #8d95a6;
    cursor: pointer;
  }
}

.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 400px;
  color: #999;
}
</style>
