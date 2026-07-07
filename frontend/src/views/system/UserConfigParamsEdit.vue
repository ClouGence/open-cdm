<template>
  <div>
    <div>
      <div class="params-container">
        <nav class="params-tabs">
          <div class="params-tabs__items">
            <span class="params-tabs__item" :class="{ 'is-active': activeTab === 'COMMON' }" @click="handleTabClick('COMMON')">
              {{ $t('tong-yong-can-shu') }}
            </span>
            <span
              v-if="includesCC"
              class="params-tabs__item"
              :class="{ 'is-active': activeTab === 'CLOUDCANAL' }"
              @click="handleTabClick('CLOUDCANAL')"
            >
              CloudCanal
            </span>
            <span v-if="includesDM" class="params-tabs__item" :class="{ 'is-active': activeTab === 'CLOUDDM' }" @click="handleTabClick('CLOUDDM')">
              CloudDM
            </span>
          </div>
          <div class="params-tabs__actions">
            <slot name="actions"></slot>
          </div>
        </nav>
        <div v-show="activeTab === 'COMMON'">
          <user-config-table
            :can-edit="canEdit"
            :user-config-map="userConfigMap.COMMON"
            :handle-show-json="handleShowJson"
            :handle-edit-current="handleEditCurrent"
            :handle-show-edit-params="handleShowEditParams"
            :is-j-s-o-n="isJSON"
            :show-tag-list="showTagList"
            :loading="refreshLoading"
          ></user-config-table>
        </div>
        <div v-show="activeTab === 'CLOUDCANAL'" v-if="includesCC">
          <user-config-table
            :can-edit="canEdit"
            :user-config-map="userConfigMap.CLOUDCANAL"
            :handle-show-json="handleShowJson"
            :handle-edit-current="handleEditCurrent"
            :handle-show-edit-params="handleShowEditParams"
            :is-j-s-o-n="isJSON"
            :show-tag-list="showTagList"
            :loading="refreshLoading"
          ></user-config-table>
        </div>
        <div v-show="activeTab === 'CLOUDDM'" v-if="includesDM">
          <user-config-table
            :can-edit="canEdit"
            :user-config-map="userConfigMap.CLOUDDM"
            :handle-show-json="handleShowJson"
            :handle-edit-current="handleEditCurrent"
            :handle-show-edit-params="handleShowEditParams"
            :is-j-s-o-n="isJSON"
            :show-tag-list="showTagList"
            :loading="refreshLoading"
          ></user-config-table>
        </div>
      </div>
    </div>
    <CCModal v-model="showParamsEditConfirm" :title="$t('que-ding-yao-sheng-xiao-yi-xia-pei-zhi')" footer-hide width="800px">
      <div style="max-height: 500px; overflow: auto">
        <div id="elem"></div>
        <Table border size="small" :columns="editColumns" :data="editedParams">
          <template #configName="{ row }">
            <div :style="`color: ${row.needCreated ? '#ff6e0d' : ''}`">
              {{ row.configName }}
              {{ row.needCreated ? $t('dai-chuang-jian') : '' }}
            </div>
          </template>
          <template #editedConfigValue="{ row }">
            <div>
              <p style="position: relative; padding-right: 15px">
                <a
                  v-if="isJSON(row.configValue)"
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
                <span v-if="!isJSON(row.configValue)">
                  {{ row.configValue ? row.configValue : '-' }}
                </span>
              </p>
            </div>
          </template>
          <template #editedCurrentCount="{ row }">
            <div>
              <p style="position: relative; padding-right: 15px">
                <a
                  v-if="isJSON(row.currentCount)"
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
                <span v-if="!isJSON(row.currentCount)">
                  {{ row.currentCount ? row.currentCount : '-' }}
                </span>
              </p>
            </div>
          </template>
          <template #tag="{ row }">
            <Button type="warning" size="small" ghost :style="getTagStyle(row)">
              {{ row.userConfigTagType }}
            </Button>
          </template>
        </Table>
      </div>
      <template #footer>
        <Button @click="hideUserConfigModal">{{ $t('qu-xiao') }}</Button>
        <Button type="primary" :loading="loading" @click="updateUserConfig">
          {{ $t('que-ren') }}
        </Button>
      </template>
    </CCModal>
  </div>
