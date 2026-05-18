<template>
  <div>
    <Table border size="small" :columns="paramsColumn" :data="dsKvConfigs" max-height="600" stripe :loading="loading">
      <template #configValue="{ row }">
        <p style="position: relative; padding-right: 15px">
          <span v-if="isJSON(row)" style="white-space: nowrap; text-overflow: ellipsis; overflow: hidden; word-break: break-all; display: block">
            {{ row.configValue }}
            <a style="position: absolute; right: 0; font-size: 14px" @click="handleShowJson(row)">
              <Icon type="md-arrow-dropdown" />
            </a>
          </span>
          <span v-if="!isJSON(row)">{{ row.configValue ? row.configValue : '-' }}</span>
        </p>
      </template>
      <template #defaultValue="{ row }">
        <p style="position: relative; padding-right: 15px">
          <span v-if="isJSON(row)" style="white-space: nowrap; text-overflow: ellipsis; overflow: hidden; word-break: break-all; display: block">
            {{ row.defaultValue }}
            <a style="position: absolute; right: 0; font-size: 14px" @click="handleDefaultValueShowJson(row)">
              <Icon type="md-arrow-dropdown" />
            </a>
          </span>
          <span v-if="!isJSON(row)">{{ row.defaultValue ? row.defaultValue : '-' }}</span>
        </p>
      </template>
      <template #paramName="{ row }">
        <div style="position: relative">
          <span :style="`font-style: italic;font-size: 14px;font-weight: 500;color: ${row.needCreated ? '#ff6e0d' : ''}`">
            {{ row.configName }}
            {{ row.needCreated ? $t('dai-chuang-jian') : '' }}
            <Tooltip transfer placement="right" style="position: absolute; right: 0; top: 0">
              <template #content>
                <div v-html="row.description"></div>
              </template>
              <a><CustomIcon size="14px" type="icon-v2-InfoColorful" /></a>
            </Tooltip>
          </span>
        </div>
      </template>
      <template #currentValue="{ row }">
        <div>
          <div v-if="!row.readOnly">
            <div v-if="row.confValType === 'BOOLEAN'">
              <i-switch true-color="#52C41A" v-model="row.formatValue" @on-change="handleEditCurrent(row, $event)"></i-switch>
            </div>
            <div v-if="row.confValType !== 'BOOLEAN'">
              <span style="margin-right: 16px; display: inline-block; width: 210px">
                {{ row.currentCount }}
              </span>
              <span>
                <Poptip v-if="!isJSON(row)" v-model="row.visible" transfer @on-popper-show="handlePopShow(row)" placement="right" width="250">
                  <a><CustomIcon type="icon-v2-EditSimple" /></a>
                  <template #content>
                    <div>
                      <p style="font-size: 12px; margin-bottom: 10px">{{ $t('xiu-gai-can-shu-wei') }}:</p>
                      <p style="margin-bottom: 10px">
                        <Input size="small" type="textarea" :rows="3" v-model="currentValue" />
                      </p>
                      <p>
                        <Button style="margin-right: 5px" type="primary" size="small" @click="handleEditCurrent(row, currentValue)">
                          {{ $t('que-ding') }}
                        </Button>
                      </p>
                    </div>
                  </template>
                </Poptip>
                <a v-else>
                  <CustomIcon @click="handleOpenShowEditJson(row)" type="icon-v2-EditSimple" />
                </a>
                <Tooltip transfer style="margin-left: 5px" :content="$t('che-xiao')" placement="right" v-if="row.currentCount">
                  <a style="font-size: 16px" @click="handleCancelEdit(row)">
                    <CustomIcon type="icon-v2-Reset" />
                  </a>
                </Tooltip>
              </span>
            </div>
          </div>
          <div v-if="row.readOnly">
            {{ $t('zhi-du-can-shu') }}
          </div>
        </div>
      </template>
      <template #tag="{ row }">
        <Button type="warning" size="small" ghost :style="getTagStyle(row)">
          {{ row.configGroup }}
        </Button>
      </template>
      <template #action="{ row }">
        <Button ghost type="primary" size="small" style="margin-right: 5px" @click="handleShowEditParams(row, 'serviceCoreParamsData')">
          {{ $t('xiu-gai') }}
        </Button>
      </template>
    </Table>
    <Modal v-model="showJson" :title="$t('wan-zheng-can-shu-zhan-shi')" width="700px" footer-hide>
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
        <div class="modal-footer" style="margin-top: 20px">
          <Button @click="handleCloseShowJson">{{ $t('guan-bi') }}</Button>
        </div>
      </template>
    </Modal>
    <CCModal v-model="showEditJson" :title="$t('xiu-gai-can-shu-wei')" width="700px" footer-hide>
      <div style="position: relative">
        <Tooltip
          v-if="isJSON(selectedRow)"
          transfer
          style="display: flex; justify-content: flex-end"
          :content="$t('json-ge-shi-hua')"
          placement="right"
        >
          <!--          <i class="ivu-icon ivu-icon-md-code format-icon" @click="formatJson()"></i>-->
          <CustomIcon class="mb-2 pointer" type="icon-v2-json" size="20px" @click="formatJson()" />
        </Tooltip>
        <div class="input-json-area">
          <Input size="small" type="textarea" :rows="20" v-model="currentValue" />
        </div>
      </div>
      <template #footer>
        <div class="modal-footer" style="margin-top: 20px">
          <Button type="primary" @click="handleConfirmEditJson()">{{ $t('que-ren') }}</Button>
          <Button @click="handleCloseShowEditJson">{{ $t('guan-bi') }}</Button>
        </div>
      </template>
    </CCModal>
  </div>
