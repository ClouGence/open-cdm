<template>
  <div class="content-wrapper add-datasource-page">
    <CCModal
      v-model="showAddDataSourceTypeModal"
      :width="1040"
      :title="$t('xuan-ze-shu-ju-yuan-lei-xing')"
      class="add-datasource-type-modal"
      @on-cancel="handleCloseAddDataSourceTypeModal"
    >
      <div class="add-datasource-type-modal-body">
        <Input
          v-model="addDataSourceTypeSearchKey"
          class="add-datasource-type-search"
          clearable
          :placeholder="$t('sou-suo-shu-ju-yuan-lei-xing')"
        >
          <template #prefix>
            <Icon type="ios-search" />
          </template>
        </Input>
        <div class="add-datasource-type-grid" :class="{ 'is-empty': !filteredAddDataSourceTypes.length }">
          <button
            v-for="type in filteredAddDataSourceTypes"
            :key="type.dsKey"
            type="button"
            class="add-datasource-type-card"
            :class="{ active: selectedAddDataSourceType === type.dsKey }"
            :aria-pressed="selectedAddDataSourceType === type.dsKey"
            @click="handleSelectAddDataSourceType(type.dsKey)"
          >
            <span class="add-datasource-type-icon">
              <DataSourceIcon size="20px" :type="type.dsKey" leftMargin="0"></DataSourceIcon>
            </span>
            <span class="add-datasource-type-name" :title="type.displayName">{{ type.displayName }}</span>
          </button>
          <div v-if="!filteredAddDataSourceTypes.length" class="add-datasource-type-empty">
            {{ $t('zan-wu-shu-ju') }}
          </div>
        </div>
      </div>
      <template #footer>
        <div class="add-datasource-type-footer">
          <Button @click="handleCloseAddDataSourceTypeModal">{{ $t('qu-xiao') }}</Button>
          <Button type="primary" :disabled="!selectedAddDataSourceType" @click="handleConfirmAddDataSourceType">
            {{ $t('que-ding') }}
          </Button>
        </div>
      </template>
    </CCModal>
    <div class="add-datasource-wrapper">
      <div class="add-datasource-content">
        <DataSourceInfo
          :addDataSourceForm="addDataSourceForm"
          v-if="currentStep === 1"
          ref="dataSourceInfo"
          :current-step="currentStep"
          :show-query-config="shouldAutoEnableFeatures"
          :auto-enable-features="shouldAutoEnableFeatures"
          :driver-family-map="driverFamilyMap"
          :ds-id="editDataSourceId"
          :edit-mode="editMode"
          @driver-status-change="handleDriverStatusChange"
          @config-loaded="handleConfigLoaded"
        ></DataSourceInfo>
        <SuccessAdd v-if="currentStep > 2"></SuccessAdd>
      </div>
      <div v-if="currentStep === 1" class="add-dataSource-tools">
        <div class="add-dataSource-actions">
          <Button @click="handleTestConnection" :loading="testConnectionLoading" v-if="currentStep === 1">
            {{ $t('ce-shi-lian-jie') }}
          </Button>
          <Button
            type="primary"
            class="primary-action"
            @click="handleAddDataSource"
            :loading="addDatasourceLoading"
            :disabled="disableAddDataSource"
            v-if="currentStep === 1"
          >
            {{ editMode ? $t('bao-cun') : $t('xin-zeng-shu-ju-yuan') }}
          </Button>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import DataSourceIcon from '@/components/function/DataSourceIcon';
import DataSourceInfo from '@/components/function/addDataSource/DataSourceInfo';
import SuccessAdd from '@/components/function/addDataSource/SuccessAdd';
import { separatePort, isMySQL } from '@/utils';
import { isPostgreSQL } from '@/const/dataSource';
import { mapGetters, mapState } from 'vuex';
import { cloneDeep as deepClone } from '@/utils/lodash';
import { normalizeDsSupportNameGroups } from '@/utils/datasourceSupport';
import { EVENT_BUS_NAME_LIST } from '@/utils/eventBusName';
import DataSourceGroup from '../dataSourceGroup.json';
import store from '../../store/index';