</template>
<script>
import { pick } from '@/components/function/monitor/utils/colors';
import UserConfigTable from '@/views/system/UserConfigTable';
import { isManagedUserConfig } from '@/views/system/managedUserConfigs';
import { mapGetters } from 'vuex';
import UtilJson from '../util';

export default {
  name: 'UserConfigParamsEdit',
  props: {
    canEdit: Boolean,
    refreshLoading: Boolean
  },
  components: { UserConfigTable },
  computed: {
    ...mapGetters(['includesCC', 'includesDM', 'userInfo'])
  },
  data() {
    return {
      activeTab: 'COMMON',
      loading: false,
      userConfigList: [],
      userConfigMap: {
        COMMON: [],
        CLOUDCANAL: [],
        CLOUDDM: [],
        BLADEPIPE: []
      },
      currentTab: 'serviceCoreParamsData',
      tabLabels: [
        { name: 'serviceCoreParamsData', label: this.$t('ren-wu-he-xin-pei-zhi') },
        { name: 'sourceParamsData', label: this.$t('yuan-shu-ju-yuan-pei-zhi') },
        { name: 'targetParamsData', label: this.$t('mu-biao-shu-ju-yuan-pei-zhi') },
        { name: 'mappingParamsData', label: this.$t('ying-she-pei-zhi') }
      ],
      currentMode: 'view',
      tagList: [],
      ifRunning: false,
      sourceType: '',
      targetType: '',
      jobId: 0,
      jobInfo: '',
      selectedJson: {},
      editJson: {},
      selectedRow: '',
      showJson: false,
      showParamsEdit: false,
      currentValue: '',
      selectedData: '',
      selectedParam: {},
      showParamsEditConfirm: false,
      showTagList: [],
      jobName: '',
      searchKey: {
        key: '',
        tag: []
      },
      paramsColumn: [
        {
          title: this.$t('can-shu-ming-cheng'),
          key: 'configName',
          slot: 'paramName',
          width: 300,
          sortable: true
        },
        {
          title: this.$t('can-shu-dang-qian-yun-hang-zhi'),
          key: 'configValue',
          width: 260,
          slot: 'configValue'
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
      sourceParamsData: [],
      targetParamsData: [],
      mappingParamsData: [],
      serviceCoreParamsData: [],
      businessParamsData: [],
      sourceParamsDataMap: {},
      targetParamsDataMap: {},
      mappingParamsDataMap: {},
      serviceCoreParamsDataMap: {},
      businessParamsDataMap: {},
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
    this.getUserConfig();
    // getConfigTagList()
    //   .then((res) => {
    //     if (res.data.code === '1') {
    //       this.tagList = res.data.data;
    //     }
    //   });
  },
  methods: {
    handleTabClick(name) {
      this.activeTab = name;
    },
    getTagStyle(row) {
      return {
        color: pick(this.showTagList.indexOf(row.userConfigTagType)),
        borderColor: pick(this.showTagList.indexOf(row.userConfigTagType))
      };
    },
    showUserConfigModal() {
      this.editedParams = [];
      this.userConfigMap.COMMON.forEach((item) => {
        if (item.confValType === 'BOOLEAN' && typeof item.currentCount === 'boolean') {
          item.currentCount = JSON.stringify(item.currentCount);
        }
        if (((item.currentCount === 0 || item.currentCount) && item.currentCount !== item.configValue) || item.needCreated) {
          this.editedParams.push(item);
        }
      });
      if (this.includesCC) {
        this.userConfigMap.CLOUDCANAL.forEach((item) => {
          if (item.confValType === 'BOOLEAN' && typeof item.currentCount === 'boolean') {
            item.currentCount = JSON.stringify(item.currentCount);
          }
          if (((item.currentCount === 0 || item.currentCount) && item.currentCount !== item.configValue) || item.needCreated) {
            this.editedParams.push(item);
          }
        });
      }
      if (this.includesDM) {
        this.userConfigMap.CLOUDDM.forEach((item) => {
          if (item.confValType === 'BOOLEAN' && typeof item.currentCount === 'boolean') {
            item.currentCount = JSON.stringify(item.currentCount);
          }
          if (((item.currentCount === 0 || item.currentCount) && item.currentCount !== item.configValue) || item.needCreated) {
            this.editedParams.push(item);
          }
        });
      }

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
    async updateUserConfig() {
      this.loading = true;
      const updateConfigs = {};
      const needCreateConfigs = {};
      this.editedParams.forEach((userConfig) => {
        if (userConfig.needCreated) {
          needCreateConfigs[userConfig.configName] = userConfig.currentCount || userConfig.defaultValue;
        } else {
          updateConfigs[userConfig.configName] = userConfig.currentCount;
        }
      });
      const res = await this.$services.rdpUserConfigUpsertUserConfigs({ data: { updateConfigs, needCreateConfigs } });
      if (res.success) {
        this.$Message.success(this.$t('geng-xin-cheng-gong'));
        this.showParamsEditConfirm = false;
        await this.getUserConfig();
      }
      this.loading = false;
    },
    async getUserConfig() {
      this.userConfigMap = {
        COMMON: [],
        CLOUDCANAL: [],
        CLOUDDM: [],
        BLADEPIPE: []
      };
      this.showTagList = [];
      const res = await this.$services.rdpUserConfigGetCurrUserConfigs();
      if (res.success) {
        this.userInfo.userConfig = res.data;
        const visibleConfigs = res.data.filter((config) => !isManagedUserConfig(config.configName));
        visibleConfigs.forEach((user) => {
          if (this.showTagList.indexOf(user.userConfigTagType) === -1) {
            this.showTagList.push(user.userConfigTagType);
          }
        });
        this.$store.commit('updateUserInfo', this.userInfo);
        this.userConfigList = res.data;
        visibleConfigs.forEach((config) => {
          if (config.configValue && config.confValType === 'BOOLEAN') {
            config.formatValue = JSON.parse(config.configValue);
          }
          if (config.confBelong === 'Common') {
            this.userConfigMap.COMMON.push(config);
          } else if (config.confBelong === 'CloudCanal') {
            this.userConfigMap.CLOUDCANAL.push(config);
          } else if (config.confBelong === 'CloudDM') {
            this.userConfigMap.CLOUDDM.push(config);
          } else if (config.confBelong === 'BladePipe') {
            this.userConfigMap.BLADEPIPE.push(config);
          }
        });
      }
    },
    isJSON(str) {
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
    handleReturn() {
      this.$router.back();
    },
    handleShowEditParams(row, data) {
      row.currentCount = row.count;
      this.selectedParam = row;
      this.selectedData = data;
      this.showParamsEdit = true;
    },
    handleEditCurrent(row, currentValue) {
      row.currentCount = currentValue;
      if (row.confValType === 'BOOLEAN') {
        row.formatValue = currentValue;
      }
      this.userConfigList.forEach((item) => {
        if (item.configName === row.configName) {
          item.currentCount = currentValue;
          if (item.confValType === 'BOOLEAN') {
            item.formatValue = currentValue;
          }
        }
      });
      row.visible = false;
    },
    handleCancelEdit(row) {
      this.userConfigList.forEach((item) => {
        if (item.configName === row.configName) {
          item.currentCount = '';
        }
      });
      this.userConfigList = [...this.userConfigList];
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

.params-tabs {
  display: flex;
  align-items: center;
  justify-content: space-between;
  flex-shrink: 0;
  padding: 0 20px 0 0;
  background: var(--bg-card);

  &__items {
    display: flex;
    align-items: center;
    gap: 4px;
  }

  &__actions {
    display: flex;
    align-items: center;
    gap: 6px;
    padding-right: 20px;
  }

  &__item {
    position: relative;
    padding: 12px 20px 10px;
    color: var(--text-secondary);
    font-size: 13px;
    font-weight: 400;
    line-height: 1.4;
    cursor: pointer;
    border-bottom: none;
    transition: color 0.12s ease;

    &:hover {
      color: var(--text-primary);
      border-bottom: none;
    }

    &.is-active {
      color: var(--text-primary);
      font-weight: 500;

      &::after {
        content: '';
        position: absolute;
        left: 20px;
        right: 20px;
        bottom: 0;
        height: 2px;
        border-radius: 2px 2px 0 0;
        background: var(--primary-color);
      }
    }
  }
}

.params-edit-wrapper {
  margin-top: 10px;

  .iconfont {
    color: #8d95a6;
    cursor: pointer;
  }
}
</style>