</template>
<script>
import { pick } from '@/components/function/monitor/utils/colors';
import { handleCopy } from '@/utils/clipboard';

export default {
  name: 'ConfigParamsEdit',
  props: {
    dsKvConfigs: Array,
    loading: Boolean
  },
  data() {
    return {
      showEditJson: false,
      tagList: [],
      selectedJson: {},
      editJson: {},
      selectedRow: '',
      showJson: false,
      showParamsEdit: false,
      currentValue: 0,
      selectedData: '',
      selectedParam: {},
      showParamsEditConfirm: false,
      showTagList: [],
      paramsColumn: [
        {
          title: this.$t('can-shu-ming-cheng'),
          key: 'configName',
          slot: 'paramName',
          minWidth: 230,
          sortable: true
        },
        {
          title: this.$t('can-shu-dang-qian-yun-hang-zhi'),
          key: 'configValue',
          minWidth: 200,
          slot: 'configValue'
        },
        {
          title: this.$t('mo-ren-zhi'),
          minWidth: 200,
          key: 'defaultValue',
          // render: (h, params) => h('div', {}, params.row.defaultValue || '-')
          slot: 'defaultValue'
        },
        {
          title: this.$t('xiu-gai-hou-de-can-shu-zhi'),
          key: 'currentCount',
          slot: 'currentValue',
          minWidth: 300,
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
          key: 'valueAdvance',
          minWidth: 300
        },
        {
          title: this.$t('biao-qian'),
          minWidth: 150,
          slot: 'tag'
        }
      ],
      editedParams: []
    };
  },
  mounted() {
    // getConfigTagList()
    //   .then((res) => {
    //     if (res.data.code === '1') {
    //       this.tagList = res.data.data;
    //     }
    //   });
  },
  watch: {
    dsKvConfigs: {
      immediate: true,
      deep: true,
      handler(configs = []) {
        configs.forEach((config) => {
          if (config.confValType === 'BOOLEAN' && typeof config.formatValue !== 'boolean') {
            config.formatValue = this.toBoolean(config.currentCount ?? config.configValue ?? config.defaultValue);
          }
        });
      }
    }
  },
  methods: {
    toBoolean(value) {
      return value === true || value === 'true';
    },
    getTagStyle(row) {
      return {
        color: pick(this.showTagList.indexOf(row.userConfigTagType)),
        borderColor: pick(this.showTagList.indexOf(row.userConfigTagType))
      };
    },
    formatJson() {
      this.currentValue = this.getFormattedJson(this.currentValue);
    },
    getFormattedJson(value) {
      try {
        return JSON.stringify(JSON.parse(value), null, 2);
      } catch (error) {
        return value;
      }
    },
    handleCopyJson(data) {
      handleCopy(data);
      this.$Message.success(this.$t('fu-zhi-cheng-gong'));
    },
    handleOpenShowEditJson(row) {
      this.selectedRow = row;
      this.currentValue = this.getFormattedJson(row.currentCount || row.configValue || row.defaultValue || '');
      this.showEditJson = true;
    },
    handleCloseShowEditJson() {
      this.showEditJson = false;
      this.currentValue = '';
      this.selectedRow = null;
    },
    handleConfirmEditJson() {
      if (!this.selectedRow) {
        return;
      }
      let compressedJson = this.currentValue;
      try {
        compressedJson = JSON.stringify(JSON.parse(this.currentValue));
      } catch (Error) {
        console.error(Error);
      }
      this.selectedRow.currentCount = compressedJson;
      this.dsKvConfigs.forEach((item) => {
        if (item.configName === this.selectedRow.configName) {
          item.currentCount = compressedJson;
        }
      });
      // this.dsKvConfigs = [...this.dsKvConfigs];
      this.$emit('updateDsKvConfig', this.dsKvConfigs);
      this.handleCloseShowEditJson();
    },
    handleCloseShowJson() {
      this.showJson = false;
    },
    handleCancelEditParams() {
      this.editedParams = [];
      this.showParamsEditConfirm = false;
      this.showJson = false;
      this.showParamsEdit = false;
    },
    showUserConfigModal() {
      this.editedParams = [];
      this.userConfigList.forEach((item) => {
        if (((item.currentCount === 0 || item.currentCount) && item.currentCount !== item.configValue) || item.needCreated) {
          this.editedParams.push(item);
        }
      });

      if (this.editedParams.length === 0) {
        this.$Modal.warning({
          title: this.$t('cao-zuo-yi-chang'),
          content: this.$t('qing-xiu-gai-xu-yao-sheng-xiao-de-can-shu')
        });
      } else {
        this.showParamsEditConfirm = true;
      }
    },
    hideUserConfigModal() {
      this.showParamsEditConfirm = false;
    },
    isJSON(row) {
      if (!row) {
        return false;
      }
      if (row.confValType === 'JSON') {
        return true;
      }
      const str = row.configValue;
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
    handleDefaultValueShowJson(row, key) {
      this.currentMode = 'view';
      this.selectedRow = row;
      if (key) {
        if (!row[key]) {
          row[key] = row.defaultValue;
        }
        this.selectedJson = JSON.parse(row[key]);
        this.editJson = JSON.parse(row[key]);
      } else {
        this.selectedJson = JSON.parse(row.defaultValue);
        this.editJson = JSON.parse(row.defaultValue);
      }
      this.showJson = true;
    },
    handleShowEditParams(row, data) {
      row.currentCount = row.count;
      this.selectedParam = row;
      this.selectedData = data;
      this.showParamsEdit = true;
    },
    handleEditCurrent(row, value) {
      let currentCount = value;
      if (row.confValType === 'BOOLEAN') {
        const boolValue = this.toBoolean(value);
        row.formatValue = boolValue;
        currentCount = JSON.stringify(boolValue);
      }
      row.currentCount = currentCount;
      this.dsKvConfigs.forEach((item) => {
        if (item.configName === row.configName) {
          item.currentCount = currentCount;
          if (item.confValType === 'BOOLEAN') {
            item.formatValue = row.formatValue;
          }
        }
      });
      this.currentValue = '';
      row.visible = false;
      this.$emit('updateDsKvConfig', this.dsKvConfigs);
    },
    handlePopShow(row) {
      this.currentValue = row.configValue;
    },
    handleCancelEdit(row) {
      this.dsKvConfigs.forEach((item) => {
        if (item.configName === row.configName) {
          item.currentCount = '';
        }
      });
      // this.dsKvConfigs = [...this.dsKvConfigs];
      this.$emit('updateDsKvConfig', this.dsKvConfigs);
    }
  }
};
</script>
<style lang="less" scoped>
.input-json-area textarea {
  max-height: 500px !important;
}

.param-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 10px;
}

.param-header p {
  margin: 0;
  font-size: 12px;
  flex-grow: 1;
}

.format-icon {
  font-size: 16px;
  cursor: pointer;
  transition: color 0.3s ease;
  color: #0bb9f8;
}

.page-header {
  /*margin-top: 24px;*/
  /*margin-left: -16px;*/
  /*margin-right: -16px;*/

  .page-header-container {
    background: #fff;
    border-bottom: 1px solid #e8eaec;
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
</style>