const EMPTY_DATA_SOURCE_FORM = {
  fetchType: 'MANUALLY_FILL',
  dbName: '',
  noValidateDbName: '',
  driver: '',
  driverFamily: '',
  driverVersion: '',
  dsKvConfigs: [],
  hostList: [
    {
      type: 'public',
      display: true,
      host: '',
      port: '3306'
    },
    {
      type: 'public',
      display: false,
      host: '',
      port: '3306'
    }
  ],
  connectType: null,
  connectTypeValue: '',
  host: '',
  type: 'MySQL',
  envId: '',
  region: '',
  queryClusterId: '',
  queryHostType: 'PUBLIC',
  rdsList: [],
  aliyunAk: '',
  aliyunSk: '',
  instanceDesc: '',
  ifAkSK: 'true',
  port: '',
  publicHost: '',
  publicPort: '',
  hdfsSecurityType: 'NONE',
  account: '',
  password: '',
  hdfsPort: '8020',
  securityType: 'KERBEROS',
  hdfsDwDir: '/user/hive/warehouse',
  sid: '',
  service: '',
  accountRole: '',
  asSysDba: false,
  accessKey: '',
  secretKey: ''
};

export default {
  name: 'AddDataSource',
  components: {
    DataSourceIcon,
    DataSourceInfo,
    SuccessAdd
  },
  props: {
    isModal: {
      type: Boolean,
      default: false
    },
    handleSetTestDsMsg: Function,
    handleCloseAddDsModal: Function
  },
  data() {
    return {
      addDatasourceLoading: false,
      DataSourceGroup,
      store,
      currentStep: 0,
      addDataSourceForm: deepClone(EMPTY_DATA_SOURCE_FORM),
      driverReadyForAdd: true,
      driverRequiredForAdd: false,
      testConnectionLoading: false,
      showAddDataSourceTypeModal: false,
      addDataSourceTypeSearchKey: '',
      selectedAddDataSourceType: ''
    };
  },
  computed: {
    ...mapGetters(['isDesktop']),
    ...mapState(['globalDsSetting', 'dmGlobalSetting']),
    driverFamilyMap() {
      const dsSetting = this.dmGlobalSetting?.dsSettingDef || this.globalDsSetting || {};

      return Object.keys(dsSetting).reduce((result, dsType) => {
        result[dsType] = Array.isArray(dsSetting[dsType]?.driverFamilies) ? dsSetting[dsType].driverFamilies : [];
        return result;
      }, {});
    },
    shouldAutoEnableFeatures() {
      return !this.isDesktop && this.$route?.path === '/datasource/add';
    },
    editMode() {
      return this.$route.query.mode === 'edit' && !!this.editDataSourceId;
    },
    editDataSourceId() {
      const dsId = Number(this.$route.query.dsId);
      return Number.isFinite(dsId) && dsId > 0 ? dsId : null;
    },
    disableAddDataSource() {
      return this.driverRequiredForAdd && !this.driverReadyForAdd;
    },
    addDataSourceTypeGroups() {
      return normalizeDsSupportNameGroups(this.dmGlobalSetting?.dsSupportNames || []);
    },
    flatAddDataSourceTypes() {
      return this.addDataSourceTypeGroups.flatMap((group) => group);
    },
    filteredAddDataSourceTypes() {
      const keyword = this.addDataSourceTypeSearchKey.trim().toLowerCase();
      if (!keyword) {
        return this.flatAddDataSourceTypes;
      }
      return this.flatAddDataSourceTypes.filter((type) => {
        const displayName = String(type.displayName || '').toLowerCase();
        const dsKey = String(type.dsKey || '').toLowerCase();
        return displayName.includes(keyword) || dsKey.includes(keyword);
      });
    }
  },
  beforeUnmount() {
    this.$bus.off(EVENT_BUS_NAME_LIST.SHOW_ADD_DATASOURCE_TYPE_MODAL, this.handleShowAddDataSourceTypeModal);
    store.state.rdsData = [];
    store.state.addedRdsList = [];
    store.state.firstAddDataSource = true;
    store.state.selectedCluster = {};
    store.state.clusterList = [];
  },
  watch: {
    '$route.query.dsType': {
      handler() {
        this.syncStepFromRoute();
      }
    },
    '$route.query.dsId': {
      handler() {
        this.syncStepFromRoute();
      }
    }
  },
  created() {
    this.syncStepFromRoute();
    this.$bus.on(EVENT_BUS_NAME_LIST.SHOW_ADD_DATASOURCE_TYPE_MODAL, this.handleShowAddDataSourceTypeModal);
  },
  methods: {
    handleShowAddDataSourceTypeModal() {
      if (this.editMode) {
        return;
      }
      this.addDataSourceTypeSearchKey = '';
      this.selectedAddDataSourceType = this.addDataSourceForm.type || this.$route.query.dsType || '';
      this.showAddDataSourceTypeModal = true;
    },
    handleCloseAddDataSourceTypeModal() {
      this.showAddDataSourceTypeModal = false;
      this.addDataSourceTypeSearchKey = '';
      this.selectedAddDataSourceType = '';
    },
    handleSelectAddDataSourceType(dsType) {
      if (!dsType) {
        return;
      }
      this.selectedAddDataSourceType = dsType;
    },
    handleConfirmAddDataSourceType() {
      const dsType = this.selectedAddDataSourceType;
      if (!dsType) {
        return;
      }
      this.handleCloseAddDataSourceTypeModal();
      if (dsType === this.$route.query.dsType) {
        return;
      }
      this.$router.push({
        path: '/datasource/add',
        query: {
          dsType
        }
      });
    },
    handleSetEmptyDatasourceForm() {
      this.currentStep = 1;
      this.addDataSourceForm = deepClone(EMPTY_DATA_SOURCE_FORM);
    },
    separatePort,
    syncPrimaryHostFields() {
      const visibleHost =
        this.addDataSourceForm.hostList.find((item) => item.display && item.type === 'public') || this.addDataSourceForm.hostList[0] || {};

      this.addDataSourceForm.publicHost = visibleHost.host || '';
      this.addDataSourceForm.publicPort = visibleHost.port || '';
      if (!this.addDataSourceForm.host) {
        this.addDataSourceForm.host = visibleHost.host || '';
      }
      if (!this.addDataSourceForm.port) {
        this.addDataSourceForm.port = visibleHost.port || '';
      }
      this.addDataSourceForm.resolvedHost = this.resolvePrimaryHost();
      this.addDataSourceForm.queryHostType = 'PUBLIC';
    },
    resolvePrimaryHost() {
      if (this.addDataSourceForm.resolvedHost) {
        return this.addDataSourceForm.resolvedHost;
      }
      const isSeparate = this.separatePort(this.addDataSourceForm.type) || this.addDataSourceForm.type === 'Db2Fori';
      if (isSeparate && this.addDataSourceForm.host && this.addDataSourceForm.port) {
        return `${this.addDataSourceForm.host}:${this.addDataSourceForm.port}`;
      }
      return this.addDataSourceForm.host || this.addDataSourceForm.publicHost || '';
    },
    handleDriverStatusChange(status) {
      this.driverRequiredForAdd = !!status?.required;
      this.driverReadyForAdd = !this.driverRequiredForAdd || !!status?.ready;
    },
    ensureDriverReadyForAdd() {
      const driverReady = this.$refs.dataSourceInfo?.isDriverReadyForSubmit?.() ?? this.driverReadyForAdd;
      if (this.driverRequiredForAdd && !driverReady) {
        this.$Message.error(this.$t('initialization.mysqlDriverDownloadRequired'));
        return false;
      }

      return true;
    },
    handleAddDataSource() {
      if (!this.ensureDriverReadyForAdd()) {
        return;
      }

      this.$refs.dataSourceInfo.validateAddDsForm((val) => {
        if (val) {
          this.$refs.dataSourceInfo?.syncAddDsUiFormToKvConfigs?.();
          this.syncPrimaryHostFields();
          this.handleAdd();
        }
      });
    },
    handleAdd() {
      const payload = this.$refs.dataSourceInfo?.buildDsSubmitPayload?.();
      this.addDatasourceLoading = true;
      const serviceName = this.editMode ? 'rdpDataSourceUpdateDs' : 'rdpDataSourceAddDs';
      this.$services[serviceName]({ data: payload })
        .then(async (res) => {
          this.addDatasourceLoading = false;
          if (res.success) {
            if (this.editMode) {
              this.$Message.success(this.$t('xiu-gai-cheng-gong'));
              this.$router.push({ path: '/datasource' });
            } else {
              this.currentStep = 4;
            }
            return;
          }
          this.$Message.error(res.msg || this.$t('shu-ju-yuan-tian-jia-shi-bai'));
        })
        .catch((error) => {
          this.addDatasourceLoading = false;
          this.$Message.error(error?.message || this.$t('shu-ju-yuan-tian-jia-shi-bai'));
        });
    },
    handleTestConnection() {
      if (!this.ensureDriverReadyForAdd()) {
        return;
      }
      this.$refs.dataSourceInfo.validateAddDsForm((valid) => {
        if (!valid) {
          return;
        }
        const payload = this.$refs.dataSourceInfo?.buildDsSubmitPayload?.();
        this.testConnectionLoading = true;
        this.$services
          .dmDataSourceConnectDs({ data: payload })
          .then((res) => {
            const result = res.data || {};
            const connectSuccess = res.success && result.success !== false;
            if (connectSuccess) {
              this.$Message.success(this.$t('ce-shi-lian-jie-cheng-gong'));
              return;
            }
            this.$Message.error(result.message || res.msg || this.$t('ce-shi-lian-jie-shi-bai'));
          })
          .catch((error) => {
            this.$Message.error(error?.message || this.$t('ce-shi-lian-jie-shi-bai'));
          })
          .finally(() => {
            this.testConnectionLoading = false;
          });
      });
    },
    handleAddPersonalDataSource(testDs = false) {
      this.$refs.dataSourceInfo.validateAddDsForm((val) => {
        if (val) {
          this.$refs.dataSourceInfo?.syncAddDsUiFormToKvConfigs?.();
          this.syncPrimaryHostFields();
          this.handleAddPersonal(testDs);
        }
      });
    },
    handleAddPersonal(testDs) {
      if (!this.addDataSourceForm.host && !this.addDataSourceForm.publicHost) {
        this.$Modal.warning({
          title: this.$t('tian-jia-shu-ju-yuan-ti-shi'),
          content: this.$t('qing-tian-xie-wan-zheng-qie-zheng-que-de-shu-ju-yuan-di-zhi')
        });
      } else {
        const { connectTypeValue, dsKvConfigs } = this.addDataSourceForm;
        const formData = new FormData();
        const isSeparate = this.separatePort(this.addDataSourceForm.type);
        const host = isSeparate
          ? this.addDataSourceForm.host && this.addDataSourceForm.port
            ? `${this.addDataSourceForm.host}:${this.addDataSourceForm.port}`
            : ''
          : this.addDataSourceForm.host;
        const publicHost = isSeparate
          ? this.addDataSourceForm.publicHost && this.addDataSourceForm.publicPort
            ? `${this.addDataSourceForm.publicHost}:${this.addDataSourceForm.publicPort}`
            : ''
          : this.addDataSourceForm.publicHost;

        const kvConfigs = [];
        if (dsKvConfigs.length) {
          dsKvConfigs.forEach((config) => {
            const { configName, currentCount, defaultValue } = config;
            kvConfigs.push({
              configName,
              configValue: currentCount || defaultValue
            });
          });
        }
        const DataSourceAddData = {
          host: publicHost && this.addDataSourceForm.type === 'Oracle' ? `${publicHost}:${connectTypeValue}` : publicHost,
          privateHost: '',
          publicHost: publicHost && this.addDataSourceForm.type === 'Oracle' ? `${publicHost}:${connectTypeValue}` : publicHost,
          type: this.addDataSourceForm.type,
          connectType: this.addDataSourceForm.connectType,
          instanceDesc: this.addDataSourceForm.instanceDesc,
          hostType: 'PUBLIC',
          account:
            DataSourceGroup.oracle.indexOf(this.addDataSourceForm.type) > -1
              ? this.addDataSourceForm.asSysDba
                ? `${this.addDataSourceForm.account} as SYSDBA`
                : this.addDataSourceForm.account
              : this.addDataSourceForm.account,
          instanceId: this.addDataSourceForm.instanceId,
          password: this.addDataSourceForm.password,
          securityType: this.addDataSourceForm.securityType,
          dbName: this.addDataSourceForm.dbName || this.addDataSourceForm.noValidateDbName,
          dsKvConfigs: kvConfigs,
          // extraData: {
          //   hdfsIp: this.addDataSourceForm.hdfsIp,
          //   hdfsPort: this.addDataSourceForm.hdfsPort,
          //   hdfsDwDir: this.addDataSourceForm.hdfsDwDir,
          //   hdfsPrincipal: this.addDataSourceForm.hdfsPrincipal
          // },
          driver: this.addDataSourceForm.driver,
          envId: this.addDataSourceForm.envId
        };

        Object.keys(DataSourceAddData).forEach((item) => {
          if (typeof DataSourceAddData[item] === 'string') {
            DataSourceAddData[item] = DataSourceAddData[item].trim();
          }
        });

        formData.append('rdpConfig', JSON.stringify(DataSourceAddData));
        formData.append(
          'dmConfig',
          JSON.stringify({
            hostType: 'PUBLIC',
            driver: DataSourceAddData.driver
          })
        );
        if (testDs) {
          this.$services
            .dmDesktopDataSourceTestDs({
              data: formData
            })
            .then((res) => {
              if (res.success) {
                this.handleSetTestDsMsg(this.$t('ce-shi-lian-jie-cheng-gong'));
              } else {
                this.handleSetTestDsMsg(this.$t('ce-shi-lian-jie-shi-bai'));
              }
            });
        } else {
          this.$services
            .dmDesktopDataSourceAddDs({
              data: formData
            })
            .then((res) => {
              if (res.success) {
                this.handleCloseAddDsModal();
              }
            });
        }
      }
    },
    handleReset() {
      this.addDataSourceForm = {
        fetchType: 'MANUALLY_FILL',
        host: '',
        publicHost: '',
        publicPort: '',
        type: 'MySQL',
        region: '',
        rdsList: [],
        aliyunAk: '',
        aliyunSk: '',
        instanceDesc: '',
        ifAkSK: 'true',
        port: '3306',
        hdfsSecurityType: 'NONE',
        account: '',
        hdfsPort: '8020',
        securityType: 'KERBEROS',
        hdfsDwDir: '/user/hive/warehouse'
      };
    },
    syncStepFromRoute() {
      if (this.isModal) {
        this.currentStep = 1;
        return;
      }
      if (this.editMode) {
        const dsType = this.$route.query.dsType;
        if (dsType) {
          this.addDataSourceForm.type = dsType;
        }
        this.currentStep = 1;
        return;
      }
      const dsType = this.$route.query.dsType;
      if (dsType) {
        const changedType = this.addDataSourceForm.type !== dsType;
        this.addDataSourceForm.type = dsType;
        this.currentStep = 1;
        if (changedType && this.$refs.dataSourceInfo) {
          this.$refs.dataSourceInfo.handleDataSourceChange();
        }
      } else {
        this.$router.replace({ path: '/datasource' });
        return;
      }
      this.driverReadyForAdd = true;
      this.driverRequiredForAdd = false;
    },
    handleConfigLoaded(config) {
      if (!this.editMode || !config?.instanceId || this.$route.query.instanceId === config.instanceId) {
        return;
      }
      this.$router.replace({
        path: '/datasource/add',
        query: {
          ...this.$route.query,
          instanceId: config.instanceId
        }
      });
    }
  }
};
</script>
<style lang="less">
.add-datasource-page {
  display: flex;
  flex-direction: column;
  flex: 1 1 auto;
  width: 100%;
  height: 100%;
  min-height: 0;
  box-sizing: border-box;
  position: relative;
  overflow-x: hidden;
  overflow-y: hidden;
  padding: 0 !important;
  background: #ffffff;
}

.add-datasource-type-modal {
  .ant-modal-content {
    overflow: hidden;
    border: 1px solid #dfe7ef;
    border-radius: 10px !important;
    box-shadow: 0 12px 32px rgba(15, 23, 42, 0.18);
  }

  .ant-modal-body,
  .ivu-modal-body {
    padding: 20px 24px 12px;
  }

  .ant-modal-footer,
  .ivu-modal-footer {
    margin-top: 0;
    padding: 8px 24px 20px;
    border-top: 0;
  }
}

.add-datasource-type-modal-body {
  display: flex;
  flex-direction: column;
  gap: 14px;
  height: 352px;
  min-height: 352px;
}

.add-datasource-type-search {
  width: 320px;
  flex: 0 0 auto;
}

.add-datasource-type-grid {
  display: grid;
  flex: 1 1 auto;
  grid-template-columns: repeat(auto-fill, minmax(138px, 1fr));
  align-content: start;
  gap: 10px;
  min-height: 0;
  min-width: 0;
  overflow-y: auto;
  padding: 0 4px 0 0;

  &.is-empty {
    display: flex;
    align-items: center;
    justify-content: center;
  }
}

.add-datasource-type-card {
  display: flex;
  min-width: 0;
  min-height: 44px;
  align-items: center;
  gap: 8px;
  border: 1px solid #e0e6ee;
  border-radius: 6px;
  padding: 0 12px;
  background: #ffffff;
  color: #1f2937;
  cursor: pointer;
  text-align: left;
  transition:
    border-color 0.16s ease,
    box-shadow 0.16s ease,
    background-color 0.16s ease;

  &:hover {
    border-color: #41d28f;
    background: #fbfffd;
    box-shadow: inset 0 0 0 1px #41d28f;
  }

  &.active {
    border-color: #18ae66;
    background: #effbf5;
    box-shadow: inset 0 0 0 1px #18ae66;
  }
}

.add-datasource-type-icon {
  display: inline-flex;
  width: 20px;
  flex: 0 0 20px;
  align-items: center;
  justify-content: center;
}

.add-datasource-type-name {
  min-width: 0;
  font-size: 13px;
  font-weight: 500;
  line-height: 17px;
  white-space: normal;
  word-break: normal;
  overflow-wrap: anywhere;
}

.add-datasource-type-empty {
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--text-secondary);
  font-size: 13px;
}

.add-datasource-type-footer {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;

  button {
    min-width: 92px;
  }
}

.add-datasource-wrapper {
  display: flex;
  flex: 1 1 auto;
  flex-direction: column;
  height: 100%;
  min-height: 0;
  margin: 0;
  background: transparent;
  border: none;
  border-radius: 0;
  box-shadow: none;
  overflow: visible;

  .add-datasource-content {
    flex: 1 1 auto;
    display: flex;
    flex-direction: column;
    min-height: 0;
    margin-bottom: 0;
    padding: 0;
    overflow: hidden;
  }
}

.add-dataSource-tools {
  position: relative;
  flex: 0 0 auto;
  display: flex;
  align-items: center;
  min-height: 56px;
  justify-content: center;
  padding: 8px 24px 16px;
  background: transparent;
  border-top: none;
  box-shadow: none;

  .add-dataSource-actions {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 12px;
  }

  button {
    min-width: 108px;
    margin: 0;
    border-radius: 6px;
    font-weight: 500;
  }

  .primary-action {
    min-width: 148px;
    border-color: #0f9f55;
    background: #0f9f55;
  }
}

.desktop {
  padding: 0;

  .add-datasource-wrapper {
    padding: 0;
    border: none;

    .add-datasource-content {
      margin-bottom: 0;
      padding: 0;

      .add-datasource-step1 {
        padding: 0;
      }
    }
  }
}
</style>
